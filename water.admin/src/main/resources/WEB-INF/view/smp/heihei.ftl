<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 嘿嘿测试</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
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

            top.layer.confirm('确定发送嘿嘿调试', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/smp/heihei/ajax/submit",
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

        <detail>
            <form>
            <h2>嘿嘿测试</h2>
            <hr/>
            <table>
                <tr>
                    <td>手机号：</td>
                    <td><input type="text" id="target"/></td>
                </tr>
                <tr>
                    <td>内容：</td>
                    <td><input type="text" id="msg"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <#if is_admin == 1>
                        <button type="button" onclick="debug()">发送</button>
                        </#if>
                    </td>
                </tr>
            </table>
            </form>
        </detail>

</main>

</body>
</html>