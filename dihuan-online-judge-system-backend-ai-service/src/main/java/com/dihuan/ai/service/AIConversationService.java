package com.dihuan.ai.service;

import com.dihuan.ai.model.AIConversationDetailVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.model.ChatDto;

import java.util.List;

public interface AIConversationService {

    void ensureConversation(Long userId, ChatDto chatDto);

    void touchConversation(String conversationId);

    List<AIConversationListItem> getUserConversations(Long userId);

    AIConversationDetailVo getConversationDetail(Long userId, String conversationId);
}