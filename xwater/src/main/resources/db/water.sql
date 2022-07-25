
CREATE TABLE IF NOT EXISTS `water_cfg_broker` (
    `broker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '协调器ID',
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
    `broker` varchar(100) NOT NULL DEFAULT '' COMMENT '协调器',
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
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `is_alarm` int(11) NOT NULL DEFAULT '0' COMMENT '是否报警',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`broker_id`) USING BTREE,
    UNIQUE KEY `UX_broker` (`broker`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-消息协调器表';

CREATE TABLE IF NOT EXISTS `water_cfg_gateway` (
    `gateway_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '网关id',
    `tag` varchar(40) NOT NULL COMMENT '标签',
    `name` varchar(100) NOT NULL COMMENT '名称',
    `agent` varchar(255) DEFAULT NULL COMMENT '代理',
    `policy` varchar(100) DEFAULT NULL COMMENT '策略',
    `is_enabled` int(11) NOT NULL DEFAULT '1',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`gateway_id`) USING BTREE,
    UNIQUE KEY `UX_name` (`name`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-网关';


CREATE TABLE IF NOT EXISTS `water_cfg_i18n` (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL COMMENT '分组标签',
    `bundle` varchar(40) NOT NULL COMMENT '语言包名',
    `lang` varchar(40) NOT NULL COMMENT '语言',
    `name` varchar(100) NOT NULL COMMENT '名称',
    `value` varchar(5000) DEFAULT NULL COMMENT '值 ',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '禁用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `UX_key` (`tag`,`bundle`,`lang`,`name`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE,
    KEY `IX_tag_bundle_name` (`tag`,`bundle`,`name`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-多语言包';


CREATE TABLE IF NOT EXISTS  `water_cfg_key` (
    `key_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '密钥ID',
    `access_key` varchar(40)  NOT NULL COMMENT '访问键',
    `access_secret_key` varchar(255)  DEFAULT NULL COMMENT '访问密钥',
    `access_secret_salt` varchar(255)  DEFAULT NULL COMMENT '访问密钥盐',
    `tag` varchar(40)  DEFAULT NULL COMMENT '分组标签',
    `label` varchar(100)  DEFAULT NULL COMMENT '标记',
    `description` varchar(255)  DEFAULT NULL COMMENT '描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`key_id`) USING BTREE,
    UNIQUE KEY `UX_access_key` (`access_key`) USING BTREE,
    KEY `IX_tag` (`tag`),
    KEY `IX_label` (`label`(40))
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-访问密钥';


CREATE TABLE IF NOT EXISTS   `water_cfg_logger` (
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
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `is_alarm` int(11) NOT NULL DEFAULT '0' COMMENT '是否报警',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`logger_id`) USING BTREE,
    UNIQUE KEY `UX_logger` (`logger`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-日志记录器表';

CREATE TABLE IF NOT EXISTS `water_cfg_properties`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) DEFAULT NULL COMMENT '分组标签',
    `key` varchar(99) DEFAULT NULL COMMENT '属性key',
    `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型：0:未知，1:数据库；2:Redis；3:MangoDb；4:Memcached',
    `value` varchar(8000) DEFAULT NULL COMMENT '属性值',
    `edit_mode` varchar(40) DEFAULT NULL,
    `is_editable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否可编辑',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_tag_key` (`tag`,`key`) USING BTREE,
    KEY `IX_type` (`type`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-配置-属性表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_cfg_whitelist`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
    `type` varchar(40) NOT NULL DEFAULT 'ip' COMMENT '名单类型',
    `value` varchar(40) NOT NULL COMMENT '值',
    `note` varchar(99) DEFAULT NULL COMMENT '备注',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`tag`,`type`,`value`) USING BTREE,
    KEY `IX_val` (`value`) USING BTREE,
    KEY `IX_note` (`note`(40)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-配置-安全名单表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_msg_subscriber` (
    `subscriber_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订阅者ID',
    `subscriber_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订阅者KEY（由应用方传入）',
    `subscriber_note` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `tag` varchar(40) DEFAULT NULL COMMENT '订阅者标签',
    `name` varchar(255) DEFAULT NULL COMMENT '订阅者服务名',
    `alarm_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警手机号',
    `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警签名',
    `topic_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主题名字',
    `receive_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订阅者的接收地址',
    `receive_way` int(11) NOT NULL DEFAULT '0' COMMENT '接收方式（0,1异步等待；2异步不等待,状态设为已完成；3异步不等,状态设为处理中）',
    `receive_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '订阅者的接收地址的访问KEY',
    `is_sync` int(11) NOT NULL DEFAULT '0' COMMENT '是否同步接收（0异步；1同步）',
    `is_unstable` int(11) NOT NULL DEFAULT '0' COMMENT '是否为不稳定地址',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`subscriber_id`) USING BTREE,
    UNIQUE KEY `IX_subscribe` (`subscriber_key`,`topic_name`) USING BTREE,
    KEY `IX_topic_name` (`topic_name`) USING BTREE,
    KEY `IX_receive_url` (`receive_url`(40)) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-消息-订阅者表';


CREATE TABLE IF NOT EXISTS `water_msg_topic`  (
    `topic_id` int(11) NOT NULL AUTO_INCREMENT,
    `topic_name` varchar(40) DEFAULT NULL,
    `tag` varchar(40) DEFAULT NULL COMMENT '标签',
    `max_msg_num` int(11) NOT NULL DEFAULT '0',
    `max_distribution_num` int(11) NOT NULL DEFAULT '0' COMMENT '最大派发次数（0不限）',
    `max_concurrency_num` int(11) NOT NULL DEFAULT '0' COMMENT '最大同时派发数(0不限）',
    `stat_msg_day_num` int(11) NOT NULL DEFAULT '0' COMMENT '日生产量',
    `alarm_model` int(11) NOT NULL DEFAULT '0' COMMENT '报警模式：0=普通模式；1=不报警',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`topic_id`) USING BTREE,
    UNIQUE KEY `IX_topic` (`topic_name`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-消息-主题表' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `water_ops_project`  (
    `project_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
    `name` varchar(40) NOT NULL DEFAULT '' COMMENT '项目名称',
    `type` int(11) NOT NULL DEFAULT '0' COMMENT '0服务；1网站',
    `note` varchar(100) DEFAULT NULL COMMENT '项目描述',
    `developer` varchar(50) DEFAULT NULL COMMENT '开发人',
    `git_url` varchar(255) DEFAULT NULL,
    `is_enabled` int(11) DEFAULT '1' COMMENT '是否启用  0:禁用  1:启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`project_id`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE,
    KEY `IX_name` (`name`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_ops_server`  (
    `server_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分组标签',
    `name` varchar(40) NOT NULL DEFAULT '' COMMENT '服务器名称',
    `iaas_key` varchar(40) NOT NULL DEFAULT '' COMMENT '基础设施服务KEY',
    `iaas_type` int(11) NOT NULL DEFAULT '0' COMMENT '基础设施类型（0ECS, 1BLS, 2RDS, 3Redis, 4Memcached,5DRDS）',
    `iaas_attrs` varchar(40) NOT NULL DEFAULT '' COMMENT '特性',
    `iaas_account` varchar(100) NOT NULL DEFAULT '' COMMENT '基础设施账号',
    `address` varchar(100) NOT NULL DEFAULT '' COMMENT '外网地址（IP或域）',
    `address_local` varchar(100) NOT NULL DEFAULT '' COMMENT '内网地址（IP或域）',
    `hosts_local` varchar(200) DEFAULT NULL COMMENT '本地域名映射',
    `note` varchar(100) DEFAULT NULL COMMENT '备注',
    `env_type` int(11) NOT NULL DEFAULT '0' COMMENT '0测试环境；1预生产环境；2生产环境；',
    `sev_num` int(11) NOT NULL DEFAULT '0',
    `is_enabled` int(11) NOT NULL DEFAULT '0' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`server_id`) USING BTREE,
    KEY `IX_key` (`iaas_key`) USING BTREE,
    KEY `IX_iaas_account` (`iaas_account`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-运维-服务器表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_ops_server_track_bls`  (
    `iaas_key` varchar(40) NOT NULL,
    `co_conect_num` bigint(20) NOT NULL DEFAULT '0',
    `new_conect_num` bigint(20) NOT NULL DEFAULT '0',
    `qps` bigint(20) NOT NULL DEFAULT '0',
    `traffic_tx` bigint(20) NOT NULL DEFAULT '0',
    `gmt_modified` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-运维-服务器跟踪-BLS' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_ops_server_track_dbs`  (
    `iaas_key` varchar(40) NOT NULL,
    `connect_usage` double(18,2) DEFAULT NULL COMMENT '连接数使用率',
    `cpu_usage` double(18,2) DEFAULT NULL COMMENT 'cpu使用率',
    `memory_usage` double(18,2) DEFAULT NULL COMMENT '内存使用率',
    `disk_usage` double(18,2) DEFAULT NULL COMMENT '硬盘使用率',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间 ',
    PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-运维-服务器跟踪-DBS' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_ops_server_track_ecs`  (
    `iaas_key` varchar(40) NOT NULL,
    `cpu_usage` double(18,2) NOT NULL DEFAULT '0.00',
    `memory_usage` double(18,2) NOT NULL DEFAULT '0.00',
    `disk_usage` double(18,2) NOT NULL DEFAULT '0.00',
    `broadband_usage` double(18,2) NOT NULL DEFAULT '0.00',
    `tcp_num` bigint(20) NOT NULL,
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-运维-服务器跟踪-ECS' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_consumer`  (
    `row_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `service` varchar(100) NOT NULL DEFAULT '' COMMENT '服务',
    `consumer` varchar(100) NOT NULL DEFAULT '' COMMENT '消费者',
    `consumer_address` varchar(100) NOT NULL DEFAULT '' COMMENT '消费者地址',
    `consumer_ip` varchar(100) DEFAULT NULL COMMENT '消费者远程IP',
    `is_unstable` int(11) NOT NULL DEFAULT '0' COMMENT '是否为不稳定的',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `chk_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `chk_fulltime` bigint(20) DEFAULT NULL COMMENT '最后检查时间',
    `log_fulltime` bigint(20) DEFAULT NULL COMMENT '首次记录时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`service`,`consumer_address`) USING BTREE,
    KEY `IX_consumer_address` (`consumer_address`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务消费者表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_service`  (
    `service_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `key` varchar(40) NOT NULL COMMENT 'md5(name+‘#’+address)',
    `tag` varchar(40) NOT NULL DEFAULT '',
    `name` varchar(255) NOT NULL,
    `ver` varchar(40) DEFAULT NULL COMMENT '版本号',
    `address` varchar(255) NOT NULL,
    `meta` varchar(512) DEFAULT NULL COMMENT '源信息',
    `note` varchar(512) DEFAULT '',
    `alarm_mobile` varchar(20) DEFAULT NULL,
    `alarm_sign` varchar(50) DEFAULT NULL,
    `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
    `code_location` varchar(512) DEFAULT NULL,
    `check_type` int(11) NOT NULL DEFAULT '0' COMMENT '检查方式（0被检查；1自己签到）',
    `check_url` varchar(200) NOT NULL DEFAULT '' COMMENT '状态检查地址',
    `check_last_time` bigint(20) NOT NULL COMMENT '最后检查时间',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `is_unstable` int(11) NOT NULL DEFAULT '0' COMMENT '是否为不稳定的',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`service_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_address` (`address`(100)) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务者表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_service_runtime`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `key` varchar(40) NOT NULL,
    `name` varchar(255) NOT NULL,
    `address` varchar(255) NOT NULL,
    `memory_max` int(11) NOT NULL DEFAULT '0',
    `memory_total` int(11) NOT NULL DEFAULT '0',
    `memory_used` int(11) NOT NULL DEFAULT '0',
    `thread_peak_count` int(11) NOT NULL DEFAULT '0',
    `thread_count` int(11) NOT NULL DEFAULT '0',
    `thread_daemon_count` int(11) NOT NULL DEFAULT '0',
    `log_date` int(11) NOT NULL DEFAULT '0',
    `log_hour` int(11) NOT NULL DEFAULT '0',
    `log_minute` int(11) NOT NULL DEFAULT '0',
    `log_fulltime` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`row_id`) USING BTREE,
    KEY `IX_address` (`address`(40)) USING BTREE,
    KEY `IX_key_times` (`key`,`log_date`,`log_hour`,`log_minute`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_service_speed`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `service` varchar(40) NOT NULL,
    `tag` varchar(40) NOT NULL,
    `name_md5` varchar(40) NOT NULL DEFAULT '',
    `name` varchar(1000) NOT NULL,
    `average` bigint(20) NOT NULL DEFAULT '0',
    `average_ref` bigint(20) NOT NULL DEFAULT '0' COMMENT '参考响应时间值 ',
    `fastest` bigint(20) NOT NULL DEFAULT '0',
    `slowest` bigint(20) NOT NULL DEFAULT '0',
    `total_num` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`service`,`tag`,`name_md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_service_speed_date`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `service` varchar(40) NOT NULL,
    `tag` varchar(40) NOT NULL,
    `name_md5` varchar(40) NOT NULL DEFAULT '',
    `name` varchar(1000) NOT NULL,
    `average` bigint(20) NOT NULL DEFAULT '0',
    `fastest` bigint(20) NOT NULL DEFAULT '0',
    `slowest` bigint(20) NOT NULL DEFAULT '0',
    `total_num` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
    `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`service`,`tag`,`name_md5`,`log_date`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表-按日'ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_reg_service_speed_hour`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `service` varchar(40) DEFAULT NULL,
    `tag` varchar(40) DEFAULT NULL,
    `name_md5` varchar(40) NOT NULL DEFAULT '',
    `name` varchar(1000) NOT NULL,
    `average` bigint(20) NOT NULL DEFAULT '0',
    `fastest` bigint(20) NOT NULL DEFAULT '0',
    `slowest` bigint(20) NOT NULL DEFAULT '0',
    `total_num` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow1` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow2` bigint(20) NOT NULL DEFAULT '0',
    `total_num_slow5` bigint(20) NOT NULL DEFAULT '0',
    `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期',
    `log_hour` int(11) NOT NULL DEFAULT '0' COMMENT '记录小时',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`service`,`tag`,`name_md5`,`log_date`,`log_hour`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表-按时' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS  `water_tool_certification` (
    `certification_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
    `key` varchar(40) NOT NULL COMMENT '唯一key',
    `url` varchar(500) NOT NULL COMMENT '地址',
    `note` varchar(255) DEFAULT NULL COMMENT '备注',
    `time_of_start` timestamp NULL DEFAULT NULL COMMENT '生效时间',
    `time_of_end` timestamp NULL DEFAULT NULL COMMENT '失效时间',
    `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `check_interval` int(11) NOT NULL DEFAULT '0' COMMENT '检测间隔时间(s)',
    `check_last_time` bigint(20) NOT NULL COMMENT '最后检查时间',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`certification_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-工具-证书监视';



CREATE TABLE IF NOT EXISTS  `water_tool_detection` (
    `detection_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
    `key` varchar(40) NOT NULL COMMENT '唯一key',
    `name` varchar(255) NOT NULL COMMENT '名称',
    `protocol` varchar(100) DEFAULT NULL COMMENT '协议',
    `address` varchar(500) NOT NULL COMMENT '地址',
    `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `check_interval` int(11) NOT NULL DEFAULT '0' COMMENT '检测间隔时间(s)',
    `check_last_time` bigint(20) NOT NULL COMMENT '最后检查时间',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`detection_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE,
    KEY `IX_time_of_end` (`time_of_end`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='WATER-工具-应用监视' ROW_FORMAT = DYNAMIC;


CREATE TABLE IF NOT EXISTS `water_tool_monitor`  (
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
    `is_enabled` int(11) NOT NULL DEFAULT '0' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`monitor_id`) USING BTREE,
    KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-工具-数据监视' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_tool_report`  (
    `row_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '简报ID',
    `tag` varchar(20) NOT NULL COMMENT '分类标签（外部根据标签查询）',
    `name` varchar(40) NOT NULL COMMENT '查询名称',
    `args` varchar(100) DEFAULT NULL COMMENT '参数变量',
    `code` varchar(2000) DEFAULT NULL COMMENT '查询代码',
    `note` varchar(100) DEFAULT NULL,
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-工具-数据简报' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_tool_synchronous`  (
    `sync_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '',
    `key` varchar(40) NOT NULL DEFAULT '',
    `name` varchar(40) NOT NULL,
    `type` int(11) NOT NULL DEFAULT '0' COMMENT '0,增量同步；1,更新同步；',
    `interval` int(11) NOT NULL DEFAULT '0' COMMENT '间隔时间（秒）',
    `target` varchar(100) DEFAULT NULL,
    `target_pk` varchar(50) DEFAULT NULL,
    `source_model` varchar(1000) DEFAULT NULL COMMENT '数据源模型',
    `task_tag` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步标识（用于临时存数据）',
    `alarm_mobile` varchar(100) DEFAULT NULL,
    `alarm_sign` varchar(50) DEFAULT NULL,
    `is_enabled` int(11) NOT NULL DEFAULT '0',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`sync_id`) USING BTREE,
    KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-工具-同步配置表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `water_tool_versions`  (
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
    `log_fulltime` bigint(20) DEFAULT NULL COMMENT '记录完整时间',
    PRIMARY KEY (`commit_id`) USING BTREE,
    KEY `IX_table_key` (`table`,`key_name`,`key_value`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-工具-数据版本存储表' ROW_FORMAT = DYNAMIC;

