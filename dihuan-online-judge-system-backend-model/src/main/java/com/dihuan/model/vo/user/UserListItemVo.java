package com.dihuan.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户列表信息单元Vo")
public class UserListItemVo {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户昵称")
    private String name;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "性别 (0-男/1-女)")
    private Integer gender;

    @Schema(description = "粉丝数量")
    private Long subscribeNumber;

    @Schema(description = "经验值")
    private Long experience;

    @Schema(description = "是否关注")
    private Boolean subscribeStatus;

    @Schema(description = "是否封禁 (0-否/1-是)")
    private Boolean isBanned;

    @Schema(description = "权限角色ID")
    private Long roleId;
}
