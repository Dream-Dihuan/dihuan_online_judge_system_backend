package com.dihuan.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.dto.question.AddOrUpdateQuestionInfoDto;
import com.dihuan.model.dto.ai.RagQuestionDocument;
import com.dihuan.model.vo.question.QuestionInfoVo;
import com.dihuan.model.vo.question.QuestionVo;
import com.dihuan.model.entity.Question;
import com.dihuan.question.mapper.QuestionMapper;
import com.dihuan.question.service.QuestionService;
import com.dihuan.serviceClient.service.QuestionRagFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.Serializable;

/**
* @author 迪幻
* @description 针对表【question(题目信息表)】的数据库操作Service实现
* @createDate 2025-04-09 14:07:29
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionRagFeignClient questionRagFeignClient;

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

        // 修改后的题目必须重新审核，同时删除旧向量，避免 AI 继续检索到旧版本内容。
        questionMapper.updateCheckStatus(question.getId(),0L);
        deleteQuestionFromRag(question.getId());
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
        Question question = this.getById(id);
        if (question != null) {
            if (Long.valueOf(1L).equals(checkStatus)) {
                // 只有审核通过的题目才允许进入 AI 知识库。
                syncQuestionToRag(question);
            } else {
                // 待审核、审核不通过或下架时，都必须从 AI 知识库删除。
                deleteQuestionFromRag(id);
            }
        }
    }

    @Override
    public void importQuestionInfo(Long authorId,Long checkStatus, List<Question> questionList) {
        for (Question question : questionList) {
            question.setAuthorId(authorId);
            question.setCheckStatus(checkStatus);
        }
        this.saveBatch(questionList);
        for (Question question : questionList) {
            if (Long.valueOf(1L).equals(checkStatus)) {
                // 批量导入时如果已经是发布状态，直接建立对应的 RAG 文档。
                syncQuestionToRag(question);
            } else {
                // 非发布状态不进入 RAG，并清理可能存在的旧向量。
                deleteQuestionFromRag(question.getId());
            }
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean removed = super.removeById(id);
        if (removed) {
            deleteQuestionFromRag((Long) id);
        }
        return removed;
    }

    private void syncQuestionToRag(Question question) {
        // 题目服务只负责发送结构化题目文档，Embedding 和 pgvector 存储由 AI 服务完成。
        RagQuestionDocument document = new RagQuestionDocument();
        document.setQuestionId(question.getId());
        document.setAuthorId(question.getAuthorId());
        document.setTitle(question.getTitle());
        document.setContent(question.getContent());
        document.setTags(question.getTags());
        try {
            questionRagFeignClient.upsertQuestion(document);
        } catch (RuntimeException exception) {
            log.warn("同步题目 {} 到 RAG 知识库失败，审核状态更新继续完成", question.getId(), exception);
        }
    }

    private void deleteQuestionFromRag(Long questionId) {
        try {
            questionRagFeignClient.deleteQuestion(questionId);
        } catch (RuntimeException exception) {
            log.warn("删除题目 {} 的 RAG 向量失败，题目主数据操作继续完成", questionId, exception);
        }
    }


}




