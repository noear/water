<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 参与者-编辑</title>
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
        <h2>编辑信息（<a href="#" onclick="javascript:history.back(-1);" class="t2 online">返回</a>）
        </h2>
        <hr/>
        <table>
            <tr>
                <td>分类标签</td>
                <td><input type="text" name="tag" value="${tag_name!}" id="tag"/>
                    <input type="hidden" name="actor_id" value="${actor_id!}">
                </td>
            </tr>
            <tr>
                <td>代号</td>
                <td><input type="text" name="name"/></td>
            </tr>
            <tr>
                <td>显示名</td>
                <td><input type="text" name="name_display"/></td>
            </tr>
            <tr>
                <td>备注</td>
                <td>
                    <input type="text" name="note">
                </td>

            </tr>
            <tr>
                <td></td>
                <td>
                    <#if is_operator ==1>
                        <button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;&nbsp;
                    </#if>
                    <#if is_admin == 1>
                        <button type="button" onclick="del()" class="minor">删除</button>
                    </#if>
                </td>
            </tr>
        </table>
    </form>
</detail>
<script>

    $(function () {
        var id = '${actor_id!}';
        var temp_id = 0;
        if (id != '') {
            $.post("/rubber/actor/ajax/getactor", {actor_id: id}, function (data) {
                $("#actor_add_edit").frmDeSerialize(data);
            })
        }
    });

    function saveEdit() {
        var res=  $("#actor_add_edit").serialize();
        var tag = $('#tag').val();

        $.post("/rubber/actor/edit/ajax/save",res, function (data) {
           if(data.code==1){
               top.layer.msg("保存成功");
               parent.location.href="/rubber/actor?tag_name="+tag;
           }else{
               top.layer.msg("保存失败");
           }
        })
    };

    //删除角色
    function del() {
        top.layer.confirm('确定要删除吗？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                url: "/rubber/actor/edit/ajax/del",
                data: {
                    "actor_id": '${actor_id!}'
                },
                success: function (data) {
                    if(data.code==1){
                        top.layer.msg(data.msg);
                        parent.location.href="/rubber/actor?tag_name=${tag_name!}";
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        });
    };


</script>
</body>
</html>