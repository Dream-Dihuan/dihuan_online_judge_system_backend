package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户已通过的题目关联表
 * @TableName accepted_question_id
 */
@TableName(value = "accepted_question_id")
@Data
@Schema(description = "用户已通过的题目关联表")
public class AcceptedQuestionId extends BaseEntity {

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 题目ID
     */
    @TableField(value = "question_id")
    @Schema(description = "题目ID")
    private Long questionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}