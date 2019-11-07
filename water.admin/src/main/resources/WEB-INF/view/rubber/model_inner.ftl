<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 规则计算</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addModel() {
            location.href="/rubber/model/edit?f=${f}";
        }

    </script>
</head>
<body>
<main>
<toolbar>
    <left>
        <form method="post">
            模型：<input type="text"  name="name" placeholder="代号或显示名" id="name" value="${name!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_admin == 1>
                <button onclick="addModel()" type="button"  class="edit">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
            </#if>
        </form>
    </left>
</toolbar>
<datagrid>
    <table>
        <thead>
        <tr>
            <td width="50">ID</td>
            <td width="180px">代号</td>
            <td>显示名</td>
            <td width="220px">相关数据库</td>
            <td width="60px">字段数量</td>
            <td width="110px"></td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list models as m>
            <tr>
                <td>${m.model_id}</td>
                <td class="left">
                    <a class="t2" href="/rubber/model/edit?model_id=${m.model_id}&f=${f}">${m.name!}</a>
                </td>
                <td class="left">${m.name_display!}</td>
                <td class="left">${m.related_db!}</td>
                <td>${m.field_count}</td>
                <td>
                    <a class="t2" href="${raas_uri!}/preview.js?model=${m.tag!}/${m.name!}" target="_blank">预览</a> |
                    <a class="t2" href="/rubber/model/field?model_id=${m.model_id}&f=${f}">编辑字段</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</main>
</body>
</html>