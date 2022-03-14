能力设计：
1.1.源数据可由多表组成 //ok
1.2.数据转换可动态处理 //ok

2.有出错补尝能力；人工处理异常后可自动恢复处理（不用重启）//ok

3.代码热修复能力；修改代码后可自动更新（不用重启）//ok

4.动态增加任务能力；(不用重启) //ok

//5.ETL角色分离；//ok

6.数据锁（可能会有多个任务，同步到一个目标表里；避免重复插入）

-----------------------------

如何测试?

-----------------------------

关于字段生成的5种可能：
    1.从源数据带过来；
    2.根据约束在已有的目标数据找里到；
    3.通过函数：用生成器生成; //这种情况：有可能通过约速字段查到的ID不需要生成，ID生成会浪费掉？***？？？//ok
    4.通过函数：到相关表查询; //这种情况：如果找不到，可能数据就不能或不应该入库？***后续怎么办//ok
        //转换器增加配置：require，必须要有值的字段，用于控制//在转换器处理

    //转换器添加检查，，，用于检查是否已完成转换

    5.通过函数：调用WEBAPI获取

关于主键产生的2种可能:
    1.从源数据带过来
    2.根据约束目标数据查找，如果没有就生成（但，这个查询会让检查更新那里做重复的事情）
    //所以让key的生成放到需要插入时生成???//ok

关于必须字段的可能：
    1.


关于数据的3种处理情况：
    1.需要更新；（目标数据较旧）
    2.跳过处理；（目标数据更新）//这种之前没有处理
    3.需要插入；（目标数据不存在）

对日志的规划：EtlLog：
    water_log_etl      (tag:context.name, tag1:context.table, tag2:class.name, content)
    water_log_etl_error(tag:context.name, tag1:context.table, tag2:class.name, content)
    water_log_etl_debug(tag:context.name, tag1:context.table, tag2:class.name, content)
    >设计
    water(context,tag2:class,text)
    error(context,tag2:class,ex)
    alarm(context,tag2:class,text)

通过Context开放接口，配合 Handler。实现动态修复ETL任务的能力的设计：
    1.如果etl.code 的md5码发现变化，则重新加载配置和函数

    2.如果etl.target.fields 或 trans 发生变化，则清空 queue2 ???//不清
    3.如果etl.source.model 发生变化，则清空 queue1  ???//不清
