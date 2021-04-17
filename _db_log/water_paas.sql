
-- 2021.04.17
ALTER TABLE `paas_file`
    ADD COLUMN `use_whitelist` varchar(40) NULL COMMENT '安全名单' AFTER `update_fulltime`;