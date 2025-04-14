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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;

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

    @Override
    public String sendResetPasswordEmail(String email) throws Exception {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(userLambdaQueryWrapper);

        if(user==null){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_USER_NOT_EXIST_ERROR);
        }


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        EmailVerificationCodeVo emailVerificationCodeObject = EmailVerificationUtils.createEmailVerificationCode(EmailConstantEnum.RESET_PASSWORD,email, stringRedisTemplate);

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("verificationCode", emailVerificationCodeObject.getCode());
        model.put("ttl", EmailConstantEnum.RESET_PASSWORD.getTTL());

        Template template = freemarkerConfig.getTemplate("email/resetPassword/password-reset.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        //读取迪幻的图片文件
        Resource resource = new ClassPathResource("templates/email/resetPassword/dihuanImage.png");
        File dihuanImage = Files.createTempFile("dihuanImage", ".png").toFile();
        Files.copy(resource.getInputStream(), dihuanImage.toPath(), StandardCopyOption.REPLACE_EXISTING);

        helper.setFrom("449134710@qq.com");
        helper.setTo(email);
        helper.setSubject("迪幻OJ在线判题系统 - 密码重置请求");
        helper.setText(htmlContent, true);
        FileSystemResource dihuanImageResource = new FileSystemResource(dihuanImage);
        helper.addInline("dihuanImage", dihuanImageResource);

        mailSender.send(mimeMessage);
        return emailVerificationCodeObject.getUuid();
    }

    @Override
    public String sendRegisterEmail(String email) throws Exception {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(userLambdaQueryWrapper);
        if(user!=null){
            throw new DihuanException(ResultCodeEnum.EMAIL_VERIFICATION_CODE_USER_EXIST_ERROR);
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        EmailVerificationCodeVo emailVerificationCodeObject = EmailVerificationUtils.createEmailVerificationCode(EmailConstantEnum.REGISTER_ACCOUNT,email, stringRedisTemplate);

        Map<String, Object> model = new HashMap<>();
        model.put("verificationCode", emailVerificationCodeObject.getCode());
        model.put("ttl", EmailConstantEnum.RESET_PASSWORD.getTTL());

        Template template = freemarkerConfig.getTemplate("email/registerAccount/register-account.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        //读取迪幻的图片文件
        Resource resource = new ClassPathResource("templates/email/registerAccount/dihuanImage.png");
        File dihuanImage = Files.createTempFile("dihuanImage", ".png").toFile();
        Files.copy(resource.getInputStream(), dihuanImage.toPath(), StandardCopyOption.REPLACE_EXISTING);

        helper.setFrom("449134710@qq.com");
        helper.setTo(email);
        helper.setSubject("迪幻OJ在线判题系统 - 账号注册请求");
        helper.setText(htmlContent, true);
        FileSystemResource dihuanImageResource = new FileSystemResource(dihuanImage);
        helper.addInline("dihuanImage", dihuanImageResource);

        mailSender.send(mimeMessage);
        return emailVerificationCodeObject.getUuid();
    }
}
