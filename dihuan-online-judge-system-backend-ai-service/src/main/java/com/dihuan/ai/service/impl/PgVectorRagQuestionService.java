package com.dihuan.ai.service.impl;

import com.dihuan.ai.service.RagQuestionService;
import com.dihuan.model.dto.ai.RagQuestionDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rag.search.keyword-weight:0.3}")
    private double keywordWeight;

    @Value("${rag.search.semantic-weight:0.7}")
    private double semanticWeight;

    public PgVectorRagQuestionService(ObjectProvider<VectorStore> vectorStoreProvider,
                                      JdbcTemplate jdbcTemplate,
                                      ObjectMapper objectMapper) {
        this.vectorStoreProvider = vectorStoreProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void upsert(RagQuestionDocument document) {
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
        VectorStore vectorStore = vectorStoreProvider.getIfAvailable();
        if (vectorStore != null && questionId != null) {
            vectorStore.delete(List.of(documentId(questionId)));
        }
    }

    @Override
    public List<Document> search(String query, int topK) {
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
        String tags = document.getTags() == null ? "" : String.join(", ", document.getTags());
        return "题目标题: " + document.getTitle() + "\n题目标签: " + tags + "\n题目内容: " + document.getContent();
    }

    private List<Document> keywordSearch(String query, int topK) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return jdbcTemplate.query("""
                SELECT id::text, content, metadata::text
                FROM vector_store
                WHERE to_tsvector('simple', content) @@ plainto_tsquery('simple', ?)
                ORDER BY ts_rank_cd(to_tsvector('simple', content), plainto_tsquery('simple', ?)) DESC
                LIMIT ?
                """, (resultSet, rowNumber) -> {
            try {
                Map<String, Object> metadata = objectMapper.readValue(
                        resultSet.getString("metadata"), new TypeReference<>() { });
                return new Document(resultSet.getString("id"), resultSet.getString("content"), metadata);
            } catch (JsonProcessingException exception) {
                throw new IllegalStateException("Failed to parse pgvector metadata", exception);
            }
        }, query, query, topK);
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
        double semanticScore = document.semanticRank == Integer.MAX_VALUE
                ? 0 : 1.0 / document.semanticRank;
        double keywordScore = document.keywordRank == Integer.MAX_VALUE
                ? 0 : 1.0 / document.keywordRank;
        return semanticWeight * semanticScore + keywordWeight * keywordScore;
    }

    private VectorStore getVectorStore() {
        VectorStore vectorStore = vectorStoreProvider.getIfAvailable();
        if (vectorStore == null) {
            throw new IllegalStateException("No pgvector VectorStore configured; configure an EmbeddingModel first");
        }
        return vectorStore;
    }

    private String documentId(Long questionId) {
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