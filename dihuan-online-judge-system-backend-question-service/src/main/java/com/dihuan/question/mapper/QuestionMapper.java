package com.dihuan.question.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;
import org.apache.ibatis.annotations.Update;

/**
* @author 迪幻
* @description 针对表【question(题目信息表)】的数据库操作Mapper
* @createDate 2025-04-09 14:07:29
* @Entity generator.domain.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    DihuanPage<QuestionVo> getQuestionList(String title,String tag,Long id,Long authorId,Long userId,Boolean collected,Boolean subscribeUser,Long checkStatus,DihuanPage<QuestionVo> dihuanPage);

    QuestionInfoVo getQuestionInfo(Long id,Long userId,Long checkStatus);

    // 只增加提交数
    @Update("UPDATE question SET submit_number = submit_number + 1 WHERE id = #{questionId}")
    void incrementSubmitCount(Long questionId);

    // 仅增加通过数
    @Update("UPDATE question SET accepted_number = accepted_number + 1 WHERE id = #{questionId}")
    void incrementAcceptedCount(Long questionId);

    // 修改题目的审核状态
    @Update("UPDATE question SET check_status = #{checkStatus}, update_time = NOW() WHERE id = #{id}")
    int updateCheckStatus(Long id,Long checkStatus);
}




