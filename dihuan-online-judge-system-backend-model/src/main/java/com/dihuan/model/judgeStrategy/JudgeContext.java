package com.dihuan.model.judgeStrategy;

import com.dihuan.model.codeSandbox.ExecuteCodeInfo;
import com.dihuan.model.entity.question.JudgeCase;
import com.dihuan.model.entity.question.JudgeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {

    private List<JudgeConfig> judgeConfigLst;

    private String language;

    private List<JudgeCase> judgeCaseList;

    private List<ExecuteCodeInfo> executeCodeInfo;
}
