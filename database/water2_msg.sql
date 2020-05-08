/*
Navicat MySQL Data Transfer

Source Server         : db.dev.zmapi.cn
Source Server Version : 50722
Source Host           : mysql.dev.zmapi.cn:3306
Source Database       : water2_msg

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-05-08 14:20:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for water_msg_distribution
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_distribution`;
CREATE TABLE `water_msg_distribution` (
  `dist_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分发ID',
  `msg_id` bigint(20) NOT NULL COMMENT '待分发的消息ID',
  `subscriber_id` int(11) NOT NULL,
  `alarm_mobile` varchar(20) DEFAULT NULL,
  `alarm_sign` varchar(50) DEFAULT NULL,
  `receive_url` varchar(200) NOT NULL DEFAULT '',
  `receive_way` int(11) NOT NULL DEFAULT '0' COMMENT '接收方式（0HTTP异步等待；1HTTP同步等待；2HTTP异步不等待）',
  `receive_check` varchar(40) NOT NULL DEFAULT 'OK',
  `access_key` varchar(40) NOT NULL DEFAULT '',
  `is_sync` int(1) NOT NULL DEFAULT '0',
  `duration` int(11) NOT NULL DEFAULT '0' COMMENT '消耗时长（s）',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '分发状态（-1忽略；0开始；1失败；2成功；）',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '分发日期（yyyyMMdd）',
  `log_fulltime` datetime NOT NULL COMMENT '分发时间',
  PRIMARY KEY (`dist_id`),
  KEY `IX_date` (`log_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-消息-派送表';

-- ----------------------------
-- Table structure for water_msg_message
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_message`;
CREATE TABLE `water_msg_message` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `msg_key` varchar(40) NOT NULL,
  `topic_id` int(11) NOT NULL COMMENT '主题ID',
  `topic_name` varchar(40) DEFAULT NULL,
  `content` varchar(999) NOT NULL COMMENT '内容（JSON格式）',
  `receive_url` varchar(200) DEFAULT NULL COMMENT '定向接收目标',
  `receive_check` varchar(40) DEFAULT NULL COMMENT '接收检查',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）',
  `dist_count` int(11) NOT NULL DEFAULT '0' COMMENT '派发累记次数',
  `dist_nexttime` bigint(20) NOT NULL DEFAULT '0' COMMENT '下次派发时间',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` datetime NOT NULL COMMENT '记录时间',
  PRIMARY KEY (`msg_id`),
  UNIQUE KEY `IX_key` (`msg_key`) USING HASH,
  KEY `IX_topic` (`topic_id`) USING BTREE,
  KEY `IX_state` (`state`) USING BTREE,
  KEY `IX_dist_count` (`dist_count`),
  KEY `IX_log_date` (`log_date`),
  KEY `IX_dist_nexttime` (`dist_nexttime`) USING BTREE,
  KEY `IX_topic_name` (`topic_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-消息-存储表';

-- ----------------------------
-- Table structure for water_msg_message_ex_stat
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_message_ex_stat`;
CREATE TABLE `water_msg_message_ex_stat` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL DEFAULT '0' COMMENT '主题ID',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT 'yyyyMMdd',
  `log_hour` int(11) NOT NULL DEFAULT '-1' COMMENT 'HH（-1，全天）',
  `total` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-消息-统计表';

-- ----------------------------
-- Table structure for water_msg_subscriber
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_subscriber`;
CREATE TABLE `water_msg_subscriber` (
  `subscriber_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订阅者ID',
  `subscriber_key` varchar(40) NOT NULL COMMENT '订阅者KEY（由应用方传入）',
  `subscriber_note` varchar(40) DEFAULT NULL,
  `alarm_mobile` varchar(20) DEFAULT NULL COMMENT '报警手机号',
  `alarm_sign` varchar(50) DEFAULT NULL COMMENT '报警签名',
  `topic_id` int(11) NOT NULL DEFAULT '0' COMMENT '主题ID',
  `topic_name` varchar(40) NOT NULL COMMENT '主题名字',
  `receive_url` varchar(200) NOT NULL DEFAULT '' COMMENT '订阅者的接收地址',
  `receive_way` int(2) NOT NULL DEFAULT '0' COMMENT '接收方式（0HTTP异步等待；1HTTP同步等待；2HTTP异步不等待）',
  `access_key` varchar(40) NOT NULL DEFAULT '' COMMENT '订阅者的接收地址的访问KEY',
  `is_sync` int(1) NOT NULL DEFAULT '0' COMMENT '是否同步接收（0异步；1同步）',
  `log_fulltime` datetime NOT NULL COMMENT '记录的完整 时间',
  `is_unstable` int(1) NOT NULL DEFAULT '0' COMMENT '是否为不稳定地址',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
  `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
  PRIMARY KEY (`subscriber_id`),
  UNIQUE KEY `IX_subscribe` (`subscriber_key`,`topic_id`) USING BTREE,
  KEY `IX_topic_name` (`topic_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-消息-订阅者表';

-- ----------------------------
-- Table structure for water_msg_topic
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_topic`;
CREATE TABLE `water_msg_topic` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_name` varchar(40) DEFAULT NULL,
  `max_msg_num` int(11) NOT NULL DEFAULT '0',
  `max_distribution_num` int(11) NOT NULL DEFAULT '0' COMMENT '最大派发次数（0不限）',
  `max_concurrency_num` int(11) NOT NULL DEFAULT '0' COMMENT '最大同时派发数(0不限）',
  `alarm_model` int(4) NOT NULL DEFAULT '0' COMMENT '报警模式：0=普通模式；1=不报警',
  PRIMARY KEY (`topic_id`),
  UNIQUE KEY `IX_topic` (`topic_name`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-消息-主题表';
