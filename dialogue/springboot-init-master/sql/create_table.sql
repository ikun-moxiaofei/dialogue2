# 数据库初始化

-- 创建库
create database if not exists dialogue;

 -- 切换库
 use dialogue;

 -- 用户表
 create table if not exists user
 (
     id           bigint auto_increment comment 'id' primary key,
     userAccount  varchar(256)                           not null comment '账号',
     userPassword varchar(512)                           not null comment '密码',
     unionId      varchar(256)                           null comment '微信开放平台id',
     mpOpenId     varchar(256)                           null comment '公众号openId',
     userName     varchar(256)                           null comment '用户昵称',
     userAvatar   varchar(1024)                          null comment '用户头像',
     userProfile  varchar(512)                           null comment '用户简介',
     userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
     createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
     updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
     isDelete     tinyint      default 0                 not null comment '是否删除',
     index idx_unionId (unionId)
 ) comment '用户' collate = utf8mb4_unicode_ci;

INSERT INTO user (userAccount, userPassword, userName, userRole)
VALUES ('u5', 'a2854e8d5c945e3097cfa4d5f1ae2060', 'Example User', 'admin');


-- 文件表
create table if not exists file
(
    id           bigint auto_increment comment 'id' primary key,
    fileName     varchar(256)                           not null comment '文件名',
    userId       bigint                                 not null comment '创建用户id',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    fileSize     varchar(256)                           not null comment '文件大小',
    state        tinyint      default 0                 not null comment '文件状态，0是未向量化，1是已向量化'
) comment '文件' collate = utf8mb4_unicode_ci;