package com.dihuan.question.service.impl;

import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

    @Test
    void getQuestionList() {
        DihuanPage<QuestionVo> list = questionService.getQuestionList("", "简单", null, null, null, 1, 10);
        System.out.println(list);
    }
}