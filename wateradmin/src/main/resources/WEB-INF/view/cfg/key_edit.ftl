<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 访问密钥</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function del() {
            let key_id = ${m.key_id!0};
            if (key_id < 1) {
                return;
            }

            let vm = formToMap("#form");
            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            top.layer.confirm('确定删除', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/cfg/key/ajax/del",
                    data: {"key_id": key_id},
                    success: function (data) {
                        if (1 == data.code) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                parent.location.href = "/cfg/key?tag_name=" + vm.tag + "&_state=" + _state;
                            }, 800);
                        } else {
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        }

        function save() {
            let vm = formToMap("#form");

            if (!vm.tag || !vm.access_key || !vm.access_secret_key) {
                top.layer.msg("tag 或 access_key 或 access_secret_key 不能为空！");
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            $.ajax({
                type: "POST",
                url: "/cfg/key/edit/ajax/save",
                data: vm,
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/key?tag_name=" + vm.tag + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function () {
            ctl_s_save_bind(document,save);
        })
    </script>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">访问密钥</a></h2> / 编辑
        </left>
        <right class="form">
            <n>ctrl + s 可快捷保存</n>
            <button type="button" onclick="save()">保存</button>
            <#if is_admin == 1>
                <button type="button" class="minor" onclick="del()">删除</button>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form id="form">
            <input type="hidden" id="key_id" value="${m.key_id!0}">
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value="${tag_name!}" autofocus/></td>
            </tr>
            <tr>
                <th>label</th>
                <td><input type="text" id="label" value="${m.label!}" /></td>
            </tr>
            <tr>
                <th>description</th>
                <td><input type="text" class="longtxt" id="description" value="${m.description!}" /></td>
            </tr>
            <tr>
                <th>access_key</th>
                <td><input disabled="disabled" type="text" id="access_key" value="${m.access_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_key</th>
                <td><input disabled="disabled" type="text" id="access_secret_key" value="${m.access_secret_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_salt</th>
                <td><input disabled="disabled" type="text" id="access_secret_salt" value="${m.access_secret_salt!}" /></td>
            </tr>
            <tr>
                <th>启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" value="1" type="checkbox" ${(m.is_enabled = 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
        </table>
        </form>
    </detail>
</main>

</body>
</html>