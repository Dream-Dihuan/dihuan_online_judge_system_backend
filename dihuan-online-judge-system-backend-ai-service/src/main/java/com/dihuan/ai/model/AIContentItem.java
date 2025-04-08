package com.dihuan.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIContentItem implements Serializable {
    /**
     * role:对话中的角色
     */
    private String role;
    /**
     * message:对话中的内容
     */
    private String content;

    @Override
    public String toString() {
        return "{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

