package com.dihuan.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.result.Result;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.captchaUtils.CaptchaTypeEnum;
import com.dihuan.common.utils.captchaUtils.CaptchaUtils;
import com.dihuan.common.utils.captchaUtils.CaptchaVo;
import com.dihuan.common.utils.emailVerificationUtils.EmailConstantEnum;
import com.dihuan.common.utils.emailVerificationUtils.EmailVerificationUtils;
import com.dihuan.common.utils.jsonWebTokenUtils.JwtUtils;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.dto.user.ResetPasswordDto;
import com.dihuan.model.dto.user.UpdateUserInfoDto;
import com.dihuan.model.dto.user.UserLoginDto;
import com.dihuan.model.dto.user.UserRegisterDto;
import com.dihuan.model.entity.User;
import com.dihuan.model.vo.user.UserInfoVo;
import com.dihuan.model.vo.user.UserListItemVo;
import com.dihuan.user.mapper.UserMapper;
import com.dihuan.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public DihuanPage<UserListItemVo> getUserList(String name,String username, Long id, Long searchType, Long page, Long pageSize) {
        Long userId = TokenInfoHolder.getTokenInfo().getId();
        DihuanPage<UserListItemVo> userDihuanPage = new DihuanPage<>(page, pageSize);
        DihuanPage<UserListItemVo> userList = userMapper.getUserList(name,username, id, searchType,userId, userDihuanPage);
        return userList;
    }

    @Override
    public String userLogin(UserLoginDto userLoginDto,StringRedisTemplate stringRedisTemplate) {

        //验证验证码是否正确
        CaptchaUtils.verifyCaptchaCode(userLoginDto.getCaptchaKey(), userLoginDto.getCaptchaCode(), stringRedisTemplate);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper
                .eq(User::getUsername,userLoginDto.getUsername());
        User theAimUseer = this.getOne(userLambdaQueryWrapper);

        if(theAimUseer==null){
            throw new DihuanException(ResultCodeEnum.USER_NOT_FOUND_ERROR);
        }

        if(!theAimUseer.getPassword().equals(userLoginDto.getPassword())){
            throw new DihuanException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }

        String token = JwtUtils.createToken(new TokenInfo(theAimUseer.getId(), theAimUseer.getUsername()));
        return token;
    }

    @Override
    public void userRegister(UserRegisterDto userRegisterDto) {

        // 判断用户名是否被使用
        LambdaQueryWrapper<User> userUsernameLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userUsernameLambdaQueryWrapper.eq(User::getUsername,userRegisterDto.getUsername());
        User theSameUsernameUser = this.getOne(userUsernameLambdaQueryWrapper);
        if(theSameUsernameUser!=null){
            throw new DihuanException(ResultCodeEnum.USER_SAME_USERNAME_ERROR);
        }

        EmailVerificationUtils.verifyEmailVerificationCode(EmailConstantEnum.REGISTER_ACCOUNT,userRegisterDto.getUuid(),userRegisterDto.getCode(),userRegisterDto.getEmail(),stringRedisTemplate);

        User user = new User();
        BeanUtils.copyProperties(userRegisterDto,user);
        boolean save = this.save(user);
        if (!save) {
            throw new DihuanException(ResultCodeEnum.USER_REGISTER_ERROR);
        }

    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        EmailVerificationUtils.verifyEmailVerificationCode(EmailConstantEnum.RESET_PASSWORD,resetPasswordDto.getUuid(),resetPasswordDto.getCode(),resetPasswordDto.getEmail(),stringRedisTemplate);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,resetPasswordDto.getEmail());
        User user = this.getOne(userLambdaQueryWrapper);
        user.setPassword(resetPasswordDto.getPassword());
        boolean updated = this.updateById(user);
        if(!updated){
            throw new DihuanException(ResultCodeEnum.USER_RESET_PASSWORD_ERROR);
        }
    }

}




