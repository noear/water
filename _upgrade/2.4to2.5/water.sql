
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

