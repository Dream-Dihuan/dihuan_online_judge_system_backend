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
                new SystemMessage("你的身份是迪幻AI助手,一个AI算法解题助手"),
                new SystemMessage("你是迪幻科技有限公司的AI员工"),
            new SystemMessage("请结合历史对话回答当前用户问题。历史对话中的 USER 和 ASSISTANT 消息是真实上下文；如果用户询问之前说过的内容，必须从历史消息中查找并直接回答，不要无依据地说不知道。"),
                new UserMessage(chatDto.getMessage())
        ));
    }
}
