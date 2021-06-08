
-- 2021.04.17
ALTER TABLE `paas_file`
    ADD COLUMN `use_whitelist` varchar(40) NULL COMMENT '安全名单' AFTER `update_fulltime`;


-- 2021.06.08
ALTER TABLE `paas_file`
    MODIFY COLUMN `plan_begin_time` timestamp NULL DEFAULT NULL COMMENT '计划开始执行时间' AFTER `plan_state`,
    MODIFY COLUMN `plan_last_time` timestamp NULL DEFAULT NULL COMMENT '计划最后执行时间' AFTER `plan_begin_time`,
    MODIFY COLUMN `create_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `plan_count`,
    MODIFY COLUMN `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_fulltime`;
