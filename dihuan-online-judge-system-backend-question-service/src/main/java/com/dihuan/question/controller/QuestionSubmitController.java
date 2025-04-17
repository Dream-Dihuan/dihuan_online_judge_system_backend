package com.dihuan.question.controller;

import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.Result;
import com.dihuan.model.dto.question.QuestionSubmitDto;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionNumberVo;
import com.dihuan.model.vo.question.QuestionSubmitListItemVo;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.question.service.QuestionSubmitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("getQuestionSubmitInfo")
    @Operation(summary = "获取题目提交信息")
    public Result<QuestionSubmitVo> getQuestionSubmitInfo(@RequestParam Long id) {
        QuestionSubmitVo questionSubmitInfoVo = questionSubmitService.getQuestionSubmitInfo(id);
        return Result.success(questionSubmitInfoVo);
    }

    @GetMapping("getPassedQuestionNumberList")
    @Operation(summary = "获取已通过题号列表")
    public Result<List<QuestionNumberVo>> getPassedQuestionNumberList(@RequestParam Long userId) {
        List<QuestionNumberVo> passedQuestionNumberList = questionSubmitService.getPassedQuestionNumberList(userId);
        return Result.success(passedQuestionNumberList);
    }

    @GetMapping("getTryedQuestionNumberList")
    @Operation(summary = "获取已尝试题号列表")
    public Result<List<QuestionNumberVo>> getTryedQuestionNumberList(@RequestParam Long userId) {
        List<QuestionNumberVo> tryedQuestionNumberList = questionSubmitService.getTryedQuestionNumberList(userId);
        return Result.success(tryedQuestionNumberList);
    }


    @GetMapping("getQuestionSubmitList")
    @Operation(summary = "获取提交记录列表")
    public Result<DihuanPage<QuestionSubmitListItemVo>> getQuestionSubmitList(@RequestParam(required = false) Long userId,
                                                                              @RequestParam(required = false) String title,
                                                                              @RequestParam(required = false) Long questionId,
                                                                              @RequestParam(required = false) String language,
                                                                              @RequestParam(required = false) Long questionResult,
                                                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        DihuanPage<QuestionSubmitListItemVo> questionSubmitList = questionSubmitService.getQuestionSubmitList(userId,title, questionId, language, questionResult, page, pageSize);
        return Result.success(questionSubmitList);
    }
}
