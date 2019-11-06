<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<body>
<toolbar>
    <cell>
        <form>
            方案：<input type="text"  name="name" placeholder="代号或显示名" id="name" value="${name!}"/>
            <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_operator == 1>
                <button id="btn_add" type="button" class="edit">新建</button>
            </#if>
        </form>
    </cell>
</toolbar>
<datagrid>
    <table>
        <thead>
        <tr>
            <td width="40px">ID</td>
            <td width="120px">代号</td>
            <td>显示名</td>
            <td width="220px">关联模型</td>
            <td colspan="3">方案设计</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list schemes as item>
            <tr>
                <td>${item.scheme_id}</td>
                <td class="left">
                    <a id="item_scheme" target="${item.scheme_id}" class="t2">${item.name!}</a>
                </td>
                <td class="left">${item.name_display!}</td>
                <td class="left">${item.related_model!}</td>
                <td width="60px">
                    <#if item.event??>
                        <a id="event" href="/rubber/scheme/event/edit?scheme_id=${item.scheme_id}&f=${f}" class="t2" style="text-decoration: none;">事件(1)</a>
                    <#else>
                        <a id="event" href="/rubber/scheme/event/edit?scheme_id=${item.scheme_id}&f=${f}" class="t2" style="text-decoration: none;">事件(0)</a>
                    </#if>
                </td>
                <td width="60px">
                    <a id="a_rule" scheme="${item.scheme_id}" display="${item.name_display!}" class="t2">规则(${item.rule_count})</a>
                </td>
                <td width="60px">
                    <a id="a_process" scheme="${item.scheme_id}" class="t2">流程(${item.node_count})</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>
<script>

    $("a[id='a_rule']").on("click",function(){
        location.href="/rubber/scheme/rule/design?scheme_id=" + $(this).attr("scheme")
             + "&tag_name=" + $("#tag_name").val() + "&name_display=" + $(this).attr("display")+"&f=${f}";
    });

    $("a[id='a_process']").on("click",function(){
        location.href="/rubber/scheme/flow?scheme_id=" + $(this).attr("scheme");
    });

    function addorEditScheme(scheme_id) {
        var params = "";
        if(scheme_id){
            params = "&scheme_id=" + scheme_id ;
        }
        location.href="/rubber/scheme/edit?f=${f}" + params;
    };


    function impPaas(tag_name) {
        $.getJSON('/paas/plan/ajax/import?tag='+tag_name,function (rst) {
            if(rst.code){
                top.layer.msg(rst.msg);
                setTimeout(location.reload,1000);
            }
            else{
                top.layer.msg(rst.msg);
            }
        });
    };

    $("#btn_add").on("click",function(){
        addorEditScheme();
    });

    $("a[id='item_scheme']").on("click",function(){
        addorEditScheme(this.target)
    });

</script>