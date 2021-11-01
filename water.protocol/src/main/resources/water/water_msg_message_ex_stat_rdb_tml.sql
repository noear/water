CREATE TABLE IF NOT EXISTS `water_msg_message_ex_stat` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL DEFAULT '0' COMMENT '主题ID',
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT 'yyyyMMdd',
  `log_hour` int(11) NOT NULL DEFAULT '-1' COMMENT 'HH（-1，全天）',
  `total` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`row_id`) USING BTREE,
  KEY `IX_topic_id` (`topic_id`) USING BTREE,
  KEY `IX_log_date` (`log_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-消息-统计表';