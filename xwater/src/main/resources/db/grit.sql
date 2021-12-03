CREATE TABLE IF NOT EXISTS  `grit_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源父ID',
  `resource_sid` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源空间ID',
  `resource_type` int(11) NOT NULL DEFAULT '0' COMMENT '资源类型(0:entity, 1:group, 2:space)',
  `resource_code` varchar(50) NOT NULL DEFAULT '' COMMENT '资源代码(例，user:del)',
  `display_name` varchar(200) DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序值',
  `link_uri` varchar(200) NOT NULL DEFAULT '' COMMENT '链接地址(例，/user/add)',
  `link_target` varchar(50) DEFAULT NULL COMMENT '链接目标',
  `link_tags` varchar(200) DEFAULT NULL COMMENT '链接标签(用,隔开)',
  `icon_uri` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '节点级别',
  `attrs` varchar(4000) DEFAULT NULL COMMENT '属性(kv)',
  `is_fullview` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全屏',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见（可见为页面，不可见为操作）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `IX_grit_resource__resource_code` (`resource_code`) USING BTREE,
  KEY `IX_grit_resource__resource_pid` (`resource_pid`) USING BTREE,
  KEY `IX_grit_resource__resource_sid` (`resource_sid`) USING BTREE,
  KEY `IX_grit_resource__link_uri` (`link_uri`(100)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源表';


CREATE TABLE IF NOT EXISTS  `grit_resource_linked` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `subject_type` int(11) NOT NULL DEFAULT '0' COMMENT '主体类型',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE KEY `IX_grit_resource_linked__key` (`resource_id`,`subject_id`) USING BTREE,
  KEY `IX_grit_resource_linked__subject_id` (`subject_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源与主体连接表';

CREATE TABLE IF NOT EXISTS  `grit_subject` (
  `subject_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主体ID',
  `subject_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '主体父ID',
  `subject_type` int(11) NOT NULL DEFAULT '0' COMMENT '主体类型(0:entity, 1:group)',
  `subject_code` varchar(50) NOT NULL DEFAULT '' COMMENT '主体代号',
  `login_name` varchar(50) NOT NULL COMMENT '主体登录名,默认用guid填充',
  `login_password` varchar(50) DEFAULT NULL COMMENT '主体登录密码',
  `display_name` varchar(200) DEFAULT NULL COMMENT '主体显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '节点级别',
  `attrs` varchar(4000) DEFAULT NULL COMMENT '属性(kv)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE KEY `IX_grit_subject__login_name` (`login_name`) USING BTREE,
  KEY `IX_grit_subject__subject_pid` (`subject_pid`) USING BTREE,
  KEY `IX_grit_subject__subject_code` (`subject_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-主体表';


CREATE TABLE IF NOT EXISTS  `grit_subject_linked` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `group_subject_id` bigint(20) NOT NULL COMMENT '分组主体ID',
  `gmt_create` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE KEY `IX_grit_subject_linked__key` (`subject_id`,`group_subject_id`) USING BTREE,
  KEY `IX_grit_subject_linked__group_subject_id` (`group_subject_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='grit-主体与分组连接表';
