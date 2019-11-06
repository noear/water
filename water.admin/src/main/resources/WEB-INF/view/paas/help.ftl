<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 帮助文档</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>

</head>
<body>

<main>
<pre>
    JtSQL 接口说明（计划任务与PaaS共享）::v 1.1.20
----------------------------------
    使用$<...> 或 $<tag.key::...>，在Js里嵌入SQL脚本；select 返回为 [val,val]或[obj,obj]，其它为 value
>支持宏定义
    ::log   //当前执行记入日志
    ::val   //当前执行以值形式返回
    ::obj   //当前执行以{} or null 形式返回 //******
    ::cache //当前执行使用缓存
使用{{...}}，在SQL里嵌入Js脚本（注：若它处出现{{ 或 }} 当中加个空隔）

函数require(url)，用于引入一个JS脚本
函数include('tag/fun') 或 include('tag/*')，用于引入一个或多个公共函数

函数guid()->string，用于生成一个GUID
函数md5(str)->string，用于生成MD5加密值
函数sha1(str)->string，用于生成sha1加密值
函数sha256(str)->string，用于生成sha1加密值
函数hmacSha256(str,key)->string，用于生成sha1加密值
函数xorEncode(str,key)->string，用于内容xor加密 //******
函数xorDecode(str,key)->string，用于内容xor解密 //******
函数b64Encode(str)->string，用于内容base64转码  //******
函数b64Decode(str)->string，用于内容base64解码  //******

函数new Datetime(d:java.Date)->Datetime
函数parseDatetime(str,f)->Datetime

函数log(obj)，用于记录日志 //自动转为json //******
函数http({url,form,header,cache?,trace?})->string，用于请求http

函数sleep(millis)，线程休眠//millis：毫秒数

函数db2json(DataList or DataItem)->[]，将Weed.DataList 或 Weed.DataItem 对象转为json对象

对象:: cache:ICacheServiceEx //缓存对象 // 给 DbContext 的缓存控制用
对象:: water{
    client:WaterClient //完整的waterclient

    cfg('tag.key')->ConfigModel, 获取water 的配置
    db('tag.key')->DbContext, 获取water 配置的数据实例
    rpc(service,fun,args)-> //示例：water.rpc('xxx','x.x.x',{xxx})***1.7
    paas(tag,key,args)->String //调用paas平台接口*****1.9
    raas(obj,tag,name,arg)->String//调用raas平台接口
    heihei(target,msg)   //用嘿嘿发消息*****1.9
}

对象:: rock{
    client:RockClient //完整的rockclient
    util:RockUtil //完整的RockUtil

    app(appid or akey)->AppModel,
    oss(bucketName, objKey, objVal?)->String, //******1.8
    paas(tag,key,args)->String //调用paas平台接口*****1.6

    cmd(service,cmd,args)-> //示例：rock.cmd('xxx','x.x.x',{app:1,ver:120,data:{xxx}})***1.7
    api(service,fun,args)-> //示例：rock.api('xxx','x.x.x',{xxx})***1.7  //本质上与water.rpc一样，但属于业务概念
}


JtSQL.PaaS-api 特有::
----------------
参数context{
    api:PaasApiModel,
    request:HttpServletRequest,
    response:HttpServletResponse,

    get:(name)->String,       //返回一个参数值
    getDouble:(name)->double, //以double返回一个参数
    getLong:(name)->long,     //以long返回一个参数
    getInt:(name)->int,       //以int返回一个参数

    get:(name,incReferer)->String, //返回一个参数值
    getHeader:(name)->string,      //返回一个头值
    getUserAgent:()->string,       //返回UA
    getUserIP:()->string,          //返回客户端IP
    getReferer:()->string,         //返回父级URI

    code:int   //用于临时存放 //******
    msg:string //用于临时存放 //******

    isWhitelistOfIP(tag); //tag:rock的白名单分类标签 //******1.5
    isWhitelistOfHost(tag); //tag:rock的白名单分类标签//******1.5
    getPaasUri()->Strig; //获取当前接口的uri地址 //******1.5
}


约定:: return "::"+url; //表示跳转 //好处：转换都可以

////////////////////////////////////
部份模型说明
ConfigModel{
    key:string
    url:string
    user:string
    password:string
    explain:string

    getDb()->DbContext
    getLong()->long
    getInt()->int
    get(name)->ONode //获取json数据里的属性
}

AppModel{
    app_id:int
    app_key:string
    akey:string
    agroup_id:int
    ugroup_id:int
    name:string
    note:string

    ar_is_examine()->int
    ar_examine_ver()->int

    getSetting(ver)->AppSettingCollection
    getClientSetting(ver)->AppSettingCollection
    getVersion(platform)->AppVersionModel

    get(key)->AppSettingModel
}

AppSettingCollection{
    get(key)->AppVersionModel
    size()->int //数量
        ...更金接口参考：Map
}

AppVersionModel{
    ver:int     //版本号
    content:string //更新说明
    type:int        //类型
    platform:int    //平台
    url:string      //更新地址
    is_enable:int   //是否有效
    log_fulltime:Date //添加日期
}

AppSettingModel{
    name:string
    type:int   //[0,文本；1,数字; 9,JSON]
    value:string
    ver_start:int

    getLong()->long
    getInt()->int
    get(name)->ONode //获取json数据里的属性

    getNode()->ONode //转为ONode类型
}


</pre>

</main>

</main>
</body>
</html>