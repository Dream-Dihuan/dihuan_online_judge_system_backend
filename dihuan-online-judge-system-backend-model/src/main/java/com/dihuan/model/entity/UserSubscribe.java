package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 关注用户记录表
 * @TableName user_subscribe
 */
@TableName(value = "user_subscribe")
@Data
@Schema(description = "关注用户记录表")
public class UserSubscribe implements Serializable {

    /**
     * 用户关注用户记录ID
     */
    @TableId(value = "id")
    @Schema(description = "用户关注用户记录ID")
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 被关注的用户ID
     */
    @TableField(value = "subscribed_id")
    @Schema(description = "被关注的用户ID")
    private Long subscribedId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}