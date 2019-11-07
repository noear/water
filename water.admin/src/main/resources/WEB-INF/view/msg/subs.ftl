<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 订阅列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function deleteSubs(subscriber_id){
            top.layer.confirm('确定删除?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/msg/subs/ajax/delete",
                    data:{"subscriber_id":subscriber_id},
                    success:function(data){
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            location.reload();
                        },1000);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };
        
        function disableSubs(subscriber_id,is_enabled) {
            var msgtext = '启用';
            if (is_enabled == 0) {
                msgtext = '禁用';
            }
            top.layer.confirm('确定'+msgtext+'?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/msg/subs/ajax/enabled",
                    data:{
                        "subscriber_id":subscriber_id,
                        "is_enabled":is_enabled
                    },
                    success:function(data){
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            location.reload();
                        },1000);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };
    </script>
</head>
<body>
    <main>
            <toolbar>
                <left>
                    <form>
                        主题：<input type="text"  name="topic_name" placeholder="主题名称" id="topic_name"/>&nbsp;&nbsp;
                        <button type="submit">查询</button>
                        <div style="float:right;">
                            <@stateselector items="启用,未启用"/>
                        </div>
                    </form>
                </left>
            </toolbar>
            <datagrid>
                <table>
                    <thead>
                    <tr>
                        <td width="50px">主题ID</td>
                        <td>主题名称</td>
                        <td>订阅者的接收地址</td>
                        <td width="120px">接收方式</td>
                        <td width="90px">订阅时间</td>
                        <#if is_admin == 1>
                            <td width="80px">操作</td>
                        </#if>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    <#list list as sub>
                    <tr class="${sub.trClass()}">
                        <td>${sub.topic_id}</td>
                        <td class="left break">${sub.topic_name}</td>
                        <td class="left break">${sub.receive_url}
                            <#if (sub.subscriber_note!) != ''>
                                （${sub.subscriber_note}#${sub.check_last_state})
                            </#if>
                        </td><td>
                                <#if sub.receive_way==0>HTTP异步等待</#if>
                                <#if sub.receive_way==1>HTTP同步等待</#if>
                                <#if sub.receive_way==2>HTTP异步不等待</#if>
                        </td>
                        <td>${(sub.log_fulltime?string('yyyy-MM-dd'))!}</td>
                            <#if is_admin == 1>
                                <td>
                                    <a onclick="deleteSubs(${sub.subscriber_id})" class="t2">删除</a> |
                                    <#if sub.is_enabled == 1>
                                        <a class="t2" onclick="disableSubs('${sub.subscriber_id}',0)">禁用</a>
                                    </#if>
                                    <#if sub.is_enabled == 0>
                                        <a class="t2" onclick="disableSubs('${sub.subscriber_id}',1)">启用</a>
                                    </#if>
                                </td>
                            </#if>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </datagrid>
    </main>
</body>
</html>