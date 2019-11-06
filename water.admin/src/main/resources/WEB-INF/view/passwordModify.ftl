<!DOCTYPE HTML>
<html>
<head>
    <title>修改密码</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="${css}/main.css" />
    <script type="text/javascript" src="${js}/lib.js" ></script>
    <script src="${js}/layer.js"></script>
    <script type="text/javascript">
        function confirm() {
            top.layer.confirm('确认修改?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                top.layer.close(top.layer.index);
                var newPass = $("#newPass").val();
                var oldPass = $("#oldPass").val();
                var conNewPass = $("#conNewPass").val();

                if(oldPass==null||oldPass==""){
                    top.layer.msg("原密码不能为空")
                } else if(newPass==null||newPass=="" || conNewPass==null||conNewPass=="") {
                    top.layer.msg("新密码不能为空")
                } else if (newPass != conNewPass){
                    top.layer.msg("请输入相同的新密码")
                } else {
                    $.ajax({
                        type:"POST",
                        url:"/user/confirmModify",
                        data:{"newPass":newPass,"oldPass":oldPass},
                        success: function(data){
                            if(data.code == 1){
                                top.layer.msg('修改成功,去重新登陆吧');
                                setTimeout(function(){
                                    window.close();
                                },1500);

                            } else {
                                top.layer.msg(data.msg)
                            }
                        }
                    });
                }

            });
        }
    </script>
    <style>
        detail input{width: 200px;}
        detail table{margin: auto;}
    </style>
</head>
<body>
    <detail>
        <table>
            <tr>
                <td>原密码：</td><td><input type="text" id="oldPass" placeholder="请输入原密码"></td>
            </tr>
            <tr>
                <td>新密码：</td><td><input type="text" id="newPass" placeholder="请输入新密码" ></td>
            </tr>
            <tr>
                <td>确认新密码：</td><td><input type="text" id="conNewPass" placeholder="请确认新密码"></td>
            </tr>
            <tr>
                <td></td><td><button onclick="confirm()">修改</button></td>
            </tr>
        </table>
    </detail>
</body>
</html>
