package com.dihuan.ai.config;

import com.dihuan.ai.service.RagQuestionService;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagAdvisorConfig {
    /**
     * 创建题目知识库的 RAG Advisor。
     *
     * <p>Advisor 会在 ChatModel 真正生成回答之前执行。它读取用户当前问题，
     * 调用 RagQuestionService 查询 pgvector，随后把检索到的题目内容追加到 Prompt，
     * 因此聊天服务不需要在回答前直接查询题目 MySQL。</p>
     */
    @Bean
    public RetrievalAugmentationAdvisor questionRagAdvisor(RagQuestionService ragQuestionService) {
        // RagQuestionService 内部已经完成语义检索、关键词检索和 30%/70% 混合排序。
        DocumentRetriever documentRetriever = query -> ragQuestionService.search(query.text(), 5);
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
    }
}