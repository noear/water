
-- 2021.10.13
ALTER TABLE `sponge_log_admin`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `sponge_log_rock`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `sponge_log_track`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_admin`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_api`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_bcf`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_etl`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_heihei`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;


ALTER TABLE `water_log_msg`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_paas`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_raas`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_sev`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;

ALTER TABLE `water_log_upstream`
    ADD COLUMN `service` varchar(100) NULL COMMENT '服务' AFTER `summary`,
    ADD INDEX `IX_service`(`service`) USING BTREE;















