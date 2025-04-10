package com.dihuan.model.vo.question;

import com.dihuan.model.entity.question.JudgeCaseResult;
import com.dihuan.model.enums.question.QuestionResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JudgeResultInfo implements Serializable {
    private QuestionResultEnum questionResult;

    private List<JudgeCaseResult> judgeCaseResultList;
}
