
升级到 2.3 的操用说明：


1. water 更新脚本

```sql
-- 2021.10.28 //添加更新时间
ALTER TABLE `water_cfg_logger`
    ADD COLUMN `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `is_alarm`;

-- 2021.11.01
CREATE TABLE `water_cfg_broker` (
  `broker_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '协调器ID',
  `tag` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',
  `broker` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '协调器',
  `row_num` bigint(20) DEFAULT '0' COMMENT '累积行数',
  `row_num_today` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日行数',
  `row_num_today_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日错误行数',
  `row_num_yesterday` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天行数',
  `row_num_yesterday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '昨天错误行数',
  `row_num_beforeday` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天行数',
  `row_num_beforeday_error` bigint(20) NOT NULL DEFAULT '0' COMMENT '前天错误行数',
  `keep_days` int(11) NOT NULL DEFAULT '15' COMMENT '保留天数',
  `source` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '数据源',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `is_enabled` int(11) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `is_alarm` int(11) NOT NULL DEFAULT '0' COMMENT '是否报警',
  `update_fulltime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`broker_id`) USING BTREE,
  KEY `IX_tag` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='WATER-配置-消息协调器表';


```

2. water_msg 更新脚本

```sql
-- 2021.11.01
ALTER TABLE `water_msg_message`
    MODIFY COLUMN `topic_id` int NULL DEFAULT 0 COMMENT '主题ID' AFTER `tags`;

ALTER TABLE `water_msg_message_all`
    MODIFY COLUMN `topic_id` int NULL DEFAULT 0 COMMENT '主题ID' AFTER `tags`;
```

3. water_msg 的表 [water_msg_subscriber], [water_msg_topic] 复制到 water 库

4. water 的表 [rubber_*] 复制到 water_paas 库
5. water_log 的表 [rubber_*] 复制到 water_paas 库
6. 删掉 water_log 的所有表（通过 wateradmin 创建）
7. 删掉配置 water/water_msg, water/water_raas, water/water_log, water/water_msg_queue, water/water_log_level
8. 修改配置 water/water_msg_store

```properties
#rdb,mongodb
store.driverType=rdb
store.schema=water_msg_store
store.url=jdbc:mysql://mysql.water.io:3306/water_msg_store?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true
store.password=KHe85E4MYdeLBHSR
store.username=demo
store.jdbcUrl=${store.url}
#redis,local
queue.driverType=redis
queue.name=water_msg_queue_dev
queue.server=redis.water.io:6379
queue.password=L8vcQgep
queue.db=3
```

9. 添加 配置管理/消息配置: water/default 数据源为空

10. 更新 water 服务包，然后重新

注：更新后，water 部份定时任务运失败；把那失败的任务删掉，导入 water_paasfile_pln_water_20211103.jsond 数据





