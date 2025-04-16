package com.dihuan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.emailVerificationUtils.EmailConstantEnum;
import com.dihuan.common.utils.emailVerificationUtils.EmailVerificationCodeVo;
import com.dihuan.common.utils.emailVerificationUtils.EmailVerificationUtils;
import com.dihuan.model.dto.email.VerificationCodeDto;
import com.dihuan.model.entity.User;
import com.dihuan.user.service.UserEmailService;
import com.dihuan.user.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserEmailServiceImpl implements UserEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private UserEmailService userEmailService;

    @Override
    public String sendResetPasswordEmail(String email) throws Exception {

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(userLambdaQueryWrapper);

        if(user==null){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_USER_NOT_EXIST_ERROR);
        }

        EmailVerificationCodeVo emailVerificationCodeObject = EmailVerificationUtils.createEmailVerificationCode(EmailConstantEnum.RESET_PASSWORD,email, stringRedisTemplate);

        userEmailService.AsyncSendResetPasswordEmail(email, user, emailVerificationCodeObject);

        return emailVerificationCodeObject.getUuid();
    }

    @Async("mailExecutor")
    @Override
    public void AsyncSendResetPasswordEmail(String email, User user, EmailVerificationCodeVo emailVerificationCodeObject) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("verificationCode", emailVerificationCodeObject.getCode());
        model.put("ttl", EmailConstantEnum.RESET_PASSWORD.getTTL());

        Template template = freemarkerConfig.getTemplate("email/resetPassword/password-reset.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        //读取迪幻的图片文件
        Resource resource = new ClassPathResource("templates/email/resetPassword/dihuanImage.png");
        ByteArrayResource imageResource = new ByteArrayResource(resource.getContentAsByteArray());


        helper.setFrom("449134710@qq.com");
        helper.setTo(email);
        helper.setSubject("迪幻OJ在线判题系统 - 密码重置请求");
        helper.setText(htmlContent, true);
        helper.addInline("dihuanImage", imageResource, "image/png");
        mailSender.send(mimeMessage);
    }

    @Override
    public String sendRegisterEmail(String email) throws Exception {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(userLambdaQueryWrapper);
        if(user!=null){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_USER_EXIST_ERROR);
        }

        EmailVerificationCodeVo emailVerificationCodeObject = EmailVerificationUtils.createEmailVerificationCode(EmailConstantEnum.REGISTER_ACCOUNT,email, stringRedisTemplate);

        userEmailService.AsyncSendRegisterEmail(email, emailVerificationCodeObject);

        return emailVerificationCodeObject.getUuid();
    }

    @Async("mailExecutor")
    public void AsyncSendRegisterEmail(String email, EmailVerificationCodeVo emailVerificationCodeObject) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


        Map<String, Object> model = new HashMap<>();
        model.put("verificationCode", emailVerificationCodeObject.getCode());
        model.put("ttl", EmailConstantEnum.RESET_PASSWORD.getTTL());

        Template template = freemarkerConfig.getTemplate("email/registerAccount/register-account.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        //读取迪幻的图片文件
        Resource resource = new ClassPathResource("templates/email/resetPassword/dihuanImage.png");
        ByteArrayResource imageResource = new ByteArrayResource(resource.getContentAsByteArray());

        helper.setFrom("449134710@qq.com");
        helper.setTo(email);
        helper.setSubject("迪幻OJ在线判题系统 - 账号注册请求");
        helper.setText(htmlContent, true);
        helper.addInline("dihuanImage", imageResource, "image/png");

        mailSender.send(mimeMessage);
    }
}
