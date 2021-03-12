/*
 Navicat Premium Data Transfer

 Source Server         : @test-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 172.168.0.162:3306
 Source Schema         : water_log

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 12/03/2021 15:39:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rubber_log_request
-- ----------------------------
DROP TABLE IF EXISTS `rubber_log_request`;
CREATE TABLE `rubber_log_request`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int NOT NULL DEFAULT 0 COMMENT '处理策略',
  `args_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评估报告',
  `session_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int NOT NULL DEFAULT 0 COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint NOT NULL DEFAULT 0 COMMENT '处理时间（ms）',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `log_date` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-日志-请求记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rubber_log_request_all
-- ----------------------------
DROP TABLE IF EXISTS `rubber_log_request_all`;
CREATE TABLE `rubber_log_request_all`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int NOT NULL DEFAULT 0 COMMENT '处理策略',
  `args_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评估报告',
  `session_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int NOT NULL DEFAULT 0 COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint NOT NULL DEFAULT 0 COMMENT '处理时间（ms）',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `log_date` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-日志-请求记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sponge_log_admin
-- ----------------------------
DROP TABLE IF EXISTS `sponge_log_admin`;
CREATE TABLE `sponge_log_admin`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sponge_log_rock
-- ----------------------------
DROP TABLE IF EXISTS `sponge_log_rock`;
CREATE TABLE `sponge_log_rock`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_exam_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_bcf`;
CREATE TABLE `water_exam_log_bcf`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `service` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `schema` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int NOT NULL DEFAULT 0 COMMENT '秒数',
  `interval` bigint NOT NULL DEFAULT 0 COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
  `cmd_arg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  `log_hour` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_service`(`service`) USING BTREE,
  INDEX `IX_operator`(`operator`) USING BTREE,
  INDEX `IX_schema`(`schema`) USING BTREE,
  INDEX `IX_path`(`path`) USING BTREE,
  INDEX `IX_method`(`method`) USING BTREE,
  INDEX `IX_seconds`(`seconds`) USING BTREE,
  INDEX `IX_cmd_sql_md5`(`cmd_sql_md5`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-日志-BCF监视' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_exam_log_sql
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_sql`;
CREATE TABLE `water_exam_log_sql`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `service` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `schema` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int NOT NULL DEFAULT 0 COMMENT '秒数',
  `interval` bigint NOT NULL DEFAULT 0 COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
  `cmd_arg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  `log_hour` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_service`(`service`) USING BTREE,
  INDEX `IX_operator`(`operator`) USING BTREE,
  INDEX `IX_schema`(`schema`) USING BTREE,
  INDEX `IX_path`(`path`) USING BTREE,
  INDEX `IX_method`(`method`) USING BTREE,
  INDEX `IX_seconds`(`seconds`) USING BTREE,
  INDEX `IX_cmd_sql_md5`(`cmd_sql_md5`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-日志-SQL监视' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_admin
-- ----------------------------
DROP TABLE IF EXISTS `water_log_admin`;
CREATE TABLE `water_log_admin`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000094 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_api
-- ----------------------------
DROP TABLE IF EXISTS `water_log_api`;
CREATE TABLE `water_log_api`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_log_bcf`;
CREATE TABLE `water_log_bcf`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_etl
-- ----------------------------
DROP TABLE IF EXISTS `water_log_etl`;
CREATE TABLE `water_log_etl`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_heihei
-- ----------------------------
DROP TABLE IF EXISTS `water_log_heihei`;
CREATE TABLE `water_log_heihei`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_msg
-- ----------------------------
DROP TABLE IF EXISTS `water_log_msg`;
CREATE TABLE `water_log_msg`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_paas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_paas`;
CREATE TABLE `water_log_paas`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0 COMMENT '级别',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_raas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_raas`;
CREATE TABLE `water_log_raas`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0 COMMENT '级别',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_sev
-- ----------------------------
DROP TABLE IF EXISTS `water_log_sev`;
CREATE TABLE `water_log_sev`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0,
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0,
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_log_upstream
-- ----------------------------
DROP TABLE IF EXISTS `water_log_upstream`;
CREATE TABLE `water_log_upstream`  (
  `log_id` bigint NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `level` int NOT NULL DEFAULT 0 COMMENT '级别',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE,
  INDEX `IX_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
