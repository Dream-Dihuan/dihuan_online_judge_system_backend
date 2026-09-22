package com.dihuan.ai.config;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.embeddings.TextEmbeddingResultItem;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Configuration
public class AiConfig {
    /**
     * 当前项目的聊天模型仍然使用 Ollama，并将它设为默认 ChatModel。
     *
     * <p>注意：聊天模型只负责根据 Prompt 生成回答，不负责生成向量。
     * RAG 使用的 EmbeddingModel 在下面单独声明，二者可以来自不同的厂商。</p>
     */
    @Bean
    @Primary
    public ChatModel ollamaPrimaryChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }

    /**
     * 创建 RAG 专用的 EmbeddingModel。
     *
     * <p>阿里 DashScope 原生 SDK 并没有直接实现 Spring AI 的 EmbeddingModel 接口，
     * 因此通过 DashScopeEmbeddingModel 做一层适配，让 Spring AI 的 PgVectorStore
     * 可以统一调用它。</p>
     */
    @Bean
    @Primary
    public EmbeddingModel ragEmbeddingModel(@Value("${rag.embedding.model}") String model,
                                            @Value("${DASHSCOPE_API_KEY:}") String apiKey) {
        if (!apiKey.isBlank()) {
            Constants.apiKey = apiKey;
        }
        return new DashScopeEmbeddingModel(model);
    }

    /**
     * 将阿里 DashScope TextEmbedding 接口适配成 Spring AI EmbeddingModel。
     *
     * <p>PgVectorStore 会调用这个 Bean，将题目文本或用户查询转换成 float 向量，
     * 然后写入 PostgreSQL 的 pgvector，或者使用该向量执行相似度查询。</p>
     */
    private static class DashScopeEmbeddingModel implements EmbeddingModel {
        private static final Logger log = LoggerFactory.getLogger(DashScopeEmbeddingModel.class);
        private final String model;
        private final TextEmbedding textEmbedding = new TextEmbedding();

        private DashScopeEmbeddingModel(String model) {
            this.model = model;
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            // DashScope 的原生请求只需要模型名和文本列表，API Key 由 SDK 从 DASHSCOPE_API_KEY 读取。
            TextEmbeddingParam param = TextEmbeddingParam.builder()
                    .model(model)
                    .texts(request.getInstructions())
                    .build();
            try {
                TextEmbeddingResult result = textEmbedding.call(param);
                // DashScope 返回的是 List<Double>，Spring AI 要求每条结果使用 float[] 表示。
                List<Embedding> embeddings = result.getOutput().getEmbeddings().stream()
                        .map(this::toEmbedding)
                        .toList();
                return new EmbeddingResponse(embeddings);
            } catch (ApiException | NoApiKeyException exception) {
                log.error("DashScope Embedding 调用失败，model={}", model, exception);
                throw new IllegalStateException("调用阿里 DashScope Embedding 接口失败", exception);
            }
        }

        @Override
        public float[] embed(Document document) {
            return embed(document.getText());
        }

        private Embedding toEmbedding(TextEmbeddingResultItem item) {
            // 保留 DashScope 返回的文本索引，保证批量文本与向量结果的顺序对应。
            float[] vector = new float[item.getEmbedding().size()];
            for (int index = 0; index < item.getEmbedding().size(); index++) {
                vector[index] = item.getEmbedding().get(index).floatValue();
            }
            return new Embedding(vector, item.getTextIndex());
        }
    }
}
