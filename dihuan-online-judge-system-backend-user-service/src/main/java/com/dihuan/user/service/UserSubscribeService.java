package com.dihuan.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.model.entity.UserSubscribe;

/**
* @author 迪幻
* @description 针对表【user_subscribe(关注用户记录表)】的数据库操作Service
* @createDate 2025-04-13 11:22:39
*/
public interface UserSubscribeService extends IService<UserSubscribe> {

    void subscribe(Long targetUserId);

    void unSubscribe(Long targetUserId);
}
