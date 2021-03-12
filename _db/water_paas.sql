/*
 Navicat Premium Data Transfer

 Source Server         : @test-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 172.168.0.162:3306
 Source Schema         : water_paas

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 12/03/2021 15:44:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for paas_etl
-- ----------------------------
DROP TABLE IF EXISTS `paas_etl`;
CREATE TABLE `paas_etl`  (
  `etl_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类标签',
  `etl_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `code` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JSON配置代码',
  `is_enabled` int NOT NULL DEFAULT 0 COMMENT '是否启动 ',
  `is_extract` int NOT NULL DEFAULT 0 COMMENT '是否启用抽取器',
  `is_load` int NOT NULL DEFAULT 0 COMMENT '是否启用加载器',
  `is_transform` int NOT NULL DEFAULT 1 COMMENT '是否启用转换器',
  `cursor_type` int NOT NULL DEFAULT 0 COMMENT '0时间；1数值',
  `cursor` bigint NOT NULL DEFAULT 0 COMMENT '游标',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `e_enabled` int NOT NULL DEFAULT 0,
  `e_max_instance` int NOT NULL DEFAULT 1 COMMENT '抽取器集群数',
  `e_last_exectime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_enabled` int NOT NULL DEFAULT 0,
  `t_max_instance` int NOT NULL DEFAULT 1 COMMENT '转换器集群数',
  `t_last_exectime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `l_enabled` int NOT NULL DEFAULT 0,
  `l_max_instance` int NOT NULL DEFAULT 1 COMMENT '加载器集群数',
  `l_last_exectime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_extract_time` datetime NULL DEFAULT NULL COMMENT '最后抽取时间',
  `last_load_time` datetime NULL DEFAULT NULL COMMENT '最后加载时间',
  `last_transform_time` datetime NULL DEFAULT NULL COMMENT '最后转换时间',
  PRIMARY KEY (`etl_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `etl_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'PAAS-ETL配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for paas_file
-- ----------------------------
DROP TABLE IF EXISTS `paas_file`;
CREATE TABLE `paas_file`  (
  `file_id` int NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_type` int NOT NULL DEFAULT 0 COMMENT '文件类型(0:api, 1:pln, 2:tml)',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分组村签',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标记',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文件路径',
  `rank` int NOT NULL DEFAULT 0 COMMENT '排列（小的排前）',
  `is_staticize` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否静态',
  `is_editable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可编辑',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `is_exclude` tinyint(1) NOT NULL DEFAULT 0 COMMENT '排除导入',
  `link_to` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '连接到',
  `edit_mode` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '编辑模式',
  `content_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '内容类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `note` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  `plan_state` int NOT NULL DEFAULT 0 COMMENT '计划状态',
  `plan_begin_time` datetime NULL DEFAULT NULL COMMENT '计划开始执行时间',
  `plan_last_time` datetime NULL DEFAULT NULL COMMENT '计划最后执行时间',
  `plan_last_timespan` bigint NOT NULL DEFAULT 0 COMMENT '计划最后执行时间长度',
  `plan_interval` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '计划执行间隔',
  `plan_max` int NOT NULL DEFAULT 0 COMMENT '计划执行最多次数',
  `plan_count` int NOT NULL DEFAULT 0 COMMENT '计划执行累计次数',
  `create_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`path`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_label`(`label`) USING BTREE,
  INDEX `IX_file_type`(`file_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 314 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'PAAS-文件表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
