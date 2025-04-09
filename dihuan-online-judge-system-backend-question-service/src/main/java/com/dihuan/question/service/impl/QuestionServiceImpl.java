package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.dto.question.AddOrUpdateQuestionInfoDto;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;
import com.dihuan.question.mapper.QuestionMapper;
import com.dihuan.question.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        question.setAuthorId(authorId);

        BeanUtils.copyProperties(addOrUpdateQuestionInfoDto,question);
        this.saveOrUpdate(question);
    }

    @Override
    public DihuanPage<QuestionVo> getQuestionList(String title, String tag, Long id, Long authorId,Boolean collected, Integer page, Integer pageSize) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();
        DihuanPage<QuestionVo> dihuanPage = new DihuanPage<QuestionVo>(page,pageSize);
        DihuanPage<QuestionVo> result =questionMapper.getQuestionList(title,tag,id,authorId,userId,collected,dihuanPage);
        return result;
    }

    @Override
    public QuestionInfoVo getQuestionInfo(Long id) {
        QuestionInfoVo questionInfoVo = questionMapper.getQuestionInfo(id);
        return questionInfoVo;
    }


}




