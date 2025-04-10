package com.dihuan.judge.service.impl;

import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.judge.judgeStrategy.impl.DefaultJudgeStrategy;
import com.dihuan.judge.service.JudgeService;
import com.dihuan.model.codeSandbox.ExecuteCodeInfo;
import com.dihuan.model.codeSandbox.ExecuteCodeResponse;
import com.dihuan.model.codeSandbox.ExecuteCodeStatusEnum;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.JudgeResultInfo;
import com.dihuan.model.enums.question.JudgeStatusEnum;
import com.dihuan.model.judgeStrategy.JudgeContext;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private QuestionFeignClient questionFeignClient;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {


        // 1.获取题目的提交作答信息
        QuestionSubmit questionSubmitInfo = questionFeignClient.getQuestionSubmitInfo(questionSubmitId);
        if (questionSubmitInfo == null) {
            throw new DihuanException(ResultCodeEnum.FAIL, "获取题目提交信息失败");
        }

        // 2.获取题目信息
        Question questionInfo = questionFeignClient.getOriginQuestionInfo(questionSubmitInfo.getQuestionId());
        if (questionInfo == null) {
            throw new DihuanException(ResultCodeEnum.FAIL, "获取题目信息失败");
        }

        // 3.判断题目是否已经在判题中
        if (!questionSubmitInfo.getJudgeStatus().equals(JudgeStatusEnum.WAITING)) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目正在判题中");
        }
        // 设置题目提交记录正在判题中，并更新到数据库，防止重复执行
        questionSubmitInfo.setJudgeStatus(JudgeStatusEnum.JUDGING);
        Boolean firstUpdated = questionFeignClient.updateQuestionSubmitInfo(questionSubmitInfo);
        if (!firstUpdated) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目提交记录更新失败");
        }

        // 4.调用沙箱进行代码运行，并获得返回的结果

//        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        List<String> inputList = questionInfo.getJudgeCase().stream().map(JudgeCase::getInput).collect(Collectors.toList());
//        executeCodeRequest.setInputList(inputList);
//        executeCodeRequest.setCode(questionSubmitInfo.getCode());
//        executeCodeRequest.setLanguage(questionSubmitInfo.getLanguage());
//
//        CodeSandbox sandbox = CodeSandboxFactory.newInstance("remote");
//        ExecuteCodeResponse executeCodeResponse = sandbox.executeCode(executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage("测试");
        executeCodeResponse.setStatus(ExecuteCodeStatusEnum.SUCCESS);
        executeCodeResponse.setExecuteCodeInfo(
                new ArrayList<ExecuteCodeInfo>(){
                    {
                        add(new ExecuteCodeInfo("3", 1L, 1L));
                        add(new ExecuteCodeInfo("5",9L,99L));
                    }
                }
        );


        // 5.判断答案是否正确
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeConfigLst(questionInfo.getJudgeConfig());
        judgeContext.setLanguage(questionSubmitInfo.getLanguage());
        judgeContext.setJudgeCaseList(questionInfo.getJudgeCase());
        judgeContext.setExecuteCodeInfo(executeCodeResponse.getExecuteCodeInfo());

        System.out.println("ok");
        JudgeResultInfo judgeResultInfo = new DefaultJudgeStrategy().doJudge(judgeContext);

        // 修改数据库中的结果
        questionSubmitInfo.setQuestionResult(judgeResultInfo.getQuestionResult());
        questionSubmitInfo.setJudgeResultInfo(judgeResultInfo.getJudgeCaseResultList());
        questionSubmitInfo.setJudgeStatus(JudgeStatusEnum.FINISHED);
        Boolean SecondUpdated = questionFeignClient.updateQuestionSubmitInfo(questionSubmitInfo);
        if (!SecondUpdated) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目提交记录更新失败");
        }

        return questionSubmitInfo;
    }
}
