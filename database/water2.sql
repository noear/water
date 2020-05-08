/*
Navicat MySQL Data Transfer

Source Server         : db.dev.zmapi.cn
Source Server Version : 50722
Source Host           : mysql.dev.zmapi.cn:3306
Source Database       : water2

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-05-08 14:19:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bcf_config
-- ----------------------------
DROP TABLE IF EXISTS `bcf_config`;
CREATE TABLE `bcf_config` (
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL DEFAULT '' COMMENT '值',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-配置表（仅看看用）';

-- ----------------------------
-- Table structure for bcf_group
-- ----------------------------
DROP TABLE IF EXISTS `bcf_group`;
CREATE TABLE `bcf_group` (
  `pgid` int(11) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `p_pgid` int(11) NOT NULL DEFAULT '0' COMMENT '组的父节点ID',
  `r_pgid` int(11) NOT NULL DEFAULT '0' COMMENT '组的根节点ID',
  `pg_code` varchar(50) DEFAULT NULL COMMENT '组的手工编码',
  `cn_name` varchar(50) DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) DEFAULT NULL COMMENT '英文名称',
  `uri_path` varchar(100) DEFAULT NULL COMMENT '连接地址',
  `in_level` int(11) DEFAULT '0' COMMENT '级别',
  `is_branch` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为支线',
  `tags` varchar(50) DEFAULT NULL COMMENT '标签',
  `order_index` int(11) DEFAULT '0' COMMENT '排序',
  `is_disabled` tinyint(1) DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) DEFAULT '1' COMMENT '是否显示',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`pgid`),
  KEY `IX_p_pgid` (`p_pgid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-分组表';

-- ----------------------------
-- Table structure for bcf_opsx
-- ----------------------------
DROP TABLE IF EXISTS `bcf_opsx`;
CREATE TABLE `bcf_opsx` (
  `lk_objt` int(11) NOT NULL DEFAULT '0' COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL DEFAULT '0' COMMENT '连接对象ID',
  `tags` varchar(40) NOT NULL DEFAULT '' COMMENT '标签',
  `opsx` varchar(2000) DEFAULT '0' COMMENT 'JSON值 ',
  PRIMARY KEY (`lk_objt`,`lk_objt_id`,`tags`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-对象扩展表';

-- ----------------------------
-- Table structure for bcf_resource
-- ----------------------------
DROP TABLE IF EXISTS `bcf_resource`;
CREATE TABLE `bcf_resource` (
  `rsid` int(11) NOT NULL AUTO_INCREMENT,
  `rs_code` varchar(50) NOT NULL DEFAULT '' COMMENT '资源手工代码',
  `cn_name` varchar(50) DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) DEFAULT NULL COMMENT '英文名称',
  `uri_path` varchar(200) NOT NULL DEFAULT '' COMMENT '连接地址',
  `uri_target` varchar(50) DEFAULT NULL COMMENT '连接目标',
  `ico_path` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `order_index` int(11) DEFAULT '0' COMMENT '排序值',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `tags` varchar(50) DEFAULT NULL COMMENT '标签',
  `is_disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用（默认否）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`rsid`),
  KEY `IX_code` (`rs_code`) USING BTREE,
  KEY `IX_path` (`uri_path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-资源表（一切皆为资源）';

-- ----------------------------
-- Table structure for bcf_resource_linked
-- ----------------------------
DROP TABLE IF EXISTS `bcf_resource_linked`;
CREATE TABLE `bcf_resource_linked` (
  `rsid` int(11) NOT NULL COMMENT '资源ID',
  `lk_objt` int(11) NOT NULL DEFAULT '0' COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL DEFAULT '0' COMMENT '连接对象ID',
  `lk_operate` char(1) DEFAULT NULL COMMENT '连操操作符(+,-)',
  `p_express` int(3) unsigned DEFAULT '0' COMMENT '操作表达式(预留)',
  PRIMARY KEY (`rsid`,`lk_objt`,`lk_objt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-资源连接表';

-- ----------------------------
-- Table structure for bcf_user
-- ----------------------------
DROP TABLE IF EXISTS `bcf_user`;
CREATE TABLE `bcf_user` (
  `puid` int(11) NOT NULL AUTO_INCREMENT COMMENT '内部用户ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户账号',
  `out_objt` int(11) DEFAULT '0' COMMENT '外部关系对象',
  `out_objt_id` bigint(20) DEFAULT '0' COMMENT '外部关系对象ID',
  `cn_name` varchar(50) DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) DEFAULT NULL COMMENT '英文名称',
  `pw_mail` varchar(50) DEFAULT NULL COMMENT '密码找回邮箱',
  `tags` varchar(50) DEFAULT NULL COMMENT '标签',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_disabled` tinyint(1) DEFAULT NULL COMMENT '是否禁用',
  `is_visibled` tinyint(1) DEFAULT NULL COMMENT '是否可见',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update` datetime DEFAULT NULL COMMENT '最后更新时间',
  `pass_wd` varchar(50) DEFAULT NULL COMMENT '账号密码',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态（预留）',
  PRIMARY KEY (`puid`),
  UNIQUE KEY `IX_User_Id` (`user_id`),
  KEY `IX_OUT_OBJT` (`out_objt`,`out_objt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='BCF-用户表';

-- ----------------------------
-- Table structure for bcf_user_linked
-- ----------------------------
DROP TABLE IF EXISTS `bcf_user_linked`;
CREATE TABLE `bcf_user_linked` (
  `puid` int(11) NOT NULL COMMENT '内部用户ID',
  `lk_objt` int(11) unsigned NOT NULL COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL COMMENT '连接对象ID',
  `lk_operate` char(1) DEFAULT NULL COMMENT '连接操作符（+,-）',
  PRIMARY KEY (`puid`,`lk_objt`,`lk_objt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BCF-用户连接表';

-- ----------------------------
-- Table structure for paas_etl
-- ----------------------------
DROP TABLE IF EXISTS `paas_etl`;
CREATE TABLE `paas_etl` (
  `etl_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL COMMENT '分类标签',
  `etl_name` varchar(50) NOT NULL COMMENT '任务名称',
  `code` varchar(4000) DEFAULT NULL COMMENT 'JSON配置代码',
  `is_enabled` int(2) NOT NULL DEFAULT '0' COMMENT '是否启动 ',
  `is_extract` int(2) NOT NULL DEFAULT '0' COMMENT '是否启用抽取器',
  `is_load` int(2) NOT NULL DEFAULT '0' COMMENT '是否启用加载器',
  `is_transform` int(2) NOT NULL DEFAULT '1' COMMENT '是否启用转换器',
  `cursor_type` int(2) NOT NULL DEFAULT '0' COMMENT '0时间；1数值',
  `cursor` bigint(20) NOT NULL DEFAULT '0' COMMENT '游标',
  `alarm_mobile` varchar(100) DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `e_enabled` int(1) NOT NULL DEFAULT '0',
  `e_max_instance` int(11) NOT NULL DEFAULT '1' COMMENT '抽取器集群数',
  `e_last_exectime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_enabled` int(1) NOT NULL DEFAULT '0',
  `t_max_instance` int(11) NOT NULL DEFAULT '1' COMMENT '转换器集群数',
  `t_last_exectime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `l_enabled` int(1) NOT NULL DEFAULT '0',
  `l_max_instance` int(11) NOT NULL DEFAULT '1' COMMENT '加载器集群数',
  `l_last_exectime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_extract_time` datetime DEFAULT NULL COMMENT '最后抽取时间',
  `last_load_time` datetime DEFAULT NULL COMMENT '最后加载时间',
  `last_transform_time` datetime DEFAULT NULL COMMENT '最后转换时间',
  PRIMARY KEY (`etl_id`),
  UNIQUE KEY `IX_key` (`tag`,`etl_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PAAS-ETL配置表';

-- ----------------------------
-- Table structure for paas_file
-- ----------------------------
DROP TABLE IF EXISTS `paas_file`;
CREATE TABLE `paas_file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_type` int(11) NOT NULL DEFAULT '0' COMMENT '文件类型(0:api, 1:pln, 2:tml)',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组村签',
  `label` varchar(100) NOT NULL DEFAULT '' COMMENT '标记',
  `path` varchar(100) NOT NULL COMMENT '文件路径',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排列（小的排前）',
  `is_staticize` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否静态',
  `is_editable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可编辑',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_exclude` tinyint(1) NOT NULL DEFAULT '0' COMMENT '排除导入',
  `link_to` varchar(100) DEFAULT NULL COMMENT '连接到',
  `edit_mode` varchar(40) NOT NULL DEFAULT '' COMMENT '编辑模式',
  `content_type` varchar(60) NOT NULL DEFAULT '' COMMENT '内容类型',
  `content` longtext COMMENT '内容',
  `note` varchar(99) DEFAULT '' COMMENT '备注',
  `plan_state` int(11) NOT NULL DEFAULT '0' COMMENT '计划状态',
  `plan_begin_time` datetime DEFAULT NULL COMMENT '计划开始执行时间',
  `plan_last_time` datetime DEFAULT NULL COMMENT '计划最后执行时间',
  `plan_last_timespan` bigint(20) NOT NULL DEFAULT '0' COMMENT '计划最后执行时间长度',
  `plan_interval` varchar(10) NOT NULL DEFAULT '' COMMENT '计划执行间隔',
  `plan_max` int(11) NOT NULL DEFAULT '0' COMMENT '计划执行最多次数',
  `plan_count` int(11) NOT NULL DEFAULT '0' COMMENT '计划执行累计次数',
  `create_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `IX_key` (`path`) USING HASH,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_label` (`label`) USING BTREE,
  KEY `IX_file_type` (`file_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PAAS-文件表';

-- ----------------------------
-- Table structure for rubber_actor
-- ----------------------------
DROP TABLE IF EXISTS `rubber_actor`;
CREATE TABLE `rubber_actor` (
  `actor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参与ID',
  `tag` varchar(40) DEFAULT NULL COMMENT '分类标签',
  `name` varchar(40) NOT NULL COMMENT '参与者代号',
  `name_display` varchar(40) NOT NULL COMMENT '参与者显示名',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新',
  PRIMARY KEY (`actor_id`),
  UNIQUE KEY `IX_key` (`tag`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-角色表';

-- ----------------------------
-- Table structure for rubber_block
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block`;
CREATE TABLE `rubber_block` (
  `block_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) NOT NULL DEFAULT '' COMMENT '显示名',
  `note` varchar(100) DEFAULT NULL,
  `related_db` varchar(100) DEFAULT NULL COMMENT '相关数据(sponge/angel)',
  `related_tb` varchar(100) DEFAULT NULL COMMENT '相关数据表',
  `is_editable` int(4) NOT NULL DEFAULT '0',
  `is_enabled` int(2) NOT NULL DEFAULT '1',
  `struct` varchar(255) NOT NULL DEFAULT '' COMMENT '数据结构({f1:''xx''})',
  `app_expr` varchar(999) NOT NULL DEFAULT '' COMMENT '应用表达式',
  `last_updatetime` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`block_id`),
  UNIQUE KEY `IX_key` (`tag`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-数据块';

-- ----------------------------
-- Table structure for rubber_block_item
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block_item`;
CREATE TABLE `rubber_block_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `block_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL DEFAULT '',
  `f1` varchar(100) NOT NULL DEFAULT '',
  `f2` varchar(100) NOT NULL DEFAULT '',
  `f3` varchar(100) NOT NULL DEFAULT '',
  `f4` varchar(100) NOT NULL DEFAULT '',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `IX_key` (`block_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-数据块项';

-- ----------------------------
-- Table structure for rubber_model
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model`;
CREATE TABLE `rubber_model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) NOT NULL DEFAULT '' COMMENT '显示名',
  `related_db` varchar(100) NOT NULL DEFAULT '' COMMENT '相关数据库',
  `field_count` int(10) DEFAULT '0',
  `init_expr` varchar(999) NOT NULL DEFAULT '' COMMENT '构造表达式',
  `debug_args` varchar(255) DEFAULT NULL COMMENT '调试参数',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `IX_key` (`tag`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-数据模型表';

-- ----------------------------
-- Table structure for rubber_model_field
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model_field`;
CREATE TABLE `rubber_model_field` (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段ID',
  `model_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属的模型ID',
  `name` varchar(40) NOT NULL COMMENT '字段名称',
  `name_display` varchar(100) NOT NULL COMMENT '显示名',
  `expr` varchar(2000) NOT NULL DEFAULT '' COMMENT '字段动态生成代码',
  `note` varchar(100) DEFAULT NULL COMMENT '字段',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_pk` int(1) NOT NULL DEFAULT '0' COMMENT '是否是主键',
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `IX_key` (`model_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-数据模型-字段表';

-- ----------------------------
-- Table structure for rubber_scheme
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme`;
CREATE TABLE `rubber_scheme` (
  `scheme_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案ID',
  `tag` varchar(40) NOT NULL COMMENT '分类标签',
  `name` varchar(40) NOT NULL COMMENT '代号',
  `name_display` varchar(100) NOT NULL DEFAULT '' COMMENT '显示名',
  `related_model` varchar(100) NOT NULL COMMENT '相关模型(tag/name)',
  `related_model_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联模型ID',
  `related_model_display` varchar(100) DEFAULT NULL COMMENT '关联模型显示名',
  `related_block` varchar(200) DEFAULT NULL COMMENT '引用函数',
  `debug_args` varchar(255) DEFAULT NULL COMMENT '调试参数',
  `event` varchar(999) NOT NULL DEFAULT '' COMMENT '事件',
  `node_count` int(4) NOT NULL DEFAULT '0' COMMENT '下属工作流节点数据',
  `rule_count` int(4) NOT NULL DEFAULT '0' COMMENT '下属规则数量',
  `rule_relation` int(2) NOT NULL DEFAULT '0' COMMENT '规则关系（0并且关系，1或者关系）',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`scheme_id`),
  UNIQUE KEY `IX_key` (`tag`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-计算方案表';

-- ----------------------------
-- Table structure for rubber_scheme_node
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node`;
CREATE TABLE `rubber_scheme_node` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `scheme_id` int(11) NOT NULL,
  `node_key` varchar(80) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束',
  `name` varchar(40) DEFAULT NULL COMMENT '代号',
  `prve_key` varchar(80) NOT NULL DEFAULT '' COMMENT '上个节点ID（type=line时，才有值 ）',
  `next_key` varchar(80) NOT NULL DEFAULT '' COMMENT '下个节点ID（type=line时，才有值 ）',
  `condition` varchar(400) DEFAULT NULL COMMENT '分支条件（type=line时，才有值 ：left,op,right,ct;left,op,right,ct）',
  `tasks` varchar(400) DEFAULT NULL COMMENT '执行任务（type=exec时，才有值：F,tag/fun1;R,tag/rule1 ）',
  `actor` varchar(100) DEFAULT NULL COMMENT '参与者（type=exec时，才有值 ：tag/name）',
  `actor_display` varchar(100) DEFAULT NULL,
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_enabled` int(1) DEFAULT '0' COMMENT '是否启用  0：未启用  1：启用 ',
  PRIMARY KEY (`node_id`),
  UNIQUE KEY `IX_key` (`scheme_id`,`node_key`),
  KEY `IX_scheme` (`scheme_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-计算方案-节点表';

-- ----------------------------
-- Table structure for rubber_scheme_node_design
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node_design`;
CREATE TABLE `rubber_scheme_node_design` (
  `scheme_id` int(11) NOT NULL,
  `details` text DEFAULT NULL,
  PRIMARY KEY (`scheme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-计算方案-节点设计表';

-- ----------------------------
-- Table structure for rubber_scheme_rule
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_rule`;
CREATE TABLE `rubber_scheme_rule` (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `scheme_id` int(11) NOT NULL DEFAULT '0' COMMENT '方案ID',
  `name_display` varchar(100) NOT NULL COMMENT '显示名',
  `advice` int(4) NOT NULL DEFAULT '0' COMMENT '评估建议(0无,1交易放行,2审慎审核,3阻断交易)',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '评估分值',
  `sort` int(3) NOT NULL DEFAULT '0' COMMENT '排序',
  `expr` varchar(400) NOT NULL COMMENT '条件表达式（m.user_day(30),>,15,&&;left,op,right,ct）',
  `expr_display` varchar(400) DEFAULT NULL,
  `event` varchar(400) NOT NULL DEFAULT '',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '状态，(0：禁用、1：启用)',
  `last_updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`rule_id`),
  KEY `IX_scheme` (`scheme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RAAS-计算方案-规则表';

-- ----------------------------
-- Table structure for water_cfg_logger
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_logger`;
CREATE TABLE `water_cfg_logger` (
  `logger_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志器ID',
  `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
  `logger` varchar(100) NOT NULL DEFAULT '' COMMENT '日志器',
  `row_num` bigint(20) DEFAULT '0' COMMENT '累积行数',
  `row_num_today` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日行数',
  `row_num_today_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日错误行数',
  `row_num_yesterday` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天行数',
  `row_num_yesterday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天错误行数',
  `row_num_beforeday` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天行数',
  `row_num_beforeday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天错误行数',
  `keep_days` int(11) NOT NULL DEFAULT '15' COMMENT '保留天数',
  `source` varchar(100) NOT NULL DEFAULT '' COMMENT '数据源',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`logger_id`),
  KEY `IX_tag` (`tag`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-配置-日志表';

-- ----------------------------
-- Table structure for water_cfg_properties
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_properties`;
CREATE TABLE `water_cfg_properties` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) DEFAULT NULL COMMENT '分组标签',
  `key` varchar(99) DEFAULT NULL COMMENT '属性key',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '类型：0:未知，1:数据库；2:Redis；3:MangoDb; 4:Memcached',
  `value` varchar(4000) DEFAULT NULL COMMENT '属性值',
  `is_editable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否可编辑',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `update_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_tag_key` (`tag`,`key`) USING BTREE,
  KEY `IX_tag` (`tag`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-配置-属性表';

-- ----------------------------
-- Table structure for water_cfg_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_whitelist`;
CREATE TABLE `water_cfg_whitelist` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
  `type` varchar(40) NOT NULL DEFAULT 'ip' COMMENT '名单类型',
  `value` varchar(40) NOT NULL COMMENT '值',
  `note` varchar(99) DEFAULT NULL COMMENT '备注',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `update_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_key` (`tag`,`type`,`value`) USING BTREE,
  KEY `IX_val` (`value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-配置-安全名单表';

-- ----------------------------
-- Table structure for water_ops_server
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server`;
CREATE TABLE `water_ops_server` (
  `server_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
  `name` varchar(40) NOT NULL DEFAULT '' COMMENT '服务器名称',
  `iaas_key` varchar(40) NOT NULL DEFAULT '' COMMENT '基础设施服务KEY',
  `iaas_type` int(11) NOT NULL DEFAULT '0' COMMENT '基础设施类型（0ECS, 1BLS, 2RDS, 3Redis, 4Memcached,5DRDS）',
  `iaas_attrs` varchar(40) NOT NULL DEFAULT '' COMMENT '特性',
  `iaas_account` varchar(100) NOT NULL DEFAULT '' COMMENT '基础设施账号',
  `address` varchar(100) NOT NULL COMMENT '外网地址（IP或域）',
  `address_local` varchar(100) NOT NULL DEFAULT '' COMMENT '内网地址（IP或域）',
  `hosts_local` varchar(200) DEFAULT NULL COMMENT '本地域名映射',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  `env_type` int(2) NOT NULL DEFAULT '0' COMMENT '0测试环境；1预生产环境；2生产环境；',
  `sev_num` int(11) NOT NULL DEFAULT '0',
  `is_enabled` int(1) NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`server_id`),
  KEY `IX_key` (`iaas_key`),
  KEY `IX_iaas_account` (`iaas_account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-运维-服务器表';

-- ----------------------------
-- Table structure for water_ops_server_track_bls
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_bls`;
CREATE TABLE `water_ops_server_track_bls` (
  `iaas_key` varchar(40) NOT NULL,
  `co_conect_num` bigint(20) NOT NULL DEFAULT '0',
  `new_conect_num` bigint(20) NOT NULL DEFAULT '0',
  `qps` bigint(20) NOT NULL DEFAULT '0',
  `traffic_tx` bigint(20) NOT NULL DEFAULT '0',
  `last_updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`iaas_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-运维-服务器跟踪-BLS';

-- ----------------------------
-- Table structure for water_ops_server_track_dbs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_dbs`;
CREATE TABLE `water_ops_server_track_dbs` (
  `iaas_key` varchar(40) NOT NULL,
  `connect_usage` double(18,2) DEFAULT NULL COMMENT '连接数使用率',
  `cpu_usage` double(18,2) DEFAULT NULL COMMENT 'cpu使用率',
  `memory_usage` double(18,2) DEFAULT NULL COMMENT '内存使用率',
  `disk_usage` double(18,2) DEFAULT NULL COMMENT '硬盘使用率',
  `last_updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`iaas_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-运维-服务器跟踪-DBS';

-- ----------------------------
-- Table structure for water_ops_server_track_ecs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_ecs`;
CREATE TABLE `water_ops_server_track_ecs` (
  `iaas_key` varchar(40) NOT NULL,
  `cpu_usage` double(18,2) NOT NULL DEFAULT '0.00',
  `memory_usage` double(18,2) NOT NULL DEFAULT '0.00',
  `disk_usage` double(18,2) NOT NULL DEFAULT '0.00',
  `broadband_usage` double(18,2) NOT NULL DEFAULT '0.00',
  `tcp_num` bigint(20) NOT NULL,
  `last_updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`iaas_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-运维-服务器跟踪-ECS';

-- ----------------------------
-- Table structure for water_reg_consumer
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_consumer`;
CREATE TABLE `water_reg_consumer` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `service` varchar(100) NOT NULL DEFAULT '' COMMENT '服务',
  `consumer` varchar(100) NOT NULL DEFAULT '' COMMENT '消费者',
  `consumer_address` varchar(100) NOT NULL DEFAULT '' COMMENT '消费者地址',
  `consumer_ip` varchar(100) DEFAULT NULL COMMENT '消费者远程IP',
  `is_unstable` int(1) NOT NULL DEFAULT '0' COMMENT '是否为不稳定的',
  `chk_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后检查时间',
  `log_fulltime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-注册-服务消费者表';

-- ----------------------------
-- Table structure for water_reg_service
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service`;
CREATE TABLE `water_reg_service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) NOT NULL COMMENT 'md5(name+‘#’+address)',
  `name` varchar(40) NOT NULL,
  `ver` varchar(40) DEFAULT NULL COMMENT '版本号',
  `address` varchar(100) NOT NULL,
  `note` varchar(255) NOT NULL DEFAULT '',
  `alarm_mobile` varchar(20) DEFAULT NULL,
  `alarm_sign` varchar(50) DEFAULT NULL,
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
  `check_type` int(11) NOT NULL DEFAULT '0' COMMENT '检查方式（0被检查；1自己签到）',
  `check_url` varchar(200) NOT NULL DEFAULT '' COMMENT '状态检查地址',
  `check_last_time` datetime NOT NULL COMMENT '最后检查时间',
  `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
  `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
  `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
  `is_unstable` int(1) NOT NULL DEFAULT '0' COMMENT '是否为不稳定的',
  `is_enabled` int(1) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `IX_key` (`key`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-注册-服务者表';

-- ----------------------------
-- Table structure for water_reg_service_speed
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed`;
CREATE TABLE `water_reg_service_speed` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) NOT NULL,
  `tag` varchar(40) NOT NULL,
  `name` varchar(100) NOT NULL,
  `average` bigint(20) NOT NULL DEFAULT '0',
  `average_ref` bigint(20) NOT NULL DEFAULT '0' COMMENT '参考响应时间值 ',
  `fastest` bigint(20) NOT NULL DEFAULT '0',
  `slowest` bigint(20) NOT NULL DEFAULT '0',
  `total_num` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
  `last_updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_key` (`service`,`tag`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-注册-服务性能记录表';

-- ----------------------------
-- Table structure for water_reg_service_speed_date
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_date`;
CREATE TABLE `water_reg_service_speed_date` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) NOT NULL,
  `tag` varchar(40) NOT NULL,
  `name` varchar(100) NOT NULL,
  `average` bigint(20) NOT NULL DEFAULT '0',
  `fastest` bigint(20) NOT NULL DEFAULT '0',
  `slowest` bigint(20) NOT NULL DEFAULT '0',
  `total_num` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录时间',
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_key` (`service`,`tag`,`name`,`log_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-注册-服务性能记录表-按日';

-- ----------------------------
-- Table structure for water_reg_service_speed_hour
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_hour`;
CREATE TABLE `water_reg_service_speed_hour` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) DEFAULT NULL,
  `tag` varchar(40) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `average` bigint(20) NOT NULL DEFAULT '0',
  `fastest` bigint(20) NOT NULL DEFAULT '0',
  `slowest` bigint(20) NOT NULL DEFAULT '0',
  `total_num` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
  `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期',
  `log_hour` int(11) NOT NULL DEFAULT '0' COMMENT '记录小时',
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `IX_key` (`service`,`tag`,`name`,`log_date`,`log_hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-注册-服务性能记录表-按时';

-- ----------------------------
-- Table structure for water_tool_monitor
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_monitor`;
CREATE TABLE `water_tool_monitor` (
  `monitor_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '',
  `key` varchar(40) NOT NULL DEFAULT '' COMMENT '监视项目key',
  `name` varchar(50) NOT NULL COMMENT '监视项目名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '监视类型（0:数据预警；1:数据简报）',
  `source_query` varchar(500) DEFAULT NULL COMMENT '数据源模型脚本',
  `rule` varchar(500) NOT NULL COMMENT '规则（输入m:{d:{},tag:''''}）',
  `task_tag` varchar(50) NOT NULL DEFAULT '0' COMMENT '监视标签',
  `task_tag_exp` varchar(100) DEFAULT NULL COMMENT '监视标签产生的表达式',
  `alarm_mobile` varchar(100) DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `alarm_exp` varchar(500) DEFAULT NULL COMMENT '报警信息产生的表达式',
  `alarm_count` int(11) NOT NULL DEFAULT '0' COMMENT '报警次数',
  `alarm_sign` varchar(50) DEFAULT NULL COMMENT '报警签名',
  `is_enabled` int(1) NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`monitor_id`),
  KEY `IX_key` (`key`) USING HASH,
  KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-工具-监视登记表（监视数据）';

-- ----------------------------
-- Table structure for water_tool_report
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_report`;
CREATE TABLE `water_tool_report` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '简报ID',
  `tag` varchar(20) NOT NULL COMMENT '分类标签（外部根据标签查询）',
  `name` varchar(40) NOT NULL COMMENT '查询名称',
  `args` varchar(100) DEFAULT NULL COMMENT '参数变量',
  `code` varchar(2000) DEFAULT NULL COMMENT '查询代码',
  `note` varchar(100) DEFAULT NULL,
  `create_fulltime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`row_id`),
  KEY `IX_tag` (`tag`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-工具-数据简报';

-- ----------------------------
-- Table structure for water_tool_synchronous
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_synchronous`;
CREATE TABLE `water_tool_synchronous` (
  `sync_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) NOT NULL DEFAULT '',
  `key` varchar(40) NOT NULL DEFAULT '',
  `name` varchar(40) NOT NULL,
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '0,增量同步；1,更新同步；',
  `interval` int(11) NOT NULL DEFAULT '0' COMMENT '间隔时间（秒）',
  `target` varchar(100) DEFAULT NULL,
  `target_pk` varchar(50) DEFAULT NULL,
  `source_model` varchar(1000) DEFAULT NULL COMMENT '数据源模型',
  `task_tag` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步标识（用于临时存数据）',
  `alarm_mobile` varchar(100) DEFAULT NULL,
  `alarm_sign` varchar(50) DEFAULT NULL,
  `is_enabled` int(1) NOT NULL DEFAULT '0',
  `last_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`sync_id`),
  KEY `IX_key` (`key`) USING HASH,
  KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-工具-同步配置表';

-- ----------------------------
-- Table structure for water_tool_versions
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_versions`;
CREATE TABLE `water_tool_versions` (
  `commit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `table` varchar(40) NOT NULL COMMENT '表名',
  `key_name` varchar(40) NOT NULL COMMENT '字段名',
  `key_value` varchar(40) NOT NULL COMMENT '字段值',
  `is_hand` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为手工备份',
  `data` text COMMENT '备份数据',
  `data_md5` varchar(40) NOT NULL DEFAULT '' COMMENT '备份数据md5',
  `log_user` varchar(40) DEFAULT NULL COMMENT '记录人',
  `log_ip` varchar(40) DEFAULT NULL COMMENT '记录IP',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期：yyyyMMdd',
  `log_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录完整时间',
  PRIMARY KEY (`commit_id`),
  KEY `IX_table_key` (`table`,`key_name`,`key_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-工具-数据版本存储表';

-- ----------------------------
-- Table structure for x_water_log_tml
-- ----------------------------
DROP TABLE IF EXISTS `x_water_log_tml`;
CREATE TABLE `x_water_log_tml` (
  `log_id` bigint(20) NOT NULL COMMENT '日志ID',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '等级',
  `tag` varchar(100) NOT NULL DEFAULT '' COMMENT '标签',
  `tag1` varchar(100) NOT NULL DEFAULT '' COMMENT '标签１',
  `tag2` varchar(100) NOT NULL DEFAULT '' COMMENT '标签２',
  `tag3` varchar(100) NOT NULL DEFAULT '' COMMENT '标签３',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `content` longtext COMMENT '内容',
  `from` varchar(200) DEFAULT NULL COMMENT '来源',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期',
  `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`log_id`),
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WATER-日志表模板';
