package com.dihuan.ai.service.impl;

import com.dihuan.ai.model.AIChatMessageVo;
import com.dihuan.ai.model.AIConversationDetailVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.service.AIConversationService;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIConversationServiceImpl implements AIConversationService {

    private final JdbcTemplate jdbcTemplate;

    public AIConversationServiceImpl(
            @Qualifier("chatMemoryJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void ensureConversation(Long userId, ChatDto chatDto) {
        String title = chatDto.getMessage().length() > 255
                ? chatDto.getMessage().substring(0, 255)
                : chatDto.getMessage();
        String conversationId = chatDto.getConversationId();
        Integer ownedCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ai_chat_conversation "
                        + "WHERE conversation_id = ? AND user_id = ?",
                Integer.class, conversationId, userId);
        Integer existingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ai_chat_conversation WHERE conversation_id = ?",
                Integer.class, conversationId);
        if (existingCount != null && existingCount > 0
                && (ownedCount == null || ownedCount == 0)) {
            throw new DihuanException(ResultCodeEnum.FAIL, "无权访问该AI会话");
        }
        if (ownedCount != null && ownedCount > 0) {
            jdbcTemplate.update("UPDATE ai_chat_conversation SET model_name = ?, "
                            + "question_id = ?, last_message_at = CURRENT_TIMESTAMP(6), "
                            + "update_time = CURRENT_TIMESTAMP(6), is_deleted = 0 "
                            + "WHERE conversation_id = ? AND user_id = ?",
                    chatDto.getModelName(), chatDto.getQuestionId(), conversationId, userId);
            return;
        }
        jdbcTemplate.update("INSERT INTO ai_chat_conversation "
                        + "(conversation_id, user_id, title, model_name, question_id, last_message_at) "
                        + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP(6))",
                conversationId, userId, title, chatDto.getModelName(), chatDto.getQuestionId());
    }

    @Override
    public void touchConversation(String conversationId) {
        jdbcTemplate.update("UPDATE ai_chat_conversation "
                        + "SET last_message_at = CURRENT_TIMESTAMP(6), update_time = CURRENT_TIMESTAMP(6) "
                        + "WHERE conversation_id = ? AND is_deleted = 0", conversationId);
    }

    @Override
    public List<AIConversationListItem> getUserConversations(Long userId) {
        String sql = "SELECT conversation_id, user_id, title, model_name, question_id, "
                + "last_message_at, create_time, update_time "
                + "FROM ai_chat_conversation "
                + "WHERE user_id = ? AND is_deleted = 0 "
                + "ORDER BY update_time DESC";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> {
            AIConversationListItem item = new AIConversationListItem();
            item.setConversationId(resultSet.getString("conversation_id"));
            item.setUserId(resultSet.getLong("user_id"));
            item.setTitle(resultSet.getString("title"));
            item.setModelName(resultSet.getString("model_name"));
            item.setQuestionId(resultSet.getObject("question_id", Long.class));
            item.setLastMessageAt(resultSet.getTimestamp("last_message_at") == null
                    ? null : resultSet.getTimestamp("last_message_at").toLocalDateTime());
            item.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            item.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            return item;
        }, userId);
    }

    @Override
    public AIConversationDetailVo getConversationDetail(Long userId, String conversationId) {
        String conversationSql = "SELECT conversation_id, user_id, title, model_name, question_id, "
                + "last_message_at, create_time, update_time "
                + "FROM ai_chat_conversation "
                + "WHERE conversation_id = ? AND user_id = ? AND is_deleted = 0";
        List<AIConversationListItem> conversations = jdbcTemplate.query(
                conversationSql,
                (resultSet, rowNumber) -> {
                    AIConversationListItem item = new AIConversationListItem();
                    item.setConversationId(resultSet.getString("conversation_id"));
                    item.setUserId(resultSet.getLong("user_id"));
                    item.setTitle(resultSet.getString("title"));
                    item.setModelName(resultSet.getString("model_name"));
                    item.setQuestionId(resultSet.getObject("question_id", Long.class));
                    item.setLastMessageAt(resultSet.getTimestamp("last_message_at") == null
                            ? null : resultSet.getTimestamp("last_message_at").toLocalDateTime());
                    item.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
                    item.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
                    return item;
                }, conversationId, userId);

        if (conversations.isEmpty()) {
            return null;
        }

        String messageSql = "SELECT id, conversation_id, message_type, content, create_time "
                + "FROM ai_chat_message WHERE conversation_id = ? "
                + "ORDER BY create_time ASC, id ASC";
        List<AIChatMessageVo> messages = jdbcTemplate.query(messageSql, (resultSet, rowNumber) -> {
            AIChatMessageVo message = new AIChatMessageVo();
            message.setId(resultSet.getLong("id"));
            message.setConversationId(resultSet.getString("conversation_id"));
            message.setMessageType(resultSet.getString("message_type"));
            message.setContent(resultSet.getString("content"));
            message.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            return message;
        }, conversationId);

        AIConversationDetailVo detail = new AIConversationDetailVo();
        detail.setConversation(conversations.get(0));
        detail.setMessages(messages);
        return detail;
    }
}