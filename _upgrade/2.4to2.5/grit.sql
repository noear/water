/*
 Navicat Premium Data Transfer

 Source Server         : @noear-mysql8
 Source Server Type    : MySQL
 Source Server Version : 50616
 Source Host           : 121.41.104.216:3306
 Source Schema         : water

 Target Server Type    : MySQL
 Target Server Version : 50616
 File Encoding         : 65001

 Date: 29/11/2021 19:20:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for grit_resource
-- ----------------------------
DROP TABLE IF EXISTS `grit_resource`;
CREATE TABLE `grit_resource`  (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '资源父ID',
  `resource_sid` bigint(20) NOT NULL DEFAULT 0 COMMENT '资源空间ID',
  `resource_type` int(11) NOT NULL DEFAULT 0 COMMENT '资源类型(0:entity, 1:group, 2:space)',
  `resource_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源代码(例，user:del)',
  `display_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) NULL DEFAULT 0 COMMENT '排序值',
  `link_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '链接地址(例，/user/add)',
  `link_target` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接目标',
  `link_tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接标签(用,隔开)',
  `icon_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '节点级别',
  `attrs` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性(kv)',
  `is_fullview` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否全屏',
  `is_visibled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可见（可见为页面，不可见为操作）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `gmt_create` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE,
  INDEX `IX_grit_resource__resource_code`(`resource_code`) USING BTREE,
  INDEX `IX_grit_resource__resource_pid`(`resource_pid`) USING BTREE,
  INDEX `IX_grit_resource__resource_sid`(`resource_sid`) USING BTREE,
  INDEX `IX_grit_resource__link_uri`(`link_uri`(100)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_resource
-- ----------------------------
INSERT INTO `grit_resource` VALUES (1, 0, 0, 2, 'spongeadmin', 'SPONGE', 0, 'http://spongeadmin', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161807169, 1638161807169);
INSERT INTO `grit_resource` VALUES (2, 0, 0, 2, 'wateradmin', 'WATER', 0, 'http://wateradmin', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161844457, 1638161844457);
INSERT INTO `grit_resource` VALUES (3, 1, 1, 1, '', '应用控制', 1, '/rock/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161871925, 1638161871925);
INSERT INTO `grit_resource` VALUES (4, 1, 1, 1, '', '价值跟踪', 2, '/track/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161889255, 1638161889255);
INSERT INTO `grit_resource` VALUES (5, 2, 2, 1, '', '日常工具', 1, '/tool/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161906547, 1638161906547);
INSERT INTO `grit_resource` VALUES (6, 2, 2, 1, '', '日志查询', 2, '/log/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161948021, 1638161948021);
INSERT INTO `grit_resource` VALUES (7, 2, 2, 1, '', '消息总线', 3, '/msg/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161964018, 1638161964018);
INSERT INTO `grit_resource` VALUES (8, 2, 2, 1, '', '服务监控', 4, '/mot/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161994147, 1638161994147);
INSERT INTO `grit_resource` VALUES (9, 2, 2, 1, '', '配置管理', 5, '/cfg/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162009746, 1638162009746);
INSERT INTO `grit_resource` VALUES (10, 2, 2, 1, '', 'FaaS', 6, '/luffy/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162025756, 1638162025756);
INSERT INTO `grit_resource` VALUES (11, 2, 2, 1, '', '规则计算', 7, '/rubber/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162040839, 1638162040839);
INSERT INTO `grit_resource` VALUES (12, 2, 2, 1, '', 'Dev', 8, '/dev/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162069561, 1638162069561);
INSERT INTO `grit_resource` VALUES (13, 2, 2, 1, '', 'Ops', 9, '/ops/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162086546, 1638162086546);
INSERT INTO `grit_resource` VALUES (14, 2, 2, 1, '', '权限', 10, '/grit/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162109384, 1638162109384);
INSERT INTO `grit_resource` VALUES (15, 5, 2, 0, '', '嘿嘿测试', 1, '/tool/heihei', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162149410, 1638183699480);
INSERT INTO `grit_resource` VALUES (16, 5, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162166447, 1638162166447);
INSERT INTO `grit_resource` VALUES (17, 5, 2, 0, '', '数据同步', 11, '/tool/sync', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162180830, 1638162180830);
INSERT INTO `grit_resource` VALUES (18, 5, 2, 0, '', '数据监视', 12, '/tool/monitor', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162198076, 1638162198076);
INSERT INTO `grit_resource` VALUES (19, 5, 2, 0, '', '数据快报', 13, '/tool/report', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162213595, 1638162222944);
INSERT INTO `grit_resource` VALUES (20, 6, 2, 0, '', '日志查询', 1, '/log/query', NULL, NULL, NULL, NULL, 0, NULL, 1, 1, 0, 1638162243215, 1638162243215);
INSERT INTO `grit_resource` VALUES (21, 7, 2, 0, '', '消息记录', 1, '/msg/list', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162281017, 1638162281017);
INSERT INTO `grit_resource` VALUES (22, 7, 2, 0, '', '消息调试', 2, '/msg/debug', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162316750, 1638162316750);
INSERT INTO `grit_resource` VALUES (23, 7, 2, 0, '', '消息发送', 3, '/msg/send', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162331209, 1638162331209);
INSERT INTO `grit_resource` VALUES (24, 7, 2, 0, '', '主题列表', 4, '/msg/topic', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162345697, 1638162345697);
INSERT INTO `grit_resource` VALUES (25, 7, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162356795, 1638162356795);
INSERT INTO `grit_resource` VALUES (26, 7, 2, 0, '', '订阅列表', 11, '/msg/subs', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162369966, 1638162369966);
INSERT INTO `grit_resource` VALUES (27, 8, 2, 0, '', '负载监控', 1, '/mot/bls', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162394193, 1638162394193);
INSERT INTO `grit_resource` VALUES (28, 8, 2, 0, '', '主机监控', 2, '/mot/ecs', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162406556, 1638162406556);
INSERT INTO `grit_resource` VALUES (29, 8, 2, 0, '', '存储监控', 3, '/mot/dbs', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162421538, 1638162421538);
INSERT INTO `grit_resource` VALUES (30, 8, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162432253, 1638162432253);
INSERT INTO `grit_resource` VALUES (31, 8, 2, 0, '', '网关监控', 11, '/mot/gw', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162448207, 1638162469457);
INSERT INTO `grit_resource` VALUES (32, 8, 2, 0, '', '服务状态', 12, '/mot/service', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162483377, 1638162483377);
INSERT INTO `grit_resource` VALUES (33, 8, 2, 0, '', '节点性能', 13, '/mot/node', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162497131, 1638162497131);
INSERT INTO `grit_resource` VALUES (34, 8, 2, 0, '', '接口性能', 14, '/mot/speed', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162513564, 1638162513564);
INSERT INTO `grit_resource` VALUES (35, 8, 2, 0, '', 'SQL性能', 15, '/mot/sql', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162527296, 1638162527296);
INSERT INTO `grit_resource` VALUES (36, 8, 2, 0, '', '$', 20, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162542872, 1638162542872);
INSERT INTO `grit_resource` VALUES (37, 8, 2, 0, '', '日志数量', 21, '/mot/log', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162558181, 1638162558181);
INSERT INTO `grit_resource` VALUES (38, 8, 2, 0, '', '行为记录', 16, '/mot/behavior', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162570801, 1638162570801);
INSERT INTO `grit_resource` VALUES (39, 8, 2, 0, '', '行为记录', 12, '/mot/behavior', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162614549, 1638162614549);
INSERT INTO `grit_resource` VALUES (40, 9, 2, 0, '', '属性配置', 1, '/cfg/prop', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162634673, 1638162634673);
INSERT INTO `grit_resource` VALUES (41, 9, 2, 0, '', '网关配置', 2, '/cfg/gateway', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162648156, 1638162648156);
INSERT INTO `grit_resource` VALUES (42, 9, 2, 0, '', '日志配置', 3, '/cfg/logger', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162663502, 1638162663502);
INSERT INTO `grit_resource` VALUES (43, 9, 2, 0, '', '消息配置', 4, '/cfg/broker', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162678256, 1638162678256);
INSERT INTO `grit_resource` VALUES (44, 9, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162689805, 1638162689805);
INSERT INTO `grit_resource` VALUES (45, 9, 2, 0, '', '安全名单', 11, '/cfg/whitelist', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162702661, 1638162702661);
INSERT INTO `grit_resource` VALUES (46, 10, 2, 0, '', '即时接口', 1, '/luffy/file/api/home', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166478687, 1638166478687);
INSERT INTO `grit_resource` VALUES (47, 10, 2, 0, '', '定时任务', 2, '/luffy/file/pln/home', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166490852, 1638166490852);
INSERT INTO `grit_resource` VALUES (48, 10, 2, 0, '', '动态事件', 4, '/luffy/file/msg/home', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166505066, 1638166505066);
INSERT INTO `grit_resource` VALUES (49, 10, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166516589, 1638166516589);
INSERT INTO `grit_resource` VALUES (50, 10, 2, 0, '', '公共模板', 11, '/luffy/file/tml/home', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166529127, 1638166529127);
INSERT INTO `grit_resource` VALUES (51, 10, 2, 0, '', '$', 20, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166536758, 1638166536758);
INSERT INTO `grit_resource` VALUES (52, 10, 2, 0, '', '代码查询', 21, '/luffy/query', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166557890, 1638166557890);
INSERT INTO `grit_resource` VALUES (53, 10, 2, 0, '', '接口手册', 22, '/luffy/help', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166569451, 1638166569451);
INSERT INTO `grit_resource` VALUES (54, 11, 2, 0, '', '数据模型', 1, '/rubber/model', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166589075, 1638166589075);
INSERT INTO `grit_resource` VALUES (55, 11, 2, 0, '', '计算方案', 2, '/rubber/scheme', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166601502, 1638166601502);
INSERT INTO `grit_resource` VALUES (56, 11, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166612858, 1638166612858);
INSERT INTO `grit_resource` VALUES (57, 11, 2, 0, '', '参与人员', 11, '/rubber/actor', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166623377, 1638166623377);
INSERT INTO `grit_resource` VALUES (58, 11, 2, 0, '', '指标仓库', 12, '/rubber/block', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166639609, 1638166639609);
INSERT INTO `grit_resource` VALUES (59, 11, 2, 0, '', '$', 20, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166651902, 1638166651902);
INSERT INTO `grit_resource` VALUES (60, 11, 2, 0, '', '请求记录', 21, '/rubber/reqrecord', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166664540, 1638166664540);
INSERT INTO `grit_resource` VALUES (61, 11, 2, 0, '', '$', 30, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166676533, 1638166676533);
INSERT INTO `grit_resource` VALUES (62, 11, 2, 0, '', '代码查询', 31, '/rubber/query', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166689388, 1638166689388);
INSERT INTO `grit_resource` VALUES (63, 12, 2, 0, '', '代码生成', 1, '/dev/code', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166779284, 1638166779284);
INSERT INTO `grit_resource` VALUES (64, 12, 2, 0, '', 'DDL查询', 2, '/dev/ddl', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166792282, 1638166792282);
INSERT INTO `grit_resource` VALUES (65, 12, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166799258, 1638166799258);
INSERT INTO `grit_resource` VALUES (66, 12, 2, 0, '', 'SqlDB查询', 11, '/dev/query_db', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166813722, 1638166813722);
INSERT INTO `grit_resource` VALUES (67, 12, 2, 0, '', 'ES查询', 12, '/dev/query_es', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166829934, 1638166829934);
INSERT INTO `grit_resource` VALUES (68, 12, 2, 0, '', 'MongoDB查询', 13, '/dev/query_mongo', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166845909, 1638166845909);
INSERT INTO `grit_resource` VALUES (69, 13, 2, 0, '', '计算资源', 1, '/ops/server', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166861837, 1638166861837);
INSERT INTO `grit_resource` VALUES (70, 13, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166876948, 1638166876948);
INSERT INTO `grit_resource` VALUES (71, 13, 2, 0, '', '项目配置', 11, '/ops/project', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166886706, 1638166886706);
INSERT INTO `grit_resource` VALUES (72, 13, 2, 0, '', '项目发布', 12, '/ops/deploy', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166900741, 1638166900741);
INSERT INTO `grit_resource` VALUES (73, 14, 2, 0, '', '资源空间', 1, '/grit/resource/space', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166935152, 1638166935152);
INSERT INTO `grit_resource` VALUES (74, 14, 2, 0, '', '资源组', 2, '/grit/resource/group', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166951268, 1638166951268);
INSERT INTO `grit_resource` VALUES (75, 14, 2, 0, '', '资源', 3, '/grit/resource/entity', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166967837, 1638166967837);
INSERT INTO `grit_resource` VALUES (76, 14, 2, 0, '', '$', 10, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166979262, 1638167032910);
INSERT INTO `grit_resource` VALUES (77, 14, 2, 0, '', '主体组', 11, '/grit/subject/group', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638166992145, 1638166992145);
INSERT INTO `grit_resource` VALUES (78, 14, 2, 0, '', '主体', 12, '/grit/subject/entity', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638167008025, 1638167008025);
INSERT INTO `grit_resource` VALUES (79, 14, 2, 0, '', '$', 20, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638167040665, 1638167040665);
INSERT INTO `grit_resource` VALUES (80, 14, 2, 0, '', '授权', 0, '/grit/auth', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638167053375, 1638167053375);
INSERT INTO `grit_resource` VALUES (81, 3, 1, 0, '', '用户组', 1, '/rock/ugroup', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181524872, 1638181524872);
INSERT INTO `grit_resource` VALUES (82, 3, 1, 0, '', '应用组', 2, '/rock/agroup', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181553441, 1638181553441);
INSERT INTO `grit_resource` VALUES (83, 3, 1, 0, '', '应用组设置', 3, '/rock/agsets', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181564813, 1638181564813);
INSERT INTO `grit_resource` VALUES (84, 3, 1, 0, '', '应用', 4, '/rock/app', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181578343, 1638181578343);
INSERT INTO `grit_resource` VALUES (85, 3, 1, 0, '', '应用设置', 5, '/rock/apsets', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181591065, 1638181591065);
INSERT INTO `grit_resource` VALUES (86, 3, 1, 0, '', '应用版本发布', 6, '/rock/apver', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181608512, 1638181608512);
INSERT INTO `grit_resource` VALUES (87, 3, 1, 0, '', '应用状态码', 7, '/rock/apcode', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181622947, 1638181622947);
INSERT INTO `grit_resource` VALUES (88, 3, 1, 0, '', '应用国际化', 8, '/rock/api18n', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181636252, 1638181636252);
INSERT INTO `grit_resource` VALUES (89, 4, 1, 0, '', '标签配置', 1, '/track/tag', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181667863, 1638181667863);
INSERT INTO `grit_resource` VALUES (90, 4, 1, 0, '', '跟踪配置', 2, '/track/url', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181679229, 1638181679229);
INSERT INTO `grit_resource` VALUES (91, 4, 1, 0, '', '数据分析', 3, '/track/stat', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638181691049, 1638181691049);

-- ----------------------------
-- Table structure for grit_resource_linked
-- ----------------------------
DROP TABLE IF EXISTS `grit_resource_linked`;
CREATE TABLE `grit_resource_linked`  (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `subject_type` int(11) NOT NULL DEFAULT 0 COMMENT '主体类型',
  `gmt_create` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE INDEX `IX_grit_resource_linked__key`(`resource_id`, `subject_id`) USING BTREE,
  INDEX `IX_grit_resource_linked__subject_id`(`subject_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-资源与主体连接表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_resource_linked
-- ----------------------------
INSERT INTO `grit_resource_linked` VALUES (1, 15, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (2, 16, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (3, 17, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (4, 18, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (5, 19, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (6, 20, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (7, 21, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (8, 22, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (9, 23, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (10, 24, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (11, 25, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (12, 26, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (13, 27, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (14, 28, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (15, 29, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (16, 30, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (17, 31, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (18, 32, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (19, 39, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (20, 33, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (21, 34, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (22, 35, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (23, 38, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (24, 36, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (25, 37, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (26, 40, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (27, 41, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (28, 42, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (29, 43, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (30, 44, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (31, 45, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (32, 46, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (33, 47, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (34, 48, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (35, 49, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (36, 50, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (37, 51, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (38, 52, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (39, 53, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (40, 54, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (41, 55, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (42, 56, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (43, 57, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (44, 58, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (45, 59, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (46, 60, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (47, 61, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (48, 62, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (49, 63, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (50, 64, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (51, 65, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (52, 66, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (53, 67, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (54, 68, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (55, 69, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (56, 70, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (57, 71, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (58, 72, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (59, 80, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (60, 73, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (61, 74, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (62, 75, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (63, 76, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (64, 77, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (65, 78, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (66, 79, 7, 1, 1638167095745);
INSERT INTO `grit_resource_linked` VALUES (67, 15, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (68, 16, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (69, 17, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (70, 18, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (71, 19, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (72, 20, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (73, 21, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (74, 22, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (75, 23, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (76, 24, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (77, 25, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (78, 26, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (79, 27, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (80, 28, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (81, 29, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (82, 30, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (83, 31, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (84, 32, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (85, 39, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (86, 33, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (87, 34, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (88, 35, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (89, 38, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (90, 36, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (91, 37, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (92, 40, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (93, 41, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (94, 42, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (95, 43, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (96, 44, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (97, 45, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (98, 46, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (99, 47, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (100, 48, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (101, 49, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (102, 50, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (103, 51, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (104, 52, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (105, 53, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (106, 54, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (107, 55, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (108, 56, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (109, 57, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (110, 58, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (111, 59, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (112, 60, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (113, 61, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (114, 62, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (115, 63, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (116, 64, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (117, 65, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (118, 66, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (119, 67, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (120, 68, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (121, 69, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (122, 70, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (123, 71, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (124, 72, 5, 1, 1638169479015);
INSERT INTO `grit_resource_linked` VALUES (125, 81, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (126, 82, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (127, 83, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (128, 84, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (129, 85, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (130, 86, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (131, 87, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (132, 88, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (133, 89, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (134, 90, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (135, 91, 10, 1, 1638181701940);
INSERT INTO `grit_resource_linked` VALUES (136, 81, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (137, 82, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (138, 83, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (139, 84, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (140, 85, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (141, 86, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (142, 87, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (143, 88, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (144, 89, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (145, 90, 8, 1, 1638181752706);
INSERT INTO `grit_resource_linked` VALUES (146, 91, 8, 1, 1638181752706);

-- ----------------------------
-- Table structure for grit_subject
-- ----------------------------
DROP TABLE IF EXISTS `grit_subject`;
CREATE TABLE `grit_subject`  (
  `subject_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主体ID',
  `subject_pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '主体父ID',
  `subject_type` int(11) NOT NULL DEFAULT 0 COMMENT '主体类型(0:entity, 1:group)',
  `subject_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主体代号',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主体登录名,默认用guid填充',
  `login_password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主体登录密码',
  `display_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主体显示名',
  `order_index` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT '节点级别',
  `attrs` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性(kv)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可见',
  `gmt_create` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE INDEX `IX_grit_subject__login_name`(`login_name`) USING BTREE,
  INDEX `IX_grit_subject__subject_pid`(`subject_pid`) USING BTREE,
  INDEX `IX_grit_subject__subject_code`(`subject_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-主体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_subject
-- ----------------------------
INSERT INTO `grit_subject` VALUES (1, 0, 1, '', '24517fa31aaf4672919b560427c8921b', NULL, '用户组', 1, NULL, 0, NULL, 0, 1, 1638162756031, 1638162756031);
INSERT INTO `grit_subject` VALUES (2, 0, 1, '', 'e3d5351856994f29bc8e0a839913d7a8', NULL, '角色组', 2, NULL, 0, NULL, 0, 1, 1638162762258, 1638162767223);
INSERT INTO `grit_subject` VALUES (3, 2, 1, '', 'e3e3bdf746bf4f8a96835272141ce08f', NULL, 'wateradmin', 0, NULL, 0, NULL, 0, 1, 1638162780814, 1638162780814);
INSERT INTO `grit_subject` VALUES (4, 2, 1, '', 'b7b90ca3ed7e45aea5d8272d5125f855', NULL, 'spongeadmin', 0, NULL, 0, NULL, 0, 1, 1638162793896, 1638162793896);
INSERT INTO `grit_subject` VALUES (5, 3, 1, 'water_role_observer', 'cab2961965f3445b97a012104abb7fb5', NULL, '观查员', 0, NULL, 0, NULL, 0, 1, 1638162818716, 1638163011867);
INSERT INTO `grit_subject` VALUES (6, 3, 1, 'water_role_operator', 'c09321291503429ebaf7e6dcc3795bf2', NULL, '操作员', 0, NULL, 0, NULL, 0, 1, 1638162828378, 1638163018274);
INSERT INTO `grit_subject` VALUES (7, 3, 1, 'water_role_admin', '7415ffeeb31a49ffb8fbd629a75880aa', NULL, '管理员', 0, NULL, 0, NULL, 0, 1, 1638162838605, 1638163023817);
INSERT INTO `grit_subject` VALUES (8, 4, 1, 'sponge_role_observer', '7b8cd5d930724cadaacbf2b69dbed571', NULL, '观查员', 0, NULL, 0, NULL, 0, 1, 1638162953252, 1638162953252);
INSERT INTO `grit_subject` VALUES (9, 4, 1, 'sponge_role_operator', '80046dfb6839442fb932f5584e211e04', NULL, '操作员', 0, NULL, 0, NULL, 0, 1, 1638162974448, 1638162974448);
INSERT INTO `grit_subject` VALUES (10, 4, 1, 'sponge_role_admin', 'ee47f4ca20a44e02a21545faa2698fee', NULL, '管理员', 0, NULL, 0, NULL, 0, 1, 1638163000521, 1638163000521);
INSERT INTO `grit_subject` VALUES (11, -1, 0, '', 'admin', '540366E0E576CDDEE57C5D59FB2CFAFD2AE71D3E', '管理员', 0, NULL, 0, NULL, 0, 1, 1638163108581, 1638180505941);
INSERT INTO `grit_subject` VALUES (12, -1, 0, '', 'demo', NULL, 'demo', 0, NULL, 0, NULL, 0, 1, 1638180523980, 1638180523980);

-- ----------------------------
-- Table structure for grit_subject_linked
-- ----------------------------
DROP TABLE IF EXISTS `grit_subject_linked`;
CREATE TABLE `grit_subject_linked`  (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `subject_id` bigint(20) NOT NULL COMMENT '主体ID',
  `group_subject_id` bigint(20) NOT NULL COMMENT '分组主体ID',
  `gmt_create` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE INDEX `IX_grit_subject_linked__key`(`subject_id`, `group_subject_id`) USING BTREE,
  INDEX `IX_grit_subject_linked__group_subject_id`(`group_subject_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-主体与分组连接表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of grit_subject_linked
-- ----------------------------
INSERT INTO `grit_subject_linked` VALUES (1, 11, 7, 1638163108586);
INSERT INTO `grit_subject_linked` VALUES (2, 12, 5, 1638180523995);

SET FOREIGN_KEY_CHECKS = 1;
