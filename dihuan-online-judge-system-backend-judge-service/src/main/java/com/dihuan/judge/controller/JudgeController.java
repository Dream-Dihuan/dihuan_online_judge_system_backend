package com.dihuan.judge.controller;

import com.dihuan.common.result.Result;
import com.dihuan.judge.config.WebSocket;
import com.dihuan.judge.service.JudgeService;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "判题服务")
public class JudgeController {

    @Autowired
    private JudgeService judgeService;



    @GetMapping("doJudge")
    @Operation(summary = "判题")
    public QuestionSubmitVo doJudge(@RequestParam("questionSubmitId") Long questionSubmitId){
        QuestionSubmitVo questionSubmitVo = judgeService.doJudge(questionSubmitId);
        WebSocket.sendMessage(questionSubmitId);
        return questionSubmitVo;
    }

    @GetMapping("testWebSocket")
    @Operation(summary = "WebSocket测试")
    private Result testWebSocket(Long questionSubmitId){
        WebSocket.sendMessage(questionSubmitId);
        return Result.success();
    }
}
