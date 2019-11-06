<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 网关配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>

    </style>

    <script>

        function save() {
            $.ajax({
                type: "POST",
                url: "/cfg/gateway/ajax/save",
                data: {
                    ori_key: '${sev_key!}',
                    sev_key: $("#key").val(),
                    url: $("#url").val(),
                    explain: $("#explain").val()
                },
                success: function (data) {
                    top.layer.msg(data.msg);
                    if (1 == data.code) {
                        setTimeout(function () {
                            location.href = "/cfg/gateway/inner?sev_key=" + $("#key").val();
                        }, 500);
                    }
                }
            });
        }
    </script>
</head>
<body>
<detail>
    <form>
        <h2>网关配置（<a href="#" onclick="javascript:history.back();" class="t2 noline">返回</a>）</h2>
        <hr/>
        <table>
            <tr>
                <td>服务名称</td>
                <td><input type="text" id="key" autofocus value="${(cfg!).key!}"/></td>
            </tr>
            <tr>
                <td>代理网关</td>
                <td><input type="text" id="url" value="${(cfg!).url!}"/></td>
            </tr>
            <tr>
                <td>负载策略</td>
                <td>
                    <select id="explain">
                        <option value="polling" <#if ((cfg!).explain!) == 'polling'>selected</#if>>轮询</option>
                        <option value="weight" <#if ((cfg!).explain!) == 'weight'>selected</#if>>权重轮询</option>
                    </select>
                </td>
            </tr>

            <tr>
                <td></td>
                <td><button type="button" onclick="save()">保存</button></td>
            </tr>
        </table>
    </form>
</detail>

</body>
</html>