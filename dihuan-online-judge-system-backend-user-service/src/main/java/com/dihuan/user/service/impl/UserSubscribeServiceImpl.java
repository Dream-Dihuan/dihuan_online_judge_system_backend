package com.dihuan.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.entity.UserSubscribe;
import com.dihuan.user.mapper.UserSubscribeMapper;
import com.dihuan.user.service.UserSubscribeService;
import org.springframework.stereotype.Service;

/**
* @author 迪幻
* @description 针对表【user_subscribe(关注用户记录表)】的数据库操作Service实现
* @createDate 2025-04-13 11:22:39
*/
@Service
public class UserSubscribeServiceImpl extends ServiceImpl<UserSubscribeMapper, UserSubscribe>
    implements UserSubscribeService {

    @Override
    public void subscribe(Long targetUserId) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();

        //判断是否关注的是自己
        if(userId.equals(targetUserId)){
            throw new DihuanException(ResultCodeEnum.USER_SUBSCRIBE_SELF_ERROR);
        }
        
        //判断是否已关注该用户
        LambdaQueryWrapper<UserSubscribe> userSubscribeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userSubscribeLambdaQueryWrapper
                .eq(UserSubscribe::getUserId,userId)
                .eq(UserSubscribe::getSubscribedId,targetUserId);
        UserSubscribe theAimRecord = this.getOne(userSubscribeLambdaQueryWrapper);
        if(theAimRecord!=null){
            throw new DihuanException(ResultCodeEnum.USER_SUBSCRIBE_SAME_ERROR);
        }

        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setUserId(userId);
        userSubscribe.setSubscribedId(targetUserId);
        boolean save = save(userSubscribe);
        if(!save){
            throw new DihuanException(ResultCodeEnum.FAIL);
        }
    }

    @Override
    public void unSubscribe(Long targetUserId) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();

        // 判断是否已关注该用户
        LambdaQueryWrapper<UserSubscribe> userSubscribeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userSubscribeLambdaQueryWrapper
                .eq(UserSubscribe::getUserId,userId)
                .eq(UserSubscribe::getSubscribedId,targetUserId);
        UserSubscribe subscribeRecord = this.getOne(userSubscribeLambdaQueryWrapper);
        if(subscribeRecord==null){
            throw new DihuanException(ResultCodeEnum.USER_UNSUBSCRIBE_ERROR);
        }

        boolean remove = this.remove(userSubscribeLambdaQueryWrapper);
        if(!remove){
            throw new DihuanException(ResultCodeEnum.USER_UNSUBSCRIBE_ERROR);
        }
    }
}




