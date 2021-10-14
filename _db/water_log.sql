SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rubber_log_request
-- ----------------------------
DROP TABLE IF EXISTS `rubber_log_request`;
CREATE TABLE `rubber_log_request` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int(11) NOT NULL DEFAULT '0' COMMENT '处理策略',
  `args_json` varchar(999) COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text COLLATE utf8mb4_general_ci COMMENT '评估报告',
  `session_json` varchar(999) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint(20) NOT NULL DEFAULT '0' COMMENT '处理时间（ms）',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `log_date` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_log_date` (`log_date`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='RAAS-日志-请求记录表';

-- ----------------------------
-- Table structure for rubber_log_request_all
-- ----------------------------
DROP TABLE IF EXISTS `rubber_log_request_all`;
CREATE TABLE `rubber_log_request_all` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `request_id` varchar(40) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求ID',
  `scheme_tagname` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方案',
  `policy` int(11) NOT NULL DEFAULT '0' COMMENT '处理策略',
  `args_json` varchar(999) COLLATE utf8mb4_general_ci NOT NULL COMMENT '输入参数',
  `model_json` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据模型',
  `matcher_json` varchar(999) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '匹配报告',
  `evaluation_json` text COLLATE utf8mb4_general_ci COMMENT '评估报告',
  `session_json` varchar(999) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会话',
  `note_json` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '摘要',
  `start_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `start_date` int(11) NOT NULL DEFAULT '0' COMMENT '开始日期',
  `end_fulltime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `timespan` bigint(20) NOT NULL DEFAULT '0' COMMENT '处理时间（ms）',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0:刚记录；1:计算中；2:已计算）',
  `callback` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `log_date` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_log_date` (`log_date`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='RAAS-日志-请求记录表';

-- ----------------------------
-- Table structure for sponge_log_admin
-- ----------------------------
DROP TABLE IF EXISTS `sponge_log_admin`;
CREATE TABLE `sponge_log_admin` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for sponge_log_rock
-- ----------------------------
DROP TABLE IF EXISTS `sponge_log_rock`;
CREATE TABLE `sponge_log_rock` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_exam_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_bcf`;
CREATE TABLE `water_exam_log_bcf` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `service` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `schema` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int(11) NOT NULL DEFAULT '0' COMMENT '秒数',
  `interval` bigint(20) NOT NULL DEFAULT '0' COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代码',
  `cmd_arg` text COLLATE utf8mb4_general_ci COMMENT '参数',
  `operator` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  `log_hour` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_service` (`service`) USING BTREE,
  KEY `IX_operator` (`operator`) USING BTREE,
  KEY `IX_schema` (`schema`) USING BTREE,
  KEY `IX_path` (`path`) USING BTREE,
  KEY `IX_method` (`method`) USING BTREE,
  KEY `IX_seconds` (`seconds`) USING BTREE,
  KEY `IX_cmd_sql_md5` (`cmd_sql_md5`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='WATER-日志-BCF监视';

-- ----------------------------
-- Table structure for water_exam_log_sql
-- ----------------------------
DROP TABLE IF EXISTS `water_exam_log_sql`;
CREATE TABLE `water_exam_log_sql` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `service` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `schema` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据库',
  `method` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '动作(SELSEC,UPDATE,INSERT,DELETE)',
  `seconds` int(11) NOT NULL DEFAULT '0' COMMENT '秒数',
  `interval` bigint(20) NOT NULL DEFAULT '0' COMMENT '毫秒数',
  `cmd_sql_md5` varchar(40) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `cmd_sql` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代码',
  `cmd_arg` text COLLATE utf8mb4_general_ci COMMENT '参数',
  `operator` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人IP',
  `path` varchar(150) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `ua` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'UA',
  `note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  `log_hour` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_service` (`service`) USING BTREE,
  KEY `IX_operator` (`operator`) USING BTREE,
  KEY `IX_schema` (`schema`) USING BTREE,
  KEY `IX_path` (`path`) USING BTREE,
  KEY `IX_method` (`method`) USING BTREE,
  KEY `IX_seconds` (`seconds`) USING BTREE,
  KEY `IX_cmd_sql_md5` (`cmd_sql_md5`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='WATER-日志-SQL监视';

-- ----------------------------
-- Table structure for water_log_admin
-- ----------------------------
DROP TABLE IF EXISTS `water_log_admin`;
CREATE TABLE `water_log_admin` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_api
-- ----------------------------
DROP TABLE IF EXISTS `water_log_api`;
CREATE TABLE `water_log_api` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_bcf
-- ----------------------------
DROP TABLE IF EXISTS `water_log_bcf`;
CREATE TABLE `water_log_bcf` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_etl
-- ----------------------------
DROP TABLE IF EXISTS `water_log_etl`;
CREATE TABLE `water_log_etl` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_heihei
-- ----------------------------
DROP TABLE IF EXISTS `water_log_heihei`;
CREATE TABLE `water_log_heihei` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_msg
-- ----------------------------
DROP TABLE IF EXISTS `water_log_msg`;
CREATE TABLE `water_log_msg` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_paas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_paas`;
CREATE TABLE `water_log_paas` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_raas
-- ----------------------------
DROP TABLE IF EXISTS `water_log_raas`;
CREATE TABLE `water_log_raas` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_sev
-- ----------------------------
DROP TABLE IF EXISTS `water_log_sev`;
CREATE TABLE `water_log_sev` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for water_log_upstream
-- ----------------------------
DROP TABLE IF EXISTS `water_log_upstream`;
CREATE TABLE `water_log_upstream` (
  `log_id` bigint(20) NOT NULL,
  `trace_id` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟踪-guid',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `tag` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `tag1` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag2` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `tag3` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `summary` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class_name` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `thread_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '线程名',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '内容',
  `from` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期（yyyyMMdd）',
  `log_fulltime` bigint(20) NOT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `IX_date` (`log_date`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE,
  KEY `IX_tag1` (`tag1`) USING BTREE,
  KEY `IX_tag2` (`tag2`) USING BTREE,
  KEY `IX_tag3` (`tag3`) USING BTREE,
  KEY `IX_log_fulltime` (`log_fulltime`) USING BTREE,
  KEY `IX_trace_id` (`trace_id`) USING BTREE,
  KEY `IX_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
