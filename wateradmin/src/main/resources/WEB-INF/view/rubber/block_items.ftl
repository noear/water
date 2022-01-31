
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据block</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function query() {
            UrlQueryByDic({fname:$('#fname').val(),fval:$('#fval').val()});
        };

        function addItem() {
            location.href = '/rubber/block/item/edit?block_id=${block.block_id}';
        }
    </script>
</head>
<body>
<main class="frm">
    <blockquote>
        <h2 class="ln30"><a href="/rubber/block/inner?tag_name=${block.tag!}" class="noline">指标仓库</a></h2> / 内容列表 :: ${block.name_display!}
    </blockquote>
    <toolbar>
        <left>
            <select id="fname">
                <#list cols as k,v>
                    <#if v?contains('*') >
                        <option value="${k}">${v}</option>
                    </#if>
                </#list>
            </select>：<input id="fval" type="text" />&nbsp;&nbsp;<button onclick="query()" type="button">查询</button>
        </left>
        <right>
            <#if (is_operator == 1) && (block.is_editable>0)>
                <button onclick="addItem()" type="button" class="edit">新增</button>
            </#if>
            &nbsp;&nbsp;
            <form action="${raas_uri!}/debug" target="_blank" method="get">
                <input type="hidden" name="block" value="${block.tag!}/${block.name!}">
                <input type="hidden" name="args" value="{cmd:null,x:'xxx'}">

                <button type="submit">调试</button>
            </form>
        </right>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <#list cols?keys as k>
                    <td>${cols[k]}</td>
                </#list>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list items as m>
                <tr>
                    <#list cols?keys as k>
                        <td>${m.get(k)!}</td>
                    </#list>
                    <td>
                        <#if (is_operator == 1 && block.is_editable>0) >
                            <a class="t2" href="/rubber/block/item/edit?block_id=${block.block_id}&item_key=${m.get(block.cols_key())}">编辑</a>
                        </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</main>
</body>
</html>