package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.dto.question.AddOrUpdateQuestionInfoDto;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;
import com.dihuan.question.mapper.QuestionMapper;
import com.dihuan.question.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
* @author 迪幻
* @description 针对表【question(题目信息表)】的数据库操作Service实现
* @createDate 2025-04-09 14:07:29
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void AddOrUpdateQuestionInfo(AddOrUpdateQuestionInfoDto addOrUpdateQuestionInfoDto) {
        Long authorId = TokenInfoHolder.getTokenInfo().getId();
        Question question;
        if(addOrUpdateQuestionInfoDto.getId()==null||addOrUpdateQuestionInfoDto.getId()==0){
            question = new Question();
        }else{
            question = this.getById(addOrUpdateQuestionInfoDto.getId());
        }

        //  是否是修改题目信息
        if(addOrUpdateQuestionInfoDto.getId()!=null){
            // 判断题目是否存在以及是否发布
            LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<Question>();
            questionLambdaQueryWrapper
//                    .eq(Question::getCheckStatus, 1)
                    .eq(Question::getId, addOrUpdateQuestionInfoDto.getId());
            Long findQuestionNumber = questionMapper.selectCount(questionLambdaQueryWrapper);
            if(findQuestionNumber==0){
                throw new DihuanException(ResultCodeEnum.QUESTION_NOT_FOUND_ERROR);
            }
        }

        // 如果已经有作者了，则不更新该字段
        if(question.getAuthorId()==null){
            question.setAuthorId(authorId);
        }


        BeanUtils.copyProperties(addOrUpdateQuestionInfoDto,question);
        this.saveOrUpdate(question);

        // 将此题目设置为待审核状态
        questionMapper.updateCheckStatus(question.getId(),0L);
    }

    @Override
    public DihuanPage<QuestionVo> getQuestionList(String title, String tag, Long id, Long authorId,Boolean collected,Boolean subscribeUser,Long checkStatus, Integer page, Integer pageSize) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();
        DihuanPage<QuestionVo> dihuanPage = new DihuanPage<QuestionVo>(page,pageSize);
        DihuanPage<QuestionVo> result =questionMapper.getQuestionList(title,tag,id,authorId,userId,collected,subscribeUser,checkStatus,dihuanPage);
        return result;
    }

    @Override
    public QuestionInfoVo getQuestionInfo(Long id,Long checkStatus) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();
        QuestionInfoVo questionInfoVo = questionMapper.getQuestionInfo(id,userId,checkStatus);
        if(questionInfoVo==null){
            throw new DihuanException(ResultCodeEnum.QUESTION_NOT_FOUND_ERROR);
        }
        return questionInfoVo;
    }

    @Override
    public Question getOriginQuestionInfo(Long id,Long checkStatus) {
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionLambdaQueryWrapper
                .eq(Question::getId,id)
                .eq(checkStatus != null,Question::getCheckStatus,checkStatus);
        Question question = this.getOne(questionLambdaQueryWrapper);
        return question;
    }

    @Override
    public void updateCheckStatus(Long id, Long checkStatus) {
        questionMapper.updateCheckStatus(id,checkStatus);
    }

    @Override
    public void importQuestionInfo(Long authorId,Long checkStatus, List<Question> questionList) {
        for (Question question : questionList) {
            question.setAuthorId(authorId);
            question.setCheckStatus(checkStatus);
        }
        this.saveBatch(questionList);
    }


}




