
-- 2021.11.06  (for 2.3.0)
ALTER TABLE `water_msg_distribution`
    MODIFY COLUMN `log_fulltime` bigint NOT NULL COMMENT '分发时间' AFTER `log_date`;

ALTER TABLE `water_msg_message`
    MODIFY COLUMN `log_fulltime` bigint NOT NULL COMMENT '记录时间' AFTER `log_date`,
    MODIFY COLUMN `last_fulltime` bigint NULL COMMENT '最后变更时间' AFTER `last_date`;

ALTER TABLE `water_msg_message_all`
    MODIFY COLUMN `log_fulltime` bigint NOT NULL COMMENT '记录时间' AFTER `log_date`,
    MODIFY COLUMN `last_fulltime` bigint NULL COMMENT '最后变更时间' AFTER `last_date`;