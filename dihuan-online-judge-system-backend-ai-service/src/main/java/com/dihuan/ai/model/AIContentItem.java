package com.dihuan.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AIContentItem implements Serializable {
    /**
     * role:对话中的角色
     */
    private String role;
    /**
     * message:对话中的内容
     */
    private String content;

    public AIContentItem(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

