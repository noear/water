<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function imp(file) {
            if(confirm("确定要导入吗？") == false){
                return;
            }

            var fromData = new FormData();
            fromData.append("file", file);
            fromData.append("tag","${tag_name!}");

            $.ajax({
                type:"POST",
                url:"ajax/import",
                data:fromData,
                processData: false,
                contentType: false,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }
        
        function exp() {
            var vm = formToMap(".sel_from");
            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            window.open("ajax/export?tag=${tag_name!}&ids=" + vm.sel_id, "_blank");
        }

        $(function(){
            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });

            $("#imp_file").change(function () {
                imp(this.files[0]);
            })
        });
    </script>
</head>
<body>
<toolbar>
    <form>
        <a class="w50">方案：</a><input type="text"  name="name" class="w350" placeholder="代号或显示名" id="name" value="${name!}"/>
        <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name!}"/>
        <button type="submit">查询</button>&nbsp;&nbsp;
        <#if is_operator == 1>
            <a href="/rubber/scheme/edit?f=${f}" class="btn edit">新建</a>
        </#if>
    </form>
    <div>
        <a class="w50"></a><file>
            <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor w80">导入</a></label>
        </file>

        <button type='button' class="minor w80 mar10-l" onclick="exp()" >导出</button>
    </div>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
            <td width="50px">ID</td>
            <td width="120px" class="left">代号</td>
            <td class="left">显示名</td>
            <td width="220px" class="left">关联模型</td>
            <td colspan="3">方案设计</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list schemes as item>
            <tr>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${item.scheme_id}" /><a></a></label></checkbox></td>
                <td>${item.scheme_id}</td>
                <td class="left">
                    <a id="item_scheme" target="${item.scheme_id}" class="t2">${item.name!}</a>
                </td>
                <td class="left">${item.name_display!}</td>
                <td class="left">${item.related_model!}</td>
                <td width="70px" class="left">
                    <#if item.event?default('')?length gt 0 >
                        <a id="event" href="/rubber/scheme/event/edit?scheme_id=${item.scheme_id}&f=${f}" class="t2" style="text-decoration: none;">事件(1)</a>
                    <#else>
                        <a id="event" href="/rubber/scheme/event/edit?scheme_id=${item.scheme_id}&f=${f}" class="t2" style="text-decoration: none;">事件(0)</a>
                    </#if>
                </td>
                <td width="70px" class="left">
                    <a id="a_rule" scheme="${item.scheme_id}" display="${item.name_display!}" class="t2">规则(${item.rule_count})</a>
                </td>
                <td width="70px" class="left">
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
    $("a[id='item_scheme']").on("click",function(){
        addorEditScheme(this.target)
    });

</script>