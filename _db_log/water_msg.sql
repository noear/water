
-- 2021.04.19
ALTER TABLE `water_msg_message`
    ADD COLUMN `last_date` int(11) NOT NULL DEFAULT 0 AFTER `log_fulltime`;

ALTER TABLE `water_msg_message`
    ADD INDEX `IX_last_date`(`last_date`) USING BTREE;


ALTER TABLE `water_msg_message_all`
    ADD COLUMN `last_date` int(11) NOT NULL DEFAULT 0 AFTER `log_fulltime`;

ALTER TABLE `water_msg_message_all`
    ADD INDEX `IX_last_date`(`last_date`) USING BTREE;

update  water_msg_message set last_date = log_date where last_date=0;
update  water_msg_message_all set last_date = log_date where last_date=0;


-- 2021.04.23
ALTER TABLE `water_msg_subscriber`
    ADD INDEX `IX_receive_url`(`receive_url`(40)) USING BTREE;


-- 2021.08.30 //适应弹性容器下，不断增加id
ALTER TABLE `water_msg_distribution`
    MODIFY COLUMN `subscriber_id` bigint(20) NOT NULL AFTER `msg_id`;

ALTER TABLE `water_msg_subscriber`
    MODIFY COLUMN `subscriber_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订阅者ID' FIRST;



-- 2021.09.07 //转换部分时间类型
ALTER TABLE `water_msg_distribution` 
    MODIFY COLUMN `log_fulltime` timestamp NOT NULL COMMENT '分发时间' AFTER `log_date`;

ALTER TABLE `water_msg_message` 
    MODIFY COLUMN `plan_time` timestamp NULL DEFAULT NULL AFTER `receive_check`,
    MODIFY COLUMN `log_fulltime` timestamp NOT NULL COMMENT '记录时间' AFTER `log_date`,
    MODIFY COLUMN `last_fulltime` timestamp NULL DEFAULT NULL COMMENT '最后变更时间' AFTER `last_date`;

ALTER TABLE `water_msg_message_all` 
    MODIFY COLUMN `plan_time` timestamp NULL DEFAULT NULL AFTER `receive_check`,
    MODIFY COLUMN `log_fulltime` timestamp NOT NULL COMMENT '记录时间' AFTER `log_date`,
    MODIFY COLUMN `last_fulltime` timestamp NULL DEFAULT NULL COMMENT '最后变更时间' AFTER `last_date`; 

ALTER TABLE `water_msg_subscriber` 
    MODIFY COLUMN `log_fulltime` timestamp NOT NULL COMMENT '记录的完整 时间' AFTER `is_sync`;


ALTER TABLE `water_msg_topic` 
    MODIFY COLUMN `create_fulltime` timestamp NULL DEFAULT CURRENT_TIMESTAMP AFTER `alarm_model`;

-- 2021.11.01
ALTER TABLE `water_msg_message`
    MODIFY COLUMN `topic_id` int NULL DEFAULT 0 COMMENT '主题ID' AFTER `tags`;

ALTER TABLE `water_msg_message_all`
    MODIFY COLUMN `topic_id` int NULL DEFAULT 0 COMMENT '主题ID' AFTER `tags`;

