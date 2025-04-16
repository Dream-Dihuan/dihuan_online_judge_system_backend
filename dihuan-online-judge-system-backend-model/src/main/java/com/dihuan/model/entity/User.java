package com.dihuan.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 */
@TableName(value = "user")
@Data
@Schema(description = "用户信息表")
public class User extends BaseEntity implements Serializable{

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "name")
    @Schema(description = "用户昵称")
    private String name;

    @TableField(value = "gender")
    @Schema(description = "性别 (0-男/1-女)")
    private Integer gender;

    @TableField(value = "description")
    @Schema(description = "用户签名")
    private String description;

    @TableField(value = "subscribe_number")
    @Schema(description = "粉丝数")
    private Long subscribeNumber;

    @TableField(value = "username")
    @Schema(description = "用户名")
    private String username;

    @TableField(value = "password")
    @Schema(description = "密码")
    private String password;

    @TableField(value = "phone")
    @Schema(description = "电话号码")
    private String phone;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "birthday")
    @Schema(description = "生日")
    private Date birthday;

    @TableField(value = "email")
    @Schema(description = "电子邮箱")
    private String email;

    @TableField(value = "experience")
    @Schema(description = "经验值")
    private Long experience;
}