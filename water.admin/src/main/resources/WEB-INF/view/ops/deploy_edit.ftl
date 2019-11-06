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
            var project_id = $('#project').val();
            var deploy_id = $('#deploy').val();
            var product_manager = $('#product_mannager').val();
            var version = $('#version').val();
            var note = $('#note').val();

            if(project_id==null && project_id==""){
                layer.msg("请选择项目！");
                return;
            }
            if(!deploy_id){
                layer.msg("请选择部署流程！");
                return;
            }
            if(product_manager==null || product_manager == ""){
                layer.msg("请选择产品人！");
                return;
            }
            if(version==null || version == ""){
                layer.msg("请选择版本号！");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/ops/deploy/ajax/save",
                data: {
                    "project_id":project_id,
                    "deploy_id":deploy_id,
                    "product_manager":product_manager,
                    "version":version,
                    "note":note
                },
                success: function (data) {
                    if(data.code==1){
                        top.layer.msg(data.msg, {icon: 1, time: 1500}, function () {
                            parent.location.href='/ops/deploy/@项目部署';
                        });
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            })
        };

        function changeProject() {
            var project_id = $("#project").val();
            $.ajax({
                type: "POST",
                url: "/ops/deploy/ajax/getProjectDeploy",
                data: {
                    "project_id":project_id
                },
                success: function (data) {
                    var htm = "";
                    for (var i in data) {
                        var obj = data[i];
                        htm = htm + '<option value="'+obj.deploy_id+'">'+obj.name+'</option>';
                    }
                    $("#deploy").empty();
                    $("#deploy").append(htm);
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
    <h2>发起部署（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
    <hr/>
    <table>
        <tr>
            <td>项目</td>
            <td width="300px">
                <select id="project" onchange="changeProject()">
                    <#list projectList as project>
                        <option value="${project.project_id}">${project.name}</option>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td>部署流程</td>
            <td width="300px">
                <select id="deploy">
                    <#list deployList as deploy>
                        <option value="${deploy.deploy_id}">${deploy.name}</option>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td>产品人</td>
            <td>
                <select id="product_mannager">
                    <#list pmList as user>
                        <option value="${user.cn_name}">${user.cn_name}</option>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td>版本</td>
            <td><input type="text" id="version"/></td>
        </tr>
        <tr>
            <td>变更描述</td>
            <td>
                <textarea id="note"></textarea>
            </td>
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