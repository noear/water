CREATE TABLE IF NOT EXISTS `water_cfg_i18n` (
    `row_id` int(11) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL COMMENT '分组标签',
    `bundle` varchar(40) NOT NULL COMMENT '语言包名',
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



CREATE TABLE IF NOT EXISTS  `water_tool_detection` (
    `detection_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
    `key` varchar(40) NOT NULL COMMENT '唯一key',
    `name` varchar(255) NOT NULL COMMENT '名称',
    `protocol` varchar(100) DEFAULT NULL COMMENT '协议',
    `address` varchar(500) NOT NULL COMMENT '地址',
    `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `check_interval` int(11) NOT NULL DEFAULT '0' COMMENT '检测间隔时间(s)',
    `check_last_time` bigint(20) NOT NULL COMMENT '最后检查时间',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`detection_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-工具-应用监视';


CREATE TABLE IF NOT EXISTS  `water_tool_certification` (
    `certification_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tag` varchar(40) NOT NULL DEFAULT '' COMMENT '分类标签',
    `key` varchar(40) NOT NULL COMMENT '唯一key',
    `url` varchar(500) NOT NULL COMMENT '地址',
    `note` varchar(255) DEFAULT NULL COMMENT '备注',
    `time_of_start` timestamp NULL DEFAULT NULL COMMENT '生效时间',
    `time_of_end` timestamp NULL DEFAULT NULL COMMENT '失效时间',
    `state` int(11) NOT NULL DEFAULT '0' COMMENT '0:待检查；1检查中',
    `check_error_num` int(11) NOT NULL DEFAULT '0' COMMENT '检测异常数量',
    `check_interval` int(11) NOT NULL DEFAULT '0' COMMENT '检测间隔时间(s)',
    `check_last_time` bigint(20) NOT NULL COMMENT '最后检查时间',
    `check_last_state` int(11) NOT NULL DEFAULT '0' COMMENT '最后检查状态（0：OK；1：error）',
    `check_last_note` varchar(255) DEFAULT NULL COMMENT '最后检查描述',
    `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否为已启用',
    `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`certification_id`) USING BTREE,
    UNIQUE KEY `IX_key` (`key`) USING BTREE,
    KEY `IX_tag` (`tag`) USING BTREE,
    KEY `IX_time_of_end` (`time_of_end`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-工具-证书监视';



