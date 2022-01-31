<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 参与者-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>

</head>
<body>
<toolbar classs="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">参与人员</a></h2> / 编辑
    </left>
    <right>
        <#if (is_admin == 1) && ((actor_id!0) > 0) >
            <button type="button" onclick="del()" class="minor">删除</button>
        </#if>
    </right>
</toolbar>
<detail>
    <form id="actor_add_edit">
        <table>
            <tr>
                <th>分类标签</th>
                <td><input type="text" name="tag" value="${tag_name!}" id="tag"/>
                    <input type="hidden" name="actor_id" value="${actor_id!}">
                </td>
            </tr>
            <tr>
                <th>代号</th>
                <td><input type="text" name="name"/></td>
            </tr>
            <tr>
                <th>显示名</th>
                <td><input type="text" name="name_display"/></td>
            </tr>
            <tr>
                <th>备注</th>
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