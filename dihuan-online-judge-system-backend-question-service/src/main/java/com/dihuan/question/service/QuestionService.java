package com.dihuan.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.dto.question.AddOrUpdateQuestionInfoDto;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;

/**
* @author 迪幻
* @description 针对表【question(题目信息表)】的数据库操作Service
* @createDate 2025-04-09 14:07:29
*/
public interface QuestionService extends IService<Question> {

    void AddOrUpdateQuestionInfo(AddOrUpdateQuestionInfoDto addOrUpdateQuestionInfoDto);


    DihuanPage<QuestionVo> getQuestionList(String title, String tag, Long id, Long authorId,Boolean collected,Boolean subscribeUser, Integer page, Integer pageSize);

    QuestionInfoVo getQuestionInfo(Long id);

    Question getOriginQuestionInfo(Long id);
}
