package com.dihuan.ai.service;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

public interface ChatMemoryService {

    void add(String conversationId, List<Message> messages);

    List<Message> get(String conversationId, int lastN);

    void clear(String conversationId);
}