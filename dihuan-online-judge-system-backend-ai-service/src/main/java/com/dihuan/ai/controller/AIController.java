package com.dihuan.ai.controller;

import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.AIConversationDetailVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIConversationService;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.Result;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.jsonWebTokenUtils.JwtUtils;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import io.jsonwebtoken.Claims;
import com.dihuan.ai.service.impl.AIThirdAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/")
public class AIController {

    private static final Logger log = LoggerFactory.getLogger(AIController.class);

    @Autowired
    private AIThirdAPIService aiService;
    @Autowired
    private AIConversationService conversationService;
//    private AIOllamaService aiService;

    @PostMapping("/chat")
    public String chat(@Valid @RequestBody ChatDto chatDto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("[AI] chat request: userId={}, conversationId={}, questionId={}, model={}, message={}",
            userId, chatDto.getConversationId(), chatDto.getQuestionId(), chatDto.getModelName(), chatDto.getMessage());
        conversationService.ensureConversation(userId, chatDto);
        String result = aiService.chat(chatDto, userId);
        conversationService.touchConversation(chatDto.getConversationId());
        log.info("[AI] chat response: conversationId={}, length={}, content={}",
            chatDto.getConversationId(), result == null ? 0 : result.length(), result);
        return result;
    }
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<StreamResponse> chatStream(@Valid @RequestBody ChatDto chatDto,
                                            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("[AI] stream request: userId={}, conversationId={}, questionId={}, model={}, message={}",
                userId, chatDto.getConversationId(), chatDto.getQuestionId(), chatDto.getModelName(), chatDto.getMessage());
        conversationService.ensureConversation(userId, chatDto);
        return aiService.chatStream(chatDto, userId)
            .doOnNext(response -> log.info("[AI] stream response chunk: conversationId={}, content={}",
                chatDto.getConversationId(), response.getContent()))
            .doOnComplete(() -> conversationService.touchConversation(
                chatDto.getConversationId()));
    }

    @GetMapping("/conversation/list")
    public Result<List<AIConversationListItem>> getConversationList(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(conversationService.getUserConversations(userId));
    }

    @GetMapping("/conversation/{conversationId}")
    public Result<AIConversationDetailVo> getConversationDetail(
            @PathVariable String conversationId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        AIConversationDetailVo detail = conversationService
                .getConversationDetail(userId, conversationId);
        if (detail == null) {
            return Result.fail(404, "AI会话不存在或无权访问");
        }
        return Result.success(detail);
    }

    /**
     * 从当前请求头解析登录用户ID。
     *
     * <p>每次请求都直接解析 token，不再读写 ThreadLocal：ThreadLocal 在请求线程上会残留，
     * 下一个请求可能拿到上一个用户的登录态（串用户）。工具调用所需的用户身份已改为
     * 通过 Spring AI 的 toolContext 传递，不依赖 ThreadLocal。</p>
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("dihuan_oj_system_token");
        Claims claims = JwtUtils.parseToken(token);
        TokenInfo tokenInfo = JwtUtils.getTokenInfo(claims);
        if (tokenInfo == null || tokenInfo.getId() == null || tokenInfo.getId() <= 0) {
            throw new DihuanException(ResultCodeEnum.FAIL, "未登录");
        }
        return tokenInfo.getId();
    }

}
