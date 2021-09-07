SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Table structure for rubber_actor
-- ----------------------------
DROP TABLE IF EXISTS `rubber_actor`;
CREATE TABLE `rubber_actor`  (
  `actor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参与ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者代号',
  `name_display` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参与者显示名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新',
  PRIMARY KEY (`actor_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_actor
-- ----------------------------
INSERT INTO `rubber_actor` VALUES (19, 'water', 'test', 'test', '', '2021-04-05 16:20:21');

-- ----------------------------
-- Table structure for rubber_block
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block`;
CREATE TABLE `rubber_block`  (
  `block_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '相关数据(sponge/angel)',
  `related_tb` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '相关数据表',
  `is_editable` int(11) NOT NULL DEFAULT 0,
  `is_enabled` int(11) NOT NULL DEFAULT 1,
  `struct` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据结构({f1:\'xx\'})',
  `app_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '应用表达式',
  `last_updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`block_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据块' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_block
-- ----------------------------
INSERT INTO `rubber_block` VALUES (57, 'water', 'code_agent_ip', '代理IP库', '', 'water/water_redis', '12', 1, 1, '{ip:\'ip*\'}', 'if(!x){\n  throw new Error(\'参数不能为空\');\n}\n\nvar bkey = this.block_key;\nreturn this.$().open1(function(rs){\n   return rs.key(bkey).hashHas(x);\n});', '2021-04-05 16:18:38');

-- ----------------------------
-- Table structure for rubber_block_item
-- ----------------------------
DROP TABLE IF EXISTS `rubber_block_item`;
CREATE TABLE `rubber_block_item`  (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `block_id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `f4` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`item_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`block_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据块项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_block_item
-- ----------------------------

-- ----------------------------
-- Table structure for rubber_model
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model`;
CREATE TABLE `rubber_model`  (
  `model_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_db` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '相关数据库',
  `field_count` int(11) NULL DEFAULT 0,
  `init_expr` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '构造表达式',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调试参数',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据模型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_model
-- ----------------------------
INSERT INTO `rubber_model` VALUES (26, 'water', 'water_monitor_ecs', '监控模型', '', 6, 'return water.db(\'water/water\')\n     .table(\"water_ops_server_track_ecs\")\n     .whereEq(\"iaas_key\",this.iaas_key())\n     .selectMap(\"*\");', '{iaas_key:\'i-23adt35wk\'}', '2021-04-07 12:58:10');

-- ----------------------------
-- Table structure for rubber_model_field
-- ----------------------------
DROP TABLE IF EXISTS `rubber_model_field`;
CREATE TABLE `rubber_model_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段ID',
  `model_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属的模型ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段名称',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '显示名',
  `expr` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段动态生成代码',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字段',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `is_pk` int(11) NOT NULL DEFAULT 0 COMMENT '是否是主键',
  PRIMARY KEY (`field_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`model_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-数据模型-字段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_model_field
-- ----------------------------
INSERT INTO `rubber_model_field` VALUES (453, 26, 'iaas_key', 'iaas_key', '', '', '2021-04-05 15:18:06', 1);
INSERT INTO `rubber_model_field` VALUES (454, 26, 'cpu_usage', 'cpu_usage', '', '', '2021-04-05 15:17:06', 0);
INSERT INTO `rubber_model_field` VALUES (457, 26, 'memory_usage', 'memory_usage', '', '', '2021-04-05 15:17:30', 0);
INSERT INTO `rubber_model_field` VALUES (458, 26, 'disk_usage', 'disk_usage', '', '', '2021-04-05 15:17:39', 0);
INSERT INTO `rubber_model_field` VALUES (459, 26, 'broadband_usage', 'broadband_usage', '', '', '2021-04-05 15:17:54', 0);
INSERT INTO `rubber_model_field` VALUES (460, 26, 'tcp_num', 'tcp_num', '', '', '2021-04-05 15:18:00', 0);

-- ----------------------------
-- Table structure for rubber_scheme
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme`;
CREATE TABLE `rubber_scheme`  (
  `scheme_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方案ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代号',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '显示名',
  `related_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '相关模型(tag/name)',
  `related_model_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联模型ID',
  `related_model_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联模型显示名',
  `related_block` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '引用函数',
  `debug_args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调试参数',
  `event` varchar(999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '事件',
  `node_count` int(11) NOT NULL DEFAULT 0 COMMENT '下属工作流节点数据',
  `rule_count` int(11) NOT NULL DEFAULT 0 COMMENT '下属规则数量',
  `rule_relation` int(11) NOT NULL DEFAULT 0 COMMENT '规则关系（0并且关系，1或者关系）',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`scheme_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_scheme
-- ----------------------------
INSERT INTO `rubber_scheme` VALUES (82, 'water', 'water_test_ecs', 'water_test_ecs', 'water/water_monitor_ecs', 0, 'water/监控模型', '', '{iaas_key:\'i-23adt35wk\'}', '', 0, 3, 0, 1, '2021-04-05 15:21:19');
INSERT INTO `rubber_scheme` VALUES (83, 'angel', 'main', '主流程', '', 0, '', '', '', '', 15, 0, 0, 1, '2021-06-18 09:49:52');

-- ----------------------------
-- Table structure for rubber_scheme_node
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node`;
CREATE TABLE `rubber_scheme_node`  (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `scheme_id` int(11) NOT NULL,
  `node_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` int(11) NOT NULL DEFAULT 0 COMMENT '节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '代号',
  `prve_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上个节点ID（type=line时，才有值 ）',
  `next_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '下个节点ID（type=line时，才有值 ）',
  `condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分支条件（type=line时，才有值 ：left,op,right,ct;left,op,right,ct）',
  `tasks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行任务（type=exec时，才有值：F,tag/fun1;R,tag/rule1 ）',
  `actor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参与者（type=exec时，才有值 ：tag/name）',
  `actor_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `is_enabled` int(11) NULL DEFAULT 0 COMMENT '是否启用  0：未启用  1：启用 ',
  PRIMARY KEY (`node_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`scheme_id`, `node_key`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-节点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_scheme_node
-- ----------------------------
INSERT INTO `rubber_scheme_node` VALUES (199, 83, '1cfa361c-478d-4411-8266-5ce4591e80f5', 2, '产品准入', '', '', NULL, '', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (200, 83, '1cfa361c-478d-4411-8266-5ce4591e80f52', 2, 'IP及设备指纹', '', '', NULL, 'R,water/water_test_ecs', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (201, 83, '1cfa361c-478d-4411-8266-5ce4591e80f53', 2, '风除号码识别', '', '', NULL, '', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (202, 83, '1cfa361c-478d-4411-8266-5ce4591e80f54', 2, '紧急联系人及通讯录', '', '', NULL, '', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (203, 83, '1cfa361c-478d-4411-8266-5ce4591e80f55', 2, '运营商', '', '', NULL, '', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (204, 83, '1cfa361c-478d-4411-8266-5ce4591e80f56', 2, '第三方数据处理', '', '', NULL, '', NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (205, 83, '6c46b626-fd12-4217-9704-13e0825da05d', 0, '开始', '', '', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (206, 83, 'da301626-8829-4800-a5de-e2b8bfed14ca', 9, '结束', '', '', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (207, 83, '6c46b626-fd12-4217-9704-13e0825da05d1cfa361c-478d-4411-8266-5ce4591e80f5', 1, NULL, '6c46b626-fd12-4217-9704-13e0825da05d', '1cfa361c-478d-4411-8266-5ce4591e80f5', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (208, 83, '1cfa361c-478d-4411-8266-5ce4591e80f51cfa361c-478d-4411-8266-5ce4591e80f53', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f5', '1cfa361c-478d-4411-8266-5ce4591e80f53', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (209, 83, '1cfa361c-478d-4411-8266-5ce4591e80f531cfa361c-478d-4411-8266-5ce4591e80f52', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f53', '1cfa361c-478d-4411-8266-5ce4591e80f52', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (210, 83, '1cfa361c-478d-4411-8266-5ce4591e80f521cfa361c-478d-4411-8266-5ce4591e80f54', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f52', '1cfa361c-478d-4411-8266-5ce4591e80f54', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (211, 83, '1cfa361c-478d-4411-8266-5ce4591e80f541cfa361c-478d-4411-8266-5ce4591e80f55', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f54', '1cfa361c-478d-4411-8266-5ce4591e80f55', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (212, 83, '1cfa361c-478d-4411-8266-5ce4591e80f551cfa361c-478d-4411-8266-5ce4591e80f56', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f55', '1cfa361c-478d-4411-8266-5ce4591e80f56', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);
INSERT INTO `rubber_scheme_node` VALUES (213, 83, '1cfa361c-478d-4411-8266-5ce4591e80f56da301626-8829-4800-a5de-e2b8bfed14ca', 1, NULL, '1cfa361c-478d-4411-8266-5ce4591e80f56', 'da301626-8829-4800-a5de-e2b8bfed14ca', NULL, NULL, NULL, NULL, '2021-06-18 09:49:52', 0);

-- ----------------------------
-- Table structure for rubber_scheme_node_design
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_node_design`;
CREATE TABLE `rubber_scheme_node_design`  (
  `scheme_id` int(11) NOT NULL,
  `details` varchar(9999) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-节点设计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_scheme_node_design
-- ----------------------------
INSERT INTO `rubber_scheme_node_design` VALUES (83, '{ \"class\": \"go.GraphLinksModel\",\n  \"modelData\": {\"position\":\"-5 -5\"},\n  \"nodeDataArray\": [ \n{\"width\":30, \"height\":30, \"key\":\"6c46b626-fd12-4217-9704-13e0825da05d\", \"text\":\"开始\", \"figure\":\"Circle\", \"fill\":\"#7BC726\", \"stepType\":1, \"loc\":\"150 80\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f5\", \"text\":\"产品准入\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"150 230\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f52\", \"text\":\"IP及设备指纹\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"300 80\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f53\", \"text\":\"风除号码识别\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"300 230\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f54\", \"text\":\"紧急联系人及通讯录\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"490 80\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f55\", \"text\":\"运营商\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"660 80\"},\n{\"width\":30, \"height\":30, \"key\":\"1cfa361c-478d-4411-8266-5ce4591e80f56\", \"text\":\"第三方数据处理\", \"remark\":\"\", \"fill\":\"#1CA9C7\", \"figure\":\"execute\", \"loc\":\"660 230\"},\n{\"width\":30, \"height\":30, \"key\":\"da301626-8829-4800-a5de-e2b8bfed14ca\", \"text\":\"结束\", \"figure\":\"Circle\", \"fill\":\"#DF3A18\", \"stepType\":4, \"loc\":\"810 230\"}\n ],\n  \"linkDataArray\": [ \n{\"from\":\"6c46b626-fd12-4217-9704-13e0825da05d\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f5\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f5\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f53\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f53\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f52\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f52\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f54\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f54\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f55\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f55\", \"to\":\"1cfa361c-478d-4411-8266-5ce4591e80f56\"},\n{\"from\":\"1cfa361c-478d-4411-8266-5ce4591e80f56\", \"to\":\"da301626-8829-4800-a5de-e2b8bfed14ca\"}\n ]}');

-- ----------------------------
-- Table structure for rubber_scheme_rule
-- ----------------------------
DROP TABLE IF EXISTS `rubber_scheme_rule`;
CREATE TABLE `rubber_scheme_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `scheme_id` int(11) NOT NULL DEFAULT 0 COMMENT '方案ID',
  `name_display` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '显示名',
  `advice` int(11) NOT NULL DEFAULT 0 COMMENT '评估建议(0无,1交易放行,2审慎审核,3阻断交易)',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT '评估分值',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `expr` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '条件表达式（m.user_day(30),>,15,&&;left,op,right,ct）',
  `expr_display` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `event` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '状态，(0：禁用、1：启用)',
  `last_updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  INDEX `IX_scheme`(`scheme_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAAS-计算方案-规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rubber_scheme_rule
-- ----------------------------
INSERT INTO `rubber_scheme_rule` VALUES (275, 82, 'rule1', 1, 1, 0, '{\"_1\":{\"op\":\">\",\"ct\":\"\",\"r\":\"0\",\"l\":\"cpu_usage\"}}', 'cpu_usage > {_1:0}  ', '', 1, '2021-04-05 15:21:26');
INSERT INTO `rubber_scheme_rule` VALUES (276, 82, 'rule2', 2, 0, 0, '{\"_1\":{\"op\":\">\",\"ct\":\"\",\"r\":\"0\",\"l\":\"memory_usage\"}}', 'memory_usage > {_1:0}  ', '', 1, '2021-04-05 15:21:32');
INSERT INTO `rubber_scheme_rule` VALUES (277, 82, 'rule3', 3, 0, 0, '{\"_1\":{\"op\":\">\",\"ct\":\"\",\"r\":\"0\",\"l\":\"disk_usage\"}}', 'disk_usage > {_1:0}  ', '', 1, '2021-04-05 15:21:19');





-- ----------------------------
-- Table structure for water_cfg_logger
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_logger`;
CREATE TABLE `water_cfg_logger`  (
  `logger_id` int NOT NULL AUTO_INCREMENT COMMENT '日志器ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `logger` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '日志器',
  `row_num` bigint NULL DEFAULT 0 COMMENT '累积行数',
  `row_num_today` bigint NOT NULL DEFAULT 0 COMMENT '今日行数',
  `row_num_today_error` bigint NOT NULL DEFAULT 0 COMMENT '今日错误行数',
  `row_num_yesterday` bigint NOT NULL DEFAULT 0 COMMENT '昨天行数',
  `row_num_yesterday_error` bigint NOT NULL DEFAULT 0 COMMENT '昨天错误行数',
  `row_num_beforeday` bigint NOT NULL DEFAULT 0 COMMENT '前天行数',
  `row_num_beforeday_error` bigint NOT NULL DEFAULT 0 COMMENT '前天错误行数',
  `keep_days` int NOT NULL DEFAULT 15 COMMENT '保留天数',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据源',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `is_alarm` int NOT NULL DEFAULT 0 COMMENT '是否报警',
  PRIMARY KEY (`logger_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_logger
-- ----------------------------
INSERT INTO `water_cfg_logger` VALUES (19, 'water', 'water_log_api', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 1);
INSERT INTO `water_cfg_logger` VALUES (21, 'water', 'water_log_sev', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (28, 'water', 'water_log_admin', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (53, 'water', 'water_log_msg', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (61, 'water', 'water_log_etl', 0, 0, 0, 0, 0, 0, 0, 3, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (69, 'water', 'water_log_raas', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (70, 'water', 'water_log_paas', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (74, 'water', 'water_exam_log_sql', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 0, 0);
INSERT INTO `water_cfg_logger` VALUES (75, 'water', 'water_exam_log_bcf', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 0, 0);
INSERT INTO `water_cfg_logger` VALUES (77, 'sponge', 'sponge_log_rock', 0, 0, 0, 0, 0, 10, 0, 15, '', '', 1, 1);
INSERT INTO `water_cfg_logger` VALUES (84, 'water', 'water_log_heihei', 0, 0, 0, 0, 0, 0, 0, 15, '', '', 1, 0);
INSERT INTO `water_cfg_logger` VALUES (94, 'sponge', 'sponge_log_admin', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (101, 'water', 'water_log_upstream', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);
INSERT INTO `water_cfg_logger` VALUES (102, 'sponge', 'sponge_log_track', 0, 0, 0, 0, 0, 0, 0, 15, '', NULL, 1, 0);

-- ----------------------------
-- Table structure for water_cfg_properties
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_properties`;
CREATE TABLE `water_cfg_properties`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分组标签',
  `key` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性key',
  `type` int NOT NULL DEFAULT 0 COMMENT '类型：0:未知，1:数据库；2:Redis；3:MangoDb; 4:Memcached',
  `value` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性值',
  `edit_mode` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_editable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否可编辑',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_tag_key`(`tag`, `key`) USING BTREE,
  INDEX `IX_type`(`type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_properties
-- ----------------------------
INSERT INTO `water_cfg_properties` VALUES (25, 'water', 'water_msg', 10, '#Data#: c2NoZW1hPXdhdGVyCnVybD1qZGJjOm15c3FsOi8vbXlzcWwud2F0ZXIuaW86MzMwNi93YXRlcj91c2VTU0w9ZmFsc2UmYWxsb3dNdWx0aVF1ZXJpZXM9dHJ1ZSZ1c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmOCZhdXRvUmVjb25uZWN0PXRydWUmcmV3cml0ZUJhdGNoZWRTdGF0ZW1lbnRzPXRydWUKcGFzc3dvcmQ9S0hlODVFNE1ZZGVMQkhTUgp1c2VybmFtZT1kZW1vCmpkYmNVcmw9JHt1cmx9', 'properties', 1, 1, '2021-04-07 11:29:51');
INSERT INTO `water_cfg_properties` VALUES (28, 'water', 'water', 10, '#Data#: c2NoZW1hPXdhdGVyCnVybD1qZGJjOm15c3FsOi8vbXlzcWwud2F0ZXIuaW86MzMwNi93YXRlcj91c2VTU0w9ZmFsc2UmYWxsb3dNdWx0aVF1ZXJpZXM9dHJ1ZSZ1c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmOCZhdXRvUmVjb25uZWN0PXRydWUmcmV3cml0ZUJhdGNoZWRTdGF0ZW1lbnRzPXRydWUKcGFzc3dvcmQ9S0hlODVFNE1ZZGVMQkhTUgp1c2VybmFtZT1kZW1vCmpkYmNVcmw9JHt1cmx9', 'properties', 1, 1, '2021-04-07 11:28:03');
INSERT INTO `water_cfg_properties` VALUES (29, 'water', 'water_log', 10, '#Data#: c2NoZW1hPXdhdGVyX2xvZwp1cmw9amRiYzpteXNxbDovL215c3FsLndhdGVyLmlvOjMzMDYvd2F0ZXJfbG9nP3VzZVNTTD1mYWxzZSZhbGxvd011bHRpUXVlcmllcz10cnVlJnVzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGY4JmF1dG9SZWNvbm5lY3Q9dHJ1ZSZyZXdyaXRlQmF0Y2hlZFN0YXRlbWVudHM9dHJ1ZQpwYXNzd29yZD1LSGU4NUU0TVlkZUxCSFNSCnVzZXJuYW1lPWRlbW8KamRiY1VybD0ke3VybH0=', 'properties', 1, 1, '2021-04-07 11:28:19');
INSERT INTO `water_cfg_properties` VALUES (39, 'water', 'water_redis', 11, '#Data#: c2VydmVyPXJlZGlzLndhdGVyLmlvOjYzNzkKcGFzc3dvcmQ9TDh2Y1FnZXAKdXNlcj1yZWRpcw==', 'properties', 1, 1, '2021-04-07 11:29:34');
INSERT INTO `water_cfg_properties` VALUES (44, 'water', 'alarm_sign', 0, '#Data#: 5ryU56S6546v5aKD', 'text', 1, 1, '2021-04-07 11:27:57');
INSERT INTO `water_cfg_properties` VALUES (53, 'water', 'water_cache', 20, '#Data#: c2VydmVyPW1lbWNhY2hlZC53YXRlci5pbzoxMTIxMQp1c2VyPW1lbWNhY2hlZA==', 'properties', 1, 1, '2021-04-07 11:28:11');
INSERT INTO `water_cfg_properties` VALUES (84, 'sponge', 'sponge_track', 10, '#Data#: c2NoZW1hPXNwb25nZV90cmFjawp1cmw9amRiYzpteXNxbDovL215c3FsLndhdGVyLmlvOjMzMDYvc3BvbmdlX3RyYWNrP3VzZVNTTD1mYWxzZSZhbGxvd011bHRpUXVlcmllcz10cnVlJnVzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGY4JmF1dG9SZWNvbm5lY3Q9dHJ1ZSZyZXdyaXRlQmF0Y2hlZFN0YXRlbWVudHM9dHJ1ZQpwYXNzd29yZD0xMjM0NTYKdXNlcm5hbWU9cm9vdApqZGJjVXJsPSR7dXJsfQ==', 'properties', 1, 1, '2021-04-07 11:38:45');
INSERT INTO `water_cfg_properties` VALUES (86, 'sponge', 'sponge_redis', 11, '#Data#: c2VydmVyPXJlZGlzLndhdGVyLmlvOjYzNzkKcGFzc3dvcmQ9MTIzNDU2CnVzZXI9cmVkaXM=', 'properties', 1, 1, '2021-04-07 11:38:27');
INSERT INTO `water_cfg_properties` VALUES (87, 'sponge', 'sponge_cache', 20, '#Data#: c2VydmVyPW1lbWNhY2hlZC53YXRlci5pbzoxMTIxMQp1c2VyPW1lbWNhY2hlZA==', 'properties', 1, 1, '2021-04-07 11:38:24');
INSERT INTO `water_cfg_properties` VALUES (167, 'water', 'paas_uri', 0, '#Data#: aHR0cDovL3BhYXMud2F0ZXIubm9lYXIub3Jn', 'properties', 1, 1, '2021-04-07 11:31:57');
INSERT INTO `water_cfg_properties` VALUES (168, 'sponge', 'track_uri', 0, '#Data#: aHR0cDovL3RyYWNrLnNwb25nZS5pby8=', 'text', 1, 1, '2021-04-07 11:38:49');
INSERT INTO `water_cfg_properties` VALUES (184, 'water', 'water_msg_queue', 1102, '#Data#: c3RvcmUubmFtZT13YXRlcl9tc2dfcXVldWVfZGV2CnN0b3JlLnR5cGU9cmVkaXMKc2VydmVyPXJlZGlzLndhdGVyLmlvOjYzNzkKdXNlcj1yZWRpcwpwYXNzd29yZD1MOHZjUWdlcAojZm9yIHJlZGlzCmRiPTMKI2ZvciByb2NrZXRtcQp2aXJ0dWFsSG9zdD0=', 'properties', 1, 1, '2021-04-07 11:32:07');
INSERT INTO `water_cfg_properties` VALUES (186, '_demo', 'aliyun_oss', 0, '#Data#: dXJsPWh0dHA6Ly9vc3MtY24tYi1lLmFsaXl1bmNzLmNvbQplbmRwb2ludD1odHRwOi8vb3NzLWNuLWMtaW50ZXJuYWwuZC5jb20KYWNjZXNzS2V5SWQ9eAphY2Nlc3NTZWNyZXQ9eA==', 'properties', 1, 1, '2021-04-07 11:30:23');
INSERT INTO `water_cfg_properties` VALUES (193, 'water', 'paas_cache_header', 0, '#Data#: UEFBU19BUElfQ0FDSEVfREVW', 'properties', 1, 1, '2021-04-07 11:27:48');
INSERT INTO `water_cfg_properties` VALUES (322, 'water', 'water_cache_header', 0, '#Data#: V0FURVIyX0FQSV9ERVZfQ0FDSEU=', 'properties', 1, 1, '2021-04-07 11:28:15');
INSERT INTO `water_cfg_properties` VALUES (324, 'water', 'raas_uri', 0, '#Data#: aHR0cDovL3JhYXMud2F0ZXIubm9lYXIub3Jn', 'text', 1, 1, '2021-04-07 11:32:00');
INSERT INTO `water_cfg_properties` VALUES (369, 'water', 'enable_tag_checker', 0, '#Data#: MA==', 'properties', 1, 1, '2021-04-07 11:27:43');
INSERT INTO `water_cfg_properties` VALUES (370, 'water', 'water_paas', 10, '#Data#: c2NoZW1hPXdhdGVyCnVybD1qZGJjOm15c3FsOi8vbXlzcWwud2F0ZXIuaW86MzMwNi93YXRlcj91c2VTU0w9ZmFsc2UmYWxsb3dNdWx0aVF1ZXJpZXM9dHJ1ZSZ1c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmOCZhdXRvUmVjb25uZWN0PXRydWUmcmV3cml0ZUJhdGNoZWRTdGF0ZW1lbnRzPXRydWUKcGFzc3dvcmQ9S0hlODVFNE1ZZGVMQkhTUgp1c2VybmFtZT1kZW1vCmpkYmNVcmw9JHt1cmx9', 'properties', 1, 1, '2021-04-07 11:29:38');
INSERT INTO `water_cfg_properties` VALUES (623, '_gateway', 'rockrpc', 9, '#Data#: eyJ1cmwiOiIiLCJzZXJ2aWNlIjoicm9ja3JwYyIsInBvbGljeSI6InBvbGxpbmcifQ==', 'properties', 1, 1, '2021-04-07 11:42:22');
INSERT INTO `water_cfg_properties` VALUES (771, '_demo', 'elasticsearch', 13, '#Data#: dXJsPWh0dHA6Ly9lcy1jbi14LnB1YmxpYy5kLmFsaXl1bmNzLmNvbTo5MjAwCnVzZXJuYW1lPXgKcGFzc3dvcmQ9eA==', 'properties', 1, 1, '2021-04-07 11:30:31');
INSERT INTO `water_cfg_properties` VALUES (772, '_system', 'config_type', 0, '#Data#: MD0KOT1nYXRld2F5CjEwPXJkYgoxMT1yZWRpcwoxMj1tb25nb2RiCjEzPWVsYXN0aWNzZWFyY2gKMTQ9aGJhc2UKMjA9bWVtY2FjaGVkCjEwMDI9c3NoLmtleQoxMDAzPWlhYXMucmFtCjExMDE9d2F0ZXIubG9nZ2VyCjExMDI9d2F0ZXIucXVldWUKMTEwMz13YXRlci5ibG9jaw==', 'properties', 0, 1, '2021-04-07 11:39:55');
INSERT INTO `water_cfg_properties` VALUES (773, '_system', 'env_type', 0, '#Data#: MD3mtYvor5Xnjq/looMKMT3pooTnlJ/kuqfnjq/looMKMj3nlJ/kuqfnjq/looM=', 'properties', 0, 1, '2021-04-07 11:39:58');
INSERT INTO `water_cfg_properties` VALUES (774, '_system', 'iaas_type', 0, '#Data#: MD1FQ1MKMT1MQlMKMj1SRFMKMz1SZWRpcwo0PU1lbWNhY2hlZAo1PURSRFMKNj1FQ0kKNz1OQVMKOD1Qb2xhckRCCjk9TW9uZ29EQg==', 'properties', 0, 1, '2021-04-07 11:40:20');
INSERT INTO `water_cfg_properties` VALUES (778, 'sponge', 'sponge_rock', 10, '#Data#: c2NoZW1hPXNwb25nZV9yb2NrCnVybD1qZGJjOm15c3FsOi8vbXlzcWwud2F0ZXIuaW86MzMwNi9zcG9uZ2Vfcm9jaz91c2VTU0w9ZmFsc2UmYWxsb3dNdWx0aVF1ZXJpZXM9dHJ1ZSZ1c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmOCZhdXRvUmVjb25uZWN0PXRydWUmcmV3cml0ZUJhdGNoZWRTdGF0ZW1lbnRzPXRydWUKcGFzc3dvcmQ9MTIzNDU2CnVzZXJuYW1lPXJvb3QKamRiY1VybD0ke3VybH0=', 'properties', 0, 1, '2021-04-07 11:38:30');
INSERT INTO `water_cfg_properties` VALUES (784, '_demo', 'test', 10, '#Data#: c2NoZW1hPXRlc3QKdXJsPWpkYmM6bXlzcWw6Ly9teXNxbC53YXRlci5pbzozMzA2L3Rlc3Q/dXNlU1NMPWZhbHNlJmFsbG93TXVsdGlRdWVyaWVzPXRydWUmdXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0ZjgmYXV0b1JlY29ubmVjdD10cnVlJnJld3JpdGVCYXRjaGVkU3RhdGVtZW50cz10cnVlCnBhc3N3b3JkPXgKdXNlcm5hbWU9eA==', 'properties', 0, 1, '2021-04-07 11:30:53');
INSERT INTO `water_cfg_properties` VALUES (789, '_system', 'iaas_region', 0, '#Data#: Y24tc2hhbmdoYWk9Y24tc2hhbmdoYWkKY24taGFuZ3pob3U9Y24taGFuZ3pob3U=', 'properties', 0, 1, '2021-04-07 11:40:02');
INSERT INTO `water_cfg_properties` VALUES (887, '_demo', 'test_yml', 0, '#Data#: c2VydmVyOgogIHBvcnQ6IDkwMDEKYXBwOgogIGlkOiBzcGVlY2gKa25vd2xlZGdlOgogIGluaXQ6CiAgICBrbm93bGVkZ2VUaXRsZXM6CiAgICAgIC0ga2RUaXRsZTog5ZCs5LiN5riFCiAgICAgICAga2V5V29yZHM6ICJb5L2g6K+05LuA5LmI77yM5rKh5ZCs5riF77yM5ZCs5LiN5riF5qWa77yM5YaN6K+05LiA6YGNXSIKICAgICAgICBxdWVzdGlvbjogIlvmsqHlkKzmh4LvvIzlkKzkuI3muIXmpZpdIgogICAgICAtIGtkVGl0bGU6IOaXoOW6lOetlAogICAgICAgIGtkSW5mb3M6CiAgICAgICAgICAtIOS9oOWlvQogICAgICAgICAgLSBoZWxsbwogICAgICAgICAgLSBoaQ==', 'yaml', 0, 1, '2021-04-07 11:31:10');
INSERT INTO `water_cfg_properties` VALUES (888, '_demo', 'test_json', 0, '#Data#: ewogICAgInNlcnZlciI6ewogICAgICAgICJwb3J0Ijo5MDAxCiAgICB9LAogICAgImFwcCI6ewogICAgICAgICJpZCI6InNwZWVjaCIKICAgIH0sCiAgICAia25vd2xlZGdlIjp7CiAgICAgICAgImluaXQiOnsKICAgICAgICAgICAgImtub3dsZWRnZVRpdGxlcyI6WwogICAgICAgICAgICAgICAgewogICAgICAgICAgICAgICAgICAgICJrZFRpdGxlIjoi5ZCs5LiN5riFIiwKICAgICAgICAgICAgICAgICAgICAia2V5V29yZHMiOiJb5L2g6K+05LuA5LmI77yM5rKh5ZCs5riF77yM5ZCs5LiN5riF5qWa77yM5YaN6K+05LiA6YGNXSIsCiAgICAgICAgICAgICAgICAgICAgInF1ZXN0aW9uIjoiW+ayoeWQrOaHgu+8jOWQrOS4jea4healml0iCiAgICAgICAgICAgICAgICB9LHsKICAgICAgICAgICAgICAgICAgICAia2RUaXRsZSI6IuaXoOW6lOetlCIsCiAgICAgICAgICAgICAgICAgICAgImtkSW5mb3MiOlsi5L2g5aW9IiwiaGVsbG8iLCJoaSJdCiAgICAgICAgICAgICAgICB9CiAgICAgICAgICAgIF0KICAgICAgICB9CiAgICB9Cn0=', 'json', 0, 1, '2021-04-07 11:31:01');
INSERT INTO `water_cfg_properties` VALUES (889, '_demo', 'test_props', 0, '#Data#: c2VydmVyLnBvcnQ9OTAwMQphcHAuaWQ9c3BlZWNoCmtub3dsZWRnZS5pbml0Lmtub3dsZWRnZVRpdGxlc1swXS5rZFRpdGxlPeWQrOS4jea4hQprbm93bGVkZ2UuaW5pdC5rbm93bGVkZ2VUaXRsZXNbMF0ua2V5V29yZHM9W+S9oOivtOS7gOS5iO+8jOayoeWQrOa4he+8jOWQrOS4jea4healmu+8jOWGjeivtOS4gOmBjV0Ka25vd2xlZGdlLmluaXQua25vd2xlZGdlVGl0bGVzWzBdLnF1ZXN0aW9uPVvmsqHlkKzmh4LvvIzlkKzkuI3muIXmpZpdCmtub3dsZWRnZS5pbml0Lmtub3dsZWRnZVRpdGxlc1sxXS5rZFRpdGxlPeaXoOW6lOetlAprbm93bGVkZ2UuaW5pdC5rbm93bGVkZ2VUaXRsZXNbMV0ua2RJbmZvc1swXT3kvaDlpb0Ka25vd2xlZGdlLmluaXQua25vd2xlZGdlVGl0bGVzWzFdLmtkSW5mb3NbMV09aGVsbG8Ka25vd2xlZGdlLmluaXQua25vd2xlZGdlVGl0bGVzWzFdLmtkSW5mb3NbMl09aGk=', 'properties', 0, 1, '2021-04-07 11:31:06');
INSERT INTO `water_cfg_properties` VALUES (910, 'water', 'water_log_level', 0, '#Data#: Mw==', 'properties', 0, 1, '2021-04-07 11:32:17');
INSERT INTO `water_cfg_properties` VALUES (913, '_gateway', 'waterapi', 9, '#Data#: eyJ1cmwiOiJodHRwOi8vd2F0ZXIiLCJzZXJ2aWNlIjoid2F0ZXJhcGkiLCJwb2xpY3kiOiJwb2xsaW5nIn0=', 'properties', 0, 1, '2021-04-07 11:42:25');
INSERT INTO `water_cfg_properties` VALUES (926, '_demo', 'test_txt', 0, '#Data#: MTIzNDU=', 'text', 0, 1, '2021-04-07 11:38:19');
INSERT INTO `water_cfg_properties` VALUES (963, 'demo', 'test_db', 10, '#Data#: c2NoZW1hPXdhdGVyX2xvZwp1cmw9amRiYzpteXNxbDovL215c3FsLndhdGVyLmlvOjMzMDYvd2F0ZXJfbG9nP3VzZVNTTD1mYWxzZSZhbGxvd011bHRpUXVlcmllcz10cnVlJnVzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGY4JmF1dG9SZWNvbm5lY3Q9dHJ1ZSZyZXdyaXRlQmF0Y2hlZFN0YXRlbWVudHM9dHJ1ZQpwYXNzd29yZD1hQnkzZFA1eHNFNEV0RHJhCnVzZXJuYW1lPXdhdGVyX2xvZwpqZGJjVXJsPSR7dXJsfQ==', 'properties', 0, 1, '2021-04-07 11:52:47');
INSERT INTO `water_cfg_properties` VALUES (964, 'demo', 'water_cache_header', 0, '#Data#: YmJi', 'properties', 0, 1, '2021-04-07 11:52:52');
INSERT INTO `water_cfg_properties` VALUES (965, 'demo', 'test.properties', 0, '#Data#: ZGIxLnVybD1qZGJjCmRiMS51c2VybmFtZT1ub2VhcgpkYjEucGFzc3dvcmQ9eHh4CmRiMS5qZGJjVXJsPSR7ZGIxLnVybH0=', 'properties', 0, 1, '2021-04-07 11:52:49');
INSERT INTO `water_cfg_properties` VALUES (969, 'water', 'water_log_store', 1101, '#Data#: c3RvcmUudHlwZT1yZGIKc2NoZW1hPXdhdGVyX2xvZwp1cmw9amRiYzpteXNxbDovL215c3FsLndhdGVyLmlvOjMzMDYvd2F0ZXJfbG9nP3VzZVNTTD1mYWxzZSZhbGxvd011bHRpUXVlcmllcz10cnVlJnVzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGY4JmF1dG9SZWNvbm5lY3Q9dHJ1ZSZyZXdyaXRlQmF0Y2hlZFN0YXRlbWVudHM9dHJ1ZQpwYXNzd29yZD1LSGU4NUU0TVlkZUxCSFNSCnVzZXJuYW1lPWRlbW8KamRiY1VybD0ke3VybH0=', 'properties', 0, 1, '2021-04-07 11:32:25');
INSERT INTO `water_cfg_properties` VALUES (970, 'water', 'water_msg_store', 1103, '#Data#: c3RvcmUudHlwZT1yZGIKc2NoZW1hPXdhdGVyCnVybD1qZGJjOm15c3FsOi8vbXlzcWwud2F0ZXIuaW86MzMwNi93YXRlcj91c2VTU0w9ZmFsc2UmYWxsb3dNdWx0aVF1ZXJpZXM9dHJ1ZSZ1c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmOCZhdXRvUmVjb25uZWN0PXRydWUmcmV3cml0ZUJhdGNoZWRTdGF0ZW1lbnRzPXRydWUKcGFzc3dvcmQ9S0hlODVFNE1ZZGVMQkhTUgp1c2VybmFtZT1kZW1vCmpkYmNVcmw9JHt1cmx9', 'properties', 0, 1, '2021-04-07 11:29:42');
INSERT INTO `water_cfg_properties` VALUES (978, 'water_bcf', 'bcf.yml', 0, '#Data#: YmNmLmNhY2hlOgogIHNlcnZlcjogIm1lbWNhY2hlZC53YXRlci5pbzoxMTIxMSIKICB1c2VyOiAibWVtY2FjaGVkIgogIApiY2YuZGI6CiAgc2NoZW1hOiB3YXRlcgogIHVybDogImpkYmM6bXlzcWw6Ly9teXNxbC53YXRlci5pbzozMzA2L3dhdGVyP3VzZVNTTD1mYWxzZSZhbGxvd011bHRpUXVlcmllcz10cnVlJnVzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGY4JmF1dG9SZWNvbm5lY3Q9dHJ1ZSZyZXdyaXRlQmF0Y2hlZFN0YXRlbWVudHM9dHJ1ZSIKICBwYXNzd29yZDogIktIZTg1RTRNWWRlTEJIU1IiCiAgdXNlcm5hbWU6ICJkZW1vIgoKc2VydmVyLnNlc3Npb246CiAgc3RhdGUuZG9tYWluOiAid2F0ZXIubm9lYXIub3JnIgogIHRpbWVvdXQ6IDcyMDA=', 'yaml', 0, 1, '2021-04-07 11:32:38');
INSERT INTO `water_cfg_properties` VALUES (980, 'water_bcf', 'bcfadmin.yml', 0, '#Data#: YmNmYWRtaW4ucGFzc3dvcmQ6ICJTeWtTWUxXTjlXVHB6Q0hxIg==', 'yaml', 0, 1, '2021-04-07 11:32:41');
INSERT INTO `water_cfg_properties` VALUES (982, 'demo', 'test', 0, '#Data#: dGVzdA==', 'properties', 0, 1, '2021-04-07 11:52:44');


-- ----------------------------
-- Table structure for water_cfg_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `water_cfg_whitelist`;
CREATE TABLE `water_cfg_whitelist`  (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ip' COMMENT '名单类型',
  `value` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '值',
  `note` varchar(99) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `is_enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `update_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`tag`, `type`, `value`) USING BTREE,
  INDEX `IX_val`(`value`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-配置-安全名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_cfg_whitelist
-- ----------------------------
INSERT INTO `water_cfg_whitelist` VALUES (129, 'server', 'ip', '127.0.0.1', NULL, 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (131, 'server', 'ip', '0:0:0:0:0:0:0:1', '本地调试', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (162, 'client', 'ip', '0:0:0:0:0:0:0:1', '本机', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (165, 'master', 'ip', '0:0:0:0:0:0:0:1', '自己', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (168, '_alarm', 'mobile', '18658857337', '谢**', 0, '2020-08-27 22:46:37');
INSERT INTO `water_cfg_whitelist` VALUES (172, 'server', 'ip', '172.168.0.162', '压测1', 1, '2021-01-29 15:59:48');
INSERT INTO `water_cfg_whitelist` VALUES (173, 'master', 'ip', '172.168.0.164', NULL, 1, '2021-01-29 15:59:16');
INSERT INTO `water_cfg_whitelist` VALUES (174, 'server', 'ip', '172.168.0.163', '压测2', 1, '2021-01-29 15:59:58');
INSERT INTO `water_cfg_whitelist` VALUES (175, 'master', 'ip', '172.168.0.165', NULL, 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (176, 'server', 'ip', '172.168.0.164', '压测3', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (177, 'server', 'ip', '172.168.0.165', '压测4', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (178, 'server', 'ip', '172.168.0.166', '压测5', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (179, 'server', 'ip', '172.168.0.167', '压测6', 1, NULL);
INSERT INTO `water_cfg_whitelist` VALUES (180, '_alarm', 'ip', '2824262699', NULL, 1, NULL);

-- ----------------------------
-- Table structure for water_ops_project
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_project`;
CREATE TABLE `water_ops_project`  (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分类标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '项目名称',
  `type` int NOT NULL DEFAULT 0 COMMENT '0服务；1网站',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '项目描述',
  `developer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开发人',
  `git_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_enabled` int NULL DEFAULT 1 COMMENT '是否启用  0:禁用  1:启用',
  PRIMARY KEY (`project_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_project
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server`;
CREATE TABLE `water_ops_server`  (
  `server_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务器名称',
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础设施服务KEY',
  `iaas_type` int NOT NULL DEFAULT 0 COMMENT '基础设施类型（0ECS, 1BLS, 2RDS, 3Redis, 4Memcached,5DRDS）',
  `iaas_attrs` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '特性',
  `iaas_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础设施账号',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '外网地址（IP或域）',
  `address_local` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '内网地址（IP或域）',
  `hosts_local` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '本地域名映射',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `env_type` int NOT NULL DEFAULT 0 COMMENT '0测试环境；1预生产环境；2生产环境；',
  `sev_num` int NOT NULL DEFAULT 0,
  `is_enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`server_id`) USING BTREE,
  INDEX `IX_key`(`iaas_key`) USING BTREE,
  INDEX `IX_iaas_account`(`iaas_account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_bls
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_bls`;
CREATE TABLE `water_ops_server_track_bls`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `co_conect_num` bigint NOT NULL DEFAULT 0,
  `new_conect_num` bigint NOT NULL DEFAULT 0,
  `qps` bigint NOT NULL DEFAULT 0,
  `traffic_tx` bigint NOT NULL DEFAULT 0,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-BLS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_bls
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_dbs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_dbs`;
CREATE TABLE `water_ops_server_track_dbs`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `connect_usage` double(18, 2) NULL DEFAULT NULL COMMENT '连接数使用率',
  `cpu_usage` double(18, 2) NULL DEFAULT NULL COMMENT 'cpu使用率',
  `memory_usage` double(18, 2) NULL DEFAULT NULL COMMENT '内存使用率',
  `disk_usage` double(18, 2) NULL DEFAULT NULL COMMENT '硬盘使用率',
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-DBS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_dbs
-- ----------------------------

-- ----------------------------
-- Table structure for water_ops_server_track_ecs
-- ----------------------------
DROP TABLE IF EXISTS `water_ops_server_track_ecs`;
CREATE TABLE `water_ops_server_track_ecs`  (
  `iaas_key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cpu_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `memory_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `disk_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `broadband_usage` double(18, 2) NOT NULL DEFAULT 0.00,
  `tcp_num` bigint NOT NULL,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`iaas_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-运维-服务器跟踪-ECS' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_ops_server_track_ecs
-- ----------------------------


-- ----------------------------
-- Table structure for water_reg_consumer
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_consumer`;
CREATE TABLE `water_reg_consumer`  (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `service` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务',
  `consumer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者',
  `consumer_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者地址',
  `consumer_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消费者远程IP',
  `is_unstable` int(11) NOT NULL DEFAULT 0 COMMENT '是否为不稳定的',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '是否为已启用',
  `chk_last_state` int(11) NOT NULL DEFAULT 0 COMMENT '最后检查状态（0：OK；1：error）',
  `chk_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后检查时间',
  `log_fulltime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `consumer_address`) USING BTREE,
  INDEX `IX_consumer_address`(`consumer_address`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 307 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务消费者表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for water_reg_service
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service`;
CREATE TABLE `water_reg_service`  (
  `service_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'md5(name+‘#’+address)',
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ver` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版本号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `meta` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '源信息',
  `note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `alarm_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `state` int(11) NOT NULL DEFAULT 0 COMMENT '0:待检查；1检查中',
  `code_location` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `check_type` int(11) NOT NULL DEFAULT 0 COMMENT '检查方式（0被检查；1自己签到）',
  `check_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '状态检查地址',
  `check_last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后检查时间',
  `check_last_state` int(11) NOT NULL DEFAULT 0 COMMENT '最后检查状态（0：OK；1：error）',
  `check_last_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后检查描述',
  `check_error_num` int(11) NOT NULL DEFAULT 0 COMMENT '检测异常数量',
  `is_unstable` int(11) NOT NULL DEFAULT 0 COMMENT '是否为不稳定的',
  `is_enabled` int(11) NOT NULL DEFAULT 1 COMMENT '是否为已启用',
  PRIMARY KEY (`service_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_address`(`address`(100)) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-注册-服务者表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for water_reg_service_runtime
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_runtime`;
CREATE TABLE `water_reg_service_runtime`  (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `memory_max` int(11) NOT NULL DEFAULT 0,
  `memory_total` int(11) NOT NULL DEFAULT 0,
  `memory_used` int(11) NOT NULL DEFAULT 0,
  `thread_peak_count` int(11) NOT NULL DEFAULT 0,
  `thread_count` int(11) NOT NULL DEFAULT 0,
  `thread_daemon_count` int(11) NOT NULL DEFAULT 0,
  `log_date` int(11) NOT NULL DEFAULT 0,
  `log_hour` int(11) NOT NULL DEFAULT 0,
  `log_minute` int(11) NOT NULL DEFAULT 0,
  `log_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_address`(`address`(40)) USING BTREE,
  INDEX `IX_key_times`(`key`, `log_date`, `log_hour`, `log_minute`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for water_reg_service_speed
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed`;
CREATE TABLE `water_reg_service_speed`  (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `average` bigint(20) NOT NULL DEFAULT 0,
  `average_ref` bigint(20) NOT NULL DEFAULT 0 COMMENT '参考响应时间值 ',
  `fastest` bigint(20) NOT NULL DEFAULT 0,
  `slowest` bigint(20) NOT NULL DEFAULT 0,
  `total_num` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow1` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow2` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow5` bigint(20) NOT NULL DEFAULT 0,
  `last_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name_md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for water_reg_service_speed_date
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_date`;
CREATE TABLE `water_reg_service_speed_date`  (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `average` bigint(20) NOT NULL DEFAULT 0,
  `fastest` bigint(20) NOT NULL DEFAULT 0,
  `slowest` bigint(20) NOT NULL DEFAULT 0,
  `total_num` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow1` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow2` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow5` bigint(20) NOT NULL DEFAULT 0,
  `log_date` int(11) NOT NULL DEFAULT 0 COMMENT '记录时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name_md5`, `log_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表-按日' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for water_reg_service_speed_hour
-- ----------------------------
DROP TABLE IF EXISTS `water_reg_service_speed_hour`;
CREATE TABLE `water_reg_service_speed_hour`  (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `average` bigint(20) NOT NULL DEFAULT 0,
  `fastest` bigint(20) NOT NULL DEFAULT 0,
  `slowest` bigint(20) NOT NULL DEFAULT 0,
  `total_num` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow1` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow2` bigint(20) NOT NULL DEFAULT 0,
  `total_num_slow5` bigint(20) NOT NULL DEFAULT 0,
  `log_date` int(11) NOT NULL DEFAULT 0 COMMENT '记录日期',
  `log_hour` int(11) NOT NULL DEFAULT 0 COMMENT '记录小时',
  PRIMARY KEY (`row_id`) USING BTREE,
  UNIQUE INDEX `IX_key`(`service`, `tag`, `name_md5`, `log_date`, `log_hour`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'WATER-注册-服务性能记录表-按时' ROW_FORMAT = Compact;



-- ----------------------------
-- Table structure for water_tool_monitor
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_monitor`;
CREATE TABLE `water_tool_monitor`  (
  `monitor_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '监视项目key',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '监视项目名称',
  `type` int NOT NULL DEFAULT 0 COMMENT '监视类型（0:数据预警；1:数据简报）',
  `source_query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源模型脚本',
  `rule` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则（输入m:{d:{},tag:\'\'}）',
  `task_tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '监视标签',
  `task_tag_exp` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '监视标签产生的表达式',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警手机号（多个以,隔开）',
  `alarm_exp` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警信息产生的表达式',
  `alarm_count` int NOT NULL DEFAULT 0 COMMENT '报警次数',
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警签名',
  `is_enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`monitor_id`) USING BTREE,
  INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-监视登记表（监视数据）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_monitor
-- ----------------------------
INSERT INTO `water_tool_monitor` VALUES (102, 'water', '7024675c5ee94dadafa97d0dfa71af3c', '消息派发情况', 0, '--water/water_msg::\nselect count(*) num \nfrom `water_msg_message` \nwhere state=0 and dist_count=0 and dist_nexttime=0 ', 'return m.d[0].num>10;', '0', '{{m.d[0].num}}', '', '未派发消息数::{{m.d[0].num}}', 0, '', 1);
INSERT INTO `water_tool_monitor` VALUES (117, 'water', '8e1c5f94f7aa4a1589d3ab82b5c1b36e', '服务监视情况', 0, '--water/water::\nselect count(*) num \nfrom `water_reg_service` \nwhere is_enabled=1 \nAND (check_last_state=1 OR check_last_time < SUBTIME(NOW(),\'0:1:0\'))', 'return m.d[0].num>0;', '0', '{{m.d[0].num}}', '', '有{{m.d[0].num}}个服务异常（或服务监测出错）', 0, '', 1);
INSERT INTO `water_tool_monitor` VALUES (118, 'water', '337c0242fca44f6fa0ce19f9b3dec027', '定时任务监视', 0, '--water/water_paas::\nselect count(*) num \nfrom paas_file \nwhere plan_state = 8 and is_disabled=0', 'return m.d[0].num !== 0;', '0', '{{m.d[0].num}}', '', '定时任务异常数量监视::{{m.d[0].num}}', 0, '定时任务', 1);


-- ----------------------------
-- Table structure for water_tool_report
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_report`;
CREATE TABLE `water_tool_report`  (
  `row_id` int NOT NULL AUTO_INCREMENT COMMENT '简报ID',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类标签（外部根据标签查询）',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '查询名称',
  `args` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数变量',
  `code` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '查询代码',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_fulltime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`row_id`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-数据简报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_report
-- ----------------------------
INSERT INTO `water_tool_report` VALUES (1, '_demo', 'water_cfg_logger', '', '--water/water::\nselect * from water_cfg_logger limit 10', '', '2021-01-07 17:23:02');

-- ----------------------------
-- Table structure for water_tool_synchronous
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_synchronous`;
CREATE TABLE `water_tool_synchronous`  (
  `sync_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `key` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` int NOT NULL DEFAULT 0 COMMENT '0,增量同步；1,更新同步；',
  `interval` int NOT NULL DEFAULT 0 COMMENT '间隔时间（秒）',
  `target` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `target_pk` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `source_model` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源模型',
  `task_tag` bigint NOT NULL DEFAULT 0 COMMENT '同步标识（用于临时存数据）',
  `alarm_mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `alarm_sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_enabled` int NOT NULL DEFAULT 0,
  `last_fulltime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`sync_id`) USING BTREE,
  INDEX `IX_key`(`key`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-同步配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_tool_synchronous
-- ----------------------------
INSERT INTO `water_tool_synchronous` VALUES (1, '_demo', '530d056e4018471fa5292065dbc549e2', '同步APPX表', 0, 100, '_demo/test::appx', 'app_id', '--_demo/rock::\nselect * from `appx` where app_id>@key order by app_id asc', 0, '', NULL, 0, '2020-05-27 15:44:26');


-- ----------------------------
-- Table structure for water_tool_versions
-- ----------------------------
DROP TABLE IF EXISTS `water_tool_versions`;
CREATE TABLE `water_tool_versions`  (
  `commit_id` int NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `table` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表名',
  `key_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段名',
  `key_value` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段值',
  `is_hand` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为手工备份',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备份数据',
  `data_md5` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备份数据md5',
  `log_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '记录人',
  `log_ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '记录IP',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期：yyyyMMdd',
  `log_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录完整时间',
  PRIMARY KEY (`commit_id`) USING BTREE,
  INDEX `IX_table_key`(`table`, `key_name`, `key_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-工具-数据版本存储表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for x_water_log_tml
-- ----------------------------
DROP TABLE IF EXISTS `x_water_log_tml`;
CREATE TABLE `x_water_log_tml`  (
  `log_id` bigint NOT NULL COMMENT '日志ID',
  `level` int NOT NULL DEFAULT 0 COMMENT '等级',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签',
  `tag1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签１',
  `tag2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签２',
  `tag3` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签３',
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `from` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `log_date` int NOT NULL DEFAULT 0 COMMENT '记录日期',
  `log_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `IX_date`(`log_date`) USING BTREE,
  INDEX `IX_tag`(`tag`) USING BTREE,
  INDEX `IX_tag1`(`tag1`) USING BTREE,
  INDEX `IX_tag2`(`tag2`) USING BTREE,
  INDEX `IX_tag3`(`tag3`) USING BTREE,
  INDEX `IX_log_fulltime`(`log_fulltime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'WATER-日志表模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of x_water_log_tml
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
