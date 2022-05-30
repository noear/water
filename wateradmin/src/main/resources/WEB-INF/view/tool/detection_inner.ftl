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
                <a href="/tool/detection/edit?tag=${tag_name!}&detection_id=0" class="btn edit" >新增</a>
            </#if>
        </left>
        <middle class="col-4 center">
            <form>
                <input type="text"  class="w200" name="detection_name" placeholder="项目名称" />
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
            <td width="180px" class="left">应用名称</td>
            <td class="left">地址</td>
            <td width="120px">检测情况</td>
            <td width="130px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" >
        <#list list as m>
            <tr>
            <td class="left">${m.name!}</td>
            <td class="left break">
                ${m.protocol!}://${m.address!}
            </td>
            <td style='${m.isAlarm()?string("color:red","")}'>
                ${(m.check_last_time?string('HH:mm:ss'))!}
                <#if m.check_last_state == 0>
                    - ok
                <#else>
                    - no
                </#if>
            </td>

            <td class="op">
                <a href="/tool/detection/edit?detection_id=${m.detection_id}" class="t2" ><#if is_admin = 1>编辑<#else>查看</#if></a>
                |
                <a href="/log/query/inner?tag_name=water&logger=water_log_sev&level=0&tagx=det_${m.detection_id}" target="_parent" class="t2">日志</a>
                |
                <a href="/mot/service/charts?key=${m.key}" class="t2">监控</a>
            </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>