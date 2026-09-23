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
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AIOllamaService implements AIService {

    private ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;
    private final RetrievalAugmentationAdvisor questionRagAdvisor;
    private final OrderedChatMemoryAdvisor chatMemoryAdvisor;

    public AIOllamaService(ChatClient.Builder chatClient,
                           RetrievalAugmentationAdvisor questionRagAdvisor,
                           ChatMemory chatMemory){
        this.chatClientBuilder = chatClient;
        this.questionRagAdvisor = questionRagAdvisor;
        this.chatMemoryAdvisor = new OrderedChatMemoryAdvisor(chatMemory);
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
    public String chat( ChatDto chatDto){

        var prompt = AIContextGenerator.GetPrompt(chatDto);
        System.out.println(chatDto);
        SetAIModel(chatDto.getModelName());
        String result;
        try{
                result = chatClient.prompt(prompt)
                    .advisors(advisor -> advisor.param(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        chatDto.getConversationId()))
                    .call()
                    .content();
        }catch(Exception e){
            return"Exception";
        }
        return result;
    }

    /**
     * 流式响应接口
     *
     * @param chatDto 用户消息和模型信息
     * @return 流式响应内容
     */
    @Override
    public Flux<StreamResponse> chatStream(ChatDto chatDto) {
        var prompt = AIContextGenerator.GetPrompt(chatDto);
        System.out.println(chatDto);
        SetAIModel(chatDto.getModelName());

        try {
            return chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        chatDto.getConversationId()))
                .stream()
                    .content()
                    // 将每个字符串 chunk 包装成 StreamResponse 对象
                    .map(StreamResponse::new)
                    // 可选：打印日志以调试
                    .doOnNext(response -> System.out.println("Sending: " + response.getContent()))
                    // 错误处理
                    .onErrorResume(e -> Flux.just(new StreamResponse("Error: " + e.getMessage())));
        } catch (Exception e) {
            return Flux.just(new StreamResponse("Error: " + e.getMessage()));
        }
    }
}
