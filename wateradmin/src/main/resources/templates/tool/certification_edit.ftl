<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 应用监视-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>

    <script>
        function saveEdit() {
            var vm = formToMap('form');

            if(!vm.tag){
                top.layer.msg("tag不能为空！");
                return;
            }

            if(!vm.url){
                top.layer.msg("域名不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/tool/certification/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            parent.location.href="/tool/certification?tag_name="+vm.tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del(certification_id) {
            if (confirm('确定删除吗？') == false) {
                return;
            }

            let tag = $('#tag').val();
            let is_enabled = $('#is_enabled').prop('checked') ? 1 : 0;

            $.ajax({
                type: "POST",
                url: "/tool/certification/edit/ajax/del",
                data: {"certification_id": certification_id,},
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功');
                        setTimeout(function () {
                            parent.location.href = "/tool/certification?tag_name=" + tag + "&_state=" + (is_enabled == 1 ? 0 : 1);
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function () {
            ctl_s_save_bind(document,saveEdit);
        })
    </script>
</head>
<body>
<main>

    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">证书监视</a></h2> / 编辑
        </left>
        <right class="form">
            <#if is_admin == 1>
                <n>ctrl + s 可快捷保存</n>
                <button type="button" class="w80" onclick="saveEdit()">保存</button>
                <#if model.certification_id gt 0>
                    <button type="button" onclick="del(${model.certification_id})" class="minor">删除</button>
                </#if>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form>
            <table>
                <tr>
                    <th>tag*</th>
                    <td><input type="text" autofocus id="tag" value="${model.tag!}"/>
                        <input type="hidden" id="certification_id" value="${model.certification_id!0}">
                    </td>
                </tr>
                <tr>
                    <th>域名*</th>
                    <td><input class="longtxt" type="text" autofocus id="url" placeholder="https://..." value="${model.url!}"/></td>
                </tr>
                <tr>
                    <th>备注</th>
                    <td><input type="text"  id="note" value="${model.note!}"/></td>
                </tr>
                <tr>
                    <th>启用</th>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(model.is_enabled=1)?string("checked","")}><a></a></label>
                        </switcher>
                    </td>
                </tr>
            </table>
        </form>
    </detail>

</main>

</body>
</html>