/*
Navicat MySQL Data Transfer

Source Server         : rock_rc
Source Server Version : 50637
Source Host           : 106.15.43.79:3306
Source Database       : water

Target Server Type    : MYSQL
Target Server Version : 50637
File Encoding         : 65001

Date: 2017-08-31 18:27:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for access
-- ----------------------------
DROP TABLE IF EXISTS `access`;
CREATE TABLE `access` (
  `access_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_key` varchar(40) DEFAULT NULL,
  `note` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`access_id`),
  UNIQUE KEY `IX_key` (`access_key`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for alarm
-- ----------------------------
DROP TABLE IF EXISTS `alarm`;
CREATE TABLE `alarm` (
  `alarm_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `note` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`alarm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(20) DEFAULT NULL COMMENT '标签（外部根据标签查询）',
  `key` varchar(40) DEFAULT NULL COMMENT '配置项关键字',
  `url` varchar(500) DEFAULT NULL COMMENT '配置项URL',
  `user` varchar(100) DEFAULT NULL COMMENT '配置项用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '配置项密码',
  `explain` varchar(100) DEFAULT '',
  PRIMARY KEY (`row_id`),
  KEY `IX_tag` (`tag`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COMMENT='服务配置表';

-- ----------------------------
-- Table structure for monitor
-- ----------------------------
DROP TABLE IF EXISTS `monitor`;
CREATE TABLE `monitor` (
  `monitor_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL COMMENT '监视项目名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '监视类型（0:数据表预警；1:数据表报喜）',
  `source` varchar(100) NOT NULL COMMENT '数据源',
  `source_model` varchar(500) DEFAULT NULL,
  `rule` varchar(500) NOT NULL COMMENT '触发规则',
  `task_tag` varchar(50) NOT NULL DEFAULT '0' COMMENT '监视标签',
  `task_tag_exp` varchar(100) DEFAULT NULL,
  `alarm_mobile` varchar(100) DEFAULT NULL COMMENT '报警手机号',
  `alarm_exp` varchar(255) DEFAULT NULL,
  `alarm_count` int(11) NOT NULL DEFAULT '0' COMMENT '报警次数',
  `is_enabled` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`monitor_id`),
  KEY `IX_key` (`key`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) NOT NULL COMMENT 'md5(name+‘#’+address)',
  `name` varchar(40) NOT NULL,
  `ver` varchar(40) DEFAULT NULL COMMENT '版本号',
  `address` varchar(100) NOT NULL,
  `note` varchar(255) NOT NULL DEFAULT '',
  `alarm_mobile` varchar(20) NOT NULL DEFAULT '',
  `state` int(10) unsigned NOT NULL DEFAULT '0',
  `check_type` int(11) NOT NULL DEFAULT '0' COMMENT '检查方式（0被检查；1自己签到）',
  `check_url` varchar(200) NOT NULL COMMENT '状态检查地址',
  `check_last_time` datetime NOT NULL COMMENT '最后检查时间',
  `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
  `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `IX_key` (`key`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='0:待检查；1检查中';

-- ----------------------------
-- Table structure for synchronous
-- ----------------------------
DROP TABLE IF EXISTS `synchronous`;
CREATE TABLE `synchronous` (
  `sync_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) NOT NULL DEFAULT '',
  `name` varchar(40) NOT NULL,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '0,增量同步；1,更新同步；',
  `interval` int(11) NOT NULL DEFAULT '0' COMMENT '间隔时间（秒）',
  `target` varchar(100) DEFAULT NULL,
  `target_pk` varchar(50) DEFAULT NULL,
  `source` varchar(100) DEFAULT NULL,
  `source_model` varchar(255) DEFAULT NULL,
  `task_tag` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步标识（用于临时存数据）',
  `is_enabled` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`sync_id`),
  KEY `IX_key` (`key`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for whitelist
-- ----------------------------
DROP TABLE IF EXISTS `whitelist`;
CREATE TABLE `whitelist` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '',
  `ip` varchar(40) NOT NULL,
  `note` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_ip` (`ip`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;
