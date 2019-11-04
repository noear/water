# 轻量级网站动态构建平台（不知道算不算是FaaS）
* 用javascript 和 freemarker 语法做动态构建
* 插件化扩展（开放的扩展中心：使用别人的成果 或 分享自己的成果）
* 支持热更新 和 热扩展
* QQ交流群：870505482
* Demo（先在上面玩玩看），每天会还原数据
> <http://jw.noear.org/.admin/?_L0n5=1CE24B1CF36B0C5B94AACE6263DBD947FFA53531>


# 部署指南（用/bin/下的文件；；如果要自己打包，参考/bin/下的结构放好）
1. 准备好 mysql（5.6+） 和 java（jdk8）运行环境
2. 创建数据库jw（可字可自定义）
3. 在服务器上下载 /bin/* 到 /data/sss/* (目录可定义)
> jw_ext/ 用于放置数据库配置 和 java 扩展包

> jw.jar 主程序包
1. 修改 /data/sss/jw_ext/_db.properties 里的数据库配置
使用脚本运行服务：java -jar /data/sss/jw.jar -server.port={端口} -extend=/data/sss/jw_ext/
   > 例：java -jar /data/sss/jw.jar -server.port=8081 -extend=/data/sss/jw_ext/
5. 配置nginx代理并加域名（不要配置静态资源缓存；jar包自己会处理）   
   > 请给站点配个帅气的域名
6. 打开管理后台：http://{网站域名}/.admin/?_L0n5=1CE24B1CF36B0C5B94AACE6263DBD947FFA53531 
   > 后台地址 = http://{网站域名}/.admin/?_L0n5={token}
   
   > {token} = sha1({_frm_admin_user}+#+{_frm_admin_pwd})
7. 进入配置界面，修改管理密码和网站域名（修改后，重启服务）
   > _frm_admin_pwd =管理密码 
8. 可以在管理界面进行网站开发了

##### 建议把脚本转为.service文件后通过systemctl操控；或其它更好友的控制脚本
