<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 安全名单</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>

    </script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-6">
                <#if is_admin == 1>
                    <a class="btn edit" href="/cfg/whitelist/edit?tag_name=${tag_name!}">新增</a>
                </#if>
            </left>
            <right class="col-6">
                <@stateselector items="启用,未启用"/>
            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="50px">ID</td>
                <td width="100px">type</td>
                <td>value</td>
                <td width="200px">备注</td>
                <td width="60px">操作</td>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from" >

            <#list list as m>
                <tr>
                    <td>${m.row_id}</td>
                    <td class="left">${m.type!}</td>
                    <td class="left">${m.value!}</td>
                    <td class="left">${m.note!}</td>
                    <#if is_admin == 1>
                        <td><a class="t2" href="/cfg/whitelist/edit?id=${m.row_id}">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>