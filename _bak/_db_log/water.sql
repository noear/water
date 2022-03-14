
-- 2021-11-19 (for 2.4.0)
ALTER TABLE `water_cfg_broker`
    CHANGE COLUMN `update_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_alarm`;

ALTER TABLE `water_cfg_broker`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_alarm`;

ALTER TABLE `water_cfg_gateway`
    CHANGE COLUMN `update_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_enabled`;

ALTER TABLE `water_cfg_gateway`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`;


ALTER TABLE `water_cfg_logger`
    CHANGE COLUMN `update_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_alarm`;

ALTER TABLE `water_cfg_logger`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_alarm`;

ALTER TABLE `water_cfg_properties`
    CHANGE COLUMN `update_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_enabled`;

ALTER TABLE `water_cfg_properties`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`;

ALTER TABLE `water_cfg_whitelist`
    CHANGE COLUMN `update_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_enabled`;

ALTER TABLE `water_cfg_whitelist`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`;

ALTER TABLE `water_msg_subscriber`
    CHANGE COLUMN `log_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `check_error_num`;

ALTER TABLE `water_msg_subscriber`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `check_error_num`;

ALTER TABLE `water_msg_topic`
    CHANGE COLUMN `create_fulltime` `gmt_create` bigint NULL COMMENT '创建时间' AFTER `alarm_model`;

ALTER TABLE `water_msg_topic`
    ADD COLUMN `gmt_modified` bigint(20) NULL COMMENT '最后修改时间' AFTER `gmt_create`;

ALTER TABLE `water_ops_project`
    ADD COLUMN `gmt_create` bigint(20) NULL COMMENT '创建时间' AFTER `is_enabled`,
    ADD COLUMN `gmt_modified` bigint(20) NULL COMMENT '最后修改时间' AFTER `gmt_create`;

ALTER TABLE `water_ops_server`
    ADD COLUMN `gmt_create` bigint(20) NULL COMMENT '创建时间' AFTER `is_enabled`,
    ADD COLUMN `gmt_modified` bigint(20) NULL COMMENT '最后修改时间' AFTER `gmt_create`;

ALTER TABLE `water_ops_server_track_bls`
    CHANGE COLUMN `last_updatetime` `gmt_modified` bigint NULL COMMENT '最后修改时间 ' AFTER `traffic_tx`;

ALTER TABLE `water_ops_server_track_dbs`
    CHANGE COLUMN `last_updatetime` `gmt_modified` bigint NULL COMMENT '最后修改时间 ' AFTER `disk_usage`;

ALTER TABLE `water_ops_server_track_ecs`
    CHANGE COLUMN `last_updatetime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `tcp_num`;

ALTER TABLE `water_reg_consumer`
    MODIFY COLUMN `chk_fulltime` bigint NULL COMMENT '最后检查时间' AFTER `chk_last_state`,
    MODIFY COLUMN `log_fulltime` bigint NULL COMMENT '首次记录时间' AFTER `chk_fulltime`;

ALTER TABLE `water_reg_service`
    MODIFY COLUMN `check_last_time` bigint NOT NULL COMMENT '最后检查时间' AFTER `check_url`;

ALTER TABLE `water_reg_service`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`;

ALTER TABLE `water_reg_service`
    ADD COLUMN `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `gmt_create`;

ALTER TABLE `water_reg_service_runtime`
    MODIFY COLUMN `log_fulltime` bigint NULL AFTER `log_minute`;

ALTER TABLE `water_reg_service_speed`
    CHANGE COLUMN `last_updatetime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `total_num_slow5`;

ALTER TABLE `water_tool_monitor`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`,
    ADD COLUMN `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `gmt_create`;

ALTER TABLE `water_tool_report`
    CHANGE COLUMN `create_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `note`;

ALTER TABLE `water_tool_report`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `note`;

ALTER TABLE `water_tool_synchronous`
    CHANGE COLUMN `last_fulltime` `gmt_modified` bigint NULL COMMENT '最后修改时间' AFTER `is_enabled`;

ALTER TABLE `water_tool_synchronous`
    ADD COLUMN `gmt_create` bigint NULL COMMENT '创建时间' AFTER `is_enabled`;

ALTER TABLE `water_tool_versions`
    MODIFY COLUMN `log_fulltime` bigint NULL COMMENT '记录完整时间' AFTER `log_date`;

-- 2021-11-21 (for 2.4.1)
ALTER TABLE `water_msg_subscriber`
    ADD COLUMN `tag` varchar(40) NULL COMMENT '订阅者标签' AFTER `subscriber_note`,
    ADD COLUMN `name` varchar(255) NULL COMMENT '订阅者服务名' AFTER `tag`,
    ADD INDEX `IX_tag`(`tag`) USING BTREE;

ALTER TABLE `water_msg_topic`
    ADD COLUMN `tag` varchar(40) NULL COMMENT '标签' AFTER `topic_name`,
    ADD INDEX `IX_tag`(`tag`) USING BTREE;


-- 2021-11-30 (for 2.5)
-- 资源表，分三个领域概念：资源空间，资源组，资源(或叫：资源实体)
CREATE TABLE `grit_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源父ID',
  `resource_sid` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源空间ID',
  `resource_type` int(11) NOT NULL DEFAULT '0' COMMENT '资源类型(0:entity, 1:group, 2:space)',
  `resource_code` varchar(50) NOT NULL DEFAULT '' COMMENT '资源代码(例，user:del)',
  `display_name` varchar(200) DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序值',
  `link_uri` varchar(200) NOT NULL DEFAULT '' COMMENT '链接地址(例，/user/add)',
  `link_target` varchar(50) DEFAULT NULL COMMENT '链接目标',
  `link_tags` varchar(200) DEFAULT NULL COMMENT '链接标签(用,隔开)',
  `icon_uri` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '节点级别',
  `attrs` varchar(4000) DEFAULT NULL COMMENT '属性(kv)',
  `is_fullview` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全屏',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见（可见为页面，不可见为操作）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `IX_grit_resource__resource_code` (`resource_code`) USING BTREE,
  KEY `IX_grit_resource__resource_pid` (`resource_pid`) USING BTREE,
  KEY `IX_grit_resource__resource_sid` (`resource_sid`) USING BTREE,
  KEY `IX_grit_resource__link_uri` (`link_uri`(100)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源表';


CREATE TABLE `grit_resource_linked` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `subject_type` int(11) NOT NULL DEFAULT '0' COMMENT '主体类型',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE KEY `IX_grit_resource_linked__key` (`resource_id`,`subject_id`) USING BTREE,
  KEY `IX_grit_resource_linked__subject_id` (`subject_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源与主体连接表';

-- 主体表，分二个领域概念：主体组，主体（或叫：主体实体）
CREATE TABLE `grit_subject` (
  `subject_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主体ID',
  `subject_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '主体父ID',
  `subject_type` int(11) NOT NULL DEFAULT '0' COMMENT '主体类型(0:entity, 1:group)',
  `subject_code` varchar(50) NOT NULL DEFAULT '' COMMENT '主体代号',
  `login_name` varchar(50) NOT NULL COMMENT '主体登录名,默认用guid填充',
  `login_password` varchar(50) DEFAULT NULL COMMENT '主体登录密码',
  `display_name` varchar(200) DEFAULT NULL COMMENT '主体显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '节点级别',
  `attrs` varchar(4000) DEFAULT NULL COMMENT '属性(kv)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE KEY `IX_grit_subject__login_name` (`login_name`) USING BTREE,
  KEY `IX_grit_subject__subject_pid` (`subject_pid`) USING BTREE,
  KEY `IX_grit_subject__subject_code` (`subject_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-主体表';


CREATE TABLE `grit_subject_linked` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `group_subject_id` bigint(20) NOT NULL COMMENT '分组主体ID',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE KEY `IX_grit_subject_linked__key` (`subject_id`,`group_subject_id`) USING BTREE,
  KEY `IX_grit_subject_linked__group_subject_id` (`group_subject_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='grit-主体与分组连接表';

