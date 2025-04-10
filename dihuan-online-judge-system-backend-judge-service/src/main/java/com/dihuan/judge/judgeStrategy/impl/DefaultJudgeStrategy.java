package com.dihuan.judge.judgeStrategy.impl;

import com.dihuan.judge.judgeStrategy.JudgeStrategy;
import com.dihuan.model.codeSandbox.ExecuteCodeInfo;
import com.dihuan.model.entity.question.JudgeCase;
import com.dihuan.model.entity.question.JudgeCaseResult;
import com.dihuan.model.entity.question.JudgeConfig;
import com.dihuan.model.vo.question.JudgeResultInfo;
import com.dihuan.model.enums.question.JudgeResultEnum;
import com.dihuan.model.enums.question.QuestionResultEnum;
import com.dihuan.model.judgeStrategy.JudgeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeResultInfo doJudge(JudgeContext judgeContext) {
        QuestionResultEnum questionResultEnum = QuestionResultEnum.PASSED;

        List<JudgeCaseResult> judgeCaseResultList = new ArrayList<>();

        // 1.找出目标的判题标准
        String language = judgeContext.getLanguage();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        List<ExecuteCodeInfo> executeCodeInfoList = judgeContext.getExecuteCodeInfo();
        JudgeConfig judgeConfig = null;


        System.out.println(judgeContext.getJudgeConfigLst().get(0).getLanguage());
        System.out.println(judgeContext.getLanguage());


        for (JudgeConfig judgeConfigItem : judgeContext.getJudgeConfigLst()) {
            if(Objects.equals(judgeConfigItem.getLanguage(), language)){
                judgeConfig = judgeConfigItem;
            }
        }

        // 2、判断每个测试用例是否正确，是否超时
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCaseResult judgeCaseResult = new JudgeCaseResult();
            judgeCaseResult.setTime(executeCodeInfoList.get(i).getTime());
            judgeCaseResult.setMemory(executeCodeInfoList.get(i).getMemory());

            //判断是否超时
            if(judgeConfig.getTimeLimit()<executeCodeInfoList.get(i).getTime()){
                //超时
                judgeCaseResult.setJudgeResultEnum(JudgeResultEnum.TIME_LIMIT_EXCEEDED);
                questionResultEnum = QuestionResultEnum.FAILED;
                judgeCaseResultList.add(judgeCaseResult);
                continue;
            }
            if(judgeConfig.getMemoryLimit()<executeCodeInfoList.get(i).getMemory()){
                //超内存
                judgeCaseResult.setJudgeResultEnum(JudgeResultEnum.MEMORY_LIMIT_EXCEEDED);
                questionResultEnum = QuestionResultEnum.FAILED;
                judgeCaseResultList.add(judgeCaseResult);
                continue;
            }
            if(!judgeCaseList.get(i).getOutput().equals(executeCodeInfoList.get(i).getOutput())){
                //答案错误
                judgeCaseResult.setJudgeResultEnum(JudgeResultEnum.WRONG_ANSWER);
                questionResultEnum = QuestionResultEnum.FAILED;
                judgeCaseResultList.add(judgeCaseResult);
                continue;
            }
            judgeCaseResult.setJudgeResultEnum(JudgeResultEnum.ACCEPTED);
            judgeCaseResultList.add(judgeCaseResult);
        }

        JudgeResultInfo judgeResultInfo = new JudgeResultInfo();
        judgeResultInfo.setQuestionResult(questionResultEnum);
        judgeResultInfo.setJudgeCaseResultList(judgeCaseResultList);
        return judgeResultInfo;
    }
}
