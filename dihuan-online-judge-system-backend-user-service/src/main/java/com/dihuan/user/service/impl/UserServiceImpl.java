package com.dihuan.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.model.dto.user.UpdateUserInfoDto;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.user.mapper.UserMapper;
import com.dihuan.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Service实现
* @createDate 2025-04-07 18:07:48
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Long id) {
        return this.getById(id);
    }

    @Override
    public UserInfoVo getUserInfo() {
        Long id = TokenInfoHolder.getTokenInfo().getId();
        UserInfoVo userInfoVo = userMapper.getUserInfo(id);
        return userInfoVo;
    }

    @Override
    public void updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        Long id = TokenInfoHolder.getTokenInfo().getId();
        User userInfo = this.getById(id);
        userInfo.setName(updateUserInfoDto.getName());
        userInfo.setGender(updateUserInfoDto.getGender());
        userInfo.setDescription(updateUserInfoDto.getDescription());
        userInfo.setPhone(updateUserInfoDto.getPhone());
        userInfo.setEmail(updateUserInfoDto.getEmail());
        boolean updated = this.updateById(userInfo);
        if(!updated){
            throw new DihuanException(ResultCodeEnum.FAIL,"用户信息更新失败");
        }
    }
}




