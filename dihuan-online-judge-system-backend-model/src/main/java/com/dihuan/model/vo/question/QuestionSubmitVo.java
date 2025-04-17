package com.dihuan.model.vo.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.dihuan.model.entity.question.JudgeCaseResult;
import com.dihuan.model.enums.question.JudgeStatusEnum;
import com.dihuan.model.enums.question.QuestionResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSubmitVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "作答用户ID")
    private Long userId;

    @Schema(description = "作答代码语言名称")
    private String language;

    @Schema(description = "用户代码")
    private String code;

    @Schema(description = "判题状态")
    private JudgeStatusEnum judgeStatus;


    @Schema(description = "题目判题结果")
    private QuestionResultEnum questionResult;

    @Schema(description = "判题结果信息")
    private List<JudgeCaseResult> judgeResultInfo;

    @Schema(description = "创建时间")
    private String createTime;
}
