/*
Navicat MySQL Data Transfer

Source Server         : db.dev.zmapi.cn
Source Server Version : 50722
Source Host           : mysql.dev.zmapi.cn:3306
Source Database       : water2_log

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-05-08 14:20:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rubber_log_request
-- ----------------------------
DROP TABLE IF EXISTS `rubber_log_request`;
CREATE TABLE `rubber_log_request` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `request_id` varchar(40) NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) NOT NULL COMMENT '计算方案',
  `policy` int(11) NOT NULL DEFAULT '0' COMMENT '处理策略',
  `args_json` varchar(999) NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text COMMENT '评估报告',
  `session_json` varchar(999) DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) DEFAULT NULL COMMENT '摘要',
  `start_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint(20) NOT NULL DEFAULT '0' COMMENT '处理时间（ms）',
  `state` int(4) NOT NULL DEFAULT '0' COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) DEFAULT NULL COMMENT '回调地址',
  `log_date` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-日志-请求记录表';

-- ----------------------------
-- Table structure for water_exam_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_bcf`;
CREATE TABLE `water_exam_log_bcf` (
  `log_id` bigint(20) NOT NULL,
  `service` varchar(50) NOT NULL COMMENT '标签',
  `schema` varchar(50) NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int(11) NOT NULL DEFAULT '0' COMMENT '秒数',
  `interval` bigint(20) NOT NULL DEFAULT '0' COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) DEFAULT NULL COMMENT '代码',
  `cmd_arg` text COMMENT '参数',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_hour` int(4) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_service` (`service`) USING BTREE,
  KEY `IX_operator` (`operator`),
  KEY `IX_schema` (`schema`),
  KEY `IX_path` (`path`),
  KEY `IX_method` (`method`) USING BTREE,
  KEY `IX_seconds` (`seconds`) USING BTREE,
  KEY `IX_cmd_sql_md5` (`cmd_sql_md5`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-日志-BCF监视';

-- ----------------------------
-- Table structure for water_exam_log_sql
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_sql`;
CREATE TABLE `water_exam_log_sql` (
  `log_id` bigint(20) NOT NULL,
  `service` varchar(50) NOT NULL COMMENT '标签',
  `schema` varchar(50) NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int(11) NOT NULL DEFAULT '0' COMMENT '秒数',
  `interval` bigint(20) NOT NULL DEFAULT '0' COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) DEFAULT NULL COMMENT '代码',
  `cmd_arg` text COMMENT '参数',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_hour` int(4) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_service` (`service`) USING BTREE,
  KEY `IX_operator` (`operator`),
  KEY `IX_schema` (`schema`),
  KEY `IX_path` (`path`),
  KEY `IX_method` (`method`),
  KEY `IX_seconds` (`seconds`) USING BTREE,
  KEY `IX_cmd_sql_md5` (`cmd_sql_md5`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-日志-SQL监视';

-- ----------------------------
-- Table structure for water_log_admin
-- ----------------------------
DROP TABLE IF EXISTS `water_log_admin`;
CREATE TABLE `water_log_admin` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=920042495 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_api
-- ----------------------------
DROP TABLE IF EXISTS `water_log_api`;
CREATE TABLE `water_log_api` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15809592627293680 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_log_bcf`;
CREATE TABLE `water_log_bcf` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_etl
-- ----------------------------
DROP TABLE IF EXISTS `water_log_etl`;
CREATE TABLE `water_log_etl` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`),
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_heihei
-- ----------------------------
DROP TABLE IF EXISTS `water_log_heihei`;
CREATE TABLE `water_log_heihei` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_msg
-- ----------------------------
DROP TABLE IF EXISTS `water_log_msg`;
CREATE TABLE `water_log_msg` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(100) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_paas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_paas`;
CREATE TABLE `water_log_paas` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的完整时间',
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_raas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_raas`;
CREATE TABLE `water_log_raas` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的完整时间',
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for water_log_sev
-- ----------------------------
DROP TABLE IF EXISTS `water_log_sev`;
CREATE TABLE `water_log_sev` (
  `log_id` bigint(20) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) NOT NULL COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '',
  `tag2` varchar(100) NOT NULL DEFAULT '',
  `tag3` varchar(100) NOT NULL DEFAULT '',
  `summary` varchar(1000) DEFAULT NULL,
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
