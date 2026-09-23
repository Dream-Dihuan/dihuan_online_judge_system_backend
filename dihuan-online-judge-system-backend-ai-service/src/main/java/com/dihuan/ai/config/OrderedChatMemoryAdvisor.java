package com.dihuan.ai.config;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE;

public class OrderedChatMemoryAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private final ChatMemory chatMemory;
    private final String defaultConversationId;
    private final int defaultRetrieveSize;

    public OrderedChatMemoryAdvisor(ChatMemory chatMemory) {
        this(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE);
    }

    public OrderedChatMemoryAdvisor(ChatMemory chatMemory, String defaultConversationId,
                                    int defaultRetrieveSize) {
        this.chatMemory = chatMemory;
        this.defaultConversationId = defaultConversationId;
        this.defaultRetrieveSize = defaultRetrieveSize;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest request, CallAroundAdvisorChain chain) {
        AdvisedRequest advisedRequest = addMemoryBeforeCurrentMessage(request);
        AdvisedResponse response = chain.nextAroundCall(advisedRequest);
        saveAssistantMessages(response);
        return response;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest request,
                                               StreamAroundAdvisorChain chain) {
        AdvisedRequest advisedRequest = addMemoryBeforeCurrentMessage(request);
        Flux<AdvisedResponse> responses = chain.nextAroundStream(advisedRequest);
        return new MessageAggregator().aggregateAdvisedResponse(responses, this::saveAssistantMessages);
    }

    private AdvisedRequest addMemoryBeforeCurrentMessage(AdvisedRequest request) {
        String conversationId = conversationId(request);
        List<Message> history = chatMemory.get(conversationId, retrieveSize(request));
        List<Message> messages = new ArrayList<>();
        request.messages().stream()
            .filter(message -> message.getMessageType() == MessageType.SYSTEM)
            .forEach(messages::add);
        history.stream()
            .filter(message -> message.getMessageType() != MessageType.SYSTEM)
            .forEach(messages::add);
        request.messages().stream()
            .filter(message -> message.getMessageType() != MessageType.SYSTEM)
            .forEach(messages::add);
        AdvisedRequest advisedRequest = AdvisedRequest.from(request).messages(messages).build();
        chatMemory.add(conversationId, new UserMessage(request.userText(), request.media()));
        return advisedRequest;
    }

    private void saveAssistantMessages(AdvisedResponse response) {
        List<Message> assistantMessages = response.response().getResults().stream()
                .map(generation -> (Message) generation.getOutput())
                .toList();
        if (!assistantMessages.isEmpty()) {
            chatMemory.add(conversationId(response.adviseContext()), assistantMessages);
        }
    }

    private String conversationId(AdvisedRequest request) {
        return conversationId(request.adviseContext());
    }

    private String conversationId(java.util.Map<String, Object> context) {
        Object value = context.get(CHAT_MEMORY_CONVERSATION_ID_KEY);
        return value == null ? defaultConversationId : value.toString();
    }

    private int retrieveSize(AdvisedRequest request) {
        Object value = request.adviseContext().get(CHAT_MEMORY_RETRIEVE_SIZE_KEY);
        return value == null ? defaultRetrieveSize : Integer.parseInt(value.toString());
    }
}