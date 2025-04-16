package com.dihuan.model.dto.user;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class UserListDto {

    /**
     * 用户名称（模糊查询）
     */
    private String name;

    /**
     * 用户名（精确查询）
     */
    private String username;

    /**
     * 用户 ID（精确查询）
     */
    private Long id;

    /**
     * 是否只查询已订阅的用户，默认为 false
     */
    private Boolean onlySubscribeUser = false;

    /**
     * 分页页码，默认为 1
     */
    private Long page = 1L;

    /**
     * 分页大小，默认为 20
     */
    private Long pageSize = 20L;

}
