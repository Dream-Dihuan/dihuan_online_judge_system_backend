package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dihuan.model.enums.question.JudgeStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目答案提交记录表
 */
@TableName(value = "question_submit")
@Data
@Schema(description = "题目答案提交记录表")
public class QuestionSubmit extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "question_id")
    @Schema(description = "题目ID")
    private Long questionId;

    @TableField(value = "user_id")
    @Schema(description = "作答用户ID")
    private Long userId;

    @TableField(value = "language")
    @Schema(description = "作答代码语言名称")
    private String language;

    @TableField(value = "code")
    @Schema(description = "用户代码")
    private String code;

    @TableField(value = "judge_status")
    @Schema(description = "判题状态")
    private JudgeStatusEnum judgeStatus;

    @TableField(value = "judge_result_info")
    @Schema(description = "判题结果信息")
    private String judgeResultInfo;
}