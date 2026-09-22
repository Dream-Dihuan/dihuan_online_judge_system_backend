package com.dihuan.model.dto.ai;

/**
 * 题目服务发送给 AI 服务的 RAG 文档。
 *
 * <p>题目服务只传递题目内容和元信息，不传递审核状态；是否允许进入 RAG
 * 由题目服务在调用 upsert 或 delete RPC 时决定。</p>
 */
public class RagQuestionDocument {
    /** 题目主键，用于生成稳定的向量文档 ID。 */
    private Long questionId;
    /** 题目作者 ID，用于 metadata 过滤和结果追踪。 */
    private Long authorId;
    /** 题目标题，会参与 embedding，也会保存到 metadata。 */
    private String title;
    /** 题目正文，是 embedding 的主要语义来源。 */
    private String content;
    /** 题目标签，会参与 embedding，并保存到 metadata。 */
    private String[] tags;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

}