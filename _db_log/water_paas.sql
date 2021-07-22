
-- 2021.04.17
ALTER TABLE `paas_file`
    ADD COLUMN `use_whitelist` varchar(40) NULL COMMENT '安全名单' AFTER `update_fulltime`;


-- 2021.06.08
ALTER TABLE `paas_file`
    MODIFY COLUMN `plan_begin_time` timestamp NULL DEFAULT NULL COMMENT '计划开始执行时间' AFTER `plan_state`,
    MODIFY COLUMN `plan_last_time` timestamp NULL DEFAULT NULL COMMENT '计划最后执行时间' AFTER `plan_begin_time`,
    MODIFY COLUMN `create_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `plan_count`,
    MODIFY COLUMN `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_fulltime`;


-- 2021.06.25
ALTER TABLE `paas_file`
    ADD COLUMN `plan_next_timestamp` bigint NOT NULL DEFAULT '0' COMMENT '计划下次执行时间戳' AFTER `plan_last_timespan`;

ALTER TABLE `paas_file`
    ADD INDEX `IX_plan_next_timestamp`(`plan_next_timestamp`) USING BTREE;

ALTER TABLE `paas_file`
    MODIFY COLUMN `plan_interval` varchar(100) NOT NULL DEFAULT '' COMMENT '计划执行间隔' AFTER `plan_next_timestamp`;


-- 2021.07.22
ALTER TABLE `paas_file`
    CHANGE COLUMN `plan_next_timestamp` `plan_next_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '计划下次执行时间戳' AFTER `plan_last_timespan`,
    MODIFY COLUMN `plan_begin_time` bigint NULL DEFAULT NULL COMMENT '计划开始执行时间' AFTER `plan_state`,
    MODIFY COLUMN `plan_last_time` bigint NULL DEFAULT NULL COMMENT '计划最后执行时间' AFTER `plan_begin_time`;

ALTER TABLE `paas_file`
DROP INDEX `IX_plan_next_timestamp`,
ADD INDEX `IX_plan_next_time`(`plan_next_time`) USING BTREE;


