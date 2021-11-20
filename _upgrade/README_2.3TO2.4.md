

## 一、升级数据库脚本

数据库几年前设计，有些细节调整得舒服些。

#### 1) [water] 库升级脚本：

```sql
CREATE TABLE IF NOT EXISTS `water_cfg_gateway` (
    `gateway_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '网关id',
    `tag` varchar(40) NOT NULL COMMENT '标签',
    `name` varchar(100) NOT NULL COMMENT '名称',
    `agent` varchar(255) DEFAULT NULL COMMENT '代理',
    `policy` varchar(100) DEFAULT NULL COMMENT '策略',
    `is_enabled` int(11) NOT NULL DEFAULT '1',
    `update_fulltime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`gateway_id`) USING BTREE,
    UNIQUE KEY `UX_name` (`name`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-网关';

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
```

#### 2) [water_paas] 库升级脚本：

```sql

ALTER TABLE `paas_etl`
    MODIFY COLUMN `e_last_exectime` bigint NULL AFTER `e_max_instance`,
    MODIFY COLUMN `t_last_exectime` bigint NULL AFTER `t_max_instance`,
    MODIFY COLUMN `l_last_exectime` bigint NULL AFTER `l_max_instance`,
    MODIFY COLUMN `last_extract_time` bigint NULL COMMENT '最后抽取时间' AFTER `l_last_exectime`;


ALTER TABLE `paas_file`
    MODIFY COLUMN `create_fulltime` bigint NULL COMMENT '创建时间' AFTER `plan_count`,
    MODIFY COLUMN `update_fulltime` bigint NULL COMMENT '更新时间' AFTER `create_fulltime`;


ALTER TABLE `paas_etl`
    MODIFY COLUMN `last_load_time` bigint NULL COMMENT '最后加载时间' AFTER `last_extract_time`,
    MODIFY COLUMN `last_transform_time` bigint NULL COMMENT '最后转换时间' AFTER `last_load_time`;


ALTER  TABLE `paas_file` RENAME TO `luffy_file`;

ALTER  TABLE `paas_etl` RENAME TO `luffy_etl`;


ALTER TABLE `rubber_actor`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `note`;

ALTER TABLE `rubber_block`
    MODIFY COLUMN `last_updatetime` bigint NULL COMMENT '最后更新时间' AFTER `app_expr`;

ALTER TABLE `rubber_block_item`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `f4`;

ALTER TABLE `rubber_log_request`
    MODIFY COLUMN `start_fulltime` bigint NULL DEFAULT NULL COMMENT '开始时间' AFTER `note_json`,
    MODIFY COLUMN `end_fulltime` bigint NULL DEFAULT NULL COMMENT '完成时间' AFTER `start_date`;

ALTER TABLE `rubber_log_request_all`
    MODIFY COLUMN `start_fulltime` bigint NULL DEFAULT NULL COMMENT '开始时间' AFTER `note_json`,
    MODIFY COLUMN `end_fulltime` bigint NULL DEFAULT NULL COMMENT '完成时间' AFTER `start_date`;

ALTER TABLE `rubber_model`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `debug_args`;

ALTER TABLE `rubber_model_field`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `note`;

ALTER TABLE `rubber_scheme`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;

ALTER TABLE `rubber_scheme_node`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `actor_display`;

ALTER TABLE `rubber_scheme_rule`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;

ALTER TABLE `rubber_scheme_node_design`
    ADD COLUMN `last_updatetime` bigint NULL COMMENT '最后更新时间' AFTER `details`;


```

## 二、修改配置

**Water 配置修改:**

| 原配置名 | 调整为新配置名 | 
| -------- | -------- | 
| `water/paas_url`     | `water/faas_url`     |

```sql
UPDATE water_cfg_properties SET `name`='faas_url' WHERE tag='water' AND `name`='paas_url';
```

**Bcf 路径修改:**

| 相关表 | 修改说明 | 
| -------- | -------- | 
| bcf_group     |  `/paas/` 开头的路径，改为： `/luffy/` 开头  |
| bcf_resource     |  `/paas/` 开头的路径，改为： `/luffy/` 开头  |

```sql
UPDATE bcf_group SET uri_path = REPLACE(uri_path,'/paas/','/luffy/') WHERE uri_path LIKE '/paas/%';
UPDATE bcf_resource SET uri_path = REPLACE(uri_path,'/paas/','/luffy/') WHERE uri_path LIKE '/paas/%';
```

## 三、升级镜像

## 四、重新导入 water 下的定时任务

```
#删掉旧的后，重新导入
water_paasfile_pln_water_20211120
```