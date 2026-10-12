package com.dihuan.ai.service;

import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.StreamResponse;
import reactor.core.publisher.Flux;

public interface AIService {

    /**
     * 执行一次对话。
     *
     * @param chatDto 对话参数
     * @param userId  当前登录用户ID，用于工具调用时校验身份
     */
    String chat(ChatDto chatDto, Long userId);

    /**
     * 流式对话。
     *
     * @param chatDto 对话参数
     * @param userId  当前登录用户ID，用于工具调用时校验身份
     */
    Flux<StreamResponse> chatStream(ChatDto chatDto, Long userId);
}
