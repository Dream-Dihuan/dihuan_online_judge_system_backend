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

@RestController
@RequestMapping("/")
@Tag(name = "题目服务")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }

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
                                                          @RequestParam(defaultValue = "1")Integer page,
                                                          @RequestParam(defaultValue = "20")Integer pageSize) {
        DihuanPage<QuestionVo> pageResult =  questionService.getQuestionList(title,tag,id,authorId,collected,page,pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取题目信息详情")
    @GetMapping("getQuestionInfo/{id}")
    public Result<QuestionInfoVo> getQuestionInfo(@PathVariable Long id) {
        QuestionInfoVo questionInfoVo = questionService.getQuestionInfo(id);
        return Result.success(questionInfoVo);
    }

    @GetMapping("test")
    public Result test(){
        Question question = new Question();
        question.setTitle("ok");
        question.setContent("ok");
        question.setTags("['简单']");
        question.setJudgeCase(new ArrayList<>(){
            {
                add(new JudgeCase("1 2","3"));
            }
        });
        question.setJudgeConfig(new ArrayList<>(){
            {
                add(new JudgeConfig("java",1000L,1000L));
            }
        });
        question.setAcceptedNumber(100L);
        question.setSubmitNumber(100L);
        question.setAuthorId(100L);

        questionService.save(question);
        return Result.success();
    }

}
