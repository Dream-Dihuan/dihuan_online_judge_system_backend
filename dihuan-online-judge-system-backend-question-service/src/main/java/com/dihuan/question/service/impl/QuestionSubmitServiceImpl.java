package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.dto.question.QuestionSubmitDto;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionNumberVo;
import com.dihuan.model.vo.question.QuestionSubmitListItemVo;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.question.mapper.QuestionMapper;
import com.dihuan.question.mapper.QuestionSubmitMapper;
import com.dihuan.question.rabbitMq.QuestionSubmitProducer;
import com.dihuan.question.service.QuestionSubmitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 迪幻
* @description 针对表【question_submit(题目答案提交记录表)】的数据库操作Service实现
* @createDate 2025-04-09 22:26:36
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {


    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionSubmitMapper questionSubmitMapper;

    @Autowired
    private QuestionSubmitProducer questionSubmitProducer;

    @Override
    public Long submitQuestionAnswer(QuestionSubmitDto questionSubmitDto) {

        // 判断题目是否存在以及是否发布
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<Question>();
        questionLambdaQueryWrapper
                .eq(Question::getCheckStatus, 1)
                .eq(Question::getId, questionSubmitDto.getQuestionId());
        Long findQuestionNumber = questionMapper.selectCount(questionLambdaQueryWrapper);
        if(findQuestionNumber==0){
            throw new DihuanException(ResultCodeEnum.QUESTION_NOT_FOUND_ERROR);
        }

        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitDto,questionSubmit);
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new DihuanException(ResultCodeEnum.FAIL,"题目答案提交失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        System.out.println(questionSubmitId);

        questionSubmitProducer.sendQuestionToJudgeService(questionSubmitId);

        return questionSubmitId;
    }

    @Override
    public QuestionSubmitVo getQuestionSubmitInfo(Long id) {
        QuestionSubmitVo questionSubmitVo = questionSubmitMapper.getQuestionSubmitInfo(id);
        return questionSubmitVo;
    }

    @Override
    public List<QuestionNumberVo> getPassedQuestionNumberList(Long userId) {
        List<QuestionNumberVo> passedQuestionNumberList = questionSubmitMapper.getPassedQuestionNumberList(userId);
        return passedQuestionNumberList;
    }

    @Override
    public List<QuestionNumberVo> getTryedQuestionNumberList(Long userId) {
        List<QuestionNumberVo> tryedQuestionNumberList = questionSubmitMapper.getTryedQuestionNumberList(userId);
        return tryedQuestionNumberList;
    }

    @Override
    public DihuanPage<QuestionSubmitListItemVo> getQuestionSubmitList(Long userId, String title, Long questionId, String language, Long questionResult, Integer page, Integer pageSize) {

        DihuanPage<QuestionSubmitListItemVo> questionSubmitListItemVoDihuanPage = new DihuanPage<>(page, pageSize);

        DihuanPage<QuestionSubmitListItemVo> questionSubmitList = questionSubmitMapper.getQuestionSubmitList(userId, title, questionId, language, questionResult, questionSubmitListItemVoDihuanPage);
        return  questionSubmitList;
    }
}




