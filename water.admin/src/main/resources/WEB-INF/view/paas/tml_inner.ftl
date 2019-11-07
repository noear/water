<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 公共模版</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addPlan() {
            location.href="/paas/tml/edit";
        }

    </script>
</head>
<body>
<toolbar>
    <left>
        <form>
            模板：<input type="text"  name="tml_name" placeholder="名称" id="tml_name" value="${tml_name!}"/>
            <input type="hidden"  name="tag" id="tag" value="${tag!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_admin == 1>
                <button onclick="addPlan()" type="button" class="edit">新增</button>
            </#if>
        </form>
    </left>
    <right>
        <@stateselector items="启用,未启用"/>
    </right>
</toolbar>
<datagrid>
    <table>
        <thead>
        <tr>
            <td width="40">ID</td>
            <td width="250">名称</td>
            <td>显示名</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list list as t>
            <tr>
                <td>${t.tml_id}</td>
                <td class="left">
                    <#if is_admin == 1>
                        <a href="/paas/tml/edit?tml_id=${t.tml_id}" class="t2">${t.tml_name!}</a>
                    <#else>
                        <a href="/paas/query/code?code_type=4&id=${t.tml_id}">${t.tml_name!}</a>
                    </#if>
                </td>
                <td class="left">${t.name_display!}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>