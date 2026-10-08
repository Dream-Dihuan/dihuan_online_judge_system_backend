package com.dihuan.ai.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface RagKeywordSearchService {

    List<Document> search(String query, int topK);
}