# 仿牛客网社区项目

![IDE](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-brightgreen.svg) ![Java](https://img.shields.io/badge/Java-1.8-blue.svg) ![Database](https://img.shields.io/badge/Database-MySQL-lightgrey.svg)

## 项目简介

烂大街的仿牛客论坛，2022年做过一次，当时就是简单地跟着视频敲，没有学到什么，当时还是免费的，现在居然收费了，还挺贵的，不过B站上有搬过来的视频，优点是技术栈挺丰富，消息队列、缓存、搜索引擎都有，缺点是不是前后端分离，不适合前端选手，打算搞Java后端的可以拿来练手，进而熟悉里面的技术，不过牛客网原本发布的视频并没有完成项目的全部功能，我这里又重新完善了一下，包括在某些方面进行优化，后续还会继续进行完善，做得过程中有问题的可以发邮件给我，最后希望大家点个star！

## 功能列表

- [x] 用户注册
  - [x] 发送邮件进行账号激活
- [x] 用户登录
  - [x] 生成验证码
- [x] 用户登出
- [x] 账号设置
  - [x] 修改头像
  - [x] 修改密码

- [x] 个人中心
  - [x] 我的关注
  - [x] 我的粉丝
  - [x] 我的帖子
- [x] 帖子相关
  - [x] 最新、最热
  - [x] 全局搜索
  - [x] 置顶、加精、删除
  - [x] 过滤敏感词
  - [x] 发布帖子
  - [x] 帖子详情
  - [x] 点赞帖子
  - [x] 评论帖子
  - [x] 点赞评论
  - [x] 回复评论
- [x] 朋友私信
- [x] 系统通知
  - [x] 点赞类
  - [x] 评论类
  - [x] 关注类
- [x] 网站数据统计
- [x] 权限控制
- [x] 网站监控

## 技术选型

| 技术          | 版本   |
| :------------ | :----- |
| Spring Boot   | 2.6.11 |
| MyBatis       | 2.0.1  |
| Redis         | 7.0.4  |
| MySQL         | 8.0.26 |
| Kafka         | 3.8.0  |
| ElasticSearch | 7.8.0  |
| caffeine      | 2.7.0  |

## 系统架构图

![](https://github.com/barret-yzh/community_Img/blob/main/15_architecture.png?raw=true)

## 运行项目需要修改的

- 配置MySQL
- 配置Redis
- 配置kafka
- 配置ElasticSearch
- 配置邮箱
- 修改文件上传路径

## 运行效果展示

见仓库：https://github.com/barret-yzh/community_Img

## 功能优化日志

- 2024-9-26
  - 用户注册时，使用Kafka处理发送邮件的业务
- 2024-11-10
  - 配置Redis、Kafka、ES集群

## 功能更新日志

2024-9-26

- 实现我的帖子功能
- 实现忘记密码功能
- 点击用户头像可以跳转到其主页



