package com.dihuan.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dihuan.model.entity.UserSubscribe;
import org.apache.ibatis.annotations.Update;

/**
* @author 迪幻
* @description 针对表【user_subscribe(关注用户记录表)】的数据库操作Mapper
* @createDate 2025-04-13 11:22:39
* @Entity generator.domain.UserSubscribe
*/
public interface UserSubscribeMapper extends BaseMapper<UserSubscribe> {
    @Update("UPDATE user SET subscribe_number = subscribe_number + 1 WHERE id = #{userId}")
    int incrementSubscribeNumber(Long userId);

    @Update("UPDATE user SET subscribe_number = IF(subscribe_number > 0, subscribe_number - 1, 0) WHERE id = #{userId}")
    int decrementSubscribeNumber(Long userId);
}




