
-- 2021.10.13
ALTER TABLE `sponge_log_admin`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `sponge_log_rock`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `sponge_log_track`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_admin`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_api`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_bcf`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_etl`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_heihei`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;


ALTER TABLE `water_log_msg`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_paas`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_raas`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_sev`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;

ALTER TABLE `water_log_upstream`
    ADD COLUMN `app_name` varchar(100) NULL COMMENT '应用名' AFTER `summary`,
    ADD INDEX `IX_app_name`(`app_name`) USING BTREE;















