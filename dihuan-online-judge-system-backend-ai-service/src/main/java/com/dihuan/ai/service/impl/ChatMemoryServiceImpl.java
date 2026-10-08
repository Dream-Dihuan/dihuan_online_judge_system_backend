package com.dihuan.ai.service.impl;

import com.dihuan.ai.service.ChatMemoryService;
import com.dihuan.ai.persistence.entity.AIChatMessageEntity;
import com.dihuan.ai.persistence.mapper.AIChatMessageMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatMemoryServiceImpl implements ChatMemoryService {

    private final AIChatMessageMapper chatMessageMapper;

    public ChatMemoryServiceImpl(AIChatMessageMapper chatMessageMapper) {
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            AIChatMessageEntity entity = new AIChatMessageEntity();
            entity.setConversationId(conversationId);
            entity.setMessageType(message.getMessageType().getValue().toUpperCase());
            entity.setContent(message.getText());
            chatMessageMapper.insert(entity);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<AIChatMessageEntity> entities = chatMessageMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AIChatMessageEntity>()
                .eq("conversation_id", conversationId)
                .orderByDesc("id")
                .last("LIMIT " + lastN));
        List<Message> messages = new ArrayList<>(entities.stream()
            .sorted(Comparator.comparing(AIChatMessageEntity::getId))
            .map(entity -> toMessage(entity.getMessageType(), entity.getContent()))
            .toList());
        return messages;
    }

    @Override
    public void clear(String conversationId) {
        chatMessageMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AIChatMessageEntity>()
            .eq("conversation_id", conversationId));
    }

    private Message toMessage(String type, String content) {
        return switch (type.toUpperCase()) {
            case "SYSTEM" -> new SystemMessage(content);
            case "ASSISTANT" -> new AssistantMessage(content);
            default -> new UserMessage(content);
        };
    }
}