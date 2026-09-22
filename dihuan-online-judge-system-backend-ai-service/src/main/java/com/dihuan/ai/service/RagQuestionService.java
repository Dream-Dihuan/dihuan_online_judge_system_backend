package com.dihuan.ai.service;

import com.dihuan.model.dto.ai.RagQuestionDocument;
import org.springframework.ai.document.Document;

import java.util.List;

public interface RagQuestionService {
    void upsert(RagQuestionDocument document);

    void delete(Long questionId);

    List<Document> search(String query, int topK);
}