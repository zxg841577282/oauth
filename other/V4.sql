/*
 Navicat Premium Data Transfer

 Source Server         : 雪利曼192.168.2.169
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 192.168.2.169:3306
 Source Schema         : user_center

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 08/06/2021 13:31:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '设置用户是否自动Approval操作, 默认值为 ''false'', 可选值包括 ''true'',''false'', ''read'',''write''.\n该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为''true''或支持的scope值,则会跳过用户Approve的页面, 直接授权.',
  `types` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `member_id` bigint(20) NOT NULL COMMENT '业户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `dep_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态 0锁定 1有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近访问时间',
  `gender` char(1) DEFAULT '2' COMMENT '性别 0男 1女 2保密',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `t_user_username` (`username`) USING BTREE,
  KEY `t_user_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for user_third_party
-- ----------------------------
DROP TABLE IF EXISTS `user_third_party`;
CREATE TABLE `user_third_party` (
  `id` varchar(20) NOT NULL COMMENT '第三方唯一标识',
  `user_id` bigint(20) DEFAULT NULL COMMENT '系统用户id',
  `types` varchar(20) NOT NULL COMMENT '类型 qq,微信，支付宝，github等',
  `access_token` varchar(255) DEFAULT NULL COMMENT '第三方token',
  `refresh_token` varchar(255) DEFAULT NULL COMMENT '刷新token',
  `open_id` varchar(255) DEFAULT NULL COMMENT '微信小程秀',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
