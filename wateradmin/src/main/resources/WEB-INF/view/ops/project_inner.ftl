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
        .tool{padding: 10px 0;}
        .tool a{display: inline-block; cursor: default; margin-right: 10px; padding: 0px 5px; border: 1px solid #ddd; border-radius: 4px; background: #f1f1f1;}
        .tool a:hover{background: #fff;}
    </style>
</head>
<script>
    function updateProjectStatus(project_id,is_enabled,hint) {
        if(confirm('确定要'+hint+'吗？') == false){
            return;
        }

        $.ajax({
            type:"POST",
            url:"/ops/project/ajax/disabled",
            data:{
                "project_id":project_id,
                "is_enabled":is_enabled
            },
            success:function (data) {
                if(data.code==1) {
                    top.layer.msg('操作成功');
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
            <a class="btn edit" href="/ops/project/edit?tag_name=${tag_name!}">新增</a>
        </#if>
    </left>
    <right>
        <@stateselector items="启用,未启用"/>
    </right>
</toolbar>

<datagrid class="list">
    <table>
        <thead>
            <tr>
                <td width="40px" >ID</td>
                <td width="130px" nowrap>项目名称</td>
                <td width="40px" nowrap>类型</td>
                <td>源码地址（Git）</td>
                <#if is_admin == 1>
                    <td  width="100px">操作</td>
                </#if>
            </tr>
        </thead>
        <tbody id="tbody">
            <#list list as m>
                <tr>
                    <td valign="top">${m.project_id}</td>
                    <td valign="top" class="left">${m.name!}</td>
                    <td valign="top">
                        <#if m.type == 0>服务</#if>
                        <#if m.type == 1>网站</#if>
                    </td>
                    <td class="left">
                        <div>${m.git_url}</div>
                        <div class="tool">
                            <a>测试</a>
                            <a>发布</a>
                        </div>
                    </td>
                    <#if is_admin == 1>
                        <td valign="top" width="100px" class="op">
                            <a href="/ops/project/edit?project_id=${m.project_id}" class="t2">编辑</a> |
                            <#if m.is_enabled==1>
                                <a onclick="updateProjectStatus('${m.project_id}',0 , '禁用')" class="t2">禁用</a>
                            <#else>
                                <a onclick="updateProjectStatus('${m.project_id}',1, '启用')" class="t2">启用</a>
                            </#if>
                        </td>
                    </#if>
                </tr>
            </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>