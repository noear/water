
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