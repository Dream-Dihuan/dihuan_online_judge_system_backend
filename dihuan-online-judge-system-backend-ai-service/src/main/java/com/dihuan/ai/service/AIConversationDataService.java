package com.dihuan.ai.service;

import com.dihuan.ai.model.AIChatMessageVo;
import com.dihuan.ai.model.AIConversationListItem;

import java.util.List;

public interface AIConversationDataService {

    Integer countOwned(String conversationId, Long userId);

    Integer countExisting(String conversationId);

    void updateConversation(String conversationId, Long userId, String modelName, Long questionId);

    void insertConversation(String conversationId, Long userId, String title,
                            String modelName, Long questionId);

    void touchConversation(String conversationId);

    List<AIConversationListItem> findUserConversations(Long userId);

    List<AIConversationListItem> findConversation(String conversationId, Long userId);

    List<AIChatMessageVo> findMessages(String conversationId);
}