package com.dihuan.user.controller;

import com.dihuan.common.result.Result;
import com.dihuan.user.service.UserImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@Tag(name = "用户图片服务")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @Operation(summary = "上传用户头像")
    @PostMapping("uploadUserAvatar")
    public Result uploadUserAvatar(MultipartFile avatarFile) throws Exception {
        String avatarUrl = userImageService.uploadUserAvatar(avatarFile);
        return Result.success(avatarUrl);
    }

    @Operation(summary = "删除用户头像")
    @DeleteMapping("deleteUserAvatar")
    public Result deleteUserAvatar() throws Exception {
        userImageService.deleteUserAvatar();
        return Result.success();
    }

    @Operation(summary = "上传用户Banner图")
    @PostMapping("uploadUserBanner")
    public Result uploadUserBanner(MultipartFile bannerFile) throws Exception {
        String bannerUrl = userImageService.uploadUserBanner(bannerFile);
        return Result.success(bannerUrl);
    }

    @Operation(summary = "删除用户Banner图")
    @DeleteMapping("deleteUserBanner")
    public Result deleteUserBanner() throws Exception {
        userImageService.deleteUserBanner();
        return Result.success();
    }


}
