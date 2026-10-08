package com.dihuan.ai.service.impl;

import com.dihuan.ai.service.RagKeywordSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RagKeywordSearchServiceImpl implements RagKeywordSearchService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public RagKeywordSearchServiceImpl(
            @Qualifier("ragJdbcTemplate") JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Document> search(String query, int topK) {
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
}