package com.dihuan.question.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.vo.question.QuestionNumberVo;
import com.dihuan.model.vo.question.QuestionSubmitListItemVo;
import com.dihuan.model.vo.question.QuestionSubmitVo;

import java.util.List;

/**
* @author 迪幻
* @description 针对表【question_submit(题目答案提交记录表)】的数据库操作Mapper
* @createDate 2025-04-09 22:26:36
* @Entity generator.domain.QuestionSubmit
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {

    List<QuestionNumberVo> getPassedQuestionNumberList(Long userId);

    List<QuestionNumberVo> getTryedQuestionNumberList(Long userId);

    QuestionSubmitVo getQuestionSubmitInfo(Long id);

    DihuanPage<QuestionSubmitListItemVo> getQuestionSubmitList(Long userId, String title, Long questionId, String language, Long questionResult, DihuanPage<QuestionSubmitListItemVo> questionSubmitListItemVoDihuanPage);
}




