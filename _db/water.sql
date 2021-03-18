/*
 Navicat Premium Data Transfer

 Source Server         : @test-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 172.168.0.162:3306
 Source Schema         : water

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 12/03/2021 17:10:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rubber_actor
-- ----------------------------
DROP TABLE IF EXISTS `rubber_actor`;
CREATE TABLE `rubber_actor`  (
  `actor_id` int NOT NULL AUTO_INCREMENT COMMENT '参与ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者代号',
  `name_display` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者显示名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新',
  PRIMARY KEY (`actor_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_actor
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_block
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block`;
CREATE TABLE `rubber_block`  (
  `block_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '相关数据(sponge/angel)',
  `related_tb` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '相关数据表',
  `is_editable` int NOT NULL DEFAULT 0,
  `is_enabled` int NOT NULL DEFAULT 1,
  `struct` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据结构({f1:\'xx\'})',
  `app_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '应用表达式',
  `last_updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`block_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据块' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_block
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_block_item
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block_item`;
CREATE TABLE `rubber_block_item`  (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `block_id` int NOT NULL DEFAULT 0,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f4` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`block_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据块项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_block_item
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_model
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model`;
CREATE TABLE `rubber_model`  (
  `model_id` int NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '相关数据库',
  `field_count` int NULL DEFAULT 0,
  `init_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '构造表达式',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调试参数',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_model
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_model_field
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model_field`;
CREATE TABLE `rubber_model_field`  (
  `field_id` int NOT NULL AUTO_INCREMENT COMMENT '字段ID',
  `model_id` int NOT NULL DEFAULT 0 COMMENT '所属的模型ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段名称',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '显示名',
  `expr` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段动态生成代码',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字段',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_pk` int NOT NULL DEFAULT 0 COMMENT '是否是主键',
  PRIMARY KEY (`field_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`model_id`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 453 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据模型-字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_model_field
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_scheme
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme`;
CREATE TABLE `rubber_scheme`  (
  `scheme_id` int NOT NULL AUTO_INCREMENT COMMENT '方案ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '相关模型(tag/name)',
  `related_model_id` int NOT NULL DEFAULT 0 COMMENT '关联模型ID',
  `related_model_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联模型显示名',
  `related_block` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '引用函数',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调试参数',
  `event` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '事件',
  `node_count` int NOT NULL DEFAULT 0 COMMENT '下属工作流节点数据',
  `rule_count` int NOT NULL DEFAULT 0 COMMENT '下属规则数量',
  `rule_relation` int NOT NULL DEFAULT 0 COMMENT '规则关系（0并且关系，1或者关系）',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`scheme_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_scheme
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_scheme_node
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node`;
CREATE TABLE `rubber_scheme_node`  (
  `node_id` int NOT NULL AUTO_INCREMENT,
  `scheme_id` int NOT NULL,
  `node_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` int NOT NULL DEFAULT 0 COMMENT '节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '代号',
  `prve_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上个节点ID（type=line时，才有值 ）',
  `next_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '下个节点ID（type=line时，才有值 ）',
  `condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分支条件（type=line时，才有值 ：left,op,right,ct;left,op,right,ct）',
  `tasks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行任务（type=exec时，才有值：F,tag/fun1;R,tag/rule1 ）',
  `actor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参与者（type=exec时，才有值 ：tag/name）',
  `actor_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_enabled` int NULL DEFAULT 0 COMMENT '是否启用  0：未启用  1：启用 ',
  PRIMARY KEY (`node_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`scheme_id`, `node_key`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-节点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_scheme_node
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_scheme_node_design
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node_design`;
CREATE TABLE `rubber_scheme_node_design`  (
  `scheme_id` int NOT NULL,
  `details` varchar(9999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-节点设计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_scheme_node_design
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_scheme_rule
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_rule`;
CREATE TABLE `rubber_scheme_rule`  (
  `rule_id` int NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `scheme_id` int NOT NULL DEFAULT 0 COMMENT '方案ID',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '显示名',
  `advice` int NOT NULL DEFAULT 0 COMMENT '评估建议(0无,1交易放行,2审慎审核,3阻断交易)',
  `score` int NOT NULL DEFAULT 0 COMMENT '评估分值',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `expr` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '条件表达式（m.user_day(30),>,15,&&;left,op,right,ct）',
  `expr_display` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `event` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '状态，(0：禁用、1：启用)',
  `last_updatetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 275 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rubber_scheme_rule
-- ----------------------------

-- ----------------------------
-- Table structure for water_cfg_logger
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_logger`;
CREATE TABLE `water_cfg_logger`  (
  `logger_id` int NOT NULL AUTO_INCREMENT COMMENT '日志器ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `logger` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '日志器',
  `row_num` bigint NULL DEFAULT 0 COMMENT '累积行数',
  `row_num_today` bigint NOT NULL DEFAULT 0 COMMENT '今日行数',
  `row_num_today_error` bigint NOT NULL DEFAULT 0 COMMENT '今日错误行数',
  `row_num_yesterday` bigint NOT NULL DEFAULT 0 COMMENT '昨天行数',
  `row_num_yesterday_error` bigint NOT NULL DEFAULT 0 COMMENT '昨天错误行数',
  `row_num_beforeday` bigint NOT NULL DEFAULT 0 COMMENT '前天行数',
  `row_num_beforeday_error` bigint NOT NULL DEFAULT 0 COMMENT '前天错误行数',
  `keep_days` int NOT NULL DEFAULT 15 COMMENT '保留天数',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据源',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `is_alarm` int NOT NULL DEFAULT 0 COMMENT '是否报警',
  PRIMARY KEY (`logger_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_logger
-- ----------------------------
INSERT INTO `water_cfg_logger` VALUES (19, 'water', 'water_log_api', 2866677, 461179, 0, 539637, 0, 86233, 0, 15, '', NULL, 1, 1);
INSERT INTO `water_cfg_logger` VALUES (21, 'water', 'water_log_sev', 209142, 144447, 0, 15222, 0, 16190, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (28, 'water', 'water_log_admin', 58, 14, 0, 0, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (53, 'water', 'water_log_msg', 2506031, 432242, 0, 393738, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (61, 'water', 'water_log_etl', 0, 0, 0, 0, 0, 0, 0, 3, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (69, 'water', 'water_log_raas', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (70, 'water', 'water_log_paas', 131243, 13632, 0, 18355, 0, 18414, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (74, 'water', 'water_exam_log_sql', 72072, 4999, 0, 4424, 0, 163, 0, 15, '', '', 0, 0);
INSERT INTO `water_cfg_logger` VALUES (75, 'water', 'water_exam_log_bcf', 108, 56, 0, 22, 0, 0, 0, 15, '', '', 0, 0);
INSERT INTO `water_cfg_logger` VALUES (77, 'sponge', 'sponge_log_rock', 2154, 1971, 0, 173, 0, 10, 0, 15, '', '', 1, 1);
INSERT INTO `water_cfg_logger` VALUES (84, 'water', 'water_log_heihei', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (94, 'sponge', 'sponge_log_admin', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (101, 'water', 'water_log_upstream', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (102, 'sponge', 'sponge_log_track', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);

-- ----------------------------
-- Table structure for water_cfg_properties
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_properties`;
CREATE TABLE `water_cfg_properties`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分组标签',
  `key` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性key',
  `type` int NOT NULL DEFAULT 0 COMMENT '类型：0:未知，1:数据库；2:Redis；3:MangoDb; 4:Memcached',
  `value` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性值',
  `edit_mode` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_editable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否可编辑',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_tag_key`(`tag`, `key`) USING BTREE,
  INDEX `IX_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 983 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_properties
-- ----------------------------
INSERT INTO `water_cfg_properties` VALUES (25, 'water', 'water_msg', 10, 'schema=water_msg\nurl=jdbc:mysql://mysql.water.io:3306/water_msg?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root', 'properties', 1, 1, '2021-01-29 15:02:22');
INSERT INTO `water_cfg_properties` VALUES (28, 'water', 'water', 10, 'schema=water\nurl=jdbc:mysql://mysql.water.io:3306/water?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 1, 1, '2021-03-05 14:46:29');
INSERT INTO `water_cfg_properties` VALUES (29, 'water', 'water_log', 10, 'schema=water_log\nurl=jdbc:mysql://mysql.water.io:3306/water_log?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 1, 1, '2021-01-29 15:07:13');
INSERT INTO `water_cfg_properties` VALUES (39, 'water', 'water_redis', 11, 'server=redis.water.io:6379\npassword=123456\nuser=redis', 'properties', 1, 1, '2021-01-07 17:39:08');
INSERT INTO `water_cfg_properties` VALUES (44, 'water', 'alarm_sign', 0, '测试环境', 'text', 1, 1, '2021-03-11 11:08:23');
INSERT INTO `water_cfg_properties` VALUES (53, 'water', 'water_cache', 20, 'server=memcached.water.io:11211\nuser=memcached', 'properties', 1, 1, '2021-01-07 17:38:18');
INSERT INTO `water_cfg_properties` VALUES (84, 'sponge', 'sponge_track', 10, 'schema=sponge_track\nurl=jdbc:mysql://mysql.water.io:3306/sponge_track?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 1, 1, '2021-01-29 15:11:28');
INSERT INTO `water_cfg_properties` VALUES (86, 'sponge', 'sponge_redis', 11, 'server=redis.water.io:6379\npassword=123456\nuser=redis', 'properties', 1, 1, '2021-02-06 17:15:22');
INSERT INTO `water_cfg_properties` VALUES (87, 'sponge', 'sponge_cache', 20, 'server=memcached.water.io:11211\nuser=memcached', 'properties', 1, 1, '2021-02-06 17:18:47');
INSERT INTO `water_cfg_properties` VALUES (107, '_service', 'trackapi', 0, 'http://paas.water.io/sponge_track/{fun}', 'text', 1, 1, '2021-03-12 16:29:25');
INSERT INTO `water_cfg_properties` VALUES (167, 'water', 'paas_uri', 0, 'http://paas.water.io', 'properties', 1, 1, '2021-03-12 16:35:21');
INSERT INTO `water_cfg_properties` VALUES (168, 'sponge', 'track_uri', 0, 'http://track.sponge.io/', 'text', 1, 1, '2021-03-12 16:33:40');
INSERT INTO `water_cfg_properties` VALUES (184, 'water', 'water_msg_queue', 1102, 'store.name=water_msg_queue_dev\nstore.type=redis\nserver=redis.water.io:6379\nuser=redis\npassword=123456\n#for redis\ndb=3\n#for rocketmq\nvirtualHost=', 'properties', 1, 1, '2021-03-12 11:21:15');
INSERT INTO `water_cfg_properties` VALUES (186, '_demo', 'aliyun_oss', 0, 'url=http://oss-cn-shanghai-internal.aliyuncs.com\nendpoint=http://oss-cn-shanghai-internal.aliyuncs.com\naccessKeyId=LTAIMsMV4LOUCUp6\naccessSecret=HNLziZiAXUKyO0VsXZLrrDcBCaCSNV', 'properties', 1, 1, '2021-01-07 17:00:25');
INSERT INTO `water_cfg_properties` VALUES (191, '_service', 'paasapi', 0, 'http://paas.water.io/{fun}', 'properties', 1, 1, '2021-03-12 16:28:55');
INSERT INTO `water_cfg_properties` VALUES (193, 'water', 'paas_cache_header', 0, 'PAAS_API_CACHE_DEV', 'properties', 1, 1, '2021-02-06 12:39:55');
INSERT INTO `water_cfg_properties` VALUES (322, 'water', 'water_cache_header', 0, 'WATER2_API_DEV_CACHE', NULL, 1, 1, '2020-02-04 14:47:56');
INSERT INTO `water_cfg_properties` VALUES (324, 'water', 'raas_uri', 0, 'http://raas.water.io', 'text', 1, 1, '2021-03-12 16:35:30');
INSERT INTO `water_cfg_properties` VALUES (335, '_service', 'raasapi', 0, 'http://raas.dev.zmapi.cn/{fun}', NULL, 1, 1, '2020-04-13 14:25:53');
INSERT INTO `water_cfg_properties` VALUES (369, 'water', 'enable_tag_checker', 0, '0', 'properties', 1, 1, '2021-03-12 17:06:57');
INSERT INTO `water_cfg_properties` VALUES (370, 'water', 'water_paas', 10, 'schema=water_paas\nurl=jdbc:mysql://mysql.water.io:3306/water_paas?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 1, 1, '2021-01-29 15:10:41');
INSERT INTO `water_cfg_properties` VALUES (397, 'sponge', 'sponge_url', 0, 'http://8.136.188.140:8182/', 'properties', 1, 0, '2021-03-12 16:33:01');
INSERT INTO `water_cfg_properties` VALUES (623, '_gateway', 'rockrpc', 9, '{\"url\":\"\",\"service\":\"rockrpc\",\"policy\":\"polling\"}', NULL, 1, 1, '2021-01-03 21:02:46');
INSERT INTO `water_cfg_properties` VALUES (771, '_demo', 'elasticsearch', 13, 'url=http://es-cn-mp91drlei001fbmh6.public.elasticsearch.aliyuncs.com:9200\nusername=elastic\npassword=v7BNG38S2C7q', 'properties', 1, 1, '2021-01-07 16:59:54');
INSERT INTO `water_cfg_properties` VALUES (772, '_system', 'config_type', 0, '0=\n9=gateway\n10=rdb\n11=redis\n12=mongodb\n13=elasticsearch\n14=hbase\n20=memcached\n1002=ssh.key\n1003=iaas.ram\n1101=water.logger\n1102=water.queue\n1103=water.block', 'properties', 0, 1, '2021-03-12 11:05:48');
INSERT INTO `water_cfg_properties` VALUES (773, '_system', 'env_type', 0, '0=测试环境\r\n1=预生产环境\r\n2=生产环境', NULL, 0, 1, '2020-02-04 14:47:56');
INSERT INTO `water_cfg_properties` VALUES (774, '_system', 'iaas_type', 0, '0=ECS\r\n1=LBS\r\n2=RDS\r\n3=Redis\r\n4=Memcached\r\n5=DRDS\r\n6=ECI\r\n7=NAS\r\n8=PolarDB', NULL, 0, 1, '2020-06-09 11:21:42');
INSERT INTO `water_cfg_properties` VALUES (778, 'sponge', 'sponge_rock', 10, 'schema=sponge_rock\nurl=jdbc:mysql://mysql.water.io:3306/sponge_rock?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 0, 1, '2021-01-29 15:11:20');
INSERT INTO `water_cfg_properties` VALUES (780, '_demo', 'ali_oss_cfg', 0, 'host=https://img2.kdz6.cn\ndir=/cms/\nbucket=zm-51kb-estuary\nendpoint=oss-cn-shanghai-internal.aliyuncs.com\naccessKeyId=LTAIMsMV4LOUCUp6\naccessSecretKey=HNLziZiAXUKyO0VsXZLrrDcBCaCSNV', 'properties', 0, 1, '2021-02-05 13:27:08');
INSERT INTO `water_cfg_properties` VALUES (783, '_demo', 'rock', 10, 'schema=rock\r\nurl=jdbc:mysql://mysql.dev.zmapi.cn:3306/rock?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\r\npassword=gXWTCL18\r\nusername=root', NULL, 0, 1, '2021-01-29 11:07:55');
INSERT INTO `water_cfg_properties` VALUES (784, '_demo', 'test', 10, 'schema=test\r\nurl=jdbc:mysql://mysql.dev.zmapi.cn:3306/test?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\r\npassword=gXWTCL18\r\nusername=root', NULL, 0, 1, '2021-01-29 11:07:55');
INSERT INTO `water_cfg_properties` VALUES (789, '_system', 'iaas_region', 0, 'cn-shanghai=cn-shanghai\r\ncn-hangzhou=cn-hangzhou', NULL, 0, 1, '2020-03-18 09:26:59');
INSERT INTO `water_cfg_properties` VALUES (838, '_service', 'wateradmin', 0, 'http://admin.water.io/{fun}', 'properties', 0, 1, '2021-03-12 16:29:40');
INSERT INTO `water_cfg_properties` VALUES (887, '_demo', 'test_yml', 0, 'server:\r\n  port: 9001\r\napp:\r\n  id: speech\r\nknowledge:\r\n  init:\r\n    knowledgeTitles:\r\n      - kdTitle: 听不清\r\n        keyWords: \"[你说什么，没听清，听不清楚，再说一遍]\"\r\n        question: \"[没听懂，听不清楚]\"\r\n      - kdTitle: 无应答\r\n        kdInfos:\r\n          - 你好\r\n          - hello\r\n          - hi', 'yaml', 0, 1, '2020-06-24 12:09:21');
INSERT INTO `water_cfg_properties` VALUES (888, '_demo', 'test_json', 0, '{\r\n    \"server\":{\r\n        \"port\":9001\r\n    },\r\n    \"app\":{\r\n        \"id\":\"speech\"\r\n    },\r\n    \"knowledge\":{\r\n        \"init\":{\r\n            \"knowledgeTitles\":[\r\n                {\r\n                    \"kdTitle\":\"听不清\",\r\n                    \"keyWords\":\"[你说什么，没听清，听不清楚，再说一遍]\",\r\n                    \"question\":\"[没听懂，听不清楚]\"\r\n                },{\r\n                    \"kdTitle\":\"无应答\",\r\n                    \"kdInfos\":[\"你好\",\"hello\",\"hi\"]\r\n                }\r\n            ]\r\n        }\r\n    }\r\n}', 'json', 0, 1, '2020-06-24 17:21:11');
INSERT INTO `water_cfg_properties` VALUES (889, '_demo', 'test_props', 0, 'server.port=9001\r\napp.id=speech\r\nknowledge.init.knowledgeTitles[0].kdTitle=听不清\r\nknowledge.init.knowledgeTitles[0].keyWords=[你说什么，没听清，听不清楚，再说一遍]\r\nknowledge.init.knowledgeTitles[0].question=[没听懂，听不清楚]\r\nknowledge.init.knowledgeTitles[1].kdTitle=无应答\r\nknowledge.init.knowledgeTitles[1].kdInfos[0]=你好\r\nknowledge.init.knowledgeTitles[1].kdInfos[1]=hello\r\nknowledge.init.knowledgeTitles[1].kdInfos[2]=hi', 'properties', 0, 1, '2021-01-29 11:07:55');
INSERT INTO `water_cfg_properties` VALUES (910, 'water', 'water_log_level', 0, '3', 'properties', 0, 1, '2021-01-29 11:07:55');
INSERT INTO `water_cfg_properties` VALUES (913, '_gateway', 'waterapi', 9, '{\"url\":\"http://water\",\"service\":\"waterapi\",\"policy\":\"polling\"}', 'properties', 0, 1, '2021-01-07 18:50:18');
INSERT INTO `water_cfg_properties` VALUES (926, '_demo', 'test_txt', 0, '12345', 'text', 0, 1, '2020-09-25 22:05:10');
INSERT INTO `water_cfg_properties` VALUES (963, 'demo', 'test_db', 10, 'schema=water_log\nurl=jdbc:mysql://mysql.water.io:3306/water_log?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=aBy3dP5xsE4EtDra\nusername=water_log\njdbcUrl=${url}', 'properties', 0, 1, '2021-01-22 14:34:59');
INSERT INTO `water_cfg_properties` VALUES (964, 'demo', 'water_cache_header', 0, 'bbb', 'properties', 0, 1, '2021-02-02 14:14:21');
INSERT INTO `water_cfg_properties` VALUES (965, 'demo', 'test.properties', 0, 'db1.url=jdbc\ndb1.username=noear\ndb1.password=xxx\ndb1.jdbcUrl=${db1.url}', 'properties', 0, 1, '2021-01-22 18:09:59');
INSERT INTO `water_cfg_properties` VALUES (969, 'water', 'water_log_store', 1101, 'store.type=rdb\nschema=water_log\nurl=jdbc:mysql://mysql.water.io:3306/water_log?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\npassword=123456\nusername=root\njdbcUrl=${url}', 'properties', 0, 1, '2021-03-12 17:03:55');
INSERT INTO `water_cfg_properties` VALUES (970, 'water', 'water_msg_store', 1103, 'store.type=mongodb\nurl=mongodb://mongo.dev.io', 'properties', 0, 1, '2021-03-12 11:15:40');
INSERT INTO `water_cfg_properties` VALUES (978, 'water_bcf', 'bcf.yml', 0, 'bcf.cache:\n  server: \"memcached.water.io:11211\"\n  user: \"memcached\"\n  \nbcf.db:\n  schema: water_bcf\n  url: \"jdbc:mysql://mysql.water.io:3306/water_bcf?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true\"\n  password: \"123456\"\n  username: \"root\"\n\nserver.session:\n  state.domain: \"demo.water.io\"\n  timeout: 7200', 'yaml', 0, 1, '2021-03-12 16:35:53');
INSERT INTO `water_cfg_properties` VALUES (980, 'water_bcf', 'bcfadmin.yml', 0, 'bcfadmin.password: \"SykSYLWN9WTpzCHq\"', 'yaml', 0, 1, '2021-03-06 18:09:11');
INSERT INTO `water_cfg_properties` VALUES (981, 'water_bcf', 'bcfdock_env', 0, '测试环境', 'text', 0, 1, '2021-03-07 19:19:01');
INSERT INTO `water_cfg_properties` VALUES (982, 'demo', 'test', 0, 'test', NULL, 0, 1, '2021-03-12 10:33:47');

-- ----------------------------
-- Table structure for water_cfg_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_whitelist`;
CREATE TABLE `water_cfg_whitelist`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ip' COMMENT '名单类型',
  `value` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '值',
  `note` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `update_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `type`, `value`) USING BTREE,
  INDEX `IX_val`(`value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 181 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-安全名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_whitelist
-- ----------------------------
INSERT INTO `water_cfg_whitelist` VALUES (129, 'server', 'ip', '127.0.0.1', NULL, 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (131, 'server', 'ip', '0:0:0:0:0:0:0:1', '本地调试', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (162, 'client', 'ip', '0:0:0:0:0:0:0:1', '本机', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (165, 'master', 'ip', '0:0:0:0:0:0:0:1', '自己', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (168, '_alarm', 'mobile', '18658857337', '谢**', 0, '2020-08-27 22:46:37');
INSERT INTO `water_cfg_whitelist` VALUES (172, 'server', 'ip', '172.168.0.162', '压测1', 1, '2021-01-29 15:59:48');
INSERT INTO `water_cfg_whitelist` VALUES (173, 'master', 'ip', '172.168.0.164', NULL, 1, '2021-01-29 15:59:16');
INSERT INTO `water_cfg_whitelist` VALUES (174, 'server', 'ip', '172.168.0.163', '压测2', 1, '2021-01-29 15:59:58');
INSERT INTO `water_cfg_whitelist` VALUES (175, 'master', 'ip', '172.168.0.165', NULL, 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (176, 'server', 'ip', '172.168.0.164', '压测3', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (177, 'server', 'ip', '172.168.0.165', '压测4', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (178, 'server', 'ip', '172.168.0.166', '压测5', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (179, 'server', 'ip', '172.168.0.167', '压测6', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (180, '_alarm', 'ip', '2824262699', NULL, 1, NULL);

-- ----------------------------
-- Table structure for water_ops_project
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_project`;
CREATE TABLE `water_ops_project`  (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '项目名称',
  `type` int NOT NULL DEFAULT 0 COMMENT '0服务；1网站',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '项目描述',
  `developer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开发人',
  `git_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_enabled` int NULL DEFAULT 1 COMMENT '是否启用  0:禁用  1:启用',
  PRIMARY KEY (`project_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_project
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server`;
CREATE TABLE `water_ops_server`  (
  `server_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务器名称',
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础设施服务KEY',
  `iaas_type` int NOT NULL DEFAULT 0 COMMENT '基础设施类型（0ECS, 1BLS, 2RDS, 3Redis, 4Memcached,5DRDS）',
  `iaas_attrs` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '特性',
  `iaas_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础设施账号',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '外网地址（IP或域）',
  `address_local` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '内网地址（IP或域）',
  `hosts_local` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '本地域名映射',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `env_type` int NOT NULL DEFAULT 0 COMMENT '0测试环境；1预生产环境；2生产环境；',
  `sev_num` int NOT NULL DEFAULT 0,
  `is_enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`server_id`) USING BTREE,
  INDEX `IX_key`(`iaas_key`) USING BTREE,
  INDEX `IX_iaas_account`(`iaas_account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_bls
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_bls`;
CREATE TABLE `water_ops_server_track_bls`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `co_conect_num` bigint NOT NULL DEFAULT 0,
  `new_conect_num` bigint NOT NULL DEFAULT 0,
  `qps` bigint NOT NULL DEFAULT 0,
  `traffic_tx` bigint NOT NULL DEFAULT 0,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-BLS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_bls
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_dbs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_dbs`;
CREATE TABLE `water_ops_server_track_dbs`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `connect_usage` double(18, 2) NULL DEFAULT NULL COMMENT '连接数使用率',
  `cpu_usage` double(18, 2) NULL DEFAULT NULL COMMENT 'cpu使用率',
  `memory_usage` double(18, 2) NULL DEFAULT NULL COMMENT '内存使用率',
  `disk_usage` double(18, 2) NULL DEFAULT NULL COMMENT '硬盘使用率',
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-DBS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_dbs
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_ecs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_ecs`;
CREATE TABLE `water_ops_server_track_ecs`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cpu_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `memory_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `disk_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `broadband_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `tcp_num` bigint NOT NULL,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-ECS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_ecs
-- ----------------------------

-- ----------------------------
-- Table structure for water_reg_consumer
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_consumer`;
CREATE TABLE `water_reg_consumer`  (
  `row_id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `service` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务',
  `consumer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者',
  `consumer_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者地址',
  `consumer_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消费者远程IP',
  `is_unstable` int NOT NULL DEFAULT 0 COMMENT '是否为不稳定的',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否为已启用',
  `chk_last_state` int NOT NULL DEFAULT 0 COMMENT '最后检查状态（0：OK；1：error）',
  `chk_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后检查时间',
  `log_fulltime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_consumer_address`(`consumer_address`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务消费者表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_reg_consumer
-- ----------------------------
INSERT INTO `water_reg_consumer` VALUES (98, 'rockrpc', 'demoapi', '192.168.31.82:8080', '172.168.0.33', 0, 1, 1, '2021-03-12 10:33:48', '2021-03-10 16:01:51');
INSERT INTO `water_reg_consumer` VALUES (99, 'helloapi', 'helloapp', '192.168.31.82:8080', '172.168.0.33', 0, 1, 1, '2021-03-12 10:33:48', '2021-03-12 10:09:49');

-- ----------------------------
-- Table structure for water_reg_service
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service`;
CREATE TABLE `water_reg_service`  (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'md5(name+‘#’+address)',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ver` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版本号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `meta` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '源信息',
  `note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `alarm_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `state` int NOT NULL DEFAULT 0 COMMENT '0:待检查；1检查中',
  `code_location` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `check_type` int NOT NULL DEFAULT 0 COMMENT '检查方式（0被检查；1自己签到）',
  `check_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '状态检查地址',
  `check_last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后检查时间',
  `check_last_state` int NOT NULL DEFAULT 0 COMMENT '最后检查状态（0：OK；1：error）',
  `check_last_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后检查描述',
  `check_error_num` int NOT NULL DEFAULT 0 COMMENT '检测异常数量',
  `is_unstable` int NOT NULL DEFAULT 0 COMMENT '是否为不稳定的',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否为已启用',
  PRIMARY KEY (`service_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_address`(`address`(100)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务者表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_reg_service_runtime
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_runtime`;
CREATE TABLE `water_reg_service_runtime`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `memory_max` int NOT NULL DEFAULT 0,
  `memory_total` int NOT NULL DEFAULT 0,
  `memory_used` int NOT NULL DEFAULT 0,
  `thread_peak_count` int NOT NULL DEFAULT 0,
  `thread_count` int NOT NULL DEFAULT 0,
  `thread_daemon_count` int NOT NULL DEFAULT 0,
  `log_date` int NOT NULL DEFAULT 0,
  `log_hour` int NOT NULL DEFAULT 0,
  `log_minute` int NOT NULL DEFAULT 0,
  `log_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_address`(`address`(40)) USING BTREE,
  INDEX `IX_key_times`(`key`, `log_date`, `log_hour`, `log_minute`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_reg_service_runtime
-- ----------------------------

-- ----------------------------
-- Table structure for water_reg_service_speed
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed`;
CREATE TABLE `water_reg_service_speed`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `average` bigint NOT NULL DEFAULT 0,
  `average_ref` bigint NOT NULL DEFAULT 0 COMMENT '参考响应时间值 ',
  `fastest` bigint NOT NULL DEFAULT 0,
  `slowest` bigint NOT NULL DEFAULT 0,
  `total_num` bigint NOT NULL DEFAULT 0,
  `total_num_slow1` bigint NOT NULL DEFAULT 0,
  `total_num_slow2` bigint NOT NULL DEFAULT 0,
  `total_num_slow5` bigint NOT NULL DEFAULT 0,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务性能记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_reg_service_speed_date
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_date`;
CREATE TABLE `water_reg_service_speed_date`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `average` bigint NOT NULL DEFAULT 0,
  `fastest` bigint NOT NULL DEFAULT 0,
  `slowest` bigint NOT NULL DEFAULT 0,
  `total_num` bigint NOT NULL DEFAULT 0,
  `total_num_slow1` bigint NOT NULL DEFAULT 0,
  `total_num_slow2` bigint NOT NULL DEFAULT 0,
  `total_num_slow5` bigint NOT NULL DEFAULT 0,
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name`, `log_date`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务性能记录表-按日' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_reg_service_speed_hour
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_hour`;
CREATE TABLE `water_reg_service_speed_hour`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `average` bigint NOT NULL DEFAULT 0,
  `fastest` bigint NOT NULL DEFAULT 0,
  `slowest` bigint NOT NULL DEFAULT 0,
  `total_num` bigint NOT NULL DEFAULT 0,
  `total_num_slow1` bigint NOT NULL DEFAULT 0,
  `total_num_slow2` bigint NOT NULL DEFAULT 0,
  `total_num_slow5` bigint NOT NULL DEFAULT 0,
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期',
  `log_hour` int NOT NULL DEFAULT 0 COMMENT '记录小时',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name`, `log_date`, `log_hour`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务性能记录表-按时' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_tool_monitor
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_monitor`;
CREATE TABLE `water_tool_monitor`  (
  `monitor_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '监视项目key',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '监视项目名称',
  `type` int NOT NULL DEFAULT 0 COMMENT '监视类型（0:数据预警；1:数据简报）',
  `source_query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源模型脚本',
  `rule` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则（输入m:{d:{},tag:\'\'}）',
  `task_tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '监视标签',
  `task_tag_exp` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '监视标签产生的表达式',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `alarm_exp` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警信息产生的表达式',
  `alarm_count` int NOT NULL DEFAULT 0 COMMENT '报警次数',
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警签名',
  `is_enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`monitor_id`) USING BTREE,
  INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 119 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-监视登记表（监视数据）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_monitor
-- ----------------------------
INSERT INTO `water_tool_monitor` VALUES (102, 'water', '3750fcb165684780b02cf84652ea9295', '消息派发情况', 0, '--water/water_msg::\nselect count(*) num from `message` where state=0 and dist_count=0 and dist_ntime=0 ', 'return m.d[0].num>10;', '0', '{{m.d[0].num}}', '', '未派发消息数::{{m.d[0].num}}', 0, '', 1);
INSERT INTO `water_tool_monitor` VALUES (117, 'water', '8001cbf895d5403ab847c3c690289ff0', '服务监视情况', 0, '--water/water::\nselect count(*) num \nfrom `water_reg_service` \nwhere is_enabled=1 AND check_last_state=1', 'return m.d[0].num>0;', '6', '{{m.d[0].num}}', '', '有{{m.d[0].num}}个服务异常（或服务监测出错）', 306, '', 1);
INSERT INTO `water_tool_monitor` VALUES (118, 'water', 'da9985ec12964ef6918611f4b8d173da', '定时任务监视', 0, '--water/water_paas::\nselect count(*) num from paas_file \nwhere plan_state = 8 and is_disabled=0', 'return m.d[0].num !== 0;', '5', '{{m.d[0].num}}', '', '定时任务异常数量监视::{{m.d[0].num}}', 22807, '定时任务', 1);

-- ----------------------------
-- Table structure for water_tool_report
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_report`;
CREATE TABLE `water_tool_report`  (
  `row_id` int NOT NULL AUTO_INCREMENT COMMENT '简报ID',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类标签（外部根据标签查询）',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '查询名称',
  `args` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数变量',
  `code` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '查询代码',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_fulltime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-数据简报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_report
-- ----------------------------
INSERT INTO `water_tool_report` VALUES (1, '_demo', 'water_cfg_logger', '', '--water/water::\nselect * from water_cfg_logger limit 10', '', '2021-01-07 17:23:02');

-- ----------------------------
-- Table structure for water_tool_synchronous
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_synchronous`;
CREATE TABLE `water_tool_synchronous`  (
  `sync_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` int NOT NULL DEFAULT 0 COMMENT '0,增量同步；1,更新同步；',
  `interval` int NOT NULL DEFAULT 0 COMMENT '间隔时间（秒）',
  `target` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `target_pk` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `source_model` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源模型',
  `task_tag` bigint NOT NULL DEFAULT 0 COMMENT '同步标识（用于临时存数据）',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_enabled` int NOT NULL DEFAULT 0,
  `last_fulltime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`sync_id`) USING BTREE,
  INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-同步配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_synchronous
-- ----------------------------
INSERT INTO `water_tool_synchronous` VALUES (1, '_demo', '530d056e4018471fa5292065dbc549e2', '同步APPX表', 0, 100, '_demo/test::appx', 'app_id', '--_demo/rock::\nselect * from `appx` where app_id>@key order by app_id asc', 0, '', NULL, 0, '2020-05-27 15:44:26');

-- ----------------------------
-- Table structure for water_tool_versions
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_versions`;
CREATE TABLE `water_tool_versions`  (
  `commit_id` int NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `table` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表名',
  `key_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段名',
  `key_value` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段值',
  `is_hand` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为手工备份',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备份数据',
  `data_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备份数据md5',
  `log_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '记录人',
  `log_ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '记录IP',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期：yyyyMMdd',
  `log_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录完整时间',
  PRIMARY KEY (`commit_id`) USING BTREE,
  INDEX `IX_table_key`(`table`, `key_name`, `key_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-数据版本存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_versions
-- ----------------------------
INSERT INTO `water_tool_versions` VALUES (1, 'paas_file', 'file_id', '36', 0, '{\"file_id\":36,\"file_type\":0,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/paas/help/api/view\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"freemarker\",\"content_type\":\"code/internal\",\"content\":\"<!doctype html>\\n<html class=\\\"frm10\\\">\\n<head>\\n    <title>接口手册</title>\\n    <link rel=\\\"stylesheet\\\" href=\\\"//jtx.noear.org/img/_core/durian/main.css?v=14\\\" />\\n    <script src=\\\"//cdn.bootcss.com/jquery/3.4.1/jquery.min.js\\\"></script>\\n    <style>\\n    block{margin:0 10px;}\\n    </style>\\n</head>\\n<body>\\n<main>\\n\\n<div class=\\\"mar10-t mar10-b\\\"><h2>概要：</h2></div>\\n<br/>\\n\\n<block>\\n${sysinfo!}\\n</block>\\n<br/>\\n<block>\\n    <h2>已安装的 脚本或模板执行器</h2>\\n    <hr/>\\n    <ul>\\n        <#list XUtil.executorList() as m1>\\n        <li>\\n            <div>${m1}</div>\\n        </li>\\n        </#list>\\n    </ul>\\n</block>\\n<br/>\\n\\n<block>\\n    <h2>脚本和模板执行器 共享嵌入接口或对象</h2>\\n    <hr/>\\n    <ul>\\n        <li>\\n            <div class=\\\"t3\\\">/** 默认数据访问对象（:org.noear.weed.DbContext） */</div>\\n            <div>db</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 默认缓存服务对象（:org.noear.weed.cache.ICacheServiceEx），可被替换 */</div>\\n            <div>cache</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 当前上下文对象（:org.noear.solon.core.handle.Context） */</div>\\n            <div>ctx</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 本地缓存服务对象（:org.noear.weed.cache.ICacheServiceEx） */</div>\\n            <div>localCache</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 引擎嵌入基础工具 */</div>\\n            <div>XUtil</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 引擎嵌入锁工具 */</div>\\n            <div>XLock</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 引擎嵌入函数总线（用于跨语法共享函数） */</div>\\n            <div>XFun</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 引擎嵌入消息总线（用于水平拆分或扩展业务处理） */</div>\\n            <div>XMsg</div>\\n        </li>\\n    </ul>\\n</block>\\n<br/>\\n\\n<block>\\n    <h2>脚本执行器 专属嵌入接口或对象</h2>\\n    <hr/>\\n    <ul>\\n        <li>\\n            <div class=\\\"t3\\\">/** 加载模块或类 */</div>\\n            <div>var obj = <b>requireX</b>(path);</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 返回模块和视图 */</div>\\n            <div>return <b>modelAndView</b>(path, model);</div>\\n        </li>\\n        <li>\\n            <div class=\\\"t3\\\">/** 其它嵌入类 */</div>\\n            <div>XContext、ONode、Datetime、Timecount、Timespan</div>\\n        </li>\\n    </ul>\\n</block>\\n\\n<br/>\\n<div class=\\\"mar10-t mar10-b\\\"><h2>具体：<n style=\'font-size:1rem;font-weight:normal; \'>（可借用浏览器查找功能快速定位）</n></h2></div>\\n<br/>\\n\\n<#list XUtil.interfaceList() as l1>\\n<block>\\n    <div><h2>${l1.name}::${l1.type}</h2></div>\\n    <hr/>\\n    <ul>\\n        <#list l1.methods as s1>\\n        <li>\\n            <div class=\\\"t3\\\">${XUtil.htmlEncode(s1.note)!}</div>\\n            <div>${s1.code}</div>\\n        </li>\\n        </#list>\\n    </ul>\\n</block>\\n</#list>\\n\\n\\n</main>\\n</body>\\n</html>\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1580873847000,\"update_fulltime\":1610597542000}', '33e8ce189059c86f87378c36e8c01727', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:12:22');
INSERT INTO `water_tool_versions` VALUES (2, 'paas_file', 'file_id', '64', 0, '{\"file_id\":64,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync.fun\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"this.map2json = function (map){\\n  var obj = {};\\n  map.forEach(function(k,v){\\n    obj[k] = v;\\n  });\\n  \\n  return obj;\\n}\\n\\n\\nthis.speed_log = function(m){\\n    if(!m.tag || !m.name || !m.average){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.total_num_slow1){\\n      m.total_num_slow1=0;\\n    }\\n    \\n    if(!m.total_num_slow2){\\n      m.total_num_slow2=0;\\n    }\\n    \\n    if(!m.total_num_slow5){\\n      m.total_num_slow5=0;\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'total_num_slow1\',m.total_num_slow1)\\n      .set(\'total_num_slow2\',m.total_num_slow2)\\n      .set(\'total_num_slow5\',m.total_num_slow5)\\n      .build(function(tb){\\n        if(m.log_fulltime && m.log_fulltime.indexOf(\'-\')>0){\\n    \\t   tb.set(\'last_updatetime\',m.log_fulltime);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name\');\\n    \\n    return \'OK\';\\n}\\n\\nthis.speed_log_date = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    if(m.total_time){\\n       m.average = parseInt(m.total_time/m.total_num);\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_date\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .expre(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date\');\\n    \\n    return \'OK\';    \\n}\\n\\n\\nthis.speed_log_hour = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date || !m.log_hour){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_hour\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .set(\'log_hour\',m.log_hour)\\n      .expre(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date,log_hour\');\\n    \\n    \\n    return \'OK\';    \\n}\\n\\n\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1588729248000,\"update_fulltime\":1610598528000}', 'cb32f82d23831c6054b6e7bba1b87fdf', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:28:48');
INSERT INTO `water_tool_versions` VALUES (3, 'paas_file', 'file_id', '64', 0, '{\"file_id\":64,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync.fun\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"this.map2json = function (map){\\n  var obj = {};\\n  map.forEach(function(k,v){\\n    obj[k] = v;\\n  });\\n  \\n  return obj;\\n}\\n\\n\\nthis.speed_log = function(m){\\n    if(!m.tag || !m.name || !m.average){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.total_num_slow1){\\n      m.total_num_slow1=0;\\n    }\\n    \\n    if(!m.total_num_slow2){\\n      m.total_num_slow2=0;\\n    }\\n    \\n    if(!m.total_num_slow5){\\n      m.total_num_slow5=0;\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'total_num_slow1\',m.total_num_slow1)\\n      .set(\'total_num_slow2\',m.total_num_slow2)\\n      .set(\'total_num_slow5\',m.total_num_slow5)\\n      .build(function(tb){\\n        if(m.log_fulltime && m.log_fulltime.indexOf(\'-\')>0){\\n    \\t   tb.set(\'last_updatetime\',m.log_fulltime);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name\');\\n    \\n    return \'OK\';\\n}\\n\\nthis.speed_log_date = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    if(m.total_time){\\n       m.average = parseInt(m.total_time/m.total_num);\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_date\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .build(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date\');\\n    \\n    return \'OK\';    \\n}\\n\\n\\nthis.speed_log_hour = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date || !m.log_hour){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_hour\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .set(\'log_hour\',m.log_hour)\\n      .expre(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date,log_hour\');\\n    \\n    \\n    return \'OK\';    \\n}\\n\\n\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1588729248000,\"update_fulltime\":1610598532000}', 'fb510d1fbeed0fcb2cb035fea41661ce', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:28:52');
INSERT INTO `water_tool_versions` VALUES (4, 'paas_file', 'file_id', '64', 0, '{\"file_id\":64,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync.fun\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"this.map2json = function (map){\\n  var obj = {};\\n  map.forEach(function(k,v){\\n    obj[k] = v;\\n  });\\n  \\n  return obj;\\n}\\n\\n\\nthis.speed_log = function(m){\\n    if(!m.tag || !m.name || !m.average){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.total_num_slow1){\\n      m.total_num_slow1=0;\\n    }\\n    \\n    if(!m.total_num_slow2){\\n      m.total_num_slow2=0;\\n    }\\n    \\n    if(!m.total_num_slow5){\\n      m.total_num_slow5=0;\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'total_num_slow1\',m.total_num_slow1)\\n      .set(\'total_num_slow2\',m.total_num_slow2)\\n      .set(\'total_num_slow5\',m.total_num_slow5)\\n      .build(function(tb){\\n        if(m.log_fulltime && m.log_fulltime.indexOf(\'-\')>0){\\n    \\t   tb.set(\'last_updatetime\',m.log_fulltime);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name\');\\n    \\n    return \'OK\';\\n}\\n\\nthis.speed_log_date = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    if(m.total_time){\\n       m.average = parseInt(m.total_time/m.total_num);\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_date\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .build(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date\');\\n    \\n    return \'OK\';    \\n}\\n\\n\\nthis.speed_log_hour = function(m){\\n    if(!m.tag || !m.name || !m.average || !m.log_date || !m.log_hour){\\n       return \'ERROR\';\\n    }\\n    \\n    if(!m.slowest){\\n      m.slowest=0;\\n    }\\n    \\n    if(!m.fastest){\\n      m.fastest=0;\\n    }\\n    \\n    water.db(\'water/water\')\\n      .table(\'water_reg_service_speed_hour\')\\n      .set(\'average\',m.average).set(\'fastest\',m.fastest)\\n      .set(\'slowest\',m.slowest).set(\'total_num\',m.total_num)\\n      .set(\'service\',m.service)\\n      .set(\'tag\',m.tag)\\n      .set(\'name\',m.name)\\n      .set(\'log_date\',m.log_date)\\n      .set(\'log_hour\',m.log_hour)\\n      .build(function(tb){\\n        if(m.total_num_slow1){\\n    \\t  tb.set(\'total_num_slow1\',m.total_num_slow1);\\n    \\t}\\n      \\n        if(m.total_num_slow2){\\n    \\t  tb.set(\'total_num_slow2\',m.total_num_slow2);\\n    \\t}\\n      \\n        if(m.total_num_slow5){\\n    \\t  tb.set(\'total_num_slow5\',m.total_num_slow5);\\n    \\t}\\n      })\\n      .upsert(\'service,tag,name,log_date,log_hour\');\\n    \\n    \\n    return \'OK\';    \\n}\\n\\n\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1588729248000,\"update_fulltime\":1610598539000}', 'dedd2791f06be66e78205bde31491320', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:28:59');
INSERT INTO `water_tool_versions` VALUES (5, 'paas_file', 'file_id', '60', 0, '{\"file_id\":60,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"let util = requireX(\'/water/speed_sync.fun\');\\n\\nlet rd = water.rd(\'water/water_redis\',5);\\n\\nlet rlist = rd.open1(function(ru){\\n  return ru.key(\'monitor_keys\').hashGetAll();\\n});\\n\\nvar jlist = [];\\nrlist.forEach(function(k,v){\\n  jlist.push(k);\\n});\\n\\nlet now = new Datetime();\\n\\nlet log_date = now.toString(\'yyyyMMdd\');\\nlet log_hour = now.toString(\'HH\');\\n\\nfor(let i in jlist){\\n  let ikey = jlist[i];\\n  let dkey = ikey +\'$\'+ log_date;\\n  \\n  let dd = rd.open1((ru)=>{\\n\\treturn ru.key(dkey).hashGetAll();\\n  });\\n  \\n  if(dd && dd.size()>0){\\n    let ss = ikey.split(\'$\');\\n\\tlet mm = util.map2json(dd);\\n\\t\\n\\tmm.service = ss[0];\\n\\tmm.tag = ss[1];\\n\\tmm.name = ss[2];\\n\\tmm.log_fulltime = rlist.get(ikey);\\n  \\n    if(mm.service && mm.service != \'null\'){\\n        util.speed_log(mm);\\n    }\\n  }\\n}\\n\\nreturn \'OK\';\\n\\n\",\"note\":\"\",\"plan_state\":8,\"plan_begin_time\":1588694400000,\"plan_last_time\":1610598877000,\"plan_last_timespan\":0,\"plan_interval\":\"5m\",\"plan_max\":0,\"plan_count\":47494,\"create_fulltime\":1588729125000,\"update_fulltime\":1610599023000}', '670230983d5f65930e73c2b3ecbc3ecf', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:37:03');
INSERT INTO `water_tool_versions` VALUES (6, 'paas_file', 'file_id', '60', 0, '{\"file_id\":60,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"let util = requireX(\'/water/speed_sync.fun\');\\n\\nlet rd = water.rd(\'water/water_redis\',5);\\n\\nlet rlist = rd.open1(function(ru){\\n  return ru.key(\'monitor_keys\').hashGetAll();\\n});\\n\\nvar jlist = [];\\nrlist.forEach(function(k,v){\\n  jlist.push(k);\\n});\\n\\nlet now = new Datetime();\\n\\nlet log_date = now.toString(\'yyyyMMdd\');\\nlet log_hour = now.toString(\'HH\');\\n\\nfor(let i in jlist){\\n  let ikey = jlist[i];\\n  let dkey = ikey +\'$\'+ log_date;\\n  \\n  let dd = rd.open1((ru)=>{\\n\\treturn ru.key(dkey).hashGetAll();\\n  });\\n  \\n  if(dd && dd.size()>0){\\n    let ss = ikey.split(\'$\');\\n\\tlet mm = util.map2json(dd);\\n\\t\\n\\tmm.service = ss[0];\\n\\tmm.tag = ss[1];\\n\\tmm.name = ss[2];\\n\\tmm.log_fulltime = rlist.get(ikey);\\n  \\n    if(mm.service && mm.service != \'null\'){\\n        util.speed_log(mm);\\n    }\\n  }\\n}\\n\\nreturn \'OK\';\\n\\n\",\"note\":\"\",\"plan_state\":8,\"plan_begin_time\":1588694400000,\"plan_last_time\":1610598877000,\"plan_last_timespan\":0,\"plan_interval\":\"5m\",\"plan_max\":0,\"plan_count\":47494,\"create_fulltime\":1588729125000,\"update_fulltime\":1610599026000}', '5f4beda53c480cce19d9626b0ba7aba3', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:37:06');
INSERT INTO `water_tool_versions` VALUES (7, 'paas_file', 'file_id', '61', 0, '{\"file_id\":61,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync_date\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"let util = requireX(\'/water/speed_sync.fun\');\\n\\nlet rd = water.rd(\'water/water_redis\',5);\\n\\nlet rlist = rd.open1(function(ru){\\n  return ru.key(\'monitor_keys\').hashGetAll();\\n});\\n\\nvar jlist = [];\\nrlist.forEach(function(k,v){\\n  jlist.push(k);\\n});\\n\\nlet now = new Datetime();\\n\\nlet log_date = now.toString(\'yyyyMMdd\');\\nlet log_hour = now.toString(\'HH\');\\n\\n\\nfor(var i in jlist){\\n  let ikey = jlist[i];\\n  let dkey = ikey +\'$\'+ log_date;\\n  \\n  let dd = rd.open1((ru)=>{\\n\\treturn ru.key(dkey).hashGetAll();\\n  });\\n  \\n  if(dd && dd.size()>0){\\n    let ss = ikey.split(\'$\');\\n\\tlet mm = util.map2json(dd);\\n\\t\\n\\tmm.service = ss[0];\\n\\tmm.tag = ss[1];\\n\\tmm.name = ss[2];\\n\\tmm.log_date = log_date;\\n  \\n    if(mm.service && mm.service != \'null\'){\\n        util.speed_log_date(mm);\\n    }\\n  }\\n}\\n\\n\\nreturn \'OK\';\\n\\n\",\"note\":\"\",\"plan_state\":8,\"plan_begin_time\":1588694400000,\"plan_last_time\":1610598877000,\"plan_last_timespan\":0,\"plan_interval\":\"5m\",\"plan_max\":0,\"plan_count\":47489,\"create_fulltime\":1588729136000,\"update_fulltime\":1610599035000}', '70f84981e7bdc2d01083ccde3a588063', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:37:15');
INSERT INTO `water_tool_versions` VALUES (8, 'paas_file', 'file_id', '62', 0, '{\"file_id\":62,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/speed_sync_hour\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"let util = requireX(\'/water/speed_sync.fun\');\\n\\nlet rd = water.rd(\'water/water_redis\',5);\\n\\nlet rlist = rd.open1(function(ru){\\n  return ru.key(\'monitor_keys\').hashGetAll();\\n});\\n\\nvar jlist = [];\\nrlist.forEach(function(k,v){\\n  jlist.push(k);\\n});\\n\\nlet now = new Datetime();\\n\\nlet log_date = now.toString(\'yyyyMMdd\');\\nlet log_hour = now.toString(\'HH\');\\n\\n\\nfor(let i in jlist){\\n  let ikey = jlist[i];\\n  let dkey = ikey +\'$\'+ log_date + log_hour;\\n  \\n  let dd = rd.open1((ru)=>{\\n\\treturn ru.key(dkey).hashGetAll();\\n  });\\n  \\n  if(dd && dd.size()>0){\\n    let ss = ikey.split(\'$\');\\n\\tlet mm = util.map2json(dd);\\n\\t\\n\\tmm.service = ss[0];\\n\\tmm.tag = ss[1];\\n\\tmm.name = ss[2];\\n\\tmm.log_date = log_date;\\n\\tmm.log_hour = log_hour;\\n  \\n    if(mm.service && mm.service != \'null\'){\\n        util.speed_log_hour(mm);\\n    }\\n  }\\n}\\n\\nreturn \'OK\';\",\"note\":\"\",\"plan_state\":8,\"plan_begin_time\":1588694400000,\"plan_last_time\":1610598877000,\"plan_last_timespan\":0,\"plan_interval\":\"5m\",\"plan_max\":0,\"plan_count\":48471,\"create_fulltime\":1588729145000,\"update_fulltime\":1610599041000}', 'afe4b2b7e16c219e603308608dd549c5', '谢月甲', '125.119.234.227', 20210114, '2021-01-14 12:37:21');
INSERT INTO `water_tool_versions` VALUES (9, 'paas_file', 'file_id', '286', 0, '{\"file_id\":286,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/_service_runtime_15d_del\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\" var date = Datetime.Now().addDay(-15).getDate();\\n \\n db2.table(\\\"water_reg_service_runtime\\\").where(\'log_date<?\',date).delete();\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1611124899000,\"update_fulltime\":1611124970000}', 'ec622ab1d7fd22e4e3f149ec00acf2cc', '谢月甲', '115.199.104.127', 20210120, '2021-01-20 14:42:50');
INSERT INTO `water_tool_versions` VALUES (10, 'paas_file', 'file_id', '286', 0, '{\"file_id\":286,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/_service_runtime_15d_del\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\" var db2 = water.db(\\\"water/eater\\\");\\n var date = Datetime.Now().addDay(-15).getDate();\\n \\n db2.table(\\\"water_reg_service_runtime\\\").where(\'log_date<?\',date).delete();\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1611124899000,\"update_fulltime\":1611124994000}', 'b2d4aa8d746474e912b7c622bc90ea3d', '谢月甲', '115.199.104.127', 20210120, '2021-01-20 14:43:14');
INSERT INTO `water_tool_versions` VALUES (11, 'paas_file', 'file_id', '286', 0, '{\"file_id\":286,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/_service_runtime_15d_del\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\" var db2 = water.db(\\\"water/water\\\");\\n var date = Datetime.Now().addDay(-15).getDate();\\n \\n db2.table(\\\"water_reg_service_runtime\\\").where(\'log_date<?\',date).delete();\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1611124899000,\"update_fulltime\":1611125010000}', 'd39fd69d10f4668c46c38c670d13a2a8', '谢月甲', '115.199.104.127', 20210120, '2021-01-20 14:43:30');
INSERT INTO `water_tool_versions` VALUES (12, 'paas_file', 'file_id', '286', 0, '{\"file_id\":286,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/_service_runtime_15d_del\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\" var db2 = water.db(\\\"water/water\\\");\\n var date = Datetime.Now().addDay(-15).getDate();\\n \\n db2.table(\\\"water_reg_service_runtime\\\").where(\'log_date<?\',date).delete();\\n\\n\\n return \'OK\';\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1611124899000,\"update_fulltime\":1611125020000}', '017e126a1a342baedafbf60f64d075f1', '谢月甲', '115.199.104.127', 20210120, '2021-01-20 14:43:40');
INSERT INTO `water_tool_versions` VALUES (13, 'paas_file', 'file_id', '23', 0, '{\"file_id\":23,\"file_type\":0,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/log_stat.clz\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"code/class\",\"content\":\"\\nthis.logger_stat_date = function(list,date,field){\\n    var db_log = water.db(\\\"water/water_log\\\");\\n    var db     = water.db(\\\"water/water\\\");\\n    \\n    for(var i in list){\\n        var lm = list[i];\\n        \\n        try{\\n            var num = db_log.exe(\\\"SELECT COUNT(*) FROM \\\"+lm.logger+\\\" WHERE log_date=?\\\",date);\\n            db.exe(\\\"UPDATE water_cfg_logger SET \\\"+field+\\\"=? WHERE logger_id=?\\\", num, lm.logger_id);\\n        }\\n        catch(err){}\\n    }\\n}\\n\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1579238729000,\"update_fulltime\":1611460622000}', '2e4c3de93c833baf9ea5944a1fc3b07f', '谢月甲', '115.196.68.137', 20210124, '2021-01-24 11:57:02');
INSERT INTO `water_tool_versions` VALUES (14, 'paas_file', 'file_id', '13', 0, '{\"file_id\":13,\"file_type\":0,\"tag\":\"sdk_water\",\"label\":\"hook.start\",\"path\":\"/sdk_water/_init.jsx\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"if(__global.water && __global.water.ver==1){\\n    return {msg:\'old\'};\\n}\\n\\n// 不带 var 开头的变量为引擎全局变量\\n__global.water = {ver:2};\\n\\nwater = __global.water;\\n    \\nWaterClient = Java.type(\'org.noear.water.WaterClient\');\\nWaterProxy = Java.type(\'org.noear.water.WaterProxy\');\\nWaterLogger = Java.type(\'org.noear.water.log.WaterLogger\');\\n\\nwater.client = WaterClient;\\nwater.proxy  = WaterProxy;\\n\\nwater.cfg = function(tagKey){return WaterClient.Config.getByTagKey(tagKey)};\\nwater.db = function(tagKey){return water.cfg(tagKey).getDb()};\\nwater.rd = function(tagKey,i){return water.cfg(tagKey).getRd(i)};\\nwater.mg = function(tagKey,c){return water.cfg(tagKey).getMg(c)};\\nwater.updateCache = function(tags){WaterClient.Notice.updateCache(tags)};\\n\\nwater.call = function(service,fun,args){if(!args){args={}} return WaterProxy.call(service,fun,args);};\\nwater.paas = function(path,args){if(!args){args={}} return WaterProxy.paas(path,args)};\\nwater.raas = function(path,args){var args2={};if(args){for(var k in args){var v=args[k];if(v){if(v instanceof Object){args2[k]=JSON.stringify(v)}else{args2[k]=v}}}}return WaterProxy.raas(path,args2)};\\n\\nwater.heihei = function(target,msg){return WaterClient.Notice.heihei(target, msg);};\\n\\nwater.sendMessage = function(topic,message){ return WaterClient.Message.sendMessage(topic, message); }\\n\\nLocalDate = Java.type(\'java.time.LocalDate\');\\nLocalTime = Java.type(\'java.time.LocalTime\');\\nLocalDateTime = Java.type(\'java.time.LocalDateTime\');\\n\\nXUtil.ridAdd(\'WaterLogger.get(name)\',WaterLogger.class);\\n\\nreturn \'OK\';\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1579159438000,\"update_fulltime\":1612258419000}', 'd2b608aec01a6df3bc4119b272b39a58', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:33:39');
INSERT INTO `water_tool_versions` VALUES (15, 'paas_file', 'file_id', '13', 0, '{\"file_id\":13,\"file_type\":0,\"tag\":\"sdk_water\",\"label\":\"hook.start\",\"path\":\"/sdk_water/_init.jsx\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"if(__global.water && __global.water.ver==1){\\n    return {msg:\'old\'};\\n}\\n\\n// 不带 var 开头的变量为引擎全局变量\\n__global.water = {ver:2};\\n\\nwater = __global.water;\\n    \\nWaterClient = Java.type(\'org.noear.water.WaterClient\');\\nWaterProxy = Java.type(\'org.noear.water.WaterProxy\');\\nWaterLogger = Java.type(\'org.noear.water.log.WaterLogger\');\\n\\nwater.client = WaterClient;\\nwater.proxy  = WaterProxy;\\n\\nwater.cfg = function(tagKey){return WaterClient.Config.getByTagKey(tagKey)};\\nwater.db = function(tagKey){return water.cfg(tagKey).getDb()};\\nwater.rd = function(tagKey,i){return water.cfg(tagKey).getRd(i)};\\nwater.mg = function(tagKey,c){return water.cfg(tagKey).getMg(c)};\\nwater.updateCache = function(tags){WaterClient.Notice.updateCache(tags)};\\n\\nwater.call = function(service,fun,args){if(!args){args={}} return WaterProxy.call(service,fun,args);};\\nwater.paas = function(path,args){if(!args){args={}} return WaterProxy.paas(path,args)};\\nwater.raas = function(path,args){var args2={};if(args){for(var k in args){var v=args[k];if(v){if(v instanceof Object){args2[k]=JSON.stringify(v)}else{args2[k]=v}}}}return WaterProxy.raas(path,args2)};\\n\\nwater.heihei = function(target,msg){return WaterClient.Notice.heihei(target, msg);};\\n\\nwater.sendMessage = function(topic,message){ return WaterClient.Message.sendMessage(topic, message); }\\n\\nLocalDate = Java.type(\'java.time.LocalDate\');\\nLocalTime = Java.type(\'java.time.LocalTime\');\\nLocalDateTime = Java.type(\'java.time.LocalDateTime\');\\n\\nXUtil.ridAdd(\'WaterLogger.get(name)\',WaterLogger.class);\\n\\nreturn \'OK\';\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1579159438000,\"update_fulltime\":1612258451000}', '1244f889f86f8ee1c6d5df0f94118115', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:34:11');
INSERT INTO `water_tool_versions` VALUES (16, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user2\\\", {name:\\\"noear\\\"});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612258533000}', '53ae9fbda20e3ffcbd528e7150c48201', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:35:33');
INSERT INTO `water_tool_versions` VALUES (17, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user2\\\", {name:\\\"noear\\\"});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612258832000}', 'fde3ad45189600d8766d31ddf3c0a009', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:40:32');
INSERT INTO `water_tool_versions` VALUES (18, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user2\\\", {name:\\\"noear\\\", lavel: 12});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612259611000}', 'e3fe90e1fc60d2b1193391740cb5348c', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:53:31');
INSERT INTO `water_tool_versions` VALUES (19, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user\\\", {name:\\\"noear\\\", lavel: 12});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612259727000}', 'f740082b9157d95f457306af6ddf6596', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:55:27');
INSERT INTO `water_tool_versions` VALUES (20, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user\\\", {name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612259754000}', '4b3fea9dc5994e660b17aeea307c59aa', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:55:54');
INSERT INTO `water_tool_versions` VALUES (21, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.insertOne(\\\"user2\\\", {name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612259789000}', 'a832d2b237e190fe8dd931036c49b764', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 17:56:29');
INSERT INTO `water_tool_versions` VALUES (22, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.createIndex(\\\"user2\\\",{name:1}, {max:1})\\n\\ndb2.insertOne(\\\"user2\\\", {createdAt:new Date(), name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612261450000}', '7bc792407af3734e09a265e4280bd173', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 18:24:10');
INSERT INTO `water_tool_versions` VALUES (23, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.createIndex(\\\"user2\\\",{\\\"createdAt\\\":1}, {expireAfterSeconds:2});\\n\\ndb2.insertOne(\\\"user2\\\", {createdAt:new Date(), name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612261474000}', 'ab64a3bf9c02489077f6bf2646512a62', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 18:24:34');
INSERT INTO `water_tool_versions` VALUES (24, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.createIndex(\\\"user2\\\", {createdAt:1}, {expireAfterSeconds:2});\\n\\ndb2.insertOne(\\\"user2\\\", {createdAt:new Date(), name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612261480000}', 'fc3a98f4dafb563cb0d3117c5c32689b', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 18:24:40');
INSERT INTO `water_tool_versions` VALUES (25, 'paas_file', 'file_id', '289', 0, '{\"file_id\":289,\"file_type\":0,\"tag\":\"_test\",\"label\":\"\",\"path\":\"/_test/mongodb\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"\\nvar db2 = water.mg(\\\"water/water_mongo\\\", \\\"demo\\\");\\n\\ndb2.createIndex(\\\"user2\\\", {createdAt:1}, {expireAfterSeconds:2});\\n\\ndb2.insertOne(\\\"user2\\\", {createdAt:new Date().getTime(), name:\\\"noear\\\", lavel: 12, sex:111});\\n\\nreturn \'OK\';\\n\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1612258525000,\"update_fulltime\":1612261520000}', 'cf56ae9bafb2b61f5110014f7a03cea5', '谢月甲', '172.168.0.46', 20210202, '2021-02-02 18:25:20');
INSERT INTO `water_tool_versions` VALUES (26, 'paas_file', 'file_id', '290', 0, '{\"file_id\":290,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/log_del_15d_bef\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"let list = water.db(\\\"water/water\\\")\\n                .sql(\\\"SELECT logger_id,tag,logger,keep_days,DATE_FORMAT(DATE_ADD(NOW(),INTERVAL -keep_days DAY),\'%Y%m%d\') date FROM water_cfg_logger WHERE source=\'\'\\\")\\n                .getMapList();\\n\\nlet hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nfor(let i in list){\\n    let m = list[i];\\n    \\n    try{\\n        hub.logQuerier.clear(m.logger);\\n        //water.db(\\\"water/water_log\\\").exe(\\\"DELETE FROM \\\"+m.logger+\\\" WHERE log_date <= \\\"+m.date);\\n    }catch(err){\\n\\t    XUtil.log({\\\"content\\\":err});\\n    }\\n}\\n\\nreturn \\\"ok\\\";\",\"note\":\"日志-删除过期的数据\",\"plan_state\":9,\"plan_begin_time\":1579236868000,\"plan_last_time\":1612335268000,\"plan_last_timespan\":32,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":5,\"create_fulltime\":1579236881000,\"update_fulltime\":1612335461000}', 'b76e943ce358cb2fb4b499d986e20bba', '谢月甲', '172.168.0.46', 20210203, '2021-02-03 14:57:41');
INSERT INTO `water_tool_versions` VALUES (27, 'paas_file', 'file_id', '35', 0, '{\"file_id\":35,\"file_type\":0,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/paas/help/api/\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var System = Java.type(\'java.lang.System\');\\n\\n\\nvar sysinfo = (\'Running on Java version: \' + System.getProperty(\'java.version\')) + \'；\' + (\'Unix time from Java: \' + System.currentTimeMillis());\\n\\n\\nreturn modelAndView(\\\"/water/paas/help/api/view\\\",{sysinfo:sysinfo});\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1580873159000,\"update_fulltime\":1612335947000}', 'b6849a8ec7642dfad2a51c797600dab9', '谢月甲', '172.168.0.46', 20210203, '2021-02-03 15:05:47');
INSERT INTO `water_tool_versions` VALUES (28, 'paas_file', 'file_id', '35', 0, '{\"file_id\":35,\"file_type\":0,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/paas/help/api/\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var System = Java.type(\'java.lang.System\');\\n\\n\\nvar sysinfo = (\'Running on Java version: \' + System.getProperty(\'java.version\')) + \'；\' + (\'Unix time from Java: \' + System.currentTimeMillis());\\n\\n\\nreturn modelAndView(\\\"/water/paas/help/api/view\\\",{sysinfo:sysinfo});\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1580873159000,\"update_fulltime\":1612491658000}', '4d42bf3848ec4992947c974093f453ea', '谢月甲', '172.168.0.46', 20210205, '2021-02-05 10:20:58');
INSERT INTO `water_tool_versions` VALUES (29, 'paas_file', 'file_id', '34', 0, '{\"file_id\":34,\"file_type\":0,\"tag\":\"_demo\",\"label\":\"\",\"path\":\"/_demo/ali_oss_cfg_xutil\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"return XUtil.cfg(\'_demo/ali_oss_cfg\').getProp();\",\"note\":\"测试\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1579590725000,\"update_fulltime\":1612588561000}', '931ea67154924358aff3ac075f8cc8f4', '谢月甲', '172.168.0.46', 20210206, '2021-02-06 13:16:02');
INSERT INTO `water_tool_versions` VALUES (30, 'paas_file', 'file_id', '293', 0, '{\"file_id\":293,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_reset\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"// var currTime = new Datetime().getTicks();\\n// var timeOuts = 1000 * 30; //30s\\n// var refTime  = currTime - timeOuts;\\n\\n// var db2 = water.db(\\\"water/water_msg\\\");\\n// var rst = 0;\\n\\n// if(db2.table(\'water_msg_message\').whereEq(\\\"state\\\",1).andLt(\\\"dist_nexttime\\\",refTime).exists()){\\n//     rst = db2.table(\'water_msg_message\')\\n//        .set(\'state\',0)\\n//        .whereEq(\\\"state\\\",1).andLt(\\\"dist_nexttime\\\",refTime)\\n//        .update();\\n       \\n//     XUtil.log(\\\"reset-\\\"+rst);\\n// }\\n\\nlet hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nlet rst = hub.messageSource().reset(30);\\n\\nXUtil.log(\\\"reset-\\\"+rst);\\n\\n//db2.exe(`UPDATE water_msg_message SET state=0  WHERE state=1 AND (dist_nexttime -${currTime})<-${timeOuts}`);\\n\\nreturn \\\"ok\\\" + rst;\",\"note\":\"消息异常重置\",\"plan_state\":9,\"plan_begin_time\":1579239545000,\"plan_last_time\":1612589524000,\"plan_last_timespan\":272,\"plan_interval\":\"1m\",\"plan_max\":0,\"plan_count\":4359,\"create_fulltime\":1579238632000,\"update_fulltime\":1612589579000}', '094c7fa5f8dc39d193b9379629c6c275', '谢月甲', '172.168.0.46', 20210206, '2021-02-06 13:32:59');
INSERT INTO `water_tool_versions` VALUES (31, 'paas_file', 'file_id', '35', 0, '{\"file_id\":35,\"file_type\":0,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/paas/help/api/\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var System = Java.type(\'java.lang.System\');\\n\\n\\nvar sysinfo = (\'Running on Java version: \' + System.getProperty(\'java.version\')) + \'；\' + (\'Unix time from Java: \' + System.currentTimeMillis());\\n\\n\\nreturn modelAndView(\\\"/water/paas/help/api/view\\\",{sysinfo:sysinfo});\",\"note\":\"\",\"plan_state\":0,\"plan_begin_time\":null,\"plan_last_time\":null,\"plan_last_timespan\":0,\"plan_interval\":\"\",\"plan_max\":0,\"plan_count\":0,\"create_fulltime\":1580873159000,\"update_fulltime\":1614926814000}', 'c5a8a6af94bf45377c49256b14d2d5a5', '谢月甲', '172.168.0.46', 20210305, '2021-03-05 14:46:54');
INSERT INTO `water_tool_versions` VALUES (32, 'paas_file', 'file_id', '291', 0, '{\"file_id\":291,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_del_and_stat\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date3 = Datetime.Now().addDay(-3).getDate();\\nvar date1 = Datetime.Now().addDay(-1).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nvar topicList = db.exe(\\\"list::SELECT topic_id,topic_name FROM water_msg_topic\\\");\\n\\nvar num = hub.messageSource().stat(date1,0);  //db.exe(\\\"val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=?\\\", date1);\\n\\n\\n//统计\\n//\\ndb.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date=?\\\", date1);\\ndb.exe(\\\"INSERT INTO water_msg_message_ex_stat (log_date,total) VALUES (?,?)\\\", date1, num);\\n\\n\\nfor(var tp of topicList){\\n    var num1 = hub.messageSource().stat(date1, tp.topic_id);\\n\\n    db.exe(\\\"INSERT INTO water_msg_message_ex_stat (topic_id,log_date,total) VALUES (?,?)\\\", tp.topic_id, date1, num);\\t\\n} \\n\\n//更新主题数量\\ndb.exe(`UPDATE water_msg_topic t,water_msg_message_ex_stat s\\n        SET t.stat_msg_day_num = s.total \\n        WHERE t.topic_id = s.topic_id AND s.log_date = ?`,date1);\\n\\n\\n//删除\\n//\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=2\\\", date3);\\t\\t\\t\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=2\\\", date3);\\t\\t\\t\\t\\t\\t\\n\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=3\\\", date3);\\t\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=3\\\", date3);\\n\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state<0\\\", date3);\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state<0\\\", date3);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息清理和统计\",\"plan_state\":9,\"plan_begin_time\":1579237979000,\"plan_last_time\":1615443179000,\"plan_last_timespan\":17,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":880,\"create_fulltime\":1579237980000,\"update_fulltime\":1615446267000}', '054e84e07a974da434fc94d1ebf93e06', '谢月甲', '172.168.0.33', 20210311, '2021-03-11 15:04:27');
INSERT INTO `water_tool_versions` VALUES (33, 'paas_file', 'file_id', '291', 0, '{\"file_id\":291,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_del_and_stat\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date3 = Datetime.Now().addDay(-3).getDate();\\nvar date1 = Datetime.Now().addDay(-1).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nvar topicList = db.exe(\\\"list::SELECT topic_id,topic_name FROM water_msg_topic\\\");\\n\\nvar num = hub.messageSource().stat(date1,0);  //db.exe(\\\"val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=?\\\", date1);\\n\\n\\n//统计\\n//\\ndb.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date=?\\\", date1);\\ndb.exe(\\\"INSERT INTO water_msg_message_ex_stat (log_date,total) VALUES (?,?)\\\", date1, num);\\n\\n\\nfor(var tp of topicList){\\n    var num1 = hub.messageSource().stat(date1, tp.topic_id);\\n\\n    db.exe(\\\"INSERT INTO water_msg_message_ex_stat (topic_id,log_date,total) VALUES (?,?)\\\", tp.topic_id, date1, num);\\n    //db.exe(`INSERT INTO water_msg_message_ex_stat(topic_id,total,log_date) \\n    //            SELECT topic_id,COUNT(*) num,log_date \\n    //            FROM water_msg_message WHERE log_date=? GROUP BY topic_id`, date1);\\t\\n} \\n\\n//更新主题数量\\ndb.exe(`UPDATE water_msg_topic t,water_msg_message_ex_stat s\\n        SET t.stat_msg_day_num = s.total \\n        WHERE t.topic_id = s.topic_id AND s.log_date = ?`,date1);\\n\\n\\n//删除\\n//\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=2\\\", date3);\\t\\t\\t\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=2\\\", date3);\\t\\t\\t\\t\\t\\t\\n\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=3\\\", date3);\\t\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=3\\\", date3);\\n\\ndb.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state<0\\\", date3);\\ndb.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state<0\\\", date3);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息清理和统计\",\"plan_state\":9,\"plan_begin_time\":1579237979000,\"plan_last_time\":1615443179000,\"plan_last_timespan\":17,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":880,\"create_fulltime\":1579237980000,\"update_fulltime\":1615446283000}', '0a68c2f04272e721820cc5103c93e057', '谢月甲', '172.168.0.33', 20210311, '2021-03-11 15:04:43');
INSERT INTO `water_tool_versions` VALUES (34, 'paas_file', 'file_id', '291', 0, '{\"file_id\":291,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_del_and_stat\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date3 = Datetime.Now().addDay(-3).getDate();\\nvar date1 = Datetime.Now().addDay(-1).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nvar topicList = db.exe(\\\"list::SELECT topic_id,topic_name FROM water_msg_topic\\\");\\n\\nvar num = hub.messageSource().stat(date1,0);  //db.exe(\\\"val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=?\\\", date1);\\n\\n\\n//统计\\n//\\ndb.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date=?\\\", date1);\\ndb.exe(\\\"INSERT INTO water_msg_message_ex_stat (log_date,total) VALUES (?,?)\\\", date1, num);\\n\\n\\nfor(var tp of topicList){\\n    var num1 = hub.messageSource().stat(date1, tp.topic_id);\\n\\n    db.exe(\\\"INSERT INTO water_msg_message_ex_stat (topic_id,log_date,total) VALUES (?,?)\\\", tp.topic_id, date1, num);\\n    //db.exe(`INSERT INTO water_msg_message_ex_stat(topic_id,total,log_date) \\n    //            SELECT topic_id,COUNT(*) num,log_date \\n    //            FROM water_msg_message WHERE log_date=? GROUP BY topic_id`, date1);\\t\\n} \\n\\n//更新主题数量\\ndb.exe(`UPDATE water_msg_topic t,water_msg_message_ex_stat s\\n        SET t.stat_msg_day_num = s.total \\n        WHERE t.topic_id = s.topic_id AND s.log_date = ?`,date1);\\n\\n\\n//删除\\n//\\n\\nhub.messageSource().clear(date3);\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=2\\\", date3);\\t\\t\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=2\\\", date3);\\t\\t\\t\\t\\t\\t\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=3\\\", date3);\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=3\\\", date3);\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state<0\\\", date3);\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state<0\\\", date3);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息清理和统计\",\"plan_state\":9,\"plan_begin_time\":1579237979000,\"plan_last_time\":1615443179000,\"plan_last_timespan\":17,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":880,\"create_fulltime\":1579237980000,\"update_fulltime\":1615446333000}', '96f8df6ab7b831e270d5fa1c089fe8aa', '谢月甲', '172.168.0.33', 20210311, '2021-03-11 15:05:33');
INSERT INTO `water_tool_versions` VALUES (35, 'paas_file', 'file_id', '291', 0, '{\"file_id\":291,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_del_and_stat\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date3 = Datetime.Now().addDay(-3).getDate();\\nvar date1 = Datetime.Now().addDay(-1).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nvar topicList = db.exe(\\\"list::SELECT topic_id,topic_name FROM water_msg_topic\\\");\\n\\nvar num = hub.messageSource().stat(date1,0);  //db.exe(\\\"val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=?\\\", date1);\\n\\n\\n//统计\\n//\\ndb.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date=?\\\", date1);\\ndb.exe(\\\"INSERT INTO water_msg_message_ex_stat (log_date,total) VALUES (?,?)\\\", date1, num);\\n\\n\\nfor(var tp of topicList){\\n    var num1 = hub.messageSource().stat(date1, tp.topic_id);\\n\\n    db.exe(\\\"INSERT INTO water_msg_message_ex_stat (topic_id,log_date,total) VALUES (?,?)\\\", tp.topic_id, date1, num);\\n    //db.exe(`INSERT INTO water_msg_message_ex_stat(topic_id,total,log_date) \\n    //            SELECT topic_id,COUNT(*) num,log_date \\n    //            FROM water_msg_message WHERE log_date=? GROUP BY topic_id`, date1);\\t\\n} \\n\\n//更新主题数量\\ndb.exe(`UPDATE water_msg_topic t,water_msg_message_ex_stat s\\n        SET t.stat_msg_day_num = s.total \\n        WHERE t.topic_id = s.topic_id AND s.log_date = ?`,date1);\\n\\n\\n//删除\\n//\\n\\nhub.messageSource().clear(date3);\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=2\\\", date3);\\t\\t\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=2\\\", date3);\\t\\t\\t\\t\\t\\t\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=3\\\", date3);\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=3\\\", date3);\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state<0\\\", date3);\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state<0\\\", date3);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息清理和统计\",\"plan_state\":9,\"plan_begin_time\":1579237979000,\"plan_last_time\":1615443179000,\"plan_last_timespan\":17,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":880,\"create_fulltime\":1579237980000,\"update_fulltime\":1615446362000}', '6dfb4f8f85b7ed98965f1d94d3357d92', '谢月甲', '172.168.0.33', 20210311, '2021-03-11 15:06:02');
INSERT INTO `water_tool_versions` VALUES (36, 'paas_file', 'file_id', '308', 0, '{\"file_id\":308,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_persistence\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date90 = Datetime.Now().addDay(-90).getDate();\\nvar date2 = Datetime.Now().addDay(-2).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nhub.messageSource().persistence(date2,date90);\\n\\n// //转移数据（长久保存）\\n// //\\n// if(db.table(\'water_msg_message_all\').whereEq(\'log_date\',date2).exists() === false){\\n//     db.exe(`INSERT INTO water_msg_message_all\\n//             SELECT * FROM water_msg_message WHERE log_date = ?`, date2);\\n// }\\n\\n// //清理统计\\n// db.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date<=?\\\", date90);\\n\\n// //清理持久化\\n// db.exe(\\\"DELETE FROM water_msg_message_all WHERE log_date<=?\\\", date90);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息持久化处理\",\"plan_state\":9,\"plan_begin_time\":1593370800000,\"plan_last_time\":1615402804000,\"plan_last_timespan\":102729,\"plan_interval\":\"1d\",\"plan_max\":0,\"plan_count\":56,\"create_fulltime\":1593486768000,\"update_fulltime\":1615458377000}', 'c775632bef9e50b8a0f66a4113782dcd', NULL, '172.168.0.33', 20210311, '2021-03-11 18:26:17');
INSERT INTO `water_tool_versions` VALUES (37, 'paas_file', 'file_id', '291', 0, '{\"file_id\":291,\"file_type\":1,\"tag\":\"water\",\"label\":\"\",\"path\":\"/water/msg_del_and_stat\",\"rank\":0,\"is_staticize\":false,\"is_editable\":true,\"is_disabled\":false,\"is_exclude\":false,\"link_to\":null,\"edit_mode\":\"javascript\",\"content_type\":\"\",\"content\":\"var db = water.db(\\\"water/water_msg\\\");\\n\\nvar date3 = Datetime.Now().addDay(-3).getDate();\\nvar date1 = Datetime.Now().addDay(-1).getDate();\\n\\nvar hub = Java.type(\'org.noear.water.protocol.ProtocolHub\');\\n\\nvar topicList = db.exe(\\\"list::SELECT topic_id,topic_name FROM water_msg_topic\\\");\\n\\nvar num = hub.messageSource().stat(date1,0);  //db.exe(\\\"val::SELECT COUNT(*) num FROM water_msg_message WHERE log_date=?\\\", date1);\\n\\n\\n//统计\\n//\\ndb.exe(\\\"DELETE FROM water_msg_message_ex_stat WHERE log_date=?\\\", date1);\\ndb.exe(\\\"INSERT INTO water_msg_message_ex_stat (log_date,total) VALUES (?,?)\\\", date1, num);\\n\\n\\nfor(var tp of topicList){\\n    var num1 = hub.messageSource().stat(date1, tp.topic_id);\\n\\n    db.exe(\\\"INSERT INTO water_msg_message_ex_stat (topic_id,log_date,total) VALUES (?,?,?)\\\", tp.topic_id, date1, num);\\n    //db.exe(`INSERT INTO water_msg_message_ex_stat(topic_id,total,log_date) \\n    //            SELECT topic_id,COUNT(*) num,log_date \\n    //            FROM water_msg_message WHERE log_date=? GROUP BY topic_id`, date1);\\t\\n} \\n\\n//更新主题数量\\ndb.exe(`UPDATE water_msg_topic t,water_msg_message_ex_stat s\\n        SET t.stat_msg_day_num = s.total \\n        WHERE t.topic_id = s.topic_id AND s.log_date = ?`,date1);\\n\\n\\n//删除\\n//\\n\\nhub.messageSource().clear(date3);\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=2\\\", date3);\\t\\t\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=2\\\", date3);\\t\\t\\t\\t\\t\\t\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m  WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state=3\\\", date3);\\t\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state=3\\\", date3);\\n\\n// db.exe(\\\"DELETE d FROM water_msg_distribution d,water_msg_message m WHERE d.msg_id = m.msg_id AND m.log_date<=? and m.state<0\\\", date3);\\n// db.exe(\\\"DELETE FROM water_msg_message WHERE log_date<=? AND state<0\\\", date3);\\n\\nreturn \\\"ok\\\";\",\"note\":\"消息清理和统计\",\"plan_state\":8,\"plan_begin_time\":1579237979000,\"plan_last_time\":1615458620000,\"plan_last_timespan\":0,\"plan_interval\":\"1h\",\"plan_max\":0,\"plan_count\":880,\"create_fulltime\":1579237980000,\"update_fulltime\":1615458678000}', '50fe929de298439cc142e512c14ed356', '谢月甲', '172.168.0.33', 20210311, '2021-03-11 18:31:18');

-- ----------------------------
-- Table structure for x_water_log_tml
-- ----------------------------
DROP TABLE IF EXISTS `x_water_log_tml`;
CREATE TABLE `x_water_log_tml`  (
  `log_id` bigint NOT NULL COMMENT '日志ID',
  `level` int NOT NULL DEFAULT 0 COMMENT '等级',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签１',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签２',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签３',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期',
  `log_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-日志表模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_water_log_tml
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
