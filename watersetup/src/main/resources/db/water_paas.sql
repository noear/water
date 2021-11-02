CREATE TABLE IF NOT EXISTS `paas_etl`  (
  `etl_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类标签',
  `etl_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `code` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JSON配置代码',
  `is_enabled` int(11) NOT NULL DEFAULT 0 COMMENT '是否启动 ',
  `is_extract` int(11) NOT NULL DEFAULT 0 COMMENT '是否启用抽取器',
  `is_load` int(11) NOT NULL DEFAULT 0 COMMENT '是否启用加载器',
  `is_transform` int(11) NOT NULL DEFAULT 1 COMMENT '是否启用转换器',
  `cursor_type` int(11) NOT NULL DEFAULT 0 COMMENT '0时间；1数值',
  `cursor` bigint(20) NOT NULL DEFAULT 0 COMMENT '游标',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `e_enabled` int(11) NOT NULL DEFAULT 0,
  `e_max_instance` int(11) NOT NULL DEFAULT 1 COMMENT '抽取器集群数',
  `e_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_enabled` int(11) NOT NULL DEFAULT 0,
  `t_max_instance` int(11) NOT NULL DEFAULT 1 COMMENT '转换器集群数',
  `t_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `l_enabled` int(11) NOT NULL DEFAULT 0,
  `l_max_instance` int(11) NOT NULL DEFAULT 1 COMMENT '加载器集群数',
  `l_last_exectime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_extract_time` timestamp NULL DEFAULT NULL COMMENT '最后抽取时间',
  `last_load_time` timestamp NULL DEFAULT NULL COMMENT '最后加载时间',
  `last_transform_time` timestamp NULL DEFAULT NULL COMMENT '最后转换时间',
  PRIMARY KEY (`etl_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `etl_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'PAAS-ETL配置表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `paas_file`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_type` int(11) NOT NULL DEFAULT 0 COMMENT '文件类型(0:api, 1:pln, 2:tml)',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分组村签',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标记',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `rank` int(11) NOT NULL DEFAULT 0 COMMENT '排列（小的排前）',
  `is_staticize` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否静态',
  `is_editable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可编辑',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `is_exclude` tinyint(1) NOT NULL DEFAULT 0 COMMENT '排除导入',
  `link_to` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接到',
  `edit_mode` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '编辑模式',
  `content_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `note` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `plan_state` int(11) NOT NULL DEFAULT 0 COMMENT '计划状态',
  `plan_begin_time` bigint(20) NULL DEFAULT NULL COMMENT '计划开始执行时间',
  `plan_last_time` bigint(20) NULL DEFAULT NULL COMMENT '计划最后执行时间',
  `plan_last_timespan` bigint(20) NOT NULL DEFAULT 0 COMMENT '计划最后执行时间长度',
  `plan_next_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '计划下次执行时间戳',
  `plan_interval` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计划执行间隔',
  `plan_max` int(11) NOT NULL DEFAULT 0 COMMENT '计划执行最多次数',
  `plan_count` int(11) NOT NULL DEFAULT 0 COMMENT '计划执行累计次数',
  `create_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `use_whitelist` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全名单',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`path`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_label`(`label`) USING BTREE,
  INDEX `IX_file_type`(`file_type`) USING BTREE,
  INDEX `IX_plan_next_time`(`plan_next_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'PAAS-文件表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_actor`  (
  `actor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参与ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者代号',
  `name_display` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者显示名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新',
  PRIMARY KEY (`actor_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-角色表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_block`  (
  `block_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关数据(sponge/angel)',
  `related_tb` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关数据表',
  `is_editable` int(11) NOT NULL DEFAULT 0,
  `is_enabled` int(11) NOT NULL DEFAULT 1,
  `struct` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据结构({f1:\'xx\'})',
  `app_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '应用表达式',
  `last_updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`block_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-数据块' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_block_item`  (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `block_id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `f1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `f2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `f3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `f4` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`item_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`block_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-数据块项' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_log_request`  (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int(11) NOT NULL DEFAULT 0 COMMENT '处理策略',
  `args_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评估报告',
  `session_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int(11) NOT NULL DEFAULT 0 COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint(20) NOT NULL DEFAULT 0 COMMENT '处理时间（ms）',
  `state` int(11) NOT NULL DEFAULT 0 COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `log_date` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-日志-请求记录表' ROW_FORMAT = Compact;

CREATE TABLE IF NOT EXISTS `rubber_log_request_all`  (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int(11) NOT NULL DEFAULT 0 COMMENT '处理策略',
  `args_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评估报告',
  `session_json` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int(11) NOT NULL DEFAULT 0 COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint(20) NOT NULL DEFAULT 0 COMMENT '处理时间（ms）',
  `state` int(11) NOT NULL DEFAULT 0 COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `log_date` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_log_date`(`log_date`) USING BTREE,
  INDEX `IX_trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-日志-请求记录表' ROW_FORMAT = Compact;

CREATE TABLE IF NOT EXISTS `rubber_model`  (
  `model_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '相关数据库',
  `field_count` int(11) NULL DEFAULT 0,
  `init_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '构造表达式',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调试参数',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-数据模型表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_model_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段ID',
  `model_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属的模型ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '显示名',
  `expr` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段动态生成代码',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `is_pk` int(11) NOT NULL DEFAULT 0 COMMENT '是否是主键',
  PRIMARY KEY (`field_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`model_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-数据模型-字段表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_scheme`  (
  `scheme_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相关模型(tag/name)',
  `related_model_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联模型ID',
  `related_model_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联模型显示名',
  `related_block` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '引用函数',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调试参数',
  `event` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '事件',
  `node_count` int(11) NOT NULL DEFAULT 0 COMMENT '下属工作流节点数据',
  `rule_count` int(11) NOT NULL DEFAULT 0 COMMENT '下属规则数量',
  `rule_relation` int(11) NOT NULL DEFAULT 0 COMMENT '规则关系（0并且关系，1或者关系）',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`scheme_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-计算方案表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_scheme_node`  (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `scheme_id` int(11) NOT NULL,
  `node_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` int(11) NOT NULL DEFAULT 0 COMMENT '节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代号',
  `prve_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上个节点ID（type=line时，才有值 ）',
  `next_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下个节点ID（type=line时，才有值 ）',
  `condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分支条件（type=line时，才有值 ：left,op,right,ct；left,op,right,ct）',
  `tasks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行任务（type=exec时，才有值：F,tag/fun1；R,tag/rule1 ）',
  `actor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参与者（type=exec时，才有值 ：tag/name）',
  `actor_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `is_enabled` int(11) NULL DEFAULT 0 COMMENT '是否启用  0：未启用  1：启用 ',
  PRIMARY KEY (`node_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`scheme_id`, `node_key`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-计算方案-节点表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_scheme_node_design`  (
  `scheme_id` int(11) NOT NULL,
  `details` varchar(9999) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-计算方案-节点设计表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `rubber_scheme_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `scheme_id` int(11) NOT NULL DEFAULT 0 COMMENT '方案ID',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '显示名',
  `advice` int(11) NOT NULL DEFAULT 0 COMMENT '评估建议(0无,1交易放行,2审慎审核,3阻断交易)',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT '评估分值',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `expr` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '条件表达式（m.user_day(30),>,15,&&；left,op,right,ct）',
  `expr_display` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `event` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '状态，(0：禁用、1：启用)',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'RAAS-计算方案-规则表' ROW_FORMAT = DYNAMIC;

