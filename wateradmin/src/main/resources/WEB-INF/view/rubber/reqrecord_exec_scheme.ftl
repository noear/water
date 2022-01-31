
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 新建请求</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
</head>
<body>

<toolbar class="blockquote">
    <h2><a href="#" onclick="javascript:history.back(-1);" class="noline">请求记录</a></h2> / 请求计算
</toolbar>

<detail>

    <form action="${raas_uri}/release" target="_blank" method="get">

        <table>
            <tr>
                <th>计算方案</th>
                <td><select name="scheme">
                    <#list list as m>
                        <option value="${m.tag}/${m.name}">${m.tag}/${m.name_display}</option>
                    </#list>
                </select></td>
            </tr>
            <tr>
                <th>处理模式</th>
                <td><select name="policy">
                    <option value="1001">匹配模式</option>
                    <option value="2001">评估模式</option>
                </select>
                    <input type="hidden" name="type" value="0" />
                </td>
            </tr>

            <tr>
                <th>响应方式</th>
                <td><select name="callback">
                    <option value="">同步响应</option>
                    <option value="x">异步响应</option>
                </select>
                </td>
            </tr>

            <tr>
                <th>请求参数</th>
                <td><p>
                    <textarea name="args" >{user_id:10000064,open_id:10009492}</textarea>
                </p>
                </td>
            </tr>
            <tr>
                <th></th>
                <td><button type="submit" >提交请求</button></td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>