<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 网关配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>

    </style>

    <script>

        function save() {
            var key = $("#key").val();

            if(!key){
                top.layer.msg("服务代号不能为空！");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/cfg/gateway/ajax/save",
                data: {
                    ori_key: '${sev_key!}',
                    sev_key: $("#key").val(),
                    url: $("#url").val(),
                    policy: $("#policy").val(),
                    is_enabled:($('#is_enabled').prop("checked")?1:0)
                },
                success: function (data) {
                    if (1 == data.code) {
                        top.layer.msg('操作成功');

                        setTimeout(function () {
                            parent.location.href = "/cfg/gateway?tag=" + $("#key").val();
                            //location.href = "/cfg/gateway/inner?sev_key=" + $("#key").val();
                        }, 800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        $(function(){
            ctl_s_save_bind(document,save);
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">网关配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" onclick="save()">保存</button>
    </right>
</toolbar>

<detail>

    <form>
        <table>
            <tr>
                <th>服务代号</th>
                <td><input type="text" id="key" autofocus value="${(cfg!).service!}"/></td>
            </tr>
            <tr>
                <th>代理网关</th>
                <td><input type="text" id="url" value="${(cfg!).url!}"/></td>
            </tr>
            <tr>
                <th>负载策略</th>
                <td>
                    <select id="policy">
                        <option value="polling" <#if ((cfg!).policy!) == 'polling'>selected</#if>>轮询</option>
                        <option value="weight" <#if ((cfg!).policy!) == 'weight'>selected</#if>>权重轮询</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>启用</th>
                <td>
                    <switcher><label><input type="checkbox" id="is_enabled" value="1" ${(is_enabled = 1)?string("checked","")} /><a></a></label></switcher>

                </td>
            </tr>
        </table>
    </form>
</detail>

</body>
</html>