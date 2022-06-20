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

            if(!vm.name){
                top.layer.msg("name不能为空！");
                return;
            }

            if(!vm.address){
                top.layer.msg("地址不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/tool/detection/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            parent.location.href="/tool/detection?tag_name="+vm.tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del(detection_id) {
            if (confirm('确定删除吗？') == false) {
                return;
            }

            let tag = $('#tag').val();
            let is_enabled = $('#is_enabled').prop('checked') ? 1 : 0;

            $.ajax({
                type: "POST",
                url: "/tool/detection/edit/ajax/del",
                data: {"detection_id": detection_id,},
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功');
                        setTimeout(function () {
                            parent.location.href = "/tool/detection?tag_name=" + tag + "&_state=" + (is_enabled == 1 ? 0 : 1);
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
            <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">应用监视</a></h2> / 编辑
        </left>
        <right class="form">
            <#if is_admin == 1>
                <n>ctrl + s 可快捷保存</n>
                <button type="button" class="w80" onclick="saveEdit()">保存</button>
                <#if model.detection_id gt 0>
                    <button type="button" onclick="del(${model.detection_id})" class="minor">删除</button>
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
                        <input type="hidden" id="detection_id" value="${model.detection_id!0}">
                    </td>
                </tr>
                <tr>
                    <th>应用名称*</th>
                    <td><input type="text" autofocus id="name" value="${model.name!}"/></td>
                </tr>
                <tr>
                    <th>检测地址</th>
                    <td>
                        <select id="protocol" class="w80">
                            <option value="http" ${(model.protocol == 'http')?string('selected','')}>http://</option>
                            <option value="https" ${(model.protocol == 'https')?string('selected','')}>https://</option>
                            <option value="tcp" ${(model.protocol == 'tcp')?string('selected','')}>tcp://</option>
                            <option value="grpc" ${(model.protocol == 'grpc')?string('selected','')}>grpc://</option>
                        </select>
                        <input style="width: 510px" type="text" id="address" value="${model.address!}" />
                    </td>
                </tr>
                <tr>
                    <th>检测间隔</th>
                    <td><input type="text" class="w150" id="check_interval" value="${model.check_interval!300}"/> 秒/次</td>
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