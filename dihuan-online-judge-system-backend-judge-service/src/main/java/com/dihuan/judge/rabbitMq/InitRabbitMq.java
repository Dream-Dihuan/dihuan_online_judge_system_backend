package com.dihuan.judge.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class InitRabbitMq {

    public static void doInit() {
        try {
            // todo 改ip
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("dihuan");
            factory.setPassword("dihuan");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String EXCHANGE_NAME = "judge_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "judge_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "dihuan");
            System.out.println("消息队列启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("消息队列启动失败");
        }
    }

    public static void main(String[] args) {
        doInit();
    }
}