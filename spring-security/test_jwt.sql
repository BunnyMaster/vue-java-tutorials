/*
 Navicat Premium Data Transfer

 Source Server         : Test Account
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : rm-bp12z6hlv46vi6g8mro.mysql.rds.aliyuncs.com:3306
 Source Schema         : test_jwt

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 31/07/2025 12:17:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_auth_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_log`;
CREATE TABLE `sys_auth_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `event_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '事件类型(GRANTED=授权成功,DENIED=授权拒绝)',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `request_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求IP',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方法(GET,POST等)',
  `request_uri` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URI',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类名',
  `method_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方法名',
  `method_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '方法参数(JSON格式)',
  `required_authority` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所需权限表达式',
  `user_authorities` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户拥有的权限(JSON格式)',
  `decision_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '决策原因',
  `exception_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '删除标志(0=未删除 1=已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_event_type`(`event_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_class_method`(`class_name` ASC, `method_name` ASC) USING BTREE,
  INDEX `idx_request_uri`(`request_uri`(255) ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1946596447059324931 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统授权日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_auth_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_permission_code`(`permission_code` ASC) USING BTREE COMMENT '权限编码唯一索引',
  UNIQUE INDEX `url`(`url` ASC, `method` ASC) USING BTREE COMMENT '请求方法和url唯一索引',
  INDEX `description`(`description` ASC, `remark` ASC) USING BTREE COMMENT '详情和简述索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (1944409040897699841, 'permission::read', '/api/permission/**', 'GET', '所有用户GET请求', '用户GET请求', '2025-07-13 22:50:15', '2025-07-13 22:50:15', 0, NULL, 0);
INSERT INTO `t_permission` VALUES (1944726493780111361, 'permission::update', NULL, NULL, '拥有权限的更新', '权限更新', '2025-07-14 19:51:42', '2025-07-14 19:51:42', 0, NULL, 0);
INSERT INTO `t_permission` VALUES (1944755716804669442, 'role::read', NULL, NULL, '角色内容只读', '角色只读', '2025-07-14 21:47:49', '2025-07-14 21:47:49', 0, NULL, 0);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_role_name`(`role_code` ASC) USING BTREE COMMENT '角色名称唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'ADMIN', '管理员可以又全部的权限', '管理员角色', '2025-07-12 23:42:38', '2025-07-12 23:42:38', 1, 1, 0);
INSERT INTO `t_role` VALUES (1944391051196370945, 'USER', '普通用户的角色', '普通用户', '2025-07-13 21:38:46', '2025-07-13 21:38:46', 0, NULL, 0);
INSERT INTO `t_role` VALUES (1944398167533621250, 'DEPT', '部门相关的角色', '部门角色', '2025-07-13 22:07:03', '2025-07-13 22:07:03', 0, NULL, 0);
INSERT INTO `t_role` VALUES (1944404444649308161, 'TEST', '测试使用的角色', '测试角色', '2025-07-13 22:32:00', '2025-07-13 22:32:00', 0, NULL, 0);

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_role_id_perssion_id`(`role_id` ASC, `permission_id` ASC) USING BTREE COMMENT '角色和权限唯一索引',
  INDEX `idx_permission`(`permission_id` ASC) USING BTREE COMMENT '权限ID索引',
  INDEX `idx_role_permission`(`role_id` ASC, `permission_id` ASC) USING BTREE COMMENT '角色权限唯一索引',
  CONSTRAINT `t_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (1944643584666742786, 1, 1944409040897699841, '2025-07-14 14:22:15', '2025-07-14 14:22:15', 0, NULL, 0);
INSERT INTO `t_role_permission` VALUES (1944756083906932738, 1944391051196370945, 1944755716804669442, '2025-07-14 21:49:17', '2025-07-14 21:49:17', 0, NULL, 0);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE COMMENT '用户名唯一索引',
  UNIQUE INDEX `idx_email`(`email` ASC) USING BTREE COMMENT '邮箱唯一索引',
  UNIQUE INDEX `idx_username_email`(`username` ASC, `email` ASC) USING BTREE COMMENT '用户名邮箱唯一索引',
  INDEX `idx_password`(`password` ASC) USING BTREE COMMENT '密码唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'Keith Simmons', 'kB2gNzEhjB', 'keiths606@gmail.com', '2022-04-26 11:41:44', '2025-07-11 22:31:15', 1, 1, 1);
INSERT INTO `t_user` VALUES (2, 'Song Zitao', 'Winx0CSiQS', 'zitao70@icloud.com', '2018-02-09 10:46:28', '2025-07-13 21:55:28', 2, NULL, 0);
INSERT INTO `t_user` VALUES (1944384432521744386, 'Bunny', '$2a$10$BJakELpOIWoYfkv.m/hLQuAsZ1d0Dq00ehS6Usyi2za5MEke/iHSe', '131@qq.com', '2025-07-13 21:12:28', '2025-07-17 10:55:34', 1944384432521744386, 1944384432521744386, 0);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户ID',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE COMMENT '用户角色唯一索引',
  INDEX `role_id`(`role_id` ASC) USING BTREE COMMENT '角色唯一索引',
  INDEX `user_id`(`user_id` ASC) USING BTREE COMMENT '用户唯一索引',
  CONSTRAINT `t_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1944635731486814209, 1, 2, '2025-07-14 13:51:03', '2025-07-14 13:51:03', 0, NULL, 0);
INSERT INTO `t_user_role` VALUES (1944635731486814210, 1944398167533621250, 2, '2025-07-14 13:51:03', '2025-07-14 13:51:03', 0, NULL, 0);
INSERT INTO `t_user_role` VALUES (1946163076642508801, 1944391051196370945, 1944384432521744386, '2025-07-18 19:00:10', '2025-07-18 19:00:10', 0, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
