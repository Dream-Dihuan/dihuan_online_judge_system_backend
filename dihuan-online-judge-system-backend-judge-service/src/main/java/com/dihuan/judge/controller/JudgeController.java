package com.dihuan.judge.controller;

import com.dihuan.judge.service.JudgeService;
import com.dihuan.model.entity.QuestionSubmit;
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
    public QuestionSubmit questionSubmit(@RequestParam("questionSubmitId") Long questionSubmitId){
        QuestionSubmit questionSubmit = judgeService.doJudge(questionSubmitId);
        return questionSubmit;
    }
}
