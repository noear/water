<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 枚举管理-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveEdit() {
            var type = $('#type').val();
            var name = $('#name').val();
            var value = $('#value').val();

            $.ajax({
                type:"POST",
                url:"/cfg/enum/edit/ajax/save",
                data:{"id":${e.id},"type":type,"name":name,"value":value},
                traditional: true,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            location.href="/cfg/enum";
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
                <h2>编辑枚举（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
                <hr/>
                <table>
                    <tr>
                        <td>枚举名称</td>
                        <td><input type="text" id="type" value="${e.type!}" autofocus/></td>
                    </tr>
                    <tr>
                        <td>枚举值</td>
                        <td><input type="text" id="value" value="${e.value!}"/></td>
                    </tr>
                    <tr>
                        <td>枚举值名称</td>
                        <td><input type="text" id="name" value="${e.name!}"/></td>
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