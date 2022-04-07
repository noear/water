<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 安全名单</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function del(){
            var row_id = ${m.row_id!0};
            if(row_id < 1){
                return;
            }

            var vm = formToMap("#form");

            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/cfg/i18n/ajax/del",
                    data:{"row_id":row_id},
                    success:function(data){
                        if(1==data.code) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                parent.location.href = "/cfg/i18n?tag_name=" + vm.tag;
                            }, 800);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        function save() {
            var vm = formToMap("#form");

            if (!vm.tag || !vm.bundle || !vm.name) {
                top.layer.msg("tag 或 bundle 或 name 不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/i18n/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/i18n?tag_name="+vm.tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        $(function () {
            ctl_s_save_bind(document,save);
        })
    </script>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">国际化捆</a></h2> / 编辑
        </left>
        <right class="form">
            <n>ctrl + s 可快捷保存</n>
            <button type="button" onclick="save()">保存</button>
            <#if is_admin == 1>
                <button type="button" class="minor" onclick="del()">删除</button>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form id="form">
            <input type="hidden" id="row_id" value="${m.row_id!0}">
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value="${tag_name!}" autofocus/></td>
            </tr>
            <tr>
                <th>bundle*</th>
                <td><input type="text" id="bundle" value="${m.bundle!}" /></td>
            </tr>
            <tr>
                <th>lang</th>
                <td><input type="text" id="lang" value="${m.lang!}" /></td>
            </tr>
            <tr>
                <th>name*</th>
                <td><input type="text" id="name" value="${m.name!}" /></td>
            </tr>
            <tr>
                <th>value</th>
                <td><input type="text" class="longtxt" id="value" value="${m.value!}" /></td>
            </tr>
        </table>
        </form>
    </detail>
</main>

</body>
</html>