package com.dihuan.ai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import com.dihuan.ai.service.ChatMemoryService;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class JdbcChatMemory implements ChatMemory {

    private final ChatMemoryService chatMemoryService;

    public JdbcChatMemory(ChatMemoryService chatMemoryService) {
        this.chatMemoryService = chatMemoryService;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        chatMemoryService.add(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        return chatMemoryService.get(conversationId, lastN);
    }

    @Override
    public void clear(String conversationId) {
        chatMemoryService.clear(conversationId);
    }
}