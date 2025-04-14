package com.dihuan.question.rabbitMq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionSubmitProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 判断题目
     * @param questionSubmitId 题目提交记录ID
     */
    public void sendQuestionToJudgeService(Long questionSubmitId){
        rabbitTemplate.convertAndSend("judge_exchange","dihuan",questionSubmitId);
    }
}
