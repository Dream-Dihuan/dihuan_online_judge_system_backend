package com.dihuan.ai.controller;

import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.impl.AIOllamaService;
import com.dihuan.ai.service.impl.AIThirdAPIService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
public class AIController {

    @Autowired
    private AIThirdAPIService aiService;
//    private AIOllamaService aiService;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatDto chatDto) {
        return aiService.chat(chatDto);
    }
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<StreamResponse> chatStream(@RequestBody ChatDto chatDto) {
        return aiService.chatStream(chatDto);
    }

}
