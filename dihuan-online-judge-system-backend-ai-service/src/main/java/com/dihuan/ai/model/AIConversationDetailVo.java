package com.dihuan.ai.model;

import java.util.List;

public class AIConversationDetailVo {

    private AIConversationListItem conversation;
    private List<AIChatMessageVo> messages;

    public AIConversationListItem getConversation() {
        return conversation;
    }

    public void setConversation(AIConversationListItem conversation) {
        this.conversation = conversation;
    }

    public List<AIChatMessageVo> getMessages() {
        return messages;
    }

    public void setMessages(List<AIChatMessageVo> messages) {
        this.messages = messages;
    }
}