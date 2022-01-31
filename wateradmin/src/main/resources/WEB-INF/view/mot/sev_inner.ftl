<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        /* tooltip */
        #tooltip{
            position:absolute;
            border:1px solid #aaa;
            background:#eee;
            padding:1px;
            color:#333;
            display:none;
            font-size: small;
        }
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}
    </style>
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

            $.get('/mot/service/ajax/service_table?tag_name=${tag_name!}&name='+name+'&_state='+_state,function (rst) {
                $('datagrid').empty();
                $('datagrid').html(rst);
            })
        };

        $(function(){
            var x = 10;
            var y = 20;
            $("tr[title]").mouseover(function(e){
                this.myTitle = this.title.replace(/；/g,"<br/>")
                this.title = "";
                var tooltip = "<div id='tooltip'>"+ this.myTitle +"<\/div>"; //创建 div 元素 文字提示
                $("body").append(tooltip);    //把它追加到文档中
                $("#tooltip").css({
                    "top": (e.pageY+y) + "px",
                    "left": (e.pageX+x)  + "px"
                }).show();      //设置x坐标和y坐标，并且显示
            }).mouseout(function(){
                this.title = this.myTitle;
                $("#tooltip").remove();   //移除
            }).mousemove(function(e){
                $("#tooltip").css({
                    "top": (e.pageY+y) + "px",
                    "left": (e.pageX+x)  + "px"
                });
            });
        });
    </script>
</head>
<body>
<main>
    <#if tabs_visible!false>
    <div class="tabs">
        <tabbar>
            <#list tabs as m>
                <#if m.tag == name>
                    <a id="e${m.tag}" class="btn sel">${m.tag!}</a>
                <#else>
                    <a id="e${m.tag}" class="btn" href="/mot/service/inner?tag_name=${tag_name!}&name=${m.tag!}">${m.tag!}</a>
                </#if>
            </#list>
        </tabbar>
    </div>
    </#if>

    <form>
        <toolbar>
            <left>
                服务：<input type="text" class="w250" name="name" placeholder="名称" id="name" value="${name!}"/>&nbsp;&nbsp;
                <button type="submit">查询</button>&nbsp;&nbsp;
                <button onclick="autofresh();"  type="button" style="width: 100px;" id="fresh">开启自动刷新</button>
                <#if is_operator == 1>
                    <span class="w50"></span><a class="btn edit" href="/mot/service/edit">添加</a>
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
                <td width="140px" class="left">名称</td>
                <td class="left">地址</td>
                <td width="50px">检测<br/>类型</td>
                <td>检测路径</td>
                <td width="120px">检测情况</td>
                <#if is_admin == 1>
                    <td width="170px">操作</td>
                <#else>
                    <td width="80px">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody" >
            <#list services as m>
                <#if m.check_last_state == 1>
                <tr style="color: red" title="${m.code_location!}">
                <#else>
                    <tr title="${m.code_location!}">
                </#if>
                <td class="left">${m.name}</td>
                <td class="left break">
                    <#if m.check_type == 0>
                        <a href="/mot/service/status?s=${m.name}@${m.address}" target="_blank">
                            ${m.address}
                            <#if m.note?default("")?length gt 0>
                                - ${m.note}
                            </#if>
                        </a>
                    <#else>
                        ${m.address}
                        <#if m.note?default("")?length gt 0>
                            - ${m.note}
                        </#if>
                    </#if>
                </td>
                <td>
                    ${(m.check_type == 0)?string("被动","主动")}
                </td>
                <td class="left">
                    <#if m.check_url?default('')?length gt 0 >
                        <a href="/mot/service/check?s=${m.name}@${m.address}@${m.service_id}" target="_blank">
                            ${m.check_url!}
                        </a>
                    </#if>
                </td>

                <td style='${m.isAlarm()?string("color:red","")}'>
                    ${(m.check_last_time?string('HH:mm:ss'))!}
                    <#if m.check_last_state == 0>
                        - ok
                    <#else>
                        - no
                    </#if>
                </td>

                <td class="op">
                    <#if is_admin == 1>
                        <a class="t2" onclick="deleteService('${m.service_id}')">删除</a> |
                        <#if m.is_enabled == 1>
                            <a class="t2" onclick="disableService('${m.service_id}',0)">禁用</a>
                        </#if>
                        <#if m.is_enabled == 0>
                            <a class="t2" onclick="disableService('${m.service_id}',1)">启用</a>
                        </#if>
                    </#if>
                    |
                    <a href="/log/query/inner?tag_name=water&logger=water_log_sev&level=0&tagx=sevchk@${m.address}" class="t2">日志</a>
                    |
                    <a href="/mot/service/charts?key=${m.key}" class="t2">监控</a>
                </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>