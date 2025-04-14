package com.dihuan.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.model.dto.question.QuestionSubmitDto;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionNumberVo;

import java.util.List;

/**
* @author 迪幻
* @description 针对表【question_submit(题目答案提交记录表)】的数据库操作Service
* @createDate 2025-04-09 22:26:36
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    Long submitQuestionAnswer(QuestionSubmitDto questionSubmitDto);

    QuestionSubmit getQuestionSubmitInfo(Long id);

    List<QuestionNumberVo> getPassedQuestionNumberList();

    List<QuestionNumberVo> getTryedQuestionNumberList();
}
