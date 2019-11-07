<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 枚举管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function edit(id) {
            location.href = "/cfg/enum/edit?enum_id=" + id;
        };
    </script>
</head>
<body>
<main>
    <toolbar>
        <left>
            <form>
                type：<input type="text"  name="type" id="type"/>&nbsp;&nbsp;
                <button type="submit">查询</button>&nbsp;&nbsp;
                <#if is_admin == 1>
                    <button type="button" class="edit" onclick="edit(0);" >新增</button>
                </#if>
            </form>
        </left>
    </toolbar>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td width="50px">ID</td>
                <td width="50px">tag</td>
                <td width="150px">type</td>
                <td width="120px">value</td>
                <td width="150px">title</td>
                <td width="30"></td>
                <#if is_admin == 1>
                    <td width="60px">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody" >

            <#list list as m>
                <tr>
                    <td>${m_index + 1}</td>
                    <td style="text-align: left">${m.tag!}</td>
                    <td style="text-align: left">${m.type!}</td>
                    <td style="text-align: left">${m.value!}</td>
                    <td style="text-align: left">${m.title!}</td>
                    <td style="text-align: left">${(m.is_enabled?default(0)=1)?string('启','禁')}</td>
                    <#if is_admin == 1>
                        <td><a href="/cfg/enum/edit?id=${m.id}" style="cursor: pointer;color: blue;">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>

</body>
</html>