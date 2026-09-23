package com.dihuan.ai.controller;

import com.dihuan.ai.model.ChatDto;
import com.dihuan.ai.model.AIConversationDetailVo;
import com.dihuan.ai.model.AIConversationListItem;
import com.dihuan.ai.model.StreamResponse;
import com.dihuan.ai.service.AIConversationService;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.Result;
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

import java.util.List;

@RestController
@RequestMapping("/")
public class AIController {

    @Autowired
    private AIThirdAPIService aiService;
    @Autowired
    private AIConversationService conversationService;
//    private AIOllamaService aiService;

    @PostMapping("/chat")
    public String chat(@Valid @RequestBody ChatDto chatDto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        conversationService.ensureConversation(userId, chatDto);
        String result = aiService.chat(chatDto);
        conversationService.touchConversation(chatDto.getConversationId());
        return result;
    }
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<StreamResponse> chatStream(@Valid @RequestBody ChatDto chatDto,
                                            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        conversationService.ensureConversation(userId, chatDto);
        return aiService.chatStream(chatDto)
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

    private Long getCurrentUserId(HttpServletRequest request) {
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();
        if (tokenInfo == null) {
            String token = request.getHeader("dihuan_oj_system_token");
            Claims claims = JwtUtils.parseToken(token);
            tokenInfo = JwtUtils.getTokenInfo(claims);
            TokenInfoHolder.setLoginAdminUser(tokenInfo);
        }
        return tokenInfo.getId();
    }

}
