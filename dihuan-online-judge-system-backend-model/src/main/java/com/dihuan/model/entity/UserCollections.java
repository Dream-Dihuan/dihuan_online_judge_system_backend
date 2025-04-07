package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户题目收藏表
 * @TableName user_collections
 */
@TableName(value = "user_collections")
@Data
@Schema(description = "用户题目收藏表")
public class UserCollections implements Serializable {

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