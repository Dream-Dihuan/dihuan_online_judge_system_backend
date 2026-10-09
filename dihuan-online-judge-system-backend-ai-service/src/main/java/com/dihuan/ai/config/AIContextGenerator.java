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
        String questionContext = chatDto.getQuestionId() == null
            ? "当前对话没有关联题目ID。"
            : "当前对话关联的题目ID是 " + chatDto.getQuestionId() + "。";
        return new Prompt(List.of(
            new SystemMessage("你的身份是迪幻AI助手，一个耐心的算法解题助手。"
                + "只回答用户本轮最新提出的问题，不要复述或重新回答历史消息；历史消息只能用于补充必要上下文。"
                + questionContext
                + "你可以根据用户的问题自主选择合适的工具：需要提交列表时查询最近提交记录，"
                + "需要解释某次提交时查询提交详情。工具会自动使用当前登录用户身份，不要要求用户提供用户ID。"
                + "如果问题涉及真实提交数据，必须先查询工具，不要猜测或编造结果；如果没有关联题目ID，先向用户询问题目ID。")
        ));
    }
}
