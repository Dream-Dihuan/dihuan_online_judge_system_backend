package com.dihuan.ai.model;

import lombok.Data;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;

@Data
public class ChatDto implements Serializable {
    @NotBlank
    private String conversationId;
    @NotBlank
    private String message;
    @NotBlank
    private String modelName;
    private Long questionId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
