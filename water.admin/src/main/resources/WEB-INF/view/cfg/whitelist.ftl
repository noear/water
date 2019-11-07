<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - IP白名单</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function add() {
            location.href = "/cfg/whitelist/add";
        };

        function deleteIp(id){
            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/cfg/whitelist/ajax/del",
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
            <cell>
                <form>
                    IP：<input type="text"  name="ip" placeholder="IP" id="ip"/>&nbsp;&nbsp;
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                        <button type="button" class="edit" onclick="add();">新增</button>
                    </#if>
                </form>
            </cell>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="50px">ID</td>
                    <td width="150px">tag</td>
                    <td width="150px">IP</td>
                    <td>备注</td>
                    <td width="60px">操作</td>
                </tr>
                </thead>
                <tbody id="tbody" >

                <#list list as m>
                    <tr>
                        <td>${m.id}</td>
                        <td style="text-align: left">${m.tag!}</td>
                        <td style="text-align: left">${m.ip!}</td>
                        <td style="text-align: left">${m.note!}</td>
                        <#if is_admin == 1>
                            <td><a style="cursor: pointer;color: blue;" onclick="deleteIp('${m.id}');">删除</a></td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>
</main>
</body>
</html>