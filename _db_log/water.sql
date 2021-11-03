
-- 2021.04.25
TRUNCATE water_reg_consumer;

ALTER TABLE `water_reg_consumer`
    ADD UNIQUE INDEX `IX_key`(`service`, `consumer_address`) USING BTREE;

-- 2021.04.27
ALTER TABLE `water_reg_service_speed`
    MODIFY COLUMN `row_id` bigint(11) NOT NULL AUTO_INCREMENT FIRST;

ALTER TABLE `water_reg_service_speed_date`
    MODIFY COLUMN `row_id` bigint(11) NOT NULL AUTO_INCREMENT FIRST;

ALTER TABLE `water_reg_service_speed_hour`
    MODIFY COLUMN `row_id` bigint(11) NOT NULL AUTO_INCREMENT FIRST;


-- 2021.04.30
ALTER TABLE `water_reg_service`
    ADD COLUMN `tag` varchar(40) NOT NULL DEFAULT '' AFTER `key`,
ADD INDEX `IX_tag`(`tag`) USING BTREE;


-- 2021.05.13
ALTER TABLE `water_cfg_whitelist`
    ADD INDEX `IX_note`(`note`(40)) USING BTREE;


-- 2021.08.30 //适应弹性容器下，不断增加id
ALTER TABLE `water_reg_service`
    MODIFY COLUMN `service_id` bigint(20) NOT NULL AUTO_INCREMENT FIRST;

ALTER TABLE `water_reg_consumer`
    MODIFY COLUMN `row_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID' FIRST;


-- 2021.09.07 //转换部分时间类型
ALTER TABLE `rubber_actor`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新' AFTER `note`;

ALTER TABLE `rubber_block`
    MODIFY COLUMN `last_updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `app_expr`;

ALTER TABLE `rubber_block_item`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL AFTER `f4`;

ALTER TABLE `rubber_model`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `debug_args`;

ALTER TABLE `rubber_model_field`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `note`;

ALTER TABLE `rubber_scheme`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;

ALTER TABLE `rubber_scheme_node`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `actor_display`;

ALTER TABLE `rubber_scheme_rule`
    MODIFY COLUMN `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;



-- 2021.10.28 //添加更新时间
ALTER TABLE `water_cfg_logger`
    ADD COLUMN `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `is_alarm`;

-- 2021.11.01
CREATE TABLE `water_cfg_broker` (
  `broker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '协调器ID',
  `tag` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `broker` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '协调器',
  `row_num` bigint(20) DEFAULT '0' COMMENT '累积行数',
  `row_num_today` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日行数',
  `row_num_today_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日错误行数',
  `row_num_yesterday` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天行数',
  `row_num_yesterday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天错误行数',
  `row_num_beforeday` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天行数',
  `row_num_beforeday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天错误行数',
  `keep_days` int(11) NOT NULL DEFAULT '15' COMMENT '保留天数',
  `source` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据源',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `is_alarm` int(11) NOT NULL DEFAULT '0' COMMENT '是否报警',
  `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`broker_id`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-消息协调器表';

INSERT INTO `water_cfg_broker` VALUES (1, 'water', 'default', 0, 0, 0, 0, 0, 0, 0, 3, '', NULL, 1, 0, '2021-11-02 07:25:32');


-- 2021-11-03
ALTER TABLE `water_cfg_logger`
    ADD UNIQUE INDEX `UX_logger`(`logger`) USING BTREE;


ALTER TABLE `water_cfg_broker`
    ADD UNIQUE INDEX `UX_broker`(`broker`) USING BTREE;

