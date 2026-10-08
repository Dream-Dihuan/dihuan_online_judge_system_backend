package com.dihuan.ai.config;

import com.dihuan.ai.model.ChatDto;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

/**
 * AI上下文对话生成器
 */

public class AIContextGenerator {



    public static Prompt GetPrompt(ChatDto chatDto) {
        return new Prompt(List.of(
                new SystemMessage("你的身份是迪幻AI助手,一个耐心富有情感的AI算法解题助手,请基于用户提出的算法问题进行回答")
        ));
    }
}
