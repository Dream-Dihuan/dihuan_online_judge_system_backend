package com.dihuan.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dihuan.model.entity.UserImage;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 迪幻
* @description 针对表【user_image(用户图片sh)】的数据库操作Service
* @createDate 2025-04-09 09:23:45
*/
public interface UserImageService extends IService<UserImage> {

    String uploadUserAvatar(MultipartFile avatarFile) throws Exception;

    void deleteUserAvatar() throws Exception;

    String uploadUserBanner(MultipartFile bannerFile) throws Exception;

    void deleteUserBanner() throws Exception;
}
