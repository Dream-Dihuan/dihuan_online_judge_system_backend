package com.dihuan.ai.service.impl;

import com.dihuan.ai.config.AIContextGenerator;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIService;
import com.dihuan.ai.tools.QuestionSubmissionTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIOllamaService implements AIService {

    private static final Logger log = LoggerFactory.getLogger(AIOllamaService.class);

    private ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;
    private final RetrievalAugmentationAdvisor questionRagAdvisor;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    public AIOllamaService(ChatClient.Builder chatClient,
                           RetrievalAugmentationAdvisor questionRagAdvisor,
                           ChatMemory chatMemory){
        this.chatClientBuilder = chatClient;
        this.questionRagAdvisor = questionRagAdvisor;
        this.chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.chatClient = buildChatClient();
    }

    private void SetAIModel(String modelName){
        OllamaOptions ollamaOptions = new OllamaOptions();
        ollamaOptions.setModel(modelName);
        this.chatClient = chatClientBuilder
                .defaultOptions(ollamaOptions)
                .defaultAdvisors(questionRagAdvisor, chatMemoryAdvisor)
                .build();
    }

    private ChatClient buildChatClient() {
        return chatClientBuilder
                .defaultAdvisors(questionRagAdvisor, chatMemoryAdvisor)
                .build();
    }


    @Override
    public String chat(ChatDto chatDto, Long userId) {

        var prompt = AIContextGenerator.GetPrompt(withRagQuestionId(chatDto));
        log.info("[AI][OLLAMA] call start: conversationId={}, model={}, userId={}, userMessage={}",
            chatDto.getConversationId(), chatDto.getModelName(), userId, chatDto.getMessage());
        SetAIModel(chatDto.getModelName());
        String result;
        try{
                result = chatClient.prompt(prompt)
                    .toolContext(toolContext(userId))
                    .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        chatDto.getConversationId()))
                    .call()
                    .content();
        }catch(Exception e){
            log.error("[AI][OLLAMA] call failed: conversationId={}", chatDto.getConversationId(), e);
            return"Exception";
        }
        log.info("[AI][OLLAMA] call complete: conversationId={}, responseLength={}, response={}",
                chatDto.getConversationId(), result == null ? 0 : result.length(), result);
        return result;
    }

    /**
     * 流式响应接口
     *
     * @param chatDto 用户消息和模型信息
     * @return 流式响应内容
     */
    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto, Long userId) {
        var prompt = AIContextGenerator.GetPrompt(withRagQuestionId(chatDto));
        log.info("[AI][OLLAMA] stream start: conversationId={}, model={}, userId={}, userMessage={}",
            chatDto.getConversationId(), chatDto.getModelName(), userId, chatDto.getMessage());
        SetAIModel(chatDto.getModelName());

        try {
            return chatClient.prompt(prompt)
                .toolContext(toolContext(userId))
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        chatDto.getConversationId()))
                .stream()
                    .content()
                    // 将每个字符串 chunk 包装成 StreamResponse 对象
                    .map(StreamResponse::new)
                    // 可选：打印日志以调试
                        .doOnNext(response -> log.info("[AI][OLLAMA] stream chunk: conversationId={}, content={}",
                            chatDto.getConversationId(), response.getContent()))
                        .doOnComplete(() -> log.info("[AI][OLLAMA] stream complete: conversationId={}",
                            chatDto.getConversationId()))
                    // 错误处理
                        .onErrorResume(e -> {
                        log.error("[AI][OLLAMA] stream failed: conversationId={}", chatDto.getConversationId(), e);
                        return Flux.just(new StreamResponse("Error: " + e.getMessage()));
                        });
        } catch (Exception e) {
                    log.error("[AI][OLLAMA] stream setup failed: conversationId={}", chatDto.getConversationId(), e);
            return Flux.just(new StreamResponse("Error: " + e.getMessage()));
        }
    }

    private ChatDto withRagQuestionId(ChatDto chatDto) {
        if (chatDto.getQuestionId() == null || chatDto.getMessage().startsWith("[questionId=")) {
            return chatDto;
        }
        ChatDto copy = new ChatDto();
        copy.setConversationId(chatDto.getConversationId());
        copy.setModelName(chatDto.getModelName());
        copy.setQuestionId(chatDto.getQuestionId());
        copy.setMessage("[questionId=" + chatDto.getQuestionId() + "] " + chatDto.getMessage());
        return copy;
    }

    /**
     * 构造 Spring AI 的 ToolContext，把当前登录用户ID传给工具。
     * 流式调用时工具回调在 boundedElastic 线程执行，取不到 Tomcat 请求线程的 ThreadLocal。
     */
    private Map<String, Object> toolContext(Long userId) {
        Map<String, Object> toolContext = new HashMap<>();
        toolContext.put(QuestionSubmissionTool.USER_ID_KEY, userId == null ? "" : String.valueOf(userId));
        return toolContext;
    }
}
