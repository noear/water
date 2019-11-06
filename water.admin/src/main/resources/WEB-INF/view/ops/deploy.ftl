<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 服务部署</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        summary{padding: 5px; background: #ddd;}
    </style>
    <script>
        function operate(task_id){
            location.href='/ops/deploy/operate?task_id='+task_id;
        }
    </script>
</head>
<body>
<main>
       <toolbar>
           <left>
                <button class="edit" onclick="location.href='/ops/deploy/edit'">发起流程</button>
           </left>
           <right>
                <@stateselector items="进行中,已完结"/>
           </right>
       </toolbar>

    <datagrid>
        <table>
            <thead>
            <tr>
                <td width="40px" >ID</td>
                <td width="200px" nowrap>项目名称</td>
                <td width="80px">版本</td>
                <td width="80px">开发人</td>
                <td>状态</td>
                <#if is_admin == 1>
                    <td colspan="2" width="30px"></td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list deployList as deploy>
                <tr>
                    <td>${deploy.task_id}</td>
                    <td>${deploy.project_name}</td>
                    <td>${deploy.version}</td>
                    <td>${deploy.developer}</td>
                    <td title="${deploy.desc}">
                            ${deploy.desc}
                    </td>
                    <#if is_admin == 1>
                        <td width="50px"><a onclick="operate('${deploy.task_id}')" style="cursor: pointer;color: blue;">操作</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</main>
</body>
</html>