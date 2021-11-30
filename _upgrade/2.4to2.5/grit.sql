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

 Date: 30/11/2021 11:25:46
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
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_resource
-- ----------------------------
INSERT INTO `grit_resource` VALUES (1, 0, 0, 2, 'spongeadmin', 'SPONGE', 1, 'http://spongeadmin', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161807169, 1638234028869);
INSERT INTO `grit_resource` VALUES (2, 0, 0, 2, 'wateradmin', 'WATER', 0, 'http://wateradmin', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161844457, 1638161844457);
INSERT INTO `grit_resource` VALUES (3, 1, 1, 1, '', '应用控制', 1, '/rock/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161871925, 1638161871925);
INSERT INTO `grit_resource` VALUES (4, 1, 1, 1, '', '价值跟踪', 2, '/track/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161889255, 1638161889255);
INSERT INTO `grit_resource` VALUES (5, 2, 2, 1, '', '日常工具', 1, '/tool/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161906547, 1638161906547);
INSERT INTO `grit_resource` VALUES (6, 2, 2, 1, '', '日志查询', 2, '/log/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161948021, 1638161948021);
INSERT INTO `grit_resource` VALUES (7, 2, 2, 1, '', '消息总线', 3, '/msg/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161964018, 1638161964018);
INSERT INTO `grit_resource` VALUES (8, 2, 2, 1, '', '服务监控', 4, '/mot/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638161994147, 1638161994147);
INSERT INTO `grit_resource` VALUES (9, 2, 2, 1, '', '配置管理', 5, '/cfg/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162009746, 1638162009746);
INSERT INTO `grit_resource` VALUES (10, 2, 2, 1, '', 'FaaS', 6, '/luffy/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162025756, 1638200237867);
INSERT INTO `grit_resource` VALUES (11, 2, 2, 1, '', '规则计算', 7, '/rubber/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162040839, 1638162040839);
INSERT INTO `grit_resource` VALUES (12, 2, 2, 1, '', 'Dev', 8, '/dev/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162069561, 1638162069561);
INSERT INTO `grit_resource` VALUES (13, 2, 2, 1, '', 'Ops', 9, '/ops/', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1638162086546, 1638162086546);
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
INSERT INTO `grit_resource` VALUES (92, 2, 2, 1, '', '操作权限', -1, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638203715018, 1638203715018);
INSERT INTO `grit_resource` VALUES (93, 92, 2, 0, 'water:observer', '只读', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638203748501, 1638204248593);
INSERT INTO `grit_resource` VALUES (94, 92, 2, 0, 'water:operator', '部份可写', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638203781515, 1638204256743);
INSERT INTO `grit_resource` VALUES (95, 92, 2, 0, 'water:admin', '可写可删', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638203812644, 1638204268920);
INSERT INTO `grit_resource` VALUES (96, 1, 1, 1, '', '操作权限', -1, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638232693199, 1638232693199);
INSERT INTO `grit_resource` VALUES (97, 96, 1, 0, 'sponge:observer', '只读', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638232728937, 1638232728937);
INSERT INTO `grit_resource` VALUES (98, 96, 1, 0, 'sponge:operator', '部分可写', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638232759373, 1638232759373);
INSERT INTO `grit_resource` VALUES (99, 96, 1, 0, 'sponge:admin', '可写可删', 0, '', NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1638232782115, 1638232782115);

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
) ENGINE = InnoDB AUTO_INCREMENT = 349 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-资源与主体连接表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_resource_linked
-- ----------------------------
INSERT INTO `grit_resource_linked` VALUES (266, 97, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (267, 98, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (268, 99, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (269, 81, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (270, 82, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (271, 83, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (272, 84, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (273, 85, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (274, 86, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (275, 87, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (276, 88, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (277, 89, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (278, 90, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (279, 91, 11, 0, 1638240666572);
INSERT INTO `grit_resource_linked` VALUES (288, 93, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (289, 94, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (290, 95, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (291, 15, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (292, 16, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (293, 17, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (294, 18, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (295, 19, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (296, 20, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (297, 21, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (298, 22, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (299, 23, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (300, 24, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (301, 25, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (302, 26, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (303, 27, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (304, 28, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (305, 29, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (306, 30, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (307, 31, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (308, 32, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (309, 39, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (310, 33, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (311, 34, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (312, 35, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (313, 38, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (314, 36, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (315, 37, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (316, 40, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (317, 41, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (318, 42, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (319, 43, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (320, 44, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (321, 45, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (322, 46, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (323, 47, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (324, 48, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (325, 49, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (326, 50, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (327, 51, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (328, 52, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (329, 53, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (330, 54, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (331, 55, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (332, 56, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (333, 57, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (334, 58, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (335, 59, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (336, 60, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (337, 61, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (338, 62, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (339, 63, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (340, 64, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (341, 65, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (342, 66, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (343, 67, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (344, 68, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (345, 69, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (346, 70, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (347, 71, 11, 0, 1638242109290);
INSERT INTO `grit_resource_linked` VALUES (348, 72, 11, 0, 1638242109290);

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-主体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grit_subject
-- ----------------------------
INSERT INTO `grit_subject` VALUES (1, 0, 1, '', '24517fa31aaf4672919b560427c8921b', NULL, '用户组', 1, NULL, 0, NULL, 0, 1, 1638162756031, 1638162756031);
INSERT INTO `grit_subject` VALUES (2, 0, 1, '', 'e3d5351856994f29bc8e0a839913d7a8', NULL, '角色组', 2, NULL, 0, NULL, 0, 1, 1638162762258, 1638200143283);
INSERT INTO `grit_subject` VALUES (11, -1, 0, '', 'admin', '540366E0E576CDDEE57C5D59FB2CFAFD2AE71D3E', '管理员', 0, NULL, 0, NULL, 0, 1, 1638163108581, 1638180505941);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'grit-主体与分组连接表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of grit_subject_linked
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
