<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 脚本资源</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function add() {
        location.href = "/ops/script/add?tag=${tag}";
    };
</script>
<body>

<toolbar>
    <left>
        <#if is_admin == 1>
            <button onclick="add();" type="button">新增</button>
        </#if>
    </left>
    <right>
        <@stateselector items="启用,未启用"/>
    </right>
</toolbar>

<datagrid>
    <table>
        <thead>
            <tr>
                <td width="40px" >ID</td>
                <td width="130px" nowrap>脚本名称</td>
                <td width="40px" nowrap>参数</td>
                <#if is_admin == 1>
                    <td width="30px">操作</td>
                </#if>
            </tr>
        </thead>
        <tbody id="tbody">
            <#list list as m>
                <tr>
                    <td>${m.script_id}</td>
                    <td>${m.name}</td>
                    <td>${m.args}</td>
                    <#if is_admin == 1>
                        <td><a href="/ops/script/edit?script_id=${m.script_id}" style="cursor: pointer;color: blue;">编辑</a></td>
                    </#if>
                </tr>
            </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>