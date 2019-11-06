<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - IP白名单-新增</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveAdd() {
            var tag = $('#tag').val();
            var ip = $('#ip').val();
            var note = $('#note').val();

            if (ip == null || ip == "" || ip == undefined) {
                top.layer.msg("IP不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/whitelist/add/ajax/save",
                data:{"tag":tag,"ip":ip,"note":note},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            location.href="/cfg/whitelist?tag_name="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
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
                <h2>新建安全名单（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
                <hr/>
                <table>
                    <tr>
                    <tr>
                        <td>tag</td>
                        <td><input type="text" id="tag" autofocus/></td>
                    </tr>
                    <tr>
                        <td>IP</td>
                        <td><input type="text" id="ip"/></td>
                    </tr>
                    <tr>
                        <td>备注</td>
                        <td><input type="text" id="note"/></td>
                    </tr>

                    <tr>
                        <td></td>
                        <td><button type="button" onclick="saveAdd()">保存</button></td>
                    </tr>
                </table>
                </form>
            </detail>
    </main>
</body>
</html>