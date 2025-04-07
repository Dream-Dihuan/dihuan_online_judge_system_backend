package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题目信息表
 * @TableName question
 */
@TableName(value = "question")
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
    @TableField(value = "judge_case")
    @Schema(description = "判题检查点")
    private String judgeCase;

    /**
     * 判题配置
     */
    @TableField(value = "judge_config")
    @Schema(description = "判题配置")
    private String judgeConfig;

    /**
     * 题目通过数
     */
    @TableField(value = "accepted_number")
    @Schema(description = "题目通过数")
    private Long acceptedNumber;

    /**
     * 题目提交数
     */
    @TableField(value = "sumbit_number")
    @Schema(description = "题目提交数")
    private Long sumbitNumber;

    /**
     * 作者用户ID
     */
    @TableField(value = "author_id")
    @Schema(description = "作者用户ID")
    private Long authorId;

    /**
     * 用户收藏数
     */
    @TableField(value = "collection_number")
    @Schema(description = "用户收藏数")
    private Integer collectionNumber;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}