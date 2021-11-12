
-- 2021-11-03
ALTER TABLE `water_cfg_logger`
    ADD UNIQUE INDEX `UX_logger`(`logger`) USING BTREE;


ALTER TABLE `water_cfg_broker`
    ADD UNIQUE INDEX `UX_broker`(`broker`) USING BTREE;

-- 2021-11-12
CREATE TABLE `water_cfg_gateway` (
     `gateway_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '网关id',
     `tag` varchar(40) NOT NULL COMMENT '标签',
     `name` varchar(100) NOT NULL COMMENT '名称',
     `agent` varchar(255) DEFAULT NULL COMMENT '代理',
     `policy` varchar(100) DEFAULT NULL COMMENT '策略',
     `is_enabled` int(11) NOT NULL DEFAULT '1',
     `update_fulltime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
     PRIMARY KEY (`gateway_id`) USING BTREE,
     UNIQUE KEY `UX_name` (`name`) USING BTREE,
     KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='WATER-配置-网关';