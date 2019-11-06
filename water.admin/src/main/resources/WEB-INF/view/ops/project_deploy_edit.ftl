<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 发起部署</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveDeploy(){
            var deploy_name = $('#deploy_name').val();

            if(!deploy_name){
                layer.msg("请输入部署流程名称");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/ops/project/project_deploy_edit/ajax/save",
                data: {
                    "deploy_name":deploy_name,
                    "project_id":'${project_id!}',
                    "deploy_id":'${deploy.deploy_id}'
                },
                success: function (data) {
                    if(data.code==1){
                        top.layer.msg("编辑成功");
                        location.href='/ops/project/project_deploy_inner?project_id=${project_id!}';
                    }else{
                        top.layer.msg("编辑失败");
                    }
                }
            })
        };

    </script>
    <style>
    </style>
</head>
<body>
<detail>
    <form>
        <h2><#if deploy.deploy_id == 0>新增</#if><#if deploy.deploy_id != 0>编辑</#if>部署流程（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
        <hr/>
        <table>
            <tr>
                <td>流程名称</td>
                <td><input type="text" id="deploy_name" value="${deploy.name!}"/></td>
            </tr>
            <tr>
                <td></td>
                <td><button type="button" onclick="saveDeploy();">保存</button></td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>