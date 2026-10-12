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
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIThirdAPIService implements AIService {

    private static final Logger log = LoggerFactory.getLogger(AIThirdAPIService.class);

    private final ChatClient chatClient;

    @Autowired
    public AIThirdAPIService(@Qualifier("openAiChatModel") OpenAiChatModel chatModel,
                     RetrievalAugmentationAdvisor questionRagAdvisor,
                     ChatMemory chatMemory,
                     QuestionSubmissionTool questionSubmissionTool) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(questionSubmissionTool)
            .defaultAdvisors(
                questionRagAdvisor,
            MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String chat(ChatDto chatDto, Long userId) {
        log.info("[AI][OPENAI] call start: conversationId={}, model={}, userId={}, userMessage={}",
                chatDto.getConversationId(), chatDto.getModelName(), userId, chatDto.getMessage());
        try {
            String result = this.chatClient.prompt(AIContextGenerator.GetPrompt(chatDto))
                .messages(new UserMessage(ragMessage(chatDto)))
                .toolContext(toolContext(userId))
                .advisors(advisor -> advisor.param(
                    ChatMemory.CONVERSATION_ID,
                    chatDto.getConversationId()))
                .call()
                .content();
            log.info("[AI][OPENAI] call complete: conversationId={}, responseLength={}, response={}",
                    chatDto.getConversationId(), result == null ? 0 : result.length(), result);
            return result;
        } catch (ResourceAccessException exception) {
            log.error("[AI][OPENAI] call connection failed: conversationId={}", chatDto.getConversationId(), exception);
            return "AI服务暂时无法连接，请稍后重试。当前请求未能完成。";
        } catch (RuntimeException exception) {
            log.error("[AI][OPENAI] call failed: conversationId={}", chatDto.getConversationId(), exception);
            throw exception;
        }
    }

    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto, Long userId) {
        log.info("[AI][OPENAI] stream start: conversationId={}, model={}, userId={}, userMessage={}",
            chatDto.getConversationId(), chatDto.getModelName(), userId, chatDto.getMessage());
        try {
            return this.chatClient.prompt(AIContextGenerator.GetPrompt(chatDto))
                .messages(new UserMessage(ragMessage(chatDto)))
                .toolContext(toolContext(userId))
                .advisors(advisor -> advisor.param(
                            ChatMemory.CONVERSATION_ID,
                    chatDto.getConversationId()))
                .stream()
                .content()
                .map(StreamResponse::new)
                .doOnNext(response -> log.info("[AI][OPENAI] stream chunk: conversationId={}, content={}",
                    chatDto.getConversationId(), response.getContent()))
                .doOnComplete(() -> log.info("[AI][OPENAI] stream complete: conversationId={}",
                    chatDto.getConversationId()))
                .onErrorResume(exception -> {
                log.error("[AI][OPENAI] stream failed: conversationId={}, error={}",
                    chatDto.getConversationId(), exception.getMessage());
                return Flux.just(new StreamResponse("AI服务暂时无法连接，请稍后重试。当前请求未能完成。"));
                });
        } catch (RuntimeException exception) {
            log.error("[AI][OPENAI] stream setup failed: conversationId={}, error={}",
                chatDto.getConversationId(), exception.getMessage());
            return Flux.just(new StreamResponse("AI服务暂时无法连接，请稍后重试。当前请求未能完成。"));
        }
    }

    private String ragMessage(ChatDto chatDto) {
        return chatDto.getQuestionId() == null
                ? chatDto.getMessage()
                : "[questionId=" + chatDto.getQuestionId() + "] " + chatDto.getMessage();
    }

    /**
     * 构造 Spring AI 的 ToolContext，把当前登录用户ID传给工具。
     *
     * <p>不能依赖 ThreadLocal：流式调用时工具回调在 {@code Schedulers.boundedElastic()}
     * 线程执行，Tomcat 请求线程写入的 ThreadLocal 取不到，工具会误判为未登录。</p>
     */
    private Map<String, Object> toolContext(Long userId) {
        Map<String, Object> toolContext = new HashMap<>();
        toolContext.put(QuestionSubmissionTool.USER_ID_KEY, userId == null ? "" : String.valueOf(userId));
        return toolContext;
    }
}
