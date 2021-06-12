<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 嘿嘿测试</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function debug() {
            var target = $('#target').val();
            var msg = $('#msg').val();
            if (target == "" || target == null || target == undefined ) {
                top.layer.msg("target不能为空");
                return;
            }
            if (msg == "" || msg == null || msg == undefined ) {
                top.layer.msg("msg不能为空");
                return;
            }

            top.layer.confirm('确定发送嘿嘿测试', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/tool/heihei/ajax/submit",
                    data:{"target":target,"msg":msg},
                    success:function(data){
                        top.layer.msg(data.msg);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };
    </script>
</head>
<body>

<main>
    <blockquote>
        <h2 class="ln30">嘿嘿测试</h2>
    </blockquote>

    <detail>
        <form>
            <table>
                <tr>
                    <th>手机号</th>
                    <td><input type="text" id="target"/></td>
                </tr>
                <tr>
                    <th>内容</th>
                    <td><input type="text" id="msg"/></td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <@authRole name="water_p_admin">
                            <button type="button" onclick="debug()">发送</button>
                        </@authRole>
                    </td>
                </tr>
            </table>
        </form>
    </detail>

</main>

</body>
</html>