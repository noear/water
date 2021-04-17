ALTER TABLE `water_paas`.`paas_file`
    ADD COLUMN `use_whitelist` varchar(40) NULL COMMENT '安全名单' AFTER `update_fulltime`;