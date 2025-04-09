package com.dihuan.question.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;

/**
* @author 迪幻
* @description 针对表【question(题目信息表)】的数据库操作Mapper
* @createDate 2025-04-09 14:07:29
* @Entity generator.domain.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    DihuanPage<QuestionVo> getQuestionList(String title,String tag,Long id,Long authorId,Long userId,Boolean collected,DihuanPage<QuestionVo> dihuanPage);

    QuestionInfoVo getQuestionInfo(Long id);
}




