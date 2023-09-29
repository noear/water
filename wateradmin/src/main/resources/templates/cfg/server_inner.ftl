
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算资源</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
</head>
<script>

</script>
<body>
<toolbar>
    <form>
        <#if is_admin == 1>
            <a class="btn edit" href="/cfg/server/edit?tag_name=${tag_name!}">新增</a>
        </#if>
        <div style="float:right;">
            <@stateselector items="启用,未启用"/>
        </div>
    </form>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="40px">ID</td>
            <td width="380px">资源名称</td>
            <td>资源地址</td>
            <td width="45px">环境<br/>类型</td>
            <#if is_admin == 1>
                <td width="50px">操作</td>
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
                    <td class="op">
                        <a href="/cfg/server/edit?server_id=${m.server_id}" class="t2">编辑</a>
                    </td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>