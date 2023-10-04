
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算资源-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function saveEdit() {
            var vm = formToMap('form');
            vm.server_id = '${server.server_id}';

            if (!vm.tag) {
                top.layer.msg("标签不能为空！");
                return;
            }
            if (!vm.name) {
                top.layer.msg("服务器名称不能为空！");
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            $.ajax({
                type: "POST",
                url: "/cfg/server/edit/ajax/save",
                data: vm,
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/server?tag_name=" + vm.tag + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        function delDo() {
            var vm = formToMap('form');
            vm.server_id = '${server.server_id}';

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            if (confirm("确定要删除吗？")) {
                $.ajax({
                    type: "POST",
                    url: "/cfg/server/delete",
                    data: {server_id: vm.server_id},
                    success: function (data) {
                        if (data.code == 1) {
                            top.layer.msg('操作成功')
                            setTimeout(function () {
                                parent.location.href = "/cfg/server?tag_name=" + vm.tag + "&_state=" + _state;
                            }, 800);
                        } else {
                            top.layer.msg(data.msg);
                        }
                    }
                });
            }
        };

        $(function(){
            <#if is_admin = 1>
            ctl_s_save_bind(document,saveEdit);
            </#if>
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">计算资源</a></h2> / 编辑
    </left>
    <right class="form">
        <#if is_admin = 1>
            <n>ctrl + s 可快捷保存</n>
            <button type="button" onclick="saveEdit()">保存</button>
            <button type="button" class="minor" onclick="delDo()">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form>
    <table>
        <tr>
        <tr>
            <th>标签</th>
            <td>
                <input type="text" id="tag" value="${server.tag!}"/>
            </td>
        </tr>
        <tr>
            <th>资源名称</th>
            <td><input type="text" id="name" value="${server.name!}"/></td>
        </tr>
        <tr>
            <th>IAAS 实例</th>
            <td>
                <input type="text" id="iaas_key" value="${server.iaas_key!}"/>
            </td>
        </tr>
        <tr>
            <th>IAAS 类型</th>
            <td>
                <@enum group="iaas_type" style="select" id="iaas_type" value="${server.iaas_type!}"></@enum>
            </td>
        </tr>
        <tr>
            <th>IAAS 账号</th>
            <td>
                <select id="iaas_account">
                    <option value=""></option>
                    <#list accounts as m>
                    <option value="${m.tag}/${m.key}">${m.tag}/${m.key}</option>
                    </#list>
                </select>
                <script>
                    $('#iaas_account').val('${server.iaas_account!}');
                </script>
            </td>
        </tr>

        <tr>
            <th>外网地址</th>
            <td>
                <input type="text" id="address" class="longtxt" value="${server.address!}"/>
            </td>
        </tr>
        <tr>
            <th>内网地址</th>
            <td>
                <input type="text" id="address_local" class="longtxt" value="${server.address_local!}"/>
            </td>
        </tr>
        <tr style="display: none;">
            <th>本地映射</th>
            <td>
                <textarea id="hosts_local" style="height: 60px;">${server.hosts_local!}</textarea>
            </td>
        </tr>
        <tr style="display: none;">
            <th>备注</th>
            <td><input type="text" id="note" value="${server.note!}"/></td>
        </tr>
        <tr>
            <th>环境类型</th>
            <td>
                <select id="env_type">
                    <#if server.env_type == 0>
                        <option value="0" selected="selected">测试环境</option>
                        <option value="1">预生产环境</option>
                        <option value="2">生产环境</option>
                    </#if>
                    <#if server.env_type == 1>
                        <option value="1"  selected="selected">预生产环境</option>
                        <option value="0">测试环境</option>
                        <option value="2">生产环境</option>
                    </#if>
                    <#if server.env_type == 2>
                        <option value="2" selected="selected">生产环境</option>
                        <option value="0">测试环境</option>
                        <option value="1">预生产环境</option>
                    </#if>
                </select>
            </td>
        </tr>
        <tr>
            <th>启用</th>
            <td>
                <switcher>
                    <label><input id="is_enabled" value="1" type="checkbox" ${(server.is_enabled = 1)?string("checked","")}><a></a></label>
                </switcher>
            </td>
        </tr>

    </table>
    </form>
</detail>
</body>
</html>