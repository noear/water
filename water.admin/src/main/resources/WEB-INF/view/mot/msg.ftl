<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 异常记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function fresh() {
            location.reload();
        };
    </script>
</head>
<body>
    <main>
            <toolbar>
                <cell>
                    <button type='button' onclick="fresh()">刷新</button>
                </cell>
            </toolbar>
            <datagrid>
                <table>
                    <thead>
                    <tr>
                        <td width="70px">消息ID</td>
                        <td >消息主题</td>
                        <td>内容</td>
                        <td width="40px">已派<br/>次数</td>
                        <td width="105px">发起时间</td>
                        <#if is_admin == 1>
                            <td width="35px">操作</td>
                        </#if>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                        <#list list as msg>
                        <tr>
                            <td>${msg.msg_id}</td>
                            <td style="text-align: left;">${msg.topic_name}</td>
                            <td style="text-align: left;word-wrap : break-word;word-break:break-all;">${msg.content}</td>
                            <td>${msg.dist_count}</td>
                            <td>${msg.log_fulltime?string('MM-dd HH:mm:ss')}</td>
                            <#if is_admin == 1>
                                <td>
                                    <a href="/smp/log?project=water&tableName=water_log_msg_error&tagx=@${msg.msg_id}" class="t2">日志</a>
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