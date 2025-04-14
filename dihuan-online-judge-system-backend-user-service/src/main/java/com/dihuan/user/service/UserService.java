package com.dihuan.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.utils.captchaUtils.CaptchaVo;
import com.dihuan.model.dto.user.ResetPasswordDto;
import com.dihuan.model.dto.user.UpdateUserInfoDto;
import com.dihuan.model.dto.user.UserLoginDto;
import com.dihuan.model.dto.user.UserRegisterDto;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.model.vo.user.UserListItemVo;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Service
* @createDate 2025-04-07 18:07:48
*/
public interface UserService extends IService<User> {

    User getUser(Long id);

    UserInfoVo getUserInfo();

    void updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    DihuanPage<UserListItemVo> getUserList(String name,String username, Long id, Long searchType, Long page, Long pageSize);

    String userLogin(UserLoginDto userLoginDto, StringRedisTemplate stringRedisTemplate);

    void userRegister(UserRegisterDto userRegisterDto);

    void resetPassword(ResetPasswordDto resetPasswordDto);
}
