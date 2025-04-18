package com.dihuan.judge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.judge.codeSandbox.CodeSandbox;
import com.dihuan.judge.codeSandbox.CodeSandboxFactory;
import com.dihuan.judge.judgeStrategy.impl.DefaultJudgeStrategy;
import com.dihuan.judge.service.JudgeService;
import com.dihuan.model.codeSandbox.ExecuteCodeInfo;
import com.dihuan.model.codeSandbox.ExecuteCodeRequest;
import com.dihuan.model.codeSandbox.ExecuteCodeResponse;
import com.dihuan.model.codeSandbox.ExecuteCodeStatusEnum;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.entity.question.JudgeCase;
import com.dihuan.model.enums.question.QuestionResultEnum;
import com.dihuan.model.vo.question.JudgeResultInfo;
import com.dihuan.model.enums.question.JudgeStatusEnum;
import com.dihuan.model.judgeStrategy.JudgeContext;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import com.dihuan.serviceClient.service.UserFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private QuestionFeignClient questionFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public QuestionSubmitVo doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();

        // 1.获取题目的提交作答信息
        QuestionSubmitVo questionSubmitInfoVo = questionFeignClient.getQuestionSubmitInfo(questionSubmitId);
        if (questionSubmitInfoVo == null) {
            throw new DihuanException(ResultCodeEnum.FAIL, "获取题目提交信息失败");
        }

        // 2.获取题目信息
        Question questionInfo = questionFeignClient.getOriginQuestionInfo(questionSubmitInfoVo.getQuestionId());
        if (questionInfo == null) {
            throw new DihuanException(ResultCodeEnum.FAIL, "获取题目信息失败");
        }

        // 3.判断题目是否已经在判题中
        if (!questionSubmitInfoVo.getJudgeStatus().equals(JudgeStatusEnum.WAITING)) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目正在判题中");
        }
        // 设置题目提交记录正在判题中，并更新到数据库，防止重复执行

            // 题目提交数+1
            questionFeignClient.incrementSubmitCount(questionSubmitInfoVo.getQuestionId());

        questionSubmitInfoVo.setJudgeStatus(JudgeStatusEnum.JUDGING);
        BeanUtils.copyProperties(questionSubmitInfoVo, questionSubmit);
        Boolean firstUpdated = questionFeignClient.updateQuestionSubmitInfo(questionSubmit);
        if (!firstUpdated) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目提交记录更新失败");
        }

        // 4.调用沙箱进行代码运行，并获得返回的结果

        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> inputList = questionInfo.getJudgeCase().stream().map(JudgeCase::getInput).collect(Collectors.toList());
        executeCodeRequest.setInputList(inputList);
        executeCodeRequest.setCode(questionSubmitInfoVo.getCode());
        executeCodeRequest.setLanguage(questionSubmitInfoVo.getLanguage());
//
        CodeSandbox sandbox = CodeSandboxFactory.newInstance("remote");
        ExecuteCodeResponse executeCodeResponse = sandbox.executeCode(executeCodeRequest);


        // 5.判断答案是否正确
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeConfigLst(questionInfo.getJudgeConfig());
        judgeContext.setLanguage(questionSubmitInfoVo.getLanguage());
        judgeContext.setJudgeCaseList(questionInfo.getJudgeCase());
        judgeContext.setExecuteCodeInfo(executeCodeResponse.getExecuteCodeInfo());
        judgeContext.setExecuteCodeStatusEnum(executeCodeResponse.getStatus());

        System.out.println("ok");
        JudgeResultInfo judgeResultInfo = new DefaultJudgeStrategy().doJudge(judgeContext);
        System.out.println("判题完成");
        System.out.println(judgeResultInfo);

        // 修改数据库中的结果
        questionSubmitInfoVo.setQuestionResult(judgeResultInfo.getQuestionResult());
        questionSubmitInfoVo.setJudgeResultInfo(judgeResultInfo.getJudgeCaseResultList());
        questionSubmitInfoVo.setJudgeStatus(JudgeStatusEnum.FINISHED);
        BeanUtils.copyProperties(questionSubmitInfoVo, questionSubmit);
        Boolean SecondUpdated = questionFeignClient.updateQuestionSubmitInfo(questionSubmit);
        if (!SecondUpdated) {
            throw new DihuanException(ResultCodeEnum.FAIL, "题目提交记录更新失败");
        }



        // 判断用户是否做题正确，正确则增加题目通过数
        if (judgeResultInfo.getQuestionResult().equals(QuestionResultEnum.PASSED)) {
            questionFeignClient.incrementAcceptedCount(questionSubmitInfoVo.getQuestionId());
        }




        // 判断该用户是否第一次通过此题，是的话就增加经验值
        Long userId = questionSubmitInfoVo.getUserId();
        Long questionId = questionSubmitInfoVo.getQuestionId();
        Long authorId = questionInfo.getAuthorId();


        Boolean firstPassTheQuestion = questionFeignClient.firstPassTheQuestion(questionId, userId);

        System.out.println(firstPassTheQuestion);
        if(firstPassTheQuestion){
            Long theExperienceOfPassAQuestion = 20L;
            userFeignClient.increaseExperience(userId,theExperienceOfPassAQuestion);
            userFeignClient.increaseExperience(authorId, (long) (theExperienceOfPassAQuestion*0.1));
        }


        return questionSubmitInfoVo;
    }
}
