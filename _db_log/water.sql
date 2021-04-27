
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