
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
    <h2>请求模型（<a href="#" onclick="javascript:history.back(-1);" class="t2">返回</a>）</h2>
    <hr/>

    <form action="${raas_uri}/release" target="_blank" method="get">
        <table>
            <tr>
                <td>数据模型</td>
                <td><select name="model">
                    <#list list as m>
                    <option value="${m.tag}/${m.name}">${m.tag}/${m.name_display}</option>
                    </#list>
                </select></td>
            </tr>

            <tr>
                <td>请求参数</td>
                <td><p>
                    <textarea name="args" >{user_id:10000064,open_id:10009492}</textarea>
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