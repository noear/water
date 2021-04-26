
-- 2021.04.25
TRUNCATE water_reg_consumer;

ALTER TABLE `water_reg_consumer`
    ADD UNIQUE INDEX `IX_key`(`service`, `consumer_address`) USING BTREE;

