
INSERT INTO `water_tool_monitor` VALUES (102, 'water', '7024675c5ee94dadafa97d0dfa71af3c', '消息派发情况', 0, '--water/water_msg::\nselect count(*) num \nfrom water_msg_message \nwhere state=0 and dist_count=0 and dist_nexttime=0 ', 'return m.d[0].num>10;', '0', '{{m.d[0].num}}', '', '未派发消息数::{{m.d[0].num}}', 0, '', 1);
INSERT INTO `water_tool_monitor` VALUES (117, 'water', '8e1c5f94f7aa4a1589d3ab82b5c1b36e', '服务监视情况', 0, '--water/water::\nselect count(*) num \nfrom water_reg_service \nwhere is_enabled=1 \nAND (check_last_state=1 OR check_last_time < SUBTIME(NOW(),\'0:1:0\'))', 'return m.d[0].num>0;', '0', '{{m.d[0].num}}', '', '有{{m.d[0].num}}个服务异常（或服务监测出错）', 0, '', 1);
INSERT INTO `water_tool_monitor` VALUES (118, 'water', '337c0242fca44f6fa0ce19f9b3dec027', '定时任务监视', 0, '--water/water_paas::\nselect count(*) num \nfrom paas_file \nwhere plan_state = 8 and is_disabled=0', 'return m.d[0].num !== 0;', '4', '{{m.d[0].num}}', '', '定时任务异常数量监视::{{m.d[0].num}}', 687, '定时任务', 1);
INSERT INTO `water_tool_monitor` VALUES (119, 'test', '2b7e5e2e09624218a6e820005b06cd12', 'FaaS测试', 0, '--faas()::\nreturn {num:12};', 'return m.d.num>10;', '12', '{{m.d.num}}', '', '测试数值::{{m.d.num}}', 110293, '', 1);

INSERT INTO `water_tool_report` VALUES (1, '_demo', 'water_cfg_logger', '', '--water/water::\nselect * from water_cfg_logger limit 10', '', '2021-01-07 17:23:02');

INSERT INTO `water_tool_synchronous` VALUES (1, '_demo', '530d056e4018471fa5292065dbc549e2', '同步APPX表', 0, 100, '_demo/test::appx', 'app_id', '--_demo/rock::\nselect * from appx where app_id>@key order by app_id asc', 0, '', NULL, 0, '2020-05-27 15:44:26');

