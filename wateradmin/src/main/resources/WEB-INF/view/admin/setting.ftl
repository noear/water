<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 运行设置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>

    <script>

    </script>
</head>
<body>
<toolbar class="blockquote">
    <left class="ln30">
        <h2>运行设置</h2>
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
                <th>服务规模</th>
                <td>
                    <boxlist>
                        <label><input type="radio" name="water.scale" value="0" checked /><a>小</a></label>
                        <label><input type="radio" name="water.scale" value="1" /><a>中</a></label>
                        <label><input type="radio" name="water.scale" value="2" /><a>大</a></label>
                        <label><input type="radio" name="water.scale" value="3" /><a>超大</a></label>
                    </boxlist>
                </td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>