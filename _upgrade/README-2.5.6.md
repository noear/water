
### 1.water job 支持传递参数

* 删除 [即时接口/sdk_water] 下的内容（先禁用，再删除）
* 然后马上导入 2.5.6/water_paasfile_api_sdk_water_20220312.jsond


### 2.升级 sponge_track 的统计 sql(符合 mysql 5.7 的模式)

* 删除 [定时任务/sponge_track] 下的内容
* 然后马上导入 2.5.6/water_paasfile_pln_sponge_track_20220312.jsond



`提示：jsond 是专用于 water 体系离线传输的数据格式（已加密）`