package com.dihuan.model.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dihuan.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@Schema(description = "用户信息Vo")
public class UserInfoVo extends BaseEntity implements Serializable{

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户昵称")
    private String name;

    @Schema(description = "性别 (0-男/1-女)")
    private Integer gender;

    @Schema(description = "用户签名")
    private String description;

    @Schema(description = "粉丝数")
    private Long subscribeNumber;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "电话号码")
    private String phone;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日")
    private Date birthday;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "经验值")
    private Long experience;

    @Schema(description = "用户头像url")
    private String avatarUrl;

    @Schema(description = "用户权限ID")
    private Long roleId;

    @Schema(description = "用户banner图url")
    private String bannerUrl;

    @Schema(description = "是否关注该用户")
    private Boolean subscribeStatus;
}