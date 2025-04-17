package com.dihuan.judge.config;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/judgeStatus/{questionSubmitId}")
public class WebSocket {

    // 使用 Map 维护用户 ID 和 Session 的映射关系
    private static Map<Long, Session> questionSubmitIdSessions = new ConcurrentHashMap<>();


    /**
     * 新建链接
     * @param questionSubmitId 提交记录id
     * @param session 对话session
     */
    @OnOpen
    public void onOpen(@PathParam("questionSubmitId") Long questionSubmitId, Session session){
        questionSubmitIdSessions.put(questionSubmitId,session);
    }

    /**
     * 断开链接
     */
    @OnClose
    public void onClose(@PathParam("questionSubmitId") Long questionSubmitId){
        questionSubmitIdSessions.remove(questionSubmitId);
    }

//    /**
//     * 接收消息
//     * @param message
//     */
//    @OnMessage
//    public void onMessage(@PathParam("questionSubmitId") Long questionSubmitId){
//
//    }

    /**
     * 发送到指定提交记录用户
     * @param questionSubmitId 题目提交记录id
     */
    public static void sendMessage(Long questionSubmitId){
        Session session = questionSubmitIdSessions.get(questionSubmitId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText("judge finished");
                System.out.println("消息已发送给题目提交记录: " + questionSubmitId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("题目提交记录 " + questionSubmitId + " 不在线");
        }
    }
}
