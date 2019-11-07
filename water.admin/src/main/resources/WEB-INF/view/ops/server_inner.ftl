
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算资源</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<script>
    function add() {
        location.href = "/ops/server/add?tag=${tag!}";
    };

    function disable(server_id,type) {
        var text = "启用";
        if (type == 0) {
            text = "禁用";
        }
        top.layer.confirm('确定'+text, {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/ops/server/disable",
                data:{"server_id":server_id,"is_enabled":type},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            location.reload();
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
            top.layer.close(top.layer.index);
        });
    };
</script>
<body>
<toolbar>
    <left>
        <form>
            <#if is_admin == 1>
                <button onclick="add();" type="button">新增</button>
            </#if>
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
            <td width="40px">ID</td>
            <td width="320px">资源名称</td>
            <td>资源地址</td>
            <td width="45px">环境<br/>类型</td>
            <#if is_admin == 1>
                <td colspan="2">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list list as m>
            <tr>
                <td>${m.server_id}</td>
                <td style="text-align: left">
                    <n>实例：${m.iaas_key}</n><br/>
                    <n>别名：</n>${m.name}（<b>${m.iaas_type_str()}</b>）
                </td>
                <td style="text-align: left">
                    <n>外网：</n>${m.address!}<br/>
                    <n>内网：</n>${m.address_local!}
                </td>
                <td>${m.env_type_str()}</td>
                <#if is_admin == 1>
                    <td width="70px">
                        <a href="/ops/server/edit?server_id=${m.server_id}" class="t2">编辑</a> |
                        <#if m.is_enabled == 0>
                            <a onclick="disable('${m.server_id}',1)" style="cursor: pointer;color: blue;">启用</a>
                        </#if>
                        <#if m.is_enabled == 1>
                            <a onclick="disable('${m.server_id}',0)" style="cursor: pointer;color: blue;">禁用</a>
                        </#if>
                    </td>
                    <td width="50px">
                        <a onclick="location.href='/ops/server/extra?server_id=${m.server_id}'" style="cursor: pointer;color: blue;">更多...</a>
                    </td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>