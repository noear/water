<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>

    <script>
        function saveEdit() {
            var vm = formToMap('form');

            if(!vm.tag){
                top.layer.msg("tag不能为空！");
                return;
            }

            if(!vm.name){
                top.layer.msg("name不能为空！");
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
                    url:"/sev/service/edit/ajax/save",
                    data:vm,
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg('操作成功');
                            setTimeout(function(){
                                location.href="/sev/service/inner?tag_name="+vm.tag;
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
            <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">服务列表</a></h2> / 添加
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
                    <th>tag*</th>
                    <td><input type="text" autofocus id="tag" value="${model.tag!}"/></td>
                </tr>
                <tr>
                    <th>service*</th>
                    <td><input type="text" autofocus id="name" value="${model.name!}"/></td>
                </tr>
                <tr>
                    <th>地址</th>
                    <td><input type="text" id="address" value="${model.address!}" /><n>（ip:port | host:port）</n></td>
                </tr>
                <tr>
                    <th>元信息</th>
                    <td><input type="text" id="meta" value="${model.meta!}" class="longtxt"/></td>
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
            </table>
        </form>
    </detail>

</main>

</body>
</html>