<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 查询简报编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/formhelper.js"></script>

</head>
<body>
<detail>
    <form id="actor_add_edit">
        <h2>简报编辑（<a href="#" onclick="javascript:history.back(-1);" class="t2 online">返回</a>）
        </h2>
        <hr/>
        <input value="${report.id}" id="id" style="display: none"/>
        <table>
            <tr>
                <td>标签*</td>
                <td>
                    <input type="text" id="tag" name="tag" value="${report.tag!}" id="tag"/>
                </td>
            </tr>
            <tr>
                <td>名称*</td>
                <td><input type="text" id="name" value="${report.name!}" name="name"/></td>
            </tr>
            <tr>
                <td>简报描述</td>
                <td>
                    <input type="text" id="note" value="${report.note!}" class="longtxt" />
                </td>
            </tr>
            <tr>
                <td>查询变量</td>
                <td>
                    <input type="text" id="args" value="${report.args!}" class="longtxt" placeholder="name:value,name:value...多个用逗号隔开"/>
                </td>
            </tr>
            <tr>
                <td>查询代码</td>
                <td><textarea name="code" id="code" style="height: 200px;">${report.code!}</textarea></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <button type="button" onclick="saveEdit()">保存</button>
                </td>
            </tr>
        </table>
    </form>
</detail>
<script>
    function saveEdit() {
        var id = $('#id').val();
        var tag = $('#tag').val();
        var name = $('#name').val();
        var code = $('#code').val();
        var note = $('#note').val();
        var args = $('#args').val();
        if (!tag){
            layer.msg("分类标签不能为空");
            return;
        }
        if (!name){
            layer.msg("查询名称不能为空");
            return;
        }
        if (!code){
            layer.msg("脚本不能为空");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/dev/report/edit/ajax/save",
            data: {
                "id": id,
                "tag": tag,
                "name": name,
                "code": code,
                "note":note,
                "args":args
            },
            success: function (data) {
                if(data.code==1){
                    top.layer.msg(data.msg);
                    parent.location.href="/dev/report?tag="+tag;
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    };

</script>
</body>
</html>