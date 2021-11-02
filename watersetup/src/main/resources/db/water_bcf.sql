CREATE TABLE IF NOT EXISTS `bcf_config`  (
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '值',
  `note` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-配置表（仅看看用）' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_group`  (
  `pgid` int(11) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `p_pgid` int(11) NOT NULL DEFAULT 0 COMMENT '组的父节点ID',
  `r_pgid` int(11) NOT NULL DEFAULT 0 COMMENT '组的根节点ID',
  `pg_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组的手工编码',
  `cn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '英文名称',
  `uri_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接地址',
  `in_level` int(11) NULL DEFAULT 0 COMMENT '级别',
  `is_branch` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为支线',
  `tags` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `order_index` int(11) NULL DEFAULT 0 COMMENT '排序',
  `is_disabled` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用',
  `is_visibled` tinyint(1) NULL DEFAULT 1 COMMENT '是否显示',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_update` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`pgid`) USING BTREE,
  INDEX `IX_p_pgid`(`p_pgid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-分组表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_opsx`  (
  `lk_objt` int(11) NOT NULL DEFAULT 0 COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL DEFAULT 0 COMMENT '连接对象ID',
  `tags` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签',
  `opsx` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT 'JSON值 ',
  PRIMARY KEY (`lk_objt`, `lk_objt_id`, `tags`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-对象扩展表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_resource`  (
  `rsid` int(11) NOT NULL AUTO_INCREMENT,
  `rs_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源手工代码',
  `cn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '英文名称',
  `uri_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '连接地址',
  `uri_target` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接目标',
  `ico_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标地址',
  `order_index` int(11) NULL DEFAULT 0 COMMENT '排序值',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tags` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `is_disabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否禁用（默认否）',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_update` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`rsid`) USING BTREE,
  INDEX `IX_code`(`rs_code`) USING BTREE,
  INDEX `IX_path`(`uri_path`(191)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-资源表（一切皆为资源）' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_resource_linked`  (
  `rsid` int(11) NOT NULL COMMENT '资源ID',
  `lk_objt` int(11) NOT NULL DEFAULT 0 COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL DEFAULT 0 COMMENT '连接对象ID',
  `lk_operate` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连操操作符(+,-)',
  `p_express` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '操作表达式(预留)',
  PRIMARY KEY (`rsid`, `lk_objt`, `lk_objt_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-资源连接表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_user`  (
  `puid` int(11) NOT NULL AUTO_INCREMENT COMMENT '内部用户ID',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `out_objt` int(11) NULL DEFAULT 0 COMMENT '外部关系对象',
  `out_objt_id` bigint(20) NULL DEFAULT 0 COMMENT '外部关系对象ID',
  `cn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `en_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '英文名称',
  `pw_mail` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码找回邮箱',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tags` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_disabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否禁用',
  `is_visibled` tinyint(1) NULL DEFAULT NULL COMMENT '是否可见',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_update` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `pass_wd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号密码',
  `state` int(11) NOT NULL DEFAULT 0 COMMENT '状态（预留）',
  PRIMARY KEY (`puid`) USING BTREE,
  UNIQUE INDEX `IX_User_Id`(`user_id`) USING BTREE,
  INDEX `IX_OUT_OBJT`(`out_objt`, `out_objt_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-用户表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `bcf_user_linked`  (
  `puid` int(11) NOT NULL COMMENT '内部用户ID',
  `lk_objt` int(10) UNSIGNED NOT NULL COMMENT '连接对象',
  `lk_objt_id` int(11) NOT NULL COMMENT '连接对象ID',
  `lk_operate` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接操作符（+,-）',
  PRIMARY KEY (`puid`, `lk_objt`, `lk_objt_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'BCF-用户连接表' ROW_FORMAT = DYNAMIC;

