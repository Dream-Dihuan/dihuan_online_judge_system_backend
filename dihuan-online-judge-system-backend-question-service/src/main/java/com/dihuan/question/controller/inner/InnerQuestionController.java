package com.dihuan.question.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dihuan.model.entity.Question;
import com.dihuan.model.entity.QuestionSubmit;
import com.dihuan.model.enums.question.QuestionResultEnum;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.question.mapper.QuestionMapper;
import com.dihuan.question.mapper.QuestionSubmitMapper;
import com.dihuan.question.service.QuestionService;
import com.dihuan.question.service.QuestionSubmitService;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inner")
@Tag(name = "内部-题目服务")
public class InnerQuestionController implements QuestionFeignClient {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionSubmitService questionSubmitService;

    @Autowired
    private QuestionSubmitMapper questionSubmitMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Operation(summary = "获取题目原始信息详情")
    @GetMapping("getOriginQuestionInfo")
    public Question getOriginQuestionInfo(@RequestParam Long id,@RequestParam(required = false,defaultValue = "1") Long checkStatus) {
        Question questionInfo = questionService.getOriginQuestionInfo(id,checkStatus);
        return questionInfo;
    }

    @GetMapping("getQuestionSubmitInfo")
    @Operation(summary = "获取题目提交信息")
    public QuestionSubmitVo getQuestionSubmitInfo(@RequestParam Long id) {
        QuestionSubmitVo questionSubmitInfoVo = questionSubmitService.getQuestionSubmitInfo(id);
        return questionSubmitInfoVo;
    }

    @PostMapping("updateQuestionSubmit")
    @Operation(summary = "更新题目提交信息")
    public Boolean updateQuestionSubmitInfo(@RequestBody QuestionSubmit questionSubmit) {
        boolean updated = questionSubmitService.updateById(questionSubmit);
        return updated;
    }

    @Override
    public Boolean firstPassTheQuestion(Long questionId, Long userId) {
        LambdaQueryWrapper<QuestionSubmit> questionSubmitLambdaQueryWrapper = new LambdaQueryWrapper<>();

        questionSubmitLambdaQueryWrapper
                .eq(QuestionSubmit::getQuestionId,questionId)
                .eq(QuestionSubmit::getUserId,userId)
                .eq(QuestionSubmit::getQuestionResult, QuestionResultEnum.PASSED);


        Long theTimeOfPassTheQuestion = questionSubmitMapper.selectCount(questionSubmitLambdaQueryWrapper);

        System.out.println(theTimeOfPassTheQuestion);
        // 这个时候第一条通过的记录已经被存入了数据库，所以是=1
        if(theTimeOfPassTheQuestion==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void incrementSubmitCount(Long questionId) {
        questionMapper.incrementSubmitCount(questionId);
    }

    @Override
    public void incrementAcceptedCount(Long questionId) {
        questionMapper.incrementAcceptedCount(questionId);
    }


}
