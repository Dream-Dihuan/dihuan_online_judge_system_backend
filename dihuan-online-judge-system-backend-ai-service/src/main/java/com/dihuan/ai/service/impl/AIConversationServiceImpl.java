package com.dihuan.ai.service.impl;

import com.dihuan.ai.model.AIConversationDetailVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.service.AIConversationDataService;
import com.dihuan.ai.service.AIConversationService;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIConversationServiceImpl implements AIConversationService {

        private final AIConversationDataService conversationDataService;

        public AIConversationServiceImpl(AIConversationDataService conversationDataService) {
                this.conversationDataService = conversationDataService;
    }

    @Override
    public void ensureConversation(Long userId, ChatDto chatDto) {
        String title = chatDto.getMessage().length() > 255
                ? chatDto.getMessage().substring(0, 255)
                : chatDto.getMessage();
        String conversationId = chatDto.getConversationId();
        Integer ownedCount = conversationDataService.countOwned(conversationId, userId);
        Integer existingCount = conversationDataService.countExisting(conversationId);
        if (existingCount != null && existingCount > 0
                && (ownedCount == null || ownedCount == 0)) {
            throw new DihuanException(ResultCodeEnum.FAIL, "无权访问该AI会话");
        }
        if (ownedCount != null && ownedCount > 0) {
            conversationDataService.updateConversation(conversationId, userId,
                    chatDto.getModelName(), chatDto.getQuestionId());
            return;
        }
        conversationDataService.insertConversation(conversationId, userId, title,
                chatDto.getModelName(), chatDto.getQuestionId());
    }

    @Override
    public void touchConversation(String conversationId) {
        conversationDataService.touchConversation(conversationId);
    }

    @Override
    public List<AIConversationListItem> getUserConversations(Long userId) {
        return conversationDataService.findUserConversations(userId);
    }

    @Override
    public AIConversationDetailVo getConversationDetail(Long userId, String conversationId) {
        List<AIConversationListItem> conversations =
                conversationDataService.findConversation(conversationId, userId);

        if (conversations.isEmpty()) {
            return null;
        }

        var messages = conversationDataService.findMessages(conversationId);

        AIConversationDetailVo detail = new AIConversationDetailVo();
        detail.setConversation(conversations.get(0));
        detail.setMessages(messages);
        return detail;
    }
}