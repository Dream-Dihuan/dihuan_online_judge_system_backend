package com.dihuan.question.controller;

import com.dihuan.common.result.Result;
import com.dihuan.question.service.QuestionCollectionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "题目收藏服务")
public class QuestionCollectionsController {

    @Autowired
    private QuestionCollectionsService questionCollectionsService;

    @Operation(summary = "收藏题目")
    @GetMapping("addQuestionCollections")
    public Result addQuestionCollections(@RequestParam Long questionId){
        questionCollectionsService.addQuestionCollections(questionId);
        return Result.success();
    }

    @Operation(summary = "取消收藏题目")
    @GetMapping("cancelQuestionCollections")
    public Result cancelQuestionCollections(@RequestParam Long questionId){
        questionCollectionsService.cancelQuestionCollections(questionId);
        return Result.success();
    }


}
