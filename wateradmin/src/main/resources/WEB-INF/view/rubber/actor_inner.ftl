<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 参与者</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function addModel() {
            location.href="/rubber/actor/edit?tag_name=${tag_name}";
        }

    </script>
</head>
<body>
<toolbar>
        <form method="post">
            人员：<input type="text" class="w350" name="name" placeholder="代号或显示名" id="name" value="${name!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_operator == 1>
                <button onclick="addModel()" type="button"  class="edit">新建</button>&nbsp;&nbsp;&nbsp;&nbsp;
            </#if>
        </form>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="50">ID</td>
            <td width="150px" class="left">代号</td>
            <td width="150px" class="left">显示名</td>
            <td class="left">备注</td>
            <td width="70px"></td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list models as m>
            <tr>
                <td>${m.actor_id}</td>
                <td  class="left">
                    ${m.name!}
                </td>
                <td class="left">${m.name_display!}</td>
                <td class="left">${m.note!}</td>
                <td class="op">
                    <a  class="t2" href="/rubber/actor/edit?actor_id=${m.actor_id}&tag_name=${tag_name!}">编辑信息</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>