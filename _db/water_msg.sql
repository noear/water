SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for water_msg_distribution
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_distribution`;
CREATE TABLE `water_msg_distribution`  (
  `dist_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分发ID',
  `msg_id` bigint NOT NULL COMMENT '待分发的消息ID',
  `msg_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `subscriber_id` int NOT NULL,
  `subscriber_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `receive_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `receive_way` int NOT NULL DEFAULT 0 COMMENT '接收方式（0HTTP异步等待；1HTTP同步等待；2HTTP异步不等待）',
  `receive_check` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OK',
  `receive_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_sync` int NOT NULL DEFAULT 0,
  `duration` int NOT NULL DEFAULT 0 COMMENT '消耗时长（s）',
  `state` int NOT NULL DEFAULT 0 COMMENT '分发状态（-1忽略；0开始；1失败；2成功；）',
  `msg_state` int NOT NULL DEFAULT 0 COMMENT '消息状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '分发日期（yyyyMMdd）',
  `log_fulltime` datetime NOT NULL COMMENT '分发时间',
  PRIMARY KEY (`dist_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_msg_id`(`msg_id`) USING BTREE,
  INDEX `IX_msg_key`(`msg_key`) USING BTREE,
  INDEX `IX_msg_state`(`msg_state`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 723531 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-派送表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_msg_message
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_message`;
CREATE TABLE `water_msg_message`  (
  `msg_id` bigint NOT NULL COMMENT '消息ID',
  `msg_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `tags` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `topic_id` int NOT NULL COMMENT '主题ID',
  `topic_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `content` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容（JSON格式）',
  `receive_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '定向接收目标',
  `receive_check` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '接收检查',
  `plan_time` datetime NULL DEFAULT NULL,
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）',
  `dist_routed` tinyint(1) NOT NULL DEFAULT 0,
  `dist_count` int NOT NULL DEFAULT 0 COMMENT '派发累记次数',
  `dist_nexttime` bigint NOT NULL DEFAULT 0 COMMENT '下次派发时间',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` datetime NOT NULL COMMENT '记录时间',
  `last_fulltime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`msg_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`msg_key`) USING BTREE,
  INDEX `IX_topic`(`topic_id`) USING BTREE,
  INDEX `IX_state`(`state`) USING BTREE,
  INDEX `IX_dist_count`(`dist_count`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_dist_nexttime`(`dist_nexttime`) USING BTREE,
  INDEX `IX_topic_name`(`topic_name`) USING BTREE,
  INDEX `IX_tags`(`tags`(40)) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_msg_message_all
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_message_all`;
CREATE TABLE `water_msg_message_all`  (
  `msg_id` bigint NOT NULL COMMENT '消息ID',
  `msg_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `tags` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `topic_id` int NOT NULL COMMENT '主题ID',
  `topic_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `content` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容（JSON格式）',
  `receive_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '定向接收目标',
  `receive_check` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '接收检查',
  `plan_time` datetime NULL DEFAULT NULL,
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）',
  `dist_routed` tinyint(1) NOT NULL DEFAULT 0,
  `dist_count` int NOT NULL DEFAULT 0 COMMENT '派发累记次数',
  `dist_nexttime` bigint NOT NULL DEFAULT 0 COMMENT '下次派发时间',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` datetime NOT NULL COMMENT '记录时间',
  `last_fulltime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`msg_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`msg_key`) USING BTREE,
  INDEX `IX_topic`(`topic_id`) USING BTREE,
  INDEX `IX_state`(`state`) USING BTREE,
  INDEX `IX_dist_count`(`dist_count`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_dist_nexttime`(`dist_nexttime`) USING BTREE,
  INDEX `IX_topic_name`(`topic_name`) USING BTREE,
  INDEX `IX_tags`(`tags`(40)) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_msg_message_ex_stat
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_message_ex_stat`;
CREATE TABLE `water_msg_message_ex_stat`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `topic_id` int NOT NULL DEFAULT 0 COMMENT '主题ID',
  `log_date` int NOT NULL DEFAULT 0 COMMENT 'yyyyMMdd',
  `log_hour` int NOT NULL DEFAULT -1 COMMENT 'HH（-1，全天）',
  `total` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_topic_id`(`topic_id`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3748 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_msg_subscriber
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_subscriber`;
CREATE TABLE `water_msg_subscriber`  (
  `subscriber_id` int NOT NULL AUTO_INCREMENT COMMENT '订阅者ID',
  `subscriber_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订阅者KEY（由应用方传入）',
  `subscriber_note` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警手机号',
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警签名',
  `topic_id` int NOT NULL DEFAULT 0 COMMENT '主题ID',
  `topic_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主题名字',
  `receive_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订阅者的接收地址',
  `receive_way` int NOT NULL DEFAULT 0 COMMENT '接收方式（0,1异步等待；2异步不等待,状态设为已完成；3异步不等,状态设为处理中）',
  `receive_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订阅者的接收地址的访问KEY',
  `is_sync` int NOT NULL DEFAULT 0 COMMENT '是否同步接收（0异步；1同步）',
  `log_fulltime` datetime NOT NULL COMMENT '记录的完整 时间',
  `is_unstable` int NOT NULL DEFAULT 0 COMMENT '是否为不稳定地址',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `check_last_state` int NOT NULL DEFAULT 0 COMMENT '最后检查状态（0：OK；1：error）',
  `check_error_num` int NOT NULL DEFAULT 0 COMMENT '检测异常数量',
  PRIMARY KEY (`subscriber_id`) USING BTREE,
  UNIQUE INDEX `IX_subscribe`(`subscriber_key`, `topic_id`) USING BTREE,
  INDEX `IX_topic_name`(`topic_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 357 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-订阅者表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_msg_topic
-- ----------------------------
DROP TABLE IF EXISTS `water_msg_topic`;
CREATE TABLE `water_msg_topic`  (
  `topic_id` int NOT NULL AUTO_INCREMENT,
  `topic_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `max_msg_num` int NOT NULL DEFAULT 0,
  `max_distribution_num` int NOT NULL DEFAULT 0 COMMENT '最大派发次数（0不限）',
  `max_concurrency_num` int NOT NULL DEFAULT 0 COMMENT '最大同时派发数(0不限）',
  `stat_msg_day_num` int NOT NULL DEFAULT 0 COMMENT '日生产量',
  `alarm_model` int NOT NULL DEFAULT 0 COMMENT '报警模式：0=普通模式；1=不报警',
  `create_fulltime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`topic_id`) USING BTREE,
  UNIQUE INDEX `IX_topic`(`topic_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-消息-主题表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
