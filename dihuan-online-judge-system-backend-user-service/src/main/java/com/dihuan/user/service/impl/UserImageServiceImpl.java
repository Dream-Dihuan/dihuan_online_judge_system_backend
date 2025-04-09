package com.dihuan.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.minio.MinioProperties;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.entity.UserImage;
import com.dihuan.user.mapper.UserImageMapper;
import com.dihuan.user.service.UserImageService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

/**
* @author 迪幻
* @description 针对表【user_image(用户图片sh)】的数据库操作Service实现
* @createDate 2025-04-09 09:23:45
*/
@Service
public class UserImageServiceImpl extends ServiceImpl<UserImageMapper, UserImage>
    implements UserImageService {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    @Override
    public String uploadUserAvatar(MultipartFile avatarFile) throws Exception{
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();

        // 判断头像存储桶是否存在
        boolean bucketExistStatus = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getAvatarBucketName()).build());
        // todo 限制文件的类型
        // 如果头像桶不存在则创建头像桶
        if(!bucketExistStatus){
            //创建这个 bucket
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(minioProperties.getAvatarBucketName())
                    .build());
            //设置这个 bucket 的访问权限
            minioClient.setBucketPolicy(SetBucketPolicyArgs.
                    builder().
                    bucket(minioProperties.getAvatarBucketName())
                    .config(createBucketPolicyConfig(minioProperties.getAvatarBucketName()))
                    .build());
        }

        // 清除用户原有的头像
        this.deleteUserAvatar();

        //上传文件的名称
        String filename = UUID.randomUUID()+"-"+ tokenInfo.getUsername() + "-" + avatarFile.getOriginalFilename();
        //上传远程文件到 minio 的 bucket 中
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(minioProperties.getAvatarBucketName())
                .stream(avatarFile.getInputStream(), avatarFile.getSize(), -1)
                .object(filename)
                .contentType(avatarFile.getContentType())
                .build());

        //返回 url 地址
        String avatarUrl = String.join("/", minioProperties.getEndpoint(), minioProperties.getAvatarBucketName(), filename);

        //更新用户的userImage数据库信息
        LambdaQueryWrapper<UserImage> userImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userImageLambdaQueryWrapper.eq(UserImage::getUserId,tokenInfo.getId());
        UserImage userImage = this.getOne(userImageLambdaQueryWrapper);
        if(userImage==null){
            userImage = new UserImage();
            userImage.setUserId(tokenInfo.getId());
        }
        userImage.setAvatarUrl(avatarUrl);
        boolean saveOrUpdate = this.saveOrUpdate(userImage);
        if(!saveOrUpdate){
            throw new DihuanException(ResultCodeEnum.FAIL,"头像上传失败");
        }

        return avatarUrl;
    }

    @Override
    public void deleteUserAvatar() throws Exception {
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();
        LambdaQueryWrapper<UserImage> userImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userImageLambdaQueryWrapper.eq(UserImage::getUserId,tokenInfo.getId());
        UserImage userImage = this.getOne(userImageLambdaQueryWrapper);

        if (userImage==null||userImage.getAvatarUrl()==null){
            return;
        }

        URI uri = new URI(userImage.getAvatarUrl());

        // 获取路径部分（去掉 domain 和 port 部分）
        String path = uri.getPath();

        // 获取文件名称
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        // 删除对象
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioProperties.getAvatarBucketName())
                        .object(fileName)
                        .build()
        );

        userImage.setAvatarUrl(null);
        boolean saveOrUpdate = this.saveOrUpdate(userImage);
        if(!saveOrUpdate){
            throw new DihuanException(ResultCodeEnum.FAIL,"头像删除失败");
        }
    }

    @Override
    public String uploadUserBanner(MultipartFile bannerFile) throws Exception {
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();

        // 判断头像存储桶是否存在
        boolean bucketExistStatus = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBannerBucketName()).build());
        // todo 限制文件的类型
        // 如果头像桶不存在则创建头像桶
        if(!bucketExistStatus){
            //创建这个 bucket
            minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(minioProperties.getBannerBucketName())
                    .build());
            //设置这个 bucket 的访问权限
            minioClient.setBucketPolicy(SetBucketPolicyArgs.
                    builder().
                    bucket(minioProperties.getBannerBucketName())
                    .config(createBucketPolicyConfig(minioProperties.getBannerBucketName()))
                    .build());
        }

        //上传文件的名称
        String filename = UUID.randomUUID()+"-"+ tokenInfo.getUsername() + "-" + bannerFile.getOriginalFilename();
        //上传远程文件到 minio 的 bucket 中
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(minioProperties.getBannerBucketName())
                .stream(bannerFile.getInputStream(), bannerFile.getSize(), -1)
                .object(filename)
                .contentType(bannerFile.getContentType())
                .build());

        // 清除原有的banner图
        this.deleteUserAvatar();

        //返回 url 地址
        String bannerUrl = String.join("/", minioProperties.getEndpoint(), minioProperties.getBannerBucketName(), filename);

        //更新用户的userImage数据库信息
        LambdaQueryWrapper<UserImage> userImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userImageLambdaQueryWrapper.eq(UserImage::getUserId,tokenInfo.getId());
        UserImage userImage = this.getOne(userImageLambdaQueryWrapper);
        if(userImage==null){
            userImage = new UserImage();
            userImage.setUserId(tokenInfo.getId());
        }
        userImage.setBannerUrl(bannerUrl);
        boolean saveOrUpdate = this.saveOrUpdate(userImage);
        if(!saveOrUpdate){
            throw new DihuanException(ResultCodeEnum.FAIL,"banner图上传失败");
        }

        return bannerUrl;
    }

    @Override
    public void deleteUserBanner() throws Exception{
        TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();

        LambdaQueryWrapper<UserImage> userImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userImageLambdaQueryWrapper.eq(UserImage::getUserId,tokenInfo.getId());
        UserImage userImage = this.getOne(userImageLambdaQueryWrapper);

        if (userImage==null||userImage.getBannerUrl()==null){
            return;
        }

        URI uri = new URI(userImage.getBannerUrl());

        // 获取路径部分（去掉 domain 和 port 部分）
        String path = uri.getPath();

        // 获取文件名称
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        // 删除对象
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioProperties.getBannerBucketName())
                        .object(fileName)
                        .build()
        );

        userImage.setBannerUrl(null);
        boolean saveOrUpdate = this.saveOrUpdate(userImage);
        if(!saveOrUpdate){
            throw new DihuanException(ResultCodeEnum.FAIL,"banner图删除失败");
        }
    }

    private String createBucketPolicyConfig(String bucketName) {
        return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);
    }
}




