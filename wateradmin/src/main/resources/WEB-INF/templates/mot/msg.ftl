<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 异常记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function fresh() {
            location.reload();
        };
    </script>
</head>
<body>
<main>
    <toolbar>
        <button type='button' onclick="fresh()">刷新</button>

    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="100px">消息ID</td>
                <td class="left">消息主题</td>
                <td class="left">内容</td>
                <td width="50px">已派<br/>次数</td>
                <td width="120px">发起时间</td>
                <#if is_admin == 1>
                    <td width="50px">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list list as msg>
                <tr>
                    <td>${msg.msg_id}</td>
                    <td class="left">${msg.topic_name}</td>
                    <td class="left break">${msg.content}</td>
                    <td>${msg.dist_count}</td>
                    <td>${msg.log_fulltime?string('MM-dd HH:mm:ss')}</td>
                    <#if is_admin == 1>
                        <td>
                            <a href="/log/query/inner?tag_name=water&logger=water_log_msg&level=1&tagx=@${msg.msg_id}" class="t2">日志</a>
                        </td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>