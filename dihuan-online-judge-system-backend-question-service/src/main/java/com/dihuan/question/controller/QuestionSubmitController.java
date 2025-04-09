package com.dihuan.question.controller;

import com.dihuan.common.result.Result;
import com.dihuan.model.dto.question.QuestionSubmitDto;
import com.dihuan.question.service.QuestionSubmitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "题目提交服务")
public class QuestionSubmitController {

    @Autowired
    private QuestionSubmitService questionSubmitService;

    @PostMapping("submitQuestionAnswer")
    @Operation(summary = "提交题目答案")
    public Result<Long> submitQuestionAnswer(@RequestBody QuestionSubmitDto questionSubmitDto) {
        Long submitId = questionSubmitService.submitQuestionAnswer(questionSubmitDto);
        return Result.success(submitId);
    }
}
