package com.dihuan.ai.service.impl;

import com.dihuan.ai.config.AIContextGenerator;
import com.dihuan.ai.config.OrderedChatMemoryAdvisor;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AIThirdAPIService implements AIService {

    private final ChatClient chatClient;

    @Autowired
    public AIThirdAPIService(@Qualifier("openAiChatModel") OpenAiChatModel chatModel,
                     RetrievalAugmentationAdvisor questionRagAdvisor,
                     ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel)
            .defaultAdvisors(
                questionRagAdvisor,
                new OrderedChatMemoryAdvisor(chatMemory))
                .build();
    }

    @Override
    public String chat(ChatDto chatDto) {
        return this.chatClient.prompt(AIContextGenerator.GetPrompt(chatDto))
            .messages(new UserMessage(chatDto.getMessage()))
            .advisors(advisor -> advisor.param(
                AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                chatDto.getConversationId()))
            .call()
            .content();
    }

    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto) {
        return this.chatClient.prompt(AIContextGenerator.GetPrompt(chatDto))
            .messages(new UserMessage(chatDto.getMessage()))
            .advisors(advisor -> advisor.param(
                AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                chatDto.getConversationId()))
            .stream()
            .content()
            .map(StreamResponse::new);
    }
}
