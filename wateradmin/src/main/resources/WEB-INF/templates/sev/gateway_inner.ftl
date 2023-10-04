<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 上游配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        .line1{text-decoration:line-through;}
    </style>
</head>
<script>

    function edit(gateway_id) {
        location.href="/sev/gateway/edit?gateway_id="+gateway_id;
    }

</script>
<body>

<toolbar>
    <left>
        <#if is_admin == 1>
            <button class="edit" onclick="edit(0);" type="button">新增</button>
        </#if>
    </left>
    <right><@stateselector items="启用,未启用"/></right>
</toolbar>

<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="220px" class="left">service</td>
            <td class="left">代理网关</td>
            <td width="100px" class="left">负载策略</td>
            <#if is_admin == 1>
                <td width="60px">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list list as m1>
            <tr>
                <td class="left">${m1.name!}</td>
                <td class="left">${m1.agent!}</td>
                <td class="left">${m1.policy!}</td>

                <#if is_admin == 1>
                    <td>
                        <a  onclick="edit('${m1.gateway_id}')" style="color: blue;cursor: pointer">编辑</a>
                    </td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>