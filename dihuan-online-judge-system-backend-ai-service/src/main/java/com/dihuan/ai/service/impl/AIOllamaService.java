package com.dihuan.ai.service.impl;

import com.dihuan.ai.config.AIContextGenerator;
import com.dihuan.ai.model.AIContentItem;
import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIService;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;


@Service
public class AIOllamaService implements AIService {

    private ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;

    @Autowired
    private QuestionFeignClient questionFeignClient;


    public AIOllamaService(ChatClient.Builder chatClient){
        this.chatClientBuilder = chatClient;
        this.chatClient=chatClient.build();
    }

    private void SetAIModel(String modelName){
        OllamaOptions ollamaOptions = new OllamaOptions();
        ollamaOptions.setModel(modelName);
        this.chatClient=chatClientBuilder.defaultOptions(ollamaOptions).build();
    }


    @Override
    public String chat( ChatDto chatDto){

        List<AIContentItem> aiContextEntities = AIContextGenerator.GetFullContext(chatDto,questionFeignClient);
        System.out.println(chatDto);
        SetAIModel(chatDto.getModelName());
        String result;
        try{
            result=chatClient.prompt().user(aiContextEntities.toString()).call().content();
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
        List<AIContentItem> aiContextEntities = AIContextGenerator.GetFullContext(chatDto,questionFeignClient);
        System.out.println(chatDto);
        SetAIModel(chatDto.getModelName());

        try {
            return chatClient.prompt()
                    .user(aiContextEntities.toString())
                    .user("你好")
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
