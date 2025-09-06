package com.dihuan.question.controller;

import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.Result;
import com.dihuan.model.dto.question.AddOrUpdateQuestionInfoDto;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.question.JudgeCase;
import com.dihuan.model.entity.question.JudgeConfig;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Tag(name = "题目服务")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Operation(summary = "添加或修改题目信息")
    @PostMapping("AddOrUpdateQuestionInfo")
    public Result AddOrUpdateQuestionInfo(@RequestBody AddOrUpdateQuestionInfoDto addOrUpdateQuestionInfoDto) {
        questionService.AddOrUpdateQuestionInfo(addOrUpdateQuestionInfoDto);
        return Result.success();
    }

    @Operation(summary = "获取题目信息列表")
    @GetMapping("getQuestionList")
    public Result<DihuanPage<QuestionVo>> getQuestionList(@RequestParam(required = false)String title,
                                                          @RequestParam(required = false)String tag,
                                                          @RequestParam(required = false)Long id,
                                                          @RequestParam(required = false)Long authorId,
                                                          @RequestParam(required = false)Boolean collected,
                                                          @RequestParam(required = false)Boolean subscribeUser,
                                                          @RequestParam(required = false)Long checkStatus,
                                                          @RequestParam(defaultValue = "1")Integer page,
                                                          @RequestParam(defaultValue = "20")Integer pageSize) {
        DihuanPage<QuestionVo> pageResult =  questionService.getQuestionList(title,tag,id,authorId,collected,subscribeUser,checkStatus,page,pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取题目信息详情")
    @GetMapping("getQuestionInfo")
    public Result<QuestionInfoVo> getQuestionInfo(@RequestParam Long id,@RequestParam(required = false,defaultValue = "1") Long checkStatus) {
        QuestionInfoVo questionInfoVo = questionService.getQuestionInfo(id,checkStatus);
        return Result.success(questionInfoVo);
    }

    @Operation(summary = "获取题目原始信息详情")
    @GetMapping("getOriginQuestionInfo")
    public Result<Question> getOriginQuestionInfo(@RequestParam Long id,@RequestParam(required = false) Long checkStatus) {
        Question questionInfo = questionService.getOriginQuestionInfo(id,checkStatus);
        return Result.success(questionInfo);
    }



    @Operation(summary = "删除题目信息")
    @DeleteMapping("deleteQuestionInfo")
    public Result deleteQuestionInfo(@RequestParam Long id) {
        questionService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "更改题目审核状态")
    @GetMapping("updateCheckStatus")
    public Result updateCheckStatus(@RequestParam Long id,@RequestParam Long checkStatus) {
        questionService.updateCheckStatus(id,checkStatus);
        return Result.success();
    }

    @Operation(summary = "批量导入题目信息")
    @PostMapping("importQuestionInfo")
    public Result importQuestionInfo(@RequestParam Long authorId,@RequestParam(required = false,defaultValue = "0") Long checkStatus,@RequestBody List<Question> questionList) {
        questionService.importQuestionInfo(authorId,checkStatus,questionList);
        return Result.success();
    }
}
