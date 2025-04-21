package com.dihuan.ai.service.impl;

import com.dihuan.ai.config.AIContextGenerator;
import com.dihuan.ai.model.AIContentItem;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIService;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AIThirdAPIService implements AIService {

    private final OpenAiChatModel chatModel;

    @Autowired
    private QuestionFeignClient questionFeignClient;

    @Autowired
    public AIThirdAPIService(@Qualifier("openAiChatModel")  OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String chat(ChatDto chatDto) {
        List<AIContentItem> messages = chatDto.getMessages();
        return this.chatModel.call(messages.toString());
    }

    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto) {
        List<AIContentItem> contextMessages = AIContextGenerator.GetFullContext(chatDto, questionFeignClient);
        return this.chatModel.stream(contextMessages.toString()).map(chatResponse -> new StreamResponse(chatResponse));
    }
}
