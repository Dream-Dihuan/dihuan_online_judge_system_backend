package com.dihuan.ai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcChatMemory implements ChatMemory {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChatMemory(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        String sql = "INSERT INTO ai_chat_message "
                + "(conversation_id, message_type, content) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, messages, messages.size(), (statement, message) -> {
            statement.setString(1, conversationId);
            statement.setString(2, message.getMessageType().getValue().toUpperCase());
            statement.setString(3, message.getText());
        });
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        String sql = "SELECT message_type, content FROM "
                + "(SELECT message_type, content, id FROM ai_chat_message "
                + "WHERE conversation_id = ? ORDER BY id DESC LIMIT ?) recent "
                + "ORDER BY id ASC";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toMessage(
                resultSet.getString("message_type"), resultSet.getString("content")),
                conversationId, lastN);
    }

    @Override
    public void clear(String conversationId) {
        jdbcTemplate.update("DELETE FROM ai_chat_message WHERE conversation_id = ?", conversationId);
    }

    private Message toMessage(String type, String content) {
        return switch (type.toUpperCase()) {
            case "SYSTEM" -> new SystemMessage(content);
            case "ASSISTANT" -> new AssistantMessage(content);
            default -> new UserMessage(content);
        };
    }
}