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
            <cell>
                <form>
                    枚举名称：<input type="text"  name="type" placeholder="枚举名称" id="group"/>&nbsp;&nbsp;
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                        <button type="button" class="edit" onclick="edit(0);" >新增</button>
                    </#if>
                </form>
            </cell>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="50px">ID</td>
                    <td width="150px">枚举组</td>
                    <td width="120px">枚举值</td>
                    <td width="150px">枚举值名称</td>
                    <#if is_admin == 1>
                        <td width="60px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody" >

                <#list list as m>
                    <tr>
                        <td>${m_index + 1}</td>
                        <td style="text-align: left">${m.type!}</td>
                        <td style="text-align: left">${m.value!}</td>
                        <td style="text-align: left">${m.name!}</td>
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