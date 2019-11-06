<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 字段列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addField() {
            location.href="/rubber/model/field/edit?model_id=${model.model_id}&f=${f!}";
        };
    </script>
</head>
<body>
<main class="frm">
    <h2>${model.name_display!}/字段列表（<a onclick="history.back(-1)" class="t2">返回</a>）</h2>
    <hr />
    <toolbar>
        <cell>
                    <#if is_operator == 1>
                        <button onclick="addField()" type="button"  class="edit">添加</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>

        </cell>
        <cell>
            <form action="${raas_uri!}/debug" target="_blank" method="get">
                <input type="hidden" name="model" value="${model.tag!}/${model.name!}">
                <input type="text" name="args" value="${model.debug_args!}" placeholder="{user_id:1}">
                <input type="hidden" name="field">
                <button type="submit">调试模型</button>
            </form>&nbsp;&nbsp;
            <form action="${raas_uri!}/preview.js" target="_blank" method="get">
                <input type="hidden" name="model" value="${model.tag!}/${model.name!}">
                <button type="submit">预览模型</button>
            </form>

        </cell>
    </toolbar>


        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="40">ID</td>
                    <td width="200">代号</td>
                    <td width="350">显示名</td>
                    <td>备注</td>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list fields as m>
                    <tr>
                        <td class="center">${m.field_id}</td>
                        <td class="left">
                            <a class="t2" href="/rubber/model/field/edit?field_id=${m.field_id}&model_id=${model.model_id}&f=${f}">${m.name!}</a>
                        </td>
                        <td class="left">${m.name_display!}</td>
                        <td class="left">${m.note!}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>

</main>
</body>
</html>