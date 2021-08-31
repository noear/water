
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