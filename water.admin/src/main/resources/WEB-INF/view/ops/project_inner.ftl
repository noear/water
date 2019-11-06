<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 项目配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function add() {
        location.href = "/ops/project/add?tag_name=${tag_name!}";
    };
    
    function updateProjectStatus(project_id,is_enabled) {
        $.ajax({
            type:"POST",
            url:"/ops/project/ajax/disabled",
            data:{
                "project_id":project_id,
                "is_enabled":is_enabled
            },
            success:function (data) {
                if(data.code==1) {
                    top.layer.msg(data.msg);
                    setTimeout(function(){
                        parent.location.href="/ops/project";
                    },1000);
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    }
</script>
<body>

<toolbar>
    <left>
        <#if is_admin == 1>
            <button class="edit" onclick="add();" type="button">新增</button>
        </#if>
    </left>
    <right>
        <@stateselector items="启用,未启用"/>
    </right>
</toolbar>

<datagrid>
    <table>
        <thead>
            <tr>
                <td width="40px" >ID</td>
                <td width="130px" nowrap>项目名称</td>
                <td width="40px" nowrap>类型</td>
                <td>源码地址（git）</td>
                <#if is_admin == 1>
                    <td colspan="3" width="30px">操作</td>
                </#if>
            </tr>
        </thead>
        <tbody id="tbody">
            <#list list as m>
                <tr>
                    <td>${m.project_id}</td>
                    <td style="text-align: left">${m.name!}</td>
                    <td>
                        <#if m.type == 0>服务</#if>
                        <#if m.type == 1>网站</#if>
                    </td>
                    <td style="text-align: left">${m.git_url}</td>
                    <#if is_admin == 1>
                        <td width="100px">
                            <a href="/ops/project/edit?project_id=${m.project_id}" style="cursor: pointer;color: blue;">编辑</a> |
                            <#if m.is_enabled==1>
                                <a onclick="updateProjectStatus('${m.project_id}',0)" style="cursor: pointer;color: blue;text-decoration: underline">禁用</a>
                            <#else>
                                <a onclick="updateProjectStatus('${m.project_id}',1)" style="cursor: pointer;color: blue;text-decoration: underline">启用</a>
                            </#if>
                        </td>
                        <td width="60px"><a style="cursor: pointer;color: blue;" href="/ops/project/project_deploy_inner?project_id=${m.project_id}">部署流程</a></td>
                        <td width="50px"><a style="cursor: pointer;color: blue;" href="">更多...</a></td>
                    </#if>
                </tr>
            </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>