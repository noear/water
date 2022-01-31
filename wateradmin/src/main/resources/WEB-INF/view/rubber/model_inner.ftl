<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 规则计算</title>
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
<main>
<toolbar>
    <form method="post">
        <a class="w50">模型：</a><input type="text"  name="name" class="w350" placeholder="代号或显示名" id="name" value="${name!}"/>
        <button type="submit">查询</button>
        <#if is_admin == 1>
            <a href="/rubber/model/edit?f=${f}" class="btn edit mar10-l">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
            <td width="50">ID</td>
            <td width="180px" class="left">代号</td>
            <td class="left">显示名</td>
            <td width="220px" class="left">相关数据库</td>
            <td width="60px">字段<br/>数量</td>
            <td width="110px"></td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list models as m>
            <tr>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.model_id}" /><a></a></label></checkbox></td>
                <td>${m.model_id}</td>
                <td class="left">
                    <a class="t2" href="/rubber/model/edit?model_id=${m.model_id}&f=${f}">${m.name!}</a>
                </td>
                <td class="left">${m.name_display!}</td>
                <td class="left">${m.related_db!}</td>
                <td>${m.field_count}</td>
                <td>
                    <a class="t2" href="${raas_uri!}/preview.js?model=${m.tag!}/${m.name!}" target="_blank">预览</a> |
                    <a class="t2" href="/rubber/model/field?model_id=${m.model_id}&f=${f}">编辑字段</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</main>
</body>
</html>