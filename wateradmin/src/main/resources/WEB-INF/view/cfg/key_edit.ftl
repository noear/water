<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 访问密钥</title>
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
                    url:"/cfg/key/ajax/del",
                    data:{"row_id":row_id},
                    success:function(data){
                        if(1==data.code) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                parent.location.href = "/cfg/key?tag_name=" + vm.tag;
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

            if (!vm.tag || !vm.access_key || !vm.access_secret_key) {
                top.layer.msg("tag 或 access_key 或 access_secret_key 不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/key/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/key?tag_name="+vm.tag;
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
            <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">访问密钥</a></h2> / 编辑
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
                <th>name</th>
                <td><input type="text" id="name" value="${m.name!}" /></td>
            </tr>
            <tr>
                <th>access_key*</th>
                <td><input type="text" id="access_key" value="${m.access_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_key*</th>
                <td><input type="text" class="longtxt" id="access_secret_key" value="${m.access_secret_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_salt</th>
                <td><input type="text" class="longtxt" id="access_secret_salt" value="${m.access_secret_salt!}" /></td>
            </tr>
            <tr>
                <th>description</th>
                <td><input type="text" class="longtxt" id="description" value="${m.description!}" /></td>
            </tr>

        </table>
        </form>
    </detail>
</main>

</body>
</html>