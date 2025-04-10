package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.dihuan.model.entity.question.JudgeCase;
import com.dihuan.model.entity.question.JudgeConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 题目信息表
 * @TableName question
 */
@TableName(value = "question", autoResultMap = true)
@Data
@Schema(description = "题目信息表")
public class Question extends BaseEntity {

    /**
     * 题目标题
     */
    @TableField(value = "title")
    @Schema(description = "题目标题")
    private String title;

    /**
     * 题目内容
     */
    @TableField(value = "content")
    @Schema(description = "题目内容")
    private String content;

    /**
     * 题目标签
     */
    @TableField(value = "tags")
    @Schema(description = "题目标签")
    private String tags;

    /**
     * 判题检查点
     */
    @TableField(value = "judge_case",typeHandler = JacksonTypeHandler.class)
    @Schema(description = "判题检查点")
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    @TableField(value = "judge_config", typeHandler = JacksonTypeHandler.class)
    @Schema(description = "判题配置")
    private List<JudgeConfig> judgeConfig;

    /**
     * 题目通过数
     */
    @TableField(value = "accepted_number")
    @Schema(description = "题目通过数")
    private Long acceptedNumber;

    /**
     * 题目提交数
     */
    @TableField(value = "submit_number")
    @Schema(description = "题目提交数")
    private Long submitNumber;

    /**
     * 作者用户ID
     */
    @TableField(value = "author_id")
    @Schema(description = "作者用户ID")
    private Long authorId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}