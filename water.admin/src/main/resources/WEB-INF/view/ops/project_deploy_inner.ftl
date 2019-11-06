<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 项目配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        datagrid tr td a{
            color: blue;
            cursor:pointer;
            text-decoration: underline
        }
    </style>
</head>
<script>
    function add() {
        location.href = "/ops/project/project_deploy_edit?project_id=${project_id!}";
    };
</script>
<body>

<toolbar>
    <left>
        <#if is_admin == 1>
            <button class="edit" onclick="add();" type="button">新增</button>
        </#if>
    </left>
</toolbar>

<datagrid>
    <table>
        <thead>
        <tr>
            <td width="80px" >部署ID</td>
            <td >部署流程名称</td>
            <td width="130px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody">
            <#list deployList as m>
                <tr>
                    <td>${m.deploy_id}</td>
                    <td>${m.name}</td>
                    <td>
                        <a href="/ops/project/deploy_design?deploy_id=${m.deploy_id}&project_id=${project_id!}">绘制流程</a>&nbsp;|
                        <a href="/ops/project/project_deploy_edit?project_id=${project_id!}&deploy_id=${m.deploy_id!}">编辑</a>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>