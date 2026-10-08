package com.dihuan.ai.service.impl;

import com.dihuan.ai.service.RagQuestionService;
import com.dihuan.ai.service.RagKeywordSearchService;
import com.dihuan.model.dto.ai.RagQuestionDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PgVectorRagQuestionService implements RagQuestionService {
    private final ObjectProvider<VectorStore> vectorStoreProvider;
    private final RagKeywordSearchService keywordSearchService;

    @Value("${rag.search.keyword-weight:0.3}")
    private double keywordWeight;

    @Value("${rag.search.semantic-weight:0.7}")
    private double semanticWeight;

    public PgVectorRagQuestionService(ObjectProvider<VectorStore> vectorStoreProvider,
                                      RagKeywordSearchService keywordSearchService) {
        this.vectorStoreProvider = vectorStoreProvider;
        this.keywordSearchService = keywordSearchService;
    }

    @Override
    public void upsert(RagQuestionDocument document) {
        // 题目服务只会为已发布题目调用 upsert；这里先删除旧版本，避免同一道题产生多条向量记录。
        VectorStore vectorStore = getVectorStore();
        delete(document.getQuestionId());
        vectorStore.add(List.of(new Document(documentId(document.getQuestionId()),
                toEmbeddingText(document), Map.of(
                        "questionId", document.getQuestionId(),
                        "authorId", document.getAuthorId(),
                        "title", document.getTitle(),
                        "tags", document.getTags() == null ? List.of() : Arrays.asList(document.getTags())))));
    }

    @Override
    public void delete(Long questionId) {
        // 题目 ID 会转换为稳定 UUID，因此更新、下架和删除都能命中同一条向量记录。
        VectorStore vectorStore = vectorStoreProvider.getIfAvailable();
        if (vectorStore != null && questionId != null) {
            vectorStore.delete(List.of(documentId(questionId)));
        }
    }

    @Override
    public List<Document> search(String query, int topK) {
        // 两路检索各自多取一些候选结果，再统一融合后截取最终 topK。
        int candidateSize = Math.max(topK * 3, 20);
        List<Document> semanticDocuments = getVectorStore().similaritySearch(SearchRequest.builder()
                .query(query)
            .topK(candidateSize)
            .build());
        List<Document> keywordDocuments = keywordSearch(query, candidateSize);

        Map<String, RankedDocument> rankedDocuments = new LinkedHashMap<>();
        addRankedDocuments(rankedDocuments, semanticDocuments, true);
        addRankedDocuments(rankedDocuments, keywordDocuments, false);

        return rankedDocuments.values().stream()
            .sorted(Comparator.comparingDouble(this::hybridScore).reversed())
            .limit(topK)
            .map(RankedDocument::document)
            .toList();
    }

    private String toEmbeddingText(RagQuestionDocument document) {
        // 标题、标签和正文共同参与向量化；metadata 则用于保存结构化信息和后续展示/过滤。
        String tags = document.getTags() == null ? "" : String.join(", ", document.getTags());
        return "题目标题: " + document.getTitle() + "\n题目标签: " + tags + "\n题目内容: " + document.getContent();
    }

    private List<Document> keywordSearch(String query, int topK) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        // PostgreSQL 全文检索负责精确术语命中，例如算法名称、题目标签和专有名词。
        return keywordSearchService.search(query, topK);
    }

    private void addRankedDocuments(Map<String, RankedDocument> rankedDocuments,
                                     List<Document> documents,
                                     boolean semantic) {
        for (int index = 0; index < documents.size(); index++) {
            Document document = documents.get(index);
            RankedDocument rankedDocument = rankedDocuments.computeIfAbsent(
                    document.getId(), ignored -> new RankedDocument(document));
            if (semantic) {
                rankedDocument.semanticRank = Math.min(rankedDocument.semanticRank, index + 1);
            } else {
                rankedDocument.keywordRank = Math.min(rankedDocument.keywordRank, index + 1);
            }
        }
    }

    private double hybridScore(RankedDocument document) {
        // 语义检索和关键词检索的原始分数不可直接相加，因此使用倒数排名归一化后再加权。
        double semanticScore = document.semanticRank == Integer.MAX_VALUE
                ? 0 : 1.0 / document.semanticRank;
        double keywordScore = document.keywordRank == Integer.MAX_VALUE
                ? 0 : 1.0 / document.keywordRank;
        return semanticWeight * semanticScore + keywordWeight * keywordScore;
    }

    private VectorStore getVectorStore() {
        VectorStore vectorStore = vectorStoreProvider.getIfAvailable();
        if (vectorStore == null) {
            throw new IllegalStateException("未配置 pgvector VectorStore，请先配置 EmbeddingModel");
        }
        return vectorStore;
    }

    private String documentId(Long questionId) {
        // nameUUIDFromBytes 对同一个题目 ID 总是生成同一个 UUID，适合作为向量文档主键。
        return UUID.nameUUIDFromBytes(("question:" + questionId).getBytes(StandardCharsets.UTF_8)).toString();
    }

    private static class RankedDocument {
        private final Document document;
        private int semanticRank = Integer.MAX_VALUE;
        private int keywordRank = Integer.MAX_VALUE;

        private RankedDocument(Document document) {
            this.document = document;
        }

        private Document document() {
            return document;
        }
    }
}