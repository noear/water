<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据block</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>

    </script>
</head>
<body>
<main>
    <toolbar>
        <cell>
            <#if is_operator == 1>
                <button onclick="addBlock()" type="button"  class="edit">新增</button>
            </#if>
        </cell>
    </toolbar>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td width="50px">ID</td>
                <td width="180px">代号</td>
                <td>显示名</td>
                <td width="260px">数据源</td>
                <td width="130px">备注</td>
                <td width="110px"></td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list blocks as m>
                <tr>
                    <td>${m.block_id}</td>
                    <td class="left"><a class="t2" href="/rubber/block/edit?block_id=${m.block_id}">${m.name!}</a></td>
                    <td class="left">${m.name_display!}</td>
                    <td class="left">
                        <#if m.related_db?has_content>
                            ${m.related_db}#${m.related_tb}
                        </#if>
                    </td>
                    <td class="left">${m.note!}</td>
                    <td><a class="t2" href="${raas_uri}/preview.js?block=${m.tag!}/${m.name!}" target="_blank">预览</a> |
                        <a class="t2" href="/rubber/block/items?block_id=${m.block_id}">内容管理</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
<script>
    function addBlock() {
        location.href = '/rubber/block/edit';
    }
</script>
</html>