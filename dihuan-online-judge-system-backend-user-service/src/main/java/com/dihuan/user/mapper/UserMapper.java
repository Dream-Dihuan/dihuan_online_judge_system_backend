package com.dihuan.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.model.vo.user.UserListItemVo;
import org.apache.ibatis.annotations.Update;

/**
* @author 迪幻
* @description 针对表【user(用户信息表)】的数据库操作Mapper
* @createDate 2025-04-07 18:07:48
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    UserInfoVo getUserInfo(Long id,Long userId);

    DihuanPage<UserListItemVo> getUserList(String name, String username, Long id, Boolean onlySubscribeUser, Long userId, DihuanPage<UserListItemVo> userDihuanPage);

    @Update("UPDATE user SET experience = experience + #{amount} WHERE id = #{userId}")
    int increaseExperience(Long userId,Long amount);

    @Update("UPDATE user SET experience = IF(experience >= #{amount}, experience - #{amount}, 0) WHERE id = #{userId}")
    int decreaseExperience(Long userId,Long amount);

}




