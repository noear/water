<!DOCTYPE HTML>
<html class="frm10-y">
<head>
    <title>${app} - 运行设置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body  detail{ width: 800px; margin: 0 auto;}
        form > table{width: 800px;}
        form > table td > h2{line-height: 40px;}
    </style>
    <script>

    </script>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left class="ln30">
            <h2>设置</h2>
        </left>
        <right class="form">
            <#if is_admin == 1>
                <n>ctrl + s 可快捷保存</n>
                <button type="button">保存</button>
            </#if>
        </right>
    </toolbar>

        <detail>
            <form>
                <table>
                    <tr><td colspan="2"> <h2>修改登录密码</h2> <hr/></td></tr>
                    <tr>
                        <th>新密码</th>
                        <td>
                            <input type="password" class="txt">
                        </td>
                    </tr>
                    <tr>
                        <th>确认新密码</th>
                        <td>
                            <input type="password" class="txt">
                        </td>
                    </tr>
                    <tr><td colspan="2"> <h2>运行配置</h2> <hr/></td></tr>
                    <tr>
                        <th>后台标题</th>
                        <td>
                            <input type="text">
                        </td>
                    </tr>
                    <tr>
                        <th>告警签名</th>
                        <td>
                            <input type="text">
                        </td>
                    </tr>
                    <tr>
                        <th>服务数量规模</th>
                        <td>
                            <boxlist>
                                <label><input type="radio" name="water.setting.scale" value="0" checked /><a>小</a></label>
                                <label><input type="radio" name="water.setting.scale" value="1" /><a>中</a></label>
                                <label><input type="radio" name="water.setting.scale" value="2" /><a>大</a></label>
                                <label><input type="radio" name="water.setting.scale" value="3" /><a>超大</a></label>
                            </boxlist>
                            <n-l>
                                会根据情况调整控制台界面布局
                            </n-l>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
</main>

</body>
</html>