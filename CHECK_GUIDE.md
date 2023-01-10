系统升级后，例行检查指南：

* 函数计算 / 定时任务 :: water ，立即执行 [检查 watersev-pln]
  * => 是否正常？
* 配置管理 / 属性配置 :: water/alarm_sign，编辑保存 [检查 watersev-msgdis]
  * => 消息总线 / 消息记录 :: 已成功，是否有记录？
* 配置管理 / 属性配置 :: water/faas_uri，变更保存 [检查 @water.config.update]
  * => 函数计算 / 即时接口 :: water :: /water/paas/help/api/ 调试，运行地址是否有变？
* 日志查询 / water :: water_log_api 打开 [检查 water log service]
  * => 是否有记录？