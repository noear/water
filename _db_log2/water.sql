
-- 2021-11-03
ALTER TABLE `water`.`water_cfg_logger`
    ADD UNIQUE INDEX `UX_logger`(`logger`) USING BTREE;


ALTER TABLE `water`.`water_cfg_broker`
    ADD UNIQUE INDEX `UX_broker`(`broker`) USING BTREE;