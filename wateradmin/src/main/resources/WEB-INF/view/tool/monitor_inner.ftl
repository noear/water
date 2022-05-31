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
<body>

<toolbar>
    <flex>
        <left class="col-4">
            <#if is_admin = 1>
                <a href="/tool/monitor/edit?tag=${tag_name!}&monitor_id=0" class="btn edit" >新增</a>
            </#if>
        </left>
        <middle class="col-4 center">
            <form>
                <input type="text"  class="w200" name="monitor_name" placeholder="项目名称" />
                <input type="hidden"  name="tag_name" value="${tag_name}"/>
                <button type="submit">查询</button>
            </form>
        </middle>
        <right class="col-4">
            <@stateselector items="启用,未启用"/>
        </right>
    </flex>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td class="left">数据名称</td>
            <td width="70px" nowrap>告警标识</td>
            <td width="100px">告警次数</td>
            <td width="90"></td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list list as monitor>
            <tr>
                <td class="left break">
                    <div>
                        ${monitor.name}
                    </div>
                    <div>
                        <note>${monitor.source_query}</note>
                    </div>
                </td>
                <td>${monitor.task_tag}</td>
                <td>${monitor.alarm_count}</td>
                <td class="op">
                    <a href="/tool/monitor/edit?monitor_id=${monitor.monitor_id}" class="t2" ><#if is_admin = 1>编辑<#else>查看</#if></a> |
                    <a href="/log/query/inner?tag_name=water&logger=water_log_sev&level=0&tagx=mot@${monitor.monitor_id}" target="_parent" class="t2">日志</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>