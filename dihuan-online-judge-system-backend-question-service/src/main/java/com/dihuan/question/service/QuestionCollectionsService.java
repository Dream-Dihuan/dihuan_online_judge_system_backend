package com.dihuan.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.model.entity.QuestionCollections;

/**
* @author 迪幻
* @description 针对表【question_collections(用户题目收藏表)】的数据库操作Service
* @createDate 2025-04-09 18:07:12
*/
public interface QuestionCollectionsService extends IService<QuestionCollections> {

    void addQuestionCollections(Long questionId);

    void cancelQuestionCollections(Long questionId);
}
