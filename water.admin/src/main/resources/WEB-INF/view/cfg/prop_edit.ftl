<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        function saveEdit() {
            var row_id = '${row_id}';
            var tag = $('#tag').val();
            var key = $('#key').val();
            var url = $('#url').val();
            var type = $('#type').val();
            var user = $('#user').val();
            var password = $('#password').val();
            var explain = $('#explain').val();

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (key == null || key == "" || key == undefined) {
                top.layer.msg("配置项关键字不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/prop/edit/ajax/save",
                data:{"row_id":row_id,"name":name,"tag":tag,"key":key,"type":type,"url":url,"user":user,"password":password,"explain":explain},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/cfg/prop?tag_name="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>
        <detail>
            <form>
            <h2>编辑配置（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
            <hr/>
            <table>
                <tr>
                <tr>
                    <td>tag*</td>
                    <td><input type="text" id="tag" autofocus value="${cfg.tag!}"/></td>
                </tr>
                <tr>
                <td>key*</td>
                <td><input type="text" id="key" value="${cfg.key!}"/></td>
                </tr>
                <tr>
                <td>type</td>
                <td>
                    <@enum group="config_type" style="select" id="type" value="${cfg.type!}" ></@enum>
                </td>
                </tr>
                <tr>
                    <td>url</td>
                    <td><textarea  id="url" >${cfg.url!}</textarea></td>
                </tr>
                <tr>
                    <td>user</td>
                    <td><input type="text" id="user" value="${cfg.user!}"/></td>
                </tr>
                <tr>
                    <td>password</td>
                    <td><input type="text" id="password" value="${cfg.password!}"/></td>
                </tr>
                <tr>
                    <td>explain</td>
                    <td><input type="text" id="explain" value="${cfg.explain!}" /></td>
                </tr>

                <tr>
                    <td></td>
                    <td><button type="button" onclick="saveEdit()">保存</button></td>
                </tr>
            </table>
            </form>
        </detail>

</body>
</html>