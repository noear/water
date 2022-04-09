CREATE TABLE IF NOT EXISTS `water_cfg_i18n` (
    `row_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '国际化id',
    `tag` varchar(40) NOT NULL COMMENT '分组标签',
    `bundle` varchar(40) NOT NULL COMMENT '捆名',
    `lang` varchar(40) NOT NULL COMMENT '语言',
    `name` varchar(100) NOT NULL COMMENT '名称',
    `value` varchar(5000) DEFAULT NULL COMMENT '值 ',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '禁用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`row_id`) USING BTREE,
    UNIQUE KEY `UX_key` (`tag`,`bundle`,`lang`,`name`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE,
    KEY `IX_tag_bundle_name` (`tag`,`bundle`,`name`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-多语言包';


CREATE TABLE IF NOT EXISTS  `water_cfg_key` (
    `key_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '密钥ID',
    `access_key` varchar(40)  NOT NULL COMMENT '访问键',
    `access_secret_key` varchar(255)  DEFAULT NULL COMMENT '访问密钥',
    `access_secret_salt` varchar(255)  DEFAULT NULL COMMENT '访问密钥盐',
    `tag` varchar(40)  DEFAULT NULL COMMENT '分组标签',
    `label` varchar(100)  DEFAULT NULL COMMENT '标记',
    `description` varchar(255)  DEFAULT NULL COMMENT '描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`key_id`) USING BTREE,
    UNIQUE KEY `UX_access_key` (`access_key`) USING BTREE,
    KEY `IX_tag` (`tag`),
    KEY `IX_label` (`label`(40))
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-访问密钥';
