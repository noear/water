<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 账户管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function add() {
            location.href = "/cfg/account/add";
        };

        function deleteIp(id){
            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/cfg/whitelist/delete",
                    data:{"id":id},
                    success:function(data){
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            location.reload();
                        },1000);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };
    </script>
</head>
<body>
<main>
        <toolbar>
            <left>
                <form>
                    账户：<input type="text"  name="name" placeholder="账户名" id="name"/>&nbsp;&nbsp;
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                        <button type="button" class="edit" onclick="add();" >新增</button>
                    </#if>
                </form>
            </left>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="50px">ID</td>
                    <td width="150px">账户名</td>
                    <td width="150px">报警手机号</td>
                    <td width="120px">访问ID</td>
                    <td width="150px">访问密钥</td>
                    <td width="85px">是否管理员</td>
                    <td>备注</td>
                    <#if is_admin == 1>
                        <td width="60px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody" >

                <#list list as m>
                    <tr>
                        <td>${m.account_id!}</td>
                        <td style="text-align: left">${m.name!}</td>
                        <td style="text-align: left">${m.alarm_mobile!}</td>
                        <td style="text-align: left">${m.access_id!}</td>
                        <td style="text-align: left">${m.access_key!}</td>
                        <td>
                            <#if m.is_admin == 1>是</#if>
                            <#if m.is_admin == 0>否</#if>
                        </td>
                        <td style="text-align: left">${m.note!}</td>
                        <#if is_admin == 1>
                            <td><a href="/cfg/account/edit?account_id=${m.account_id!}" style="cursor: pointer;color: blue;">编辑</a></td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>
</main>

</body>
</html>