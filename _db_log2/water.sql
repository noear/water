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

