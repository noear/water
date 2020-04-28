<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function editTask(monitor_id) {
        location.href="/tool/monitor/edit?monitor_id="+monitor_id;
    }

</script>
<body>

        <toolbar>
            <left>
                <form>
                    项目：<input type="text"  class="w300" name="monitor_name" placeholder="项目名称" id="monitor_name"/>
                    <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name}"/>
                    <button type="submit">查询</button>
                    <#if is_admin = 1>
                        <button onclick="editTask(0);" type="button" class="edit mar10-l" >新增</button>
                    </#if>
                </form>
            </left>
            <right>
                <@stateselector items="启用,未启用"/>
            </right>
        </toolbar>
        <datagrid class="list">
            <table>
                <thead>
                <tr>
                    <td width="40">ID</td>
                    <td class="left">监视项目</td>
                    <td width="70px" nowrap>监视标签</td>
                    <td width="110px">报警手机</td>
                    <td width="80px">报警次数</td>
                    <#if is_admin = 1>
                    <td width="40px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list monitors as monitor>
                <tr>
                    <td>${monitor.monitor_id}</td>
                    <td class="left break">
                        <div>
                            项目名称：${monitor.name} （${monitor.type_str()}）</note>
                        </div>
                        <div>
                            <note>数据采集：${monitor.source_query}</note>
                        </div>
                    </td>
                    <td>${monitor.task_tag}</td>
                    <td style="word-wrap:break-word;word-break:break-all;">${monitor.alarm_mobile!}</td>
                    <td>${monitor.alarm_count}</td>
                        <#if is_admin = 1>
                        <td onclick="editTask('${monitor.monitor_id}')"><a style="color: blue;cursor: pointer">编辑</a></td>
                        </#if>
                </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>
</body>
</html>