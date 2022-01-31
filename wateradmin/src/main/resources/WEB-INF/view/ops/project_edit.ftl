<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 项目配置-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        $(function (){
            $('#developer').val('${project.developer!}');
        });

        function save() {
            var project_id = '${project.project_id}';
            var tag = $('#tag').val();
            var name = $('#name').val();
            var git_url = $('#git_url').val();
            var note = $('#note').val();
            var developer = $('#developer').val();


            if (!tag) {
                top.layer.msg("标签不能为空！");
                return;
            }
            if (!name) {
                top.layer.msg("项目名称不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/ops/project/edit/ajax/save",
                data:{
                    "project_id":project_id,
                    "tag":tag,"name":name,
                    "git_url":git_url,
                    "note":note,
                    "developer":developer
                },
                traditional: true,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            parent.location.href="/ops/project?tag_name="+tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {

        }
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">项目配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" class="w80" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>


<detail>
    <form>
        <table>
            <tr>
            <tr>
                <th>标签</th>
                <td width="300px">
                    <input type="text" id="tag" value="${project.tag!}"/>
                </td>
            </tr>
            <tr>
                <th>项目名称</th>
                <td><input type="text" id="name" value="${project.name!}"/></td>
            </tr>
            <tr>
                <th>描述</th>
                <td><input type="text" id="note" value="${project.name!}"/></td>
            </tr>
            <tr>
                <th>开发人</th>
                <td>
                    <select id="developer">
                        <option value=""></option>
                        <#list users! as user>
                            <option value="${user.cn_name!}">${user.cn_name!}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>Git密钥</th>
                <td>
                    <select id="git_token">
                        <option value=""></option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>Git地址</th>
                <td>
                    <input type="text" id="git_url" value="${project.git_url!}" class="longtxt"/>
                </td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>