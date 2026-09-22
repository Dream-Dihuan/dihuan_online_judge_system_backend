package com.dihuan.ai.config;

import com.dihuan.ai.model.AIContentItem;
import com.dihuan.ai.model.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

/**
 * AI上下文对话生成器
 */

public class AIContextGenerator {



    private static List<AIContentItem> system_context = new ArrayList<AIContentItem>() {{
        add(new AIContentItem("system", "你的身份是迪幻AI助手,一个AI算法解题助手"));
        add(new AIContentItem("system", "你是迪幻科技有限公司的AI员工"));
//        add(new AIContextEntity("system", "系统会传入一个对话列表，请根据对话列表回答用户的信息，其中列表的每一个元素对象中有两个属性，第一个role是角色，role的属性值可能有三个，system:系统管理员给你的设定，model:是你的角色，user:是用户的角色，对象的另一个content属性里的内容就是相应角色所发表的言论"));
//        add(new AIContextEntity("system", "列表的最后一条是用户当前和你的对话，请着重回答该信息，其他的对话信息是供你参考上下文的"));
    }};

    public static List<AIContentItem> GetFullContext(ChatDto chatDto){
        List<AIContentItem> list = chatDto.getMessages();
        List<AIContentItem> aiContextEntities = new ArrayList<>();
        aiContextEntities.addAll(system_context);
        aiContextEntities.addAll(list);
        System.out.println("对话上下文");
        System.out.println(aiContextEntities);
        return aiContextEntities;
    }

    public static Prompt GetPrompt(ChatDto chatDto) {
        List<Message> messages = GetFullContext(chatDto).stream()
                .map(AIContextGenerator::toMessage)
                .toList();
        return new Prompt(messages);
    }

    private static Message toMessage(AIContentItem item) {
        return switch (item.getRole()) {
            case "system" -> new SystemMessage(item.getContent());
            case "assistant", "model" -> new AssistantMessage(item.getContent());
            default -> new UserMessage(item.getContent());
        };
    }

    public static List<AIContentItem> TransformJsonObject (String messageJsonObject){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ChatDto messageObject = objectMapper.readValue(messageJsonObject, ChatDto.class);
            List<AIContentItem> aiContextItemList = messageObject.getMessages();
            return aiContextItemList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ChatDto getChatDto (String messageJsonObject){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ChatDto chatDto = objectMapper.readValue(messageJsonObject, ChatDto.class);
            return chatDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
