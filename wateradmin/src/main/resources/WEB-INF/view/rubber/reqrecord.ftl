<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 请求记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function exec_scheme() {
            location.href="/rubber/reqrecord/exec/scheme";
        }

        function exec_model() {
            location.href="/rubber/reqrecord/exec/model";
        }

        function exec_query() {
            location.href="/rubber/reqrecord/exec/query";
        }
    </script>
</head>
<body>
<main>
    <toolbar>
        <left>
            <form method="post">
                请求：<input type="text" name="key" placeholder="请求ID或参数" size="40" id="key" value="${key!}"/>
                <button type="submit">查询</button>&nbsp;&nbsp;

            </form>
        </left>
        <right>
            <#if is_admin == 1>
            <button onclick="exec_scheme()" style="width: auto;padding: 0 10px;" type="button" >请求计算</button>
            &nbsp;&nbsp;
            <button onclick="exec_query()" style="width: auto;padding: 0 10px;" type="button" >请求查询</button>
            &nbsp;&nbsp;
            <button onclick="exec_model()" style="width: auto;padding: 0 10px;" type="button" >请求模型</button>
            </#if>
        </right>
    </toolbar>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td width="280px">No / 计算方案</td>
                <td>输入输出</td>
                <td width="110px">请求时间</td>
                <td width="70px">处理时间</td>
                <td width="40px">状态</td>
                <td width="40px"></td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list models as m>
                <tr>
                    <td title="${m.request_id}" class="left">
                        <a href="${raas_uri!}/s/${m.scheme_tagname!}?request_id=${m.request_id?c}" class="t2" target="_blank">${m.log_id?c}</a>
                        <div class="mar10-l">${m.scheme_tagname!}::${m.policy!}</div>
                    </td>
                    <td class="left break">
                        <div>输入：${m.args_json!}</div>
                        <div><note>输出：${m.node_str()!}</note></div>
                    </td>
                    <td>
                        ${m.start_fulltime?string('MM-dd HH:mm:ss')}
                    </td>
                    <td class="right">${m.timespan}</td>
                    <td>${m.state_str()}</td>
                    <td>
                        <a class="t2" href="/rubber/rerecord/detil?log_id=${m.log_id}">详情</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
    <@pagebar pageSize="${pageSize!}" rowCount="${rowCount!}"/>
</main>
</body>
</html>