package com.dihuan.userService.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.model.entity.User;
import com.dihuan.userService.mapper.UserMapper;
import com.dihuan.userService.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Service实现
* @createDate 2025-04-07 18:07:48
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Override
    public User getUser(Long id) {
        return this.getById(id);
    }
}




