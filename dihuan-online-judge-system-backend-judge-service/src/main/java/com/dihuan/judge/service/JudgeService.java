package com.dihuan.judge.service;

import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionSubmitVo;

public interface JudgeService {
    QuestionSubmitVo doJudge(Long questionSubmitId);
}
