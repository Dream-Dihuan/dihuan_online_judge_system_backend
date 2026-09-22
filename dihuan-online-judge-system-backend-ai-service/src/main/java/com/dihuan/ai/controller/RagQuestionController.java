package com.dihuan.ai.controller;

import com.dihuan.ai.service.RagQuestionService;
import com.dihuan.model.dto.ai.RagQuestionDocument;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inner/rag")
public class RagQuestionController {
    private final RagQuestionService ragQuestionService;

    public RagQuestionController(RagQuestionService ragQuestionService) {
        this.ragQuestionService = ragQuestionService;
    }

    @PostMapping("/questions")
    public void upsertQuestion(@RequestBody RagQuestionDocument document) {
        ragQuestionService.upsert(document);
    }

    @DeleteMapping("/questions/{questionId}")
    public void deleteQuestion(@PathVariable Long questionId) {
        ragQuestionService.delete(questionId);
    }

    @GetMapping("/questions/search")
    public List<Document> searchQuestions(@RequestParam String query,
                                          @RequestParam(defaultValue = "5") int topK) {
        return ragQuestionService.search(query, topK);
    }
}