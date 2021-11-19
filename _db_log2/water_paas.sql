
-- 2021-11-19
ALTER TABLE `paas_etl`
    MODIFY COLUMN `e_last_exectime` bigint NULL AFTER `e_max_instance`,
    MODIFY COLUMN `t_last_exectime` bigint NULL AFTER `t_max_instance`,
    MODIFY COLUMN `l_last_exectime` bigint NULL AFTER `l_max_instance`,
    MODIFY COLUMN `last_extract_time` bigint NULL COMMENT '最后抽取时间' AFTER `l_last_exectime`;


ALTER TABLE `paas_file`
    MODIFY COLUMN `create_fulltime` bigint NULL COMMENT '创建时间' AFTER `plan_count`,
    MODIFY COLUMN `update_fulltime` bigint NULL COMMENT '更新时间' AFTER `create_fulltime`;



