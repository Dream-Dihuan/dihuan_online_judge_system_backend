package com.dihuan.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.model.dto.user.UpdateUserInfoDto;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Service
* @createDate 2025-04-07 18:07:48
*/
public interface UserService extends IService<User> {

    User getUser(Long id);

    UserInfoVo getUserInfo();

    void updateUserInfo(UpdateUserInfoDto updateUserInfoDto);
}
