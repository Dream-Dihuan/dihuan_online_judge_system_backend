package com.dihuan.model.entity.question;

import com.dihuan.model.enums.question.JudgeResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeCaseResult implements Serializable {
    private JudgeResultEnum judgeResultEnum;

    private Long time;

    private Long memory;
}
