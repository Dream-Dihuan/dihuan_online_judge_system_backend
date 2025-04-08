package com.dihuan.ai.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChatDto implements Serializable {
    private List<AIContentItem> messages;
    private String modelName;
}
