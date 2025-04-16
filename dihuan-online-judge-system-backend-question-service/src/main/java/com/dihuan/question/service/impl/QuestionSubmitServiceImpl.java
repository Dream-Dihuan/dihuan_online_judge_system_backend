package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.dto.question.QuestionSubmitDto;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionNumberVo;
import com.dihuan.question.mapper.QuestionSubmitMapper;
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
    private QuestionSubmitMapper questionSubmitMapper;

    @Override
    public Long submitQuestionAnswer(QuestionSubmitDto questionSubmitDto) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitDto,questionSubmit);
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new DihuanException(ResultCodeEnum.FAIL,"题目答案提交失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        System.out.println(questionSubmitId);

        return questionSubmitId;
    }

    @Override
    public QuestionSubmit getQuestionSubmitInfo(Long id) {
        QuestionSubmit questionSubmit = this.getById(id);
        return questionSubmit;
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
}




