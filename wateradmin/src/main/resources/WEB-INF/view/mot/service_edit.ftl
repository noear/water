<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="https://static.kdz6.cn/lib/vue.min.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        var viewModel = ${data};

        function saveEdit() {
            if(!viewModel.name){
                top.layer.msg("名字不能为空！");
                return;
            }

            if(!viewModel.address){
                top.layer.msg("地址不能为空！");
                return;
            }

            top.layer.confirm('确定保存操作', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/mot/service/edit/ajax/save",
                    data:viewModel,
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                location.href="/mot/service";
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });
        }

        $(function () {
            window.vue = new Vue({
                el:'#view',
                data: viewModel
            });
        })
    </script>
</head>
<body>
<main>

    <blockquote>
        <h2><a href="#" onclick="javascript:history.back(-1);" class="t2">服务状态</a> / 添加</h2>
    </blockquote>

        <detail>
            <form>
                <table id="view">
                    <tr>
                    <tr>
                        <td>名称</td>
                        <td><input type="text" autofocus v-model="name"/></td>
                    </tr>
                    <tr>
                        <td>地址</td>
                        <td><input type="text" v-model="address"/><note>（ip | ip:port | x://host）</note></td>
                    </tr>
                    <tr>
                        <td>检查类型</td>
                        <td><select v-model="check_type" >
                            <option value="0" selected="selected">被动检查</option>
                            <option value="1">主动签到</option>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td>检查路径</td>
                        <td><input  type="text" v-model="check_url"/></td>
                    </tr>
                    <tr>
                        <td>启动备注</td>
                        <td><input type="text" v-model="note" class="longtxt"/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><button type="button" onclick="saveEdit()">保存</button></td>
                    </tr>
                </table>
            </form>
        </detail>

</main>

</body>
</html>