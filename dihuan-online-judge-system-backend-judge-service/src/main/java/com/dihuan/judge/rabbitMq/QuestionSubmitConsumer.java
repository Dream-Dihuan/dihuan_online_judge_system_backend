package com.dihuan.judge.rabbitMq;

import com.dihuan.judge.controller.JudgeController;
import com.dihuan.judge.service.JudgeService;
import com.dihuan.model.entity.Question;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class QuestionSubmitConsumer {

    @Autowired
    private JudgeController judgeController;

    @SneakyThrows
    @RabbitListener(queues = {"judge_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        System.out.println("receiveMessage message = " + message);
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeController.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
