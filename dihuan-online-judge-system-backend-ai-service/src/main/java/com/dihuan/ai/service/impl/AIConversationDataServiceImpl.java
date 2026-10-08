package com.dihuan.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dihuan.ai.model.AIChatMessageVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.persistence.entity.AIChatMessageEntity;
import com.dihuan.ai.persistence.entity.AIConversationEntity;
import com.dihuan.ai.persistence.mapper.AIChatMessageMapper;
import com.dihuan.ai.persistence.mapper.AIConversationMapper;
import com.dihuan.ai.service.AIConversationDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIConversationDataServiceImpl implements AIConversationDataService {

    private final AIConversationMapper conversationMapper;
    private final AIChatMessageMapper chatMessageMapper;

    public AIConversationDataServiceImpl(AIConversationMapper conversationMapper,
                                         AIChatMessageMapper chatMessageMapper) {
        this.conversationMapper = conversationMapper;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public Integer countOwned(String conversationId, Long userId) {
        return Math.toIntExact(conversationMapper.selectCount(new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getConversationId, conversationId)
                .eq(AIConversationEntity::getUserId, userId)));
    }

    @Override
    public Integer countExisting(String conversationId) {
        return Math.toIntExact(conversationMapper.selectCount(new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getConversationId, conversationId)));
    }

    @Override
    public void updateConversation(String conversationId, Long userId,
                                   String modelName, Long questionId) {
        LocalDateTime now = LocalDateTime.now();
        AIConversationEntity entity = new AIConversationEntity();
        entity.setModelName(modelName);
        entity.setQuestionId(questionId);
        entity.setLastMessageAt(now);
        entity.setUpdateTime(now);
        entity.setIsDeleted((byte) 0);
        conversationMapper.update(entity, new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getConversationId, conversationId)
                .eq(AIConversationEntity::getUserId, userId));
    }

    @Override
    public void insertConversation(String conversationId, Long userId, String title,
                                   String modelName, Long questionId) {
        AIConversationEntity entity = new AIConversationEntity();
        entity.setConversationId(conversationId);
        entity.setUserId(userId);
        entity.setTitle(title);
        entity.setModelName(modelName);
        entity.setQuestionId(questionId);
        entity.setLastMessageAt(LocalDateTime.now());
        entity.setIsDeleted((byte) 0);
        conversationMapper.insert(entity);
    }

    @Override
    public void touchConversation(String conversationId) {
        LocalDateTime now = LocalDateTime.now();
        AIConversationEntity entity = new AIConversationEntity();
        entity.setLastMessageAt(now);
        entity.setUpdateTime(now);
        conversationMapper.update(entity, new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getConversationId, conversationId)
                .eq(AIConversationEntity::getIsDeleted, (byte) 0));
    }

    @Override
    public List<AIConversationListItem> findUserConversations(Long userId) {
        return conversationMapper.selectList(new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getUserId, userId)
                .eq(AIConversationEntity::getIsDeleted, (byte) 0)
                .orderByDesc(AIConversationEntity::getUpdateTime))
                .stream().map(this::toConversation).toList();
    }

    @Override
    public List<AIConversationListItem> findConversation(String conversationId, Long userId) {
        return conversationMapper.selectList(new LambdaQueryWrapper<AIConversationEntity>()
                .eq(AIConversationEntity::getConversationId, conversationId)
                .eq(AIConversationEntity::getUserId, userId)
                .eq(AIConversationEntity::getIsDeleted, (byte) 0))
                .stream().map(this::toConversation).toList();
    }

    @Override
    public List<AIChatMessageVo> findMessages(String conversationId) {
        return chatMessageMapper.selectList(new LambdaQueryWrapper<AIChatMessageEntity>()
                .eq(AIChatMessageEntity::getConversationId, conversationId)
                .orderByAsc(AIChatMessageEntity::getCreateTime)
                .orderByAsc(AIChatMessageEntity::getId))
                .stream().map(this::toMessage).toList();
    }

    private AIConversationListItem toConversation(AIConversationEntity entity) {
        AIConversationListItem item = new AIConversationListItem();
        item.setConversationId(entity.getConversationId());
        item.setUserId(entity.getUserId());
        item.setTitle(entity.getTitle());
        item.setModelName(entity.getModelName());
        item.setQuestionId(entity.getQuestionId());
        item.setLastMessageAt(entity.getLastMessageAt());
        item.setCreateTime(entity.getCreateTime());
        item.setUpdateTime(entity.getUpdateTime());
        return item;
    }

    private AIChatMessageVo toMessage(AIChatMessageEntity entity) {
        AIChatMessageVo message = new AIChatMessageVo();
        message.setId(entity.getId());
        message.setConversationId(entity.getConversationId());
        message.setMessageType(entity.getMessageType());
        message.setContent(entity.getContent());
        message.setCreateTime(entity.getCreateTime());
        return message;
    }
}