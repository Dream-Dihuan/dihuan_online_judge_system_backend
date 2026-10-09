package com.dihuan.ai.service.impl;

import com.dihuan.ai.config.AIContextGenerator;
import com.dihuan.ai.config.OrderedChatMemoryAdvisor;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIService;
import com.dihuan.ai.tools.QuestionSubmissionTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AIThirdAPIService implements AIService {

    private final ChatClient chatClient;

    private final QuestionSubmissionTool questionSubmissionTool;

    @Autowired
    public AIThirdAPIService(@Qualifier("openAiChatModel") OpenAiChatModel chatModel,
                     RetrievalAugmentationAdvisor questionRagAdvisor,
                     ChatMemory chatMemory,
                     QuestionSubmissionTool questionSubmissionTool) {
        this.questionSubmissionTool = questionSubmissionTool;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(questionSubmissionTool)
            .defaultAdvisors(
                questionRagAdvisor,
                new OrderedChatMemoryAdvisor(chatMemory, "default", 4))
                .build();
    }

    @Override
    public String chat(ChatDto chatDto) {
        try {
            return this.chatClient.prompt(AIContextGenerator.GetPrompt(chatDto))
                .messages(new UserMessage(chatDto.getMessage()))
                .advisors(advisor -> advisor.param(
                    AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                    chatDto.getConversationId()))
                .call()
                .content();
        } catch (ResourceAccessException exception) {
            return "AI服务暂时无法连接，请稍后重试。当前请求未能完成。";
        }
    }

    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto) {
        return Flux.just(new StreamResponse(chat(chatDto)));
    }
}
