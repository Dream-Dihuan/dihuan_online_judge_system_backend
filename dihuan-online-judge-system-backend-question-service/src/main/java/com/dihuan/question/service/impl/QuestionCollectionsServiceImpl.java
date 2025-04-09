package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.entity.QuestionCollections;
import com.dihuan.question.mapper.QuestionCollectionsMapper;
import com.dihuan.question.service.QuestionCollectionsService;
import org.springframework.stereotype.Service;

/**
* @author 迪幻
* @description 针对表【question_collections(用户题目收藏表)】的数据库操作Service实现
* @createDate 2025-04-09 18:07:12
*/
@Service
public class QuestionCollectionsServiceImpl extends ServiceImpl<QuestionCollectionsMapper, QuestionCollections>
    implements QuestionCollectionsService {

    @Override
    public void addQuestionCollections(Long questionId) {

        Long userId = TokenInfoHolder.getTokenInfo().getId();

        //判断该用户是否已经收藏过该题目
        LambdaQueryWrapper<QuestionCollections> questionCollectionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionCollectionsLambdaQueryWrapper
                .eq(QuestionCollections::getUserId,userId)
                .eq(QuestionCollections::getQuestionId,questionId);

        if(this.getOne(questionCollectionsLambdaQueryWrapper)!=null){
            throw new DihuanException(ResultCodeEnum.FAIL,"您已收藏过该题目,不能收藏该题目");
        }

        QuestionCollections questionCollections = new QuestionCollections();
        questionCollections.setUserId(userId);
        questionCollections.setQuestionId(questionId);

        this.save(questionCollections);

    }

    @Override
    public void cancelQuestionCollections(Long questionId) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();

        //判断该用户是否已经收藏过该题目
        LambdaQueryWrapper<QuestionCollections> questionCollectionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        questionCollectionsLambdaQueryWrapper
                .eq(QuestionCollections::getUserId,userId)
                .eq(QuestionCollections::getQuestionId,questionId);

        QuestionCollections questionCollectionsRecord = this.getOne(questionCollectionsLambdaQueryWrapper);

        if(questionCollectionsRecord==null){
            throw new DihuanException(ResultCodeEnum.FAIL,"您未收藏该题目,取消收藏失败");
        }

        boolean remove = this.remove(questionCollectionsLambdaQueryWrapper);
        if(!remove){
            throw new DihuanException(ResultCodeEnum.FAIL,"取消收藏失败");
        }
    }
}




