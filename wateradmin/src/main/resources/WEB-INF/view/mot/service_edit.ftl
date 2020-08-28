<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        function saveEdit() {
            var vm = formToMap('form');

            if(!vm.name){
                top.layer.msg("名字不能为空！");
                return;
            }

            if(!vm.address){
                top.layer.msg("地址不能为空！");
                return;
            }

            top.layer.confirm('确定保存操作', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/mot/service/edit/ajax/save",
                    data:vm,
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                location.href="/mot/service";
                            },800);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });
        }

        $(function () {
            ctl_s_save_bind(document,saveEdit);
        })
    </script>
</head>
<body>
<main>

    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">服务状态</a></h2> / 添加
        </left>
        <right class="form">
            <#if is_admin == 1>
                <n>ctrl + s 可快捷保存</n>
                <button type="button" class="w80" onclick="saveEdit()">保存</button>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form>
            <table>
                <tr>
                <tr>
                    <th>名称</th>
                    <td><input type="text" autofocus id="name" value="${model.name!}"/><n>（name | web:name）</n></td>
                </tr>
                <tr>
                    <th>地址</th>
                    <td><input type="text" id="address" value="${model.address!}" /><n>（ip | ip:port | x://host）</n></td>
                </tr>
                <tr>
                    <th>检查类型</th>
                    <td><select id="check_type">
                            <option value="0" selected="selected">被动检查</option>
                            <option value="1">主动签到</option>
                        </select>
                        <script>
                            $('#check_type').val("${model.check_type!0}");
                        </script>
                    </td>
                </tr>
                <tr>
                    <th>检查路径</th>
                    <td><input  type="text" id="check_url" value="${model.check_url!'/'}" /><n>（/x）</n></td>
                </tr>
                <tr>
                    <th>启动备注</th>
                    <td><input type="text" id="note" value="${model.note!}" class="longtxt"/></td>
                </tr>
            </table>
        </form>
    </detail>

</main>

</body>
</html>