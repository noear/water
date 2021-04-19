
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