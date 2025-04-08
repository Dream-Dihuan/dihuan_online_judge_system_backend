package com.dihuan.ai.model;

/**
 * 包装chunk，防止空格在传输过程中丢失
 */
public class StreamResponse {
    private String content;

    public StreamResponse(String content) {
        this.content = content;
    }

    // Getter 和 Setter 必须存在以便 JSON 序列化
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}