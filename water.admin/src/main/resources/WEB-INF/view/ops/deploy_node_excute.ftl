<div>
    <table id="excuteNode" style="margin-top: 10px;">
        <tr>
            <td width="110px;" class="right"><label style="color: red">*</label>名称：</td>
            <td><input type="text" style="width:250px;height:30px;" name="nodeName" id="nodeName" value="${node.note!}"></td>
        </tr>
        <tr>
            <td class="right">资源：</td>
            <td>
                <select name="server" id="server" style="width:254px;" onchange="changeServer()">
                    <#list servers as m>
                        <#if m.server_id==server_id>
                            <option value="${m.server_id}" selected>${m.name}</option>
                        <#else>
                            <option value="${m.server_id}">${m.name}</option>
                        </#if>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td class="right">资源操作：</td>
            <td>
                <select name="operate" id="operate" style="width:254px;">
                    <#list operates as m>
                        <#if m.operate_id==node.operate_id>
                            <option value="${m.operate_id}" selected>${m.name}</option>
                        <#else>
                            <option value="${m.operate_id}">${m.name}</option>
                        </#if>
                    </#list>
                </select>
            </td>
        </tr>
    </table>
    <script>
        function changeServer() {

            var server_id = $("#server").val();
            $.ajax({
                type:"POST",
                url:"/ops/project/deploy_node_excute/ajax/changeServer",
                async: false,
                data:{
                    "server_id":server_id
                },
                success:function (data) {
                    var htm = "";
                    for(var i in data){
                        var obj = data[i];
                        var operate_id = obj.operate_id;
                        var name = obj.name;
                        htm = htm + '<option value="'+operate_id+'">'+name+'</option>';
                    }
                    $("#operate").empty();
                    $("#operate").append(htm);
                }
            });
        };
    </script>
</div>