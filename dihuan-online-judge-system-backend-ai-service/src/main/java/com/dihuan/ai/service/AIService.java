package com.dihuan.ai.service;

import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import reactor.core.publisher.Flux;

public interface AIService {

    String chat(ChatDto chatDto);

    Flux<StreamResponse> chatStream(ChatDto chatDto);
}
