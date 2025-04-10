package com.dihuan.judge.judgeStrategy;


import com.dihuan.model.vo.question.JudgeResultInfo;
import com.dihuan.model.judgeStrategy.JudgeContext;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeResultInfo doJudge(JudgeContext judgeContext);
}
