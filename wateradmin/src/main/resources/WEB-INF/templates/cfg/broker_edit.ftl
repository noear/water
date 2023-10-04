<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志配置-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        var broker_id = '${model.broker_id}';

        function save() {
            var vm = formToMap("form");
            vm.broker_id = broker_id;

            if (!vm.tag) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (broker_id == null) {
                broker_id = 0;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            top.layer.load(2);

            $.ajax({
                type: "POST",
                url: "/cfg/broker/edit/ajax/save",
                data: vm,
                success: function (data) {
                    top.layer.closeAll();

                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/broker?tag_name=" + vm.tag + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                },
                error: function (data) {
                    top.layer.closeAll();
                    top.layer.msg('网络请求出错...');
                }
            });
        }

        function del() {
            if (!broker_id) {
                return;
            }

            if (!confirm("确定要删除吗？")) {
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            $.ajax({
                type: "POST",
                url: "/cfg/broker/edit/ajax/del",
                data: {
                    "broker_id": broker_id
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/broker?tag_name=${model.tag!}&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function () {
            document.getElementById('source').value="${model.source!}";

            ctl_s_save_bind(document,save);
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left class="ln30">
        <h2><a onclick="history.back(-1)" href="#" class="noline">日志配置</a></h2> / 编辑
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
    <form>
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" autofocus value="${tag_name!}"/></td>
            </tr>
            <tr>
                <th>broker*</th>
                <td><input type="text" id="broker" value="${model.broker!}"/></td>
            </tr>
            <tr>
                <th>保留天数</th>
                <td><input type="text" id="keep_days" value="${model.keep_days!30}"/></td>
            </tr>
            <tr>
                <th>数据源</th>
                <td>
                    <select id="source">
                        <option value=""></option>
                        <#list option_sources as sss>
                            <option value="${sss}">${sss}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td><input type="text" class="longtxt" id="note" value="${model.note!}"/></td>
            </tr>
            <tr>
                <th>启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" value="1" type="checkbox" ${(model.is_enabled = 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <checkbox>
                        <label class="mar10-r"><input type="checkbox" id="is_alarm" ${(model.is_alarm = 1)?string('checked','')}  /><a>启用报警</a></label>
                    </checkbox>
                </td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>