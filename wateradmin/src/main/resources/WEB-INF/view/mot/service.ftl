<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
       function deleteService(service_id) {
           top.layer.confirm('确定删除', {
               btn: ['确定','取消'] //按钮
           }, function(){
               $.ajax({
                   type:"POST",
                   url:"/mot/service/ajax/deleteService",
                   data:{"service_id":service_id},
                   success:function(data){
                       top.layer.msg('操作成功');
                       if ( $('#fresh').text() == '开启自动刷新') {
                           location.reload();
                       }
                   }
               });
               top.layer.close(top.layer.index);
           });
       };

       function disableService(service_id,type) {
           var text = "启用";
           if (type == 0) {
               text = "禁用";
           }
           top.layer.confirm('确定'+text, {
               btn: ['确定','取消'] //按钮
           }, function(){
               $.ajax({
                   type:"POST",
                   url:"/mot/service/ajax/disable",
                   data:{"service_id":service_id,"is_enabled":type},
                   success:function(data){
                       top.layer.msg(data.msg);
                       if ( $('#fresh').text() == '开启自动刷新') {
                           location.reload();
                       }
                   }
               });
               top.layer.close(top.layer.index);
           });
       };

       function autofresh() {
           if ( $('#fresh').text() == '关闭自动刷新') {
               $('#fresh').text('开启自动刷新');
               location.reload();
           } else {
               $('#fresh').text('关闭自动刷新');
               setInterval(function(){
                   freshData();
               }, 3000);
           }

       };

       function freshData() {
           var name = $('#name').val();
           var _state = 0;
           <#if _state??>
            var _state = ${_state};
           </#if>

           $.get('/mot/service/ajax/service_table?name='+name+'&_state='+_state+"&_type=${is_web?string('web','sev')}",function (rst) {
               $('datagrid').empty();
               $('datagrid').html(rst);
           })
       };
    </script>
</head>
<body>
<main>
    <block>
        <tabbar>
            <button type="button" onclick="UrlQueryBy('_type','sev')" class="${is_web?string('','sel')}">服务</button>
            <button type="button" onclick="UrlQueryBy('_type','web')" class="${is_web?string('sel','')}">网站</button>
        </tabbar>
    </block>
        <form>
            <toolbar>
                <left>
                    服务：<input type="text"  name="name" placeholder="名称" id="name" value="${name!}"/>&nbsp;&nbsp;
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <button onclick="autofresh();"  type="button" style="width: 100px;" id="fresh">开启自动刷新</button>
                    <#if is_operator == 1>
                    <span class="w50"></span><button type="button" class="edit" onclick="location.href='service/edit'">添加</button>
                    </#if>
                </left>
                <right>
                    <@stateselector items="启用,未启用"/>
                </right>
            </toolbar>
        </form>
        <datagrid class="list">
            <table>
                <thead>
                <tr>
                    <td width="120px" class="left">名称</td>
                    <td width="65px" class="left">地址</td>
                    <td class="left">启动备注</td>
                    <td width="75px">检测类型</td>
                    <td>检查路径</td>
                    <td width="120px">最后检查时间</td>
                    <td width="60px">最后检<br/>查状态</td>
                    <td width="60px">最后检<br/>查备注</td>
                    <#if is_admin == 1>
                        <td width="90px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody" >
                <#list services as m>
                    <#if m.check_last_state == 1>
                        <tr style="color: red">
                    </#if>
                        <td class="left">${m.name}</td>
                        <td class="left">${m.address}</td>
                        <td class="left break">${m.note}</td>
                        <td>
                            <#if m.check_type == 0>
                                被动检查
                            </#if>
                            <#if m.check_type == 1>
                                主动签到
                            </#if>
                        </td>
                        <td class="left">${m.check_url!}</td>
                        <td style='${m.isAlarm()?string("color:red","")}'>${(m.check_last_time?string('MM-dd HH:mm:ss'))!}</td>
                        <td>
                            <#if m.check_last_state == 0>
                                ok
                            </#if>
                            <#if m.check_last_state == 1>
                                error
                            </#if>
                        </td>
                        <td class="left">${m.check_last_note!}</td>
                        <#if is_admin == 1>
                            <td class="op">
                                <a class="t2" onclick="deleteService('${m.service_id}')">删除</a> |
                                <#if m.is_enabled == 1>
                                    <a class="t2" onclick="disableService('${m.service_id}',0)">禁用</a>
                                </#if>
                                <#if m.is_enabled == 0>
                                    <a class="t2" onclick="disableService('${m.service_id}',1)">启用</a>
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