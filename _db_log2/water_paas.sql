
-- 2021-11-19 (for 2.3.4)
ALTER TABLE `paas_etl`
    MODIFY COLUMN `e_last_exectime` bigint NULL AFTER `e_max_instance`,
    MODIFY COLUMN `t_last_exectime` bigint NULL AFTER `t_max_instance`,
    MODIFY COLUMN `l_last_exectime` bigint NULL AFTER `l_max_instance`,
    MODIFY COLUMN `last_extract_time` bigint NULL COMMENT '最后抽取时间' AFTER `l_last_exectime`;


ALTER TABLE `paas_file`
    MODIFY COLUMN `create_fulltime` bigint NULL COMMENT '创建时间' AFTER `plan_count`,
    MODIFY COLUMN `update_fulltime` bigint NULL COMMENT '更新时间' AFTER `create_fulltime`;


ALTER TABLE `paas_etl`
    MODIFY COLUMN `last_load_time` bigint NULL COMMENT '最后加载时间' AFTER `last_extract_time`,
    MODIFY COLUMN `last_transform_time` bigint NULL COMMENT '最后转换时间' AFTER `last_load_time`;


ALTER  TABLE `paas_file` RENAME TO `luffy_file`;

ALTER  TABLE `paas_etl` RENAME TO `luffy_etl`;


ALTER TABLE `rubber_actor`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `note`;

ALTER TABLE `rubber_block`
    MODIFY COLUMN `last_updatetime` bigint NULL COMMENT '最后更新时间' AFTER `app_expr`;

ALTER TABLE `rubber_block_item`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `f4`;

ALTER TABLE `rubber_log_request`
    MODIFY COLUMN `start_fulltime` bigint NULL DEFAULT NULL COMMENT '开始时间' AFTER `note_json`,
    MODIFY COLUMN `end_fulltime` bigint NULL DEFAULT NULL COMMENT '完成时间' AFTER `start_date`;

ALTER TABLE `rubber_log_request_all`
    MODIFY COLUMN `start_fulltime` bigint NULL DEFAULT NULL COMMENT '开始时间' AFTER `note_json`,
    MODIFY COLUMN `end_fulltime` bigint NULL DEFAULT NULL COMMENT '完成时间' AFTER `start_date`;

ALTER TABLE `rubber_model`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `debug_args`;

ALTER TABLE `rubber_model_field`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `note`;

ALTER TABLE `rubber_scheme`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;

ALTER TABLE `rubber_scheme_node`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `actor_display`;

ALTER TABLE `rubber_scheme_rule`
    MODIFY COLUMN `last_updatetime` bigint NULL DEFAULT NULL COMMENT '最后更新时间' AFTER `is_enabled`;

ALTER TABLE `rubber_scheme_node_design`
    ADD COLUMN `last_updatetime` bigint NULL COMMENT '最后更新时间' AFTER `details`;


