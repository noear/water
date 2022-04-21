<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        .dis td, .dis td a{text-decoration:line-through; color:#888;}
    </style>
</head>
<script>

</script>
<body>
<toolbar>
    <left>
        <form>
            <input type="hidden"  name="tag_name" value="${tag_name!}"/>
            <input type="text"  name="key" placeholder="key" value="${key!}" class="w250"/>
            <button type="submit">查询</button>
            <#if is_admin == 1>
                <a class="btn edit mar10-l" href="/cfg/prop/edit?tag_name=${tag_name!}">新增</a>
            </#if>
        </form>
    </left>
    <right>

    </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="220px" class="left">key</td>
            <td class="left">配置内容</td>
            <#if is_admin == 1>
                <td width="50px">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as cfg>
            <tr class="${cfg.isEnabled()?string("", "dis")}">
                <td class="left">${cfg.key!}
                    <#if cfg.type != 0>
                        <n-l>::${cfg.type_str()}</n-l>
                    </#if>
                </td>
                <td class="left break">${cfg.value_html()!}</td>
                <#if is_admin == 1>
                    <td class="op"><a href="/cfg/prop/edit?row_id=${cfg.row_id}" class="t2">编辑</a></td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>