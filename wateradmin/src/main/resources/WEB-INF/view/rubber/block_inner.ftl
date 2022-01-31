<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据block</title>
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
        <form>
        <a class="w50">区块：</a><input type="text"  name="name" class="w350" placeholder="代号或显示名" id="name" value="${name!}"/>
        <button type="submit">查询</button>
            <#if is_operator == 1>
                <a href="/rubber/block/edit"  class="btn edit mar10-l">新增</a>
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
                <td width="180px" class="left">代号</td>
                <td class="left">显示名</td>
                <td width="260px" class="left">数据源</td>
                <td width="130px" class="left">备注</td>
                <td width="110px"></td>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from">
            <#list blocks as m>
                <tr>
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.block_id}" /><a></a></label></checkbox></td>
                    <td>${m.block_id}</td>
                    <td class="left"><a class="t2" href="/rubber/block/edit?block_id=${m.block_id}">${m.name!}</a></td>
                    <td class="left">${m.name_display!}</td>
                    <td class="left">
                        <#if m.related_db?has_content>
                            ${m.related_db}#${m.related_tb}
                        </#if>
                    </td>
                    <td class="left">${m.note!}</td>
                    <td class="op">
                        <a class="t2" href="${raas_uri}/preview.js?block=${m.tag!}/${m.name!}" target="_blank">预览</a> |
                        <a class="t2" href="/rubber/block/items?block_id=${m.block_id}">内容管理</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>