<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 账户管理-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveEdit() {
            var name = $('#name').val();
            var alarm_mobile = $('#alarm_mobile').val();
            var note = $('#note').val();
            var access_id = $('#access_id').val();
            var access_key = $('#access_key').val();

            if (name == null || name == "" || name == undefined) {
                top.layer.msg("账户名不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/account/edit/ajax/save",
                data:{"account_id":${account.account_id},"name":name,"alarm_mobile":alarm_mobile,"note":note,"access_id":access_id,"access_key":access_key},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            location.href="/cfg/account";
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        function genKey(type) {
            $.ajax({
                type:"POST",
                url:"/cfg/account/genKey",
                data:{"type":type},
                success:function (data) {
                    if (type == 1) {
                        $('#access_id').val(data.msg);
                    }
                    if (type == 2) {
                        $('#access_key').val(data.msg);
                    }
                }
            });
        };
    </script>
</head>
<body>
<main>
        <detail>
            <form>
            <h2>编辑账户（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
            <hr/>
            <table>
                <tr>
                <tr>
                    <td>账户名</td>
                    <td><input type="text" id="name" value="${account.name!}" autofocus/></td>
                </tr>
                <tr>
                    <td>报警手机号</td>
                    <td><input type="text" id="alarm_mobile" value="${account.alarm_mobile!}"/></td>
                </tr>
                <tr>
                    <td>访问ID</td>
                    <td>
                        <input type="text" id="access_id" value="${account.access_id!}"/>
                        <a onclick="genKey(1);" style="cursor:pointer;color: blue;">生成访问ID</a>
                    </td>
                </tr>
                <tr>
                    <td>访问密钥</td>
                    <td>
                        <input type="text" id="access_key" value="${account.access_key!}"/>
                        <a onclick="genKey(2);" style="cursor:pointer;color: blue;">生成访问密钥</a>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input type="text" id="note" value="${account.note!}"/></td>
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