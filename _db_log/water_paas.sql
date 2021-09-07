
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


-- 2021.07.22 //执行此变更前，备份一下数据库
ALTER TABLE `paas_file`
    CHANGE COLUMN `plan_next_timestamp` `plan_next_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '计划下次执行时间戳' AFTER `plan_last_timespan`,
    MODIFY COLUMN `plan_begin_time` bigint NULL DEFAULT NULL COMMENT '计划开始执行时间' AFTER `plan_state`,
    MODIFY COLUMN `plan_last_time` bigint NULL DEFAULT NULL COMMENT '计划最后执行时间' AFTER `plan_begin_time`;

ALTER TABLE `paas_file`
DROP INDEX `IX_plan_next_timestamp`,
ADD INDEX `IX_plan_next_time`(`plan_next_time`) USING BTREE;

UPDATE paas_file
SET plan_begin_time = unix_timestamp(STR_TO_DATE(CAST(plan_begin_time AS CHAR),'%Y%m%d%H%i%s'))*1000
WHERE plan_begin_time > 2000000000000;

UPDATE paas_file
SET plan_last_time = unix_timestamp(STR_TO_DATE(CAST(plan_last_time AS CHAR),'%Y%m%d%H%i%s'))*1000
WHERE plan_last_time > 2000000000000;


-- 2021.09.07 
ALTER TABLE `paas_etl` 
    MODIFY COLUMN `e_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP AFTER `e_max_instance`,
    MODIFY COLUMN `t_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP AFTER `t_max_instance`,
    MODIFY COLUMN `l_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP AFTER `l_max_instance`,
    MODIFY COLUMN `last_extract_time` timestamp NULL DEFAULT NULL COMMENT '最后抽取时间' AFTER `l_last_exectime`,
    MODIFY COLUMN `last_load_time` timestamp NULL DEFAULT NULL COMMENT '最后加载时间' AFTER `last_extract_time`,
    MODIFY COLUMN `last_transform_time` timestamp NULL DEFAULT NULL COMMENT '最后转换时间' AFTER `last_load_time`;



