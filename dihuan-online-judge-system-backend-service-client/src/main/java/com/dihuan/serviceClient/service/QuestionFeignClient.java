package com.dihuan.serviceClient.service;

import com.dihuan.common.result.Result;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dihuan-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("getOriginQuestionInfo/{id}")
    @Operation(summary = "获取题目信息")
    Question getOriginQuestionInfo(@PathVariable("id") Long id);

    @GetMapping("getQuestionSubmitInfo/{id}")
    @Operation(summary = "获取题目提交信息")
    QuestionSubmit getQuestionSubmitInfo(@PathVariable("id") Long id);

    @PostMapping("updateQuestionSubmit")
    @Operation(summary = "更新题目提交信息")
    Boolean updateQuestionSubmitInfo(@RequestBody QuestionSubmit questionSubmit);

}
