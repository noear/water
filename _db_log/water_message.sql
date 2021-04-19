
-- 2021.04.19
ALTER TABLE `water_msg_message`
    ADD COLUMN `last_date` int(11) NOT NULL DEFAULT 0 COMMENT '最后变更日期(yyyyMMdd)' AFTER `log_fulltime`,
MODIFY COLUMN `last_fulltime` datetime NULL DEFAULT NULL COMMENT '最后变更时间' AFTER `log_fulltime`,
ADD INDEX `IX_last_date`(`last_date`) USING BTREE;




ALTER TABLE `water_msg_message_all`
    ADD COLUMN `last_date` int NULL AFTER `last_fulltime`;

ALTER TABLE `water_msg_message_all`
    ADD INDEX `IX_last_date`(`last_date`) USING BTREE;

update  water_msg_message set last_date = log_date where last_date=0;
update  water_msg_message_all set last_date = log_date where last_date=0;