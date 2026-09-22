package com.dihuan.ai.config;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.embeddings.TextEmbeddingResultItem;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class AiConfig {
    @Bean
    @Primary
    public ChatModel ollamaPrimaryChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }

    @Bean
    public EmbeddingModel ragEmbeddingModel(@Value("${rag.embedding.model}") String model) {
        return new DashScopeEmbeddingModel(model);
    }

    private static class DashScopeEmbeddingModel implements EmbeddingModel {
        private final String model;
        private final TextEmbedding textEmbedding = new TextEmbedding();

        private DashScopeEmbeddingModel(String model) {
            this.model = model;
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            TextEmbeddingParam param = TextEmbeddingParam.builder()
                    .model(model)
                    .texts(request.getInstructions())
                    .build();
            try {
                TextEmbeddingResult result = textEmbedding.call(param);
                List<Embedding> embeddings = result.getOutput().getEmbeddings().stream()
                        .map(this::toEmbedding)
                        .toList();
                return new EmbeddingResponse(embeddings);
            } catch (ApiException | NoApiKeyException exception) {
                throw new IllegalStateException("DashScope embedding request failed", exception);
            }
        }

        @Override
        public float[] embed(Document document) {
            return embed(document.getText());
        }

        private Embedding toEmbedding(TextEmbeddingResultItem item) {
            float[] vector = new float[item.getEmbedding().size()];
            for (int index = 0; index < item.getEmbedding().size(); index++) {
                vector[index] = item.getEmbedding().get(index).floatValue();
            }
            return new Embedding(vector, item.getTextIndex());
        }
    }
}
