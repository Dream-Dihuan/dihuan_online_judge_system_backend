package com.dihuan.serviceClient.service;

import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.ibatis.annotations.Update;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dihuan-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("getOriginQuestionInfo")
    @Operation(summary = "获取题目信息")
    Question getOriginQuestionInfo(@RequestParam("id") Long id);

    @GetMapping("getQuestionSubmitInfo")
    @Operation(summary = "获取题目提交信息")
    QuestionSubmitVo getQuestionSubmitInfo(@RequestParam("id") Long id);

    @PostMapping("updateQuestionSubmit")
    @Operation(summary = "更新题目提交信息")
    Boolean updateQuestionSubmitInfo(@RequestBody QuestionSubmit questionSubmit);

    @GetMapping("firstPassTheQuestion")
    @Operation(summary = "是否第一次通过该题目")
    Boolean firstPassTheQuestion(@RequestParam("questionId") Long questionId,@RequestParam("userId") Long userId);

    @GetMapping("incrementSubmitCount")
    @Operation(summary = "增加题目提交数")
    void incrementSubmitCount(@RequestParam("questionId") Long questionId);

    // 仅增加通过数
    @GetMapping("incrementAcceptedCount")
    @Operation(summary = "增加题目通过数")
    void incrementAcceptedCount(@RequestParam("questionId") Long questionId);
}
