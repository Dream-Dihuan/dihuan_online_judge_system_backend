-- Dihuan Online Judge System Database Schema

-- 创建数据库
CREATE DATABASE IF NOT EXISTS dihuan_online_judge_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dihuan_online_judge_system;

-- 基础实体表，包含所有表的公共字段
-- 所有其他实体表都继承自BaseEntity，包含id, create_time, update_time, is_deleted字段

-- 用户信息表
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `name` VARCHAR(255) NOT NULL DEFAULT '幻崽' COMMENT '用户昵称',
    `gender` TINYINT NULL DEFAULT NULL COMMENT '性别 (0-男/1-女)',
    `description` TEXT NULL COMMENT '用户签名',
    `subscribe_number` BIGINT NOT NULL DEFAULT 0 COMMENT '粉丝数',
    `username` VARCHAR(255) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '电话号码',
    `birthday` DATE NULL DEFAULT NULL COMMENT '生日',
    `email` VARCHAR(255) NULL DEFAULT NULL COMMENT '电子邮箱',
    `role_id` BIGINT NOT NULL DEFAULT 0 COMMENT '权限角色id',
    `experience` BIGINT NOT NULL DEFAULT 0 COMMENT '经验值',
    `is_banned` TINYINT NOT NULL DEFAULT 0 COMMENT '是否封禁 (0-否/1-是)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 用户图片信息表
CREATE TABLE `user_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `avatar_url` VARCHAR(512) NULL DEFAULT NULL COMMENT '用户头像url',
    `banner_url` VARCHAR(512) NULL DEFAULT NULL COMMENT '用户banner图url',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户图片信息表';

-- 关注用户记录表
CREATE TABLE `user_subscribe` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户关注用户记录ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `subscribed_id` BIGINT NOT NULL COMMENT '被关注的用户ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_subscribed` (`user_id`, `subscribed_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_subscribed_id` (`subscribed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注用户记录表';

-- 题目信息表
CREATE TABLE `question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `title` VARCHAR(255) NOT NULL COMMENT '题目标题',
    `content` LONGTEXT NOT NULL COMMENT '题目内容',
    `tags` JSON NULL COMMENT '题目标签',
    `judge_case` JSON NULL COMMENT '判题检查点',
    `judge_config` JSON NULL COMMENT '判题配置',
    `accepted_number` BIGINT NOT NULL DEFAULT 0 COMMENT '题目通过数',
    `submit_number` BIGINT NOT NULL DEFAULT 0 COMMENT '题目提交数',
    `author_id` BIGINT NOT NULL COMMENT '作者用户ID',
    `check_status` BIGINT NOT NULL DEFAULT 0 COMMENT '题目审核状态',
    PRIMARY KEY (`id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_accepted_number` (`accepted_number`),
    KEY `idx_submit_number` (`submit_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目信息表';

-- 题目答案提交记录表
CREATE TABLE `question_submit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_id` BIGINT NOT NULL COMMENT '作答用户ID',
    `language` VARCHAR(50) NOT NULL COMMENT '作答代码语言名称',
    `code` LONGTEXT NOT NULL COMMENT '用户代码',
    `judge_status` TINYINT NOT NULL DEFAULT 0 COMMENT '判题状态(0-待判题 1-判题中 2-成功 3-失败)',
    `question_result` TINYINT NOT NULL DEFAULT 0 COMMENT '题目判题结果(0-未通过 1-通过)',
    `judge_result_info` JSON NULL COMMENT '判题结果信息',
    PRIMARY KEY (`id`),
    KEY `idx_question_id` (`question_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_judge_status` (`judge_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目答案提交记录表';

-- 用户题目收藏表
CREATE TABLE `question_collections` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删除 1-已删除)',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_question` (`user_id`, `question_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户题目收藏表';