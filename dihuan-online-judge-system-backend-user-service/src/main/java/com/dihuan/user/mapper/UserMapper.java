package com.dihuan.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Mapper
* @createDate 2025-04-07 18:07:48
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    UserInfoVo getUserInfo(Long id);
}




