
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
    <h2>请求计算（<a href="#" onclick="javascript:history.back(-1);" class="t2">返回</a>）</h2>
    <hr/>

    <form action="${raas_uri}/release" target="_blank" method="get">

        <table>
            <tr>
                <td>计算方案</td>
                <td><select name="scheme">
                    <#list list as m>
                        <option value="${m.tag}/${m.name}">${m.tag}/${m.name_display}</option>
                    </#list>
                </select></td>
            </tr>
            <tr>
                <td>处理模式</td>
                <td><select name="policy">
                    <option value="1001">匹配模式</option>
                    <option value="2001">评估模式</option>
                </select>
                    <input type="hidden" name="type" value="0" />
                </td>
            </tr>

            <tr>
                <td>响应方式</td>
                <td><select name="callback">
                    <option value="">同步响应</option>
                    <option value="x">异步响应</option>
                </select>
                </td>
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