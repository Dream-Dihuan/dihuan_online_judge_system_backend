package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户图片信息表
 * @TableName user_image
 */
@TableName(value = "user_image")
@Data
@Schema(description = "用户图片信息表")
public class UserImage implements Serializable {

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户头像url
     */
    @TableField(value = "avatar_url")
    @Schema(description = "用户头像url")
    private String avatarUrl;

    /**
     * 用户banner图url
     */
    @TableField(value = "banner_url")
    @Schema(description = "用户banner图url")
    private String bannerUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}