
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 新建请求</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<body>
<detail>
    <h2>请求查询（<a href="#" onclick="javascript:history.back(-1);" class="t2">返回</a>）</h2>
    <hr/>

    <form action="${raas_uri}/release" target="_blank" method="get">

        <table>
            <tr>
                <td>查询方案</td>
                <td><select name="scheme">
                    <#list list as m>
                    <option value="${m.tag}/${m.name}">${m.tag}/${m.name_display}</option>
                    </#list>
                </select></td>
            </tr>
            <tr>
                <td>请求动作</td>
                <td><select name="type">
                    <option value="11">数据展示</option>
                    <option value="10">数据查询</option>
                    <option value="12">数据导出</option>
                </select></td>
            </tr>
            <tr>
                <td>显示数量</td>
                <td><p>
                    <input type="text" name="limit" value="10" />
                </p>
                </td>
            </tr>

            <tr>
                <td></td>
                <td><button type="submit" >提交请求</button></td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>