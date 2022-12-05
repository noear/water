<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
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
        function del(act,hint){
            var vm = formToMap(".sel_from");

            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            if(confirm("确定要"+hint+"吗？") == false) {
                return;
            }

            $.ajax({
                type:"POST",
                url:"ajax/batch",
                data:{act: act, ids: vm.sel_id},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

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

            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
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
                    <a id="e${m.tag}" class="btn" href="/sev/service/inner?tag_name=${tag_name!}&name=${m.tag!}">${m.tag!}</a>
                </#if>
            </#list>
        </tabbar>
    </div>
    </#if>

    <form>
        <toolbar>
            <flex>
                <left class="col-4">
                    <#if is_admin == 1>
                        <#if _state == 0>
                            <button type='button' class="minor mar10-r" onclick="del(0,'禁用')" >禁用</button>
                        <#else>
                            <button type='button' class="minor mar10-r" onclick="del(1,'启用')" >启用</button>
                            <button type='button' class="minor mar10-r" onclick="del(9,'删除')" >删除</button>
                        </#if>

                        <a class="btn edit" href="/sev/service/edit">手动添加</a>
                    </#if>
                </left>
                <middle class="col-4 center">
                    <input type="text" class="w200" name="name" placeholder="service" id="name" value="${name!}"/>
                    <input type="hidden" name="tag_name" value="${tag_name!}"/>
                    <button type="submit">查询</button>
                </middle>
                <right class="col-4">
                    <@stateselector items="启用,未启用"/>
                </right>
            </flex>
        </toolbar>
    </form>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                <td width="140px" class="left">service</td>
                <td class="left">地址</td>
                <td width="50px">检测<br/>类型</td>
                <td>检测路径</td>
                <td width="120px">检测情况</td>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from">
            <#list services as m>
                <#if m.check_last_state == 1>
                <tr style="color: red" title="${m.code_location!}">
                <#else>
                    <tr title="${m.code_location!}">
                </#if>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.service_id}" /><a></a></label></checkbox></td>
                <td class="left">${m.name}</td>
                <td class="left break">
                    <#if m.check_type == 0>
                        <a href="/sev/service/status?s=${m.name}@${m.address}" target="_blank">
                            ${m.address}
                            <#if m.meta?default("")?length gt 0>
                                - ${m.meta}
                            </#if>
                        </a>
                    <#else>
                        ${m.address}
                        <#if m.meta?default("")?length gt 0>
                            - ${m.meta}
                        </#if>
                    </#if>
                </td>
                <td>
                    ${(m.check_type == 0)?string("被动","主动")}
                </td>
                <td class="left">
                    <#if m.check_url?default('')?length gt 0 >
                        <a href="/sev/service/check?s=${m.name}@${m.address}@${m.service_id}" target="_blank">
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
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>