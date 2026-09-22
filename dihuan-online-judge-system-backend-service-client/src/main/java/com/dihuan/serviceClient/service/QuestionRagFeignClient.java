package com.dihuan.serviceClient.service;

import com.dihuan.model.dto.ai.RagQuestionDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dihuan-ai-service", path = "/api/ai/inner/rag")
public interface QuestionRagFeignClient {

    @PostMapping("/questions")
    void upsertQuestion(@RequestBody RagQuestionDocument document);

    @DeleteMapping("/questions/{questionId}")
    void deleteQuestion(@PathVariable("questionId") Long questionId);
}