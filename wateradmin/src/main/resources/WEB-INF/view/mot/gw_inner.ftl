<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 网关配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        .line1{text-decoration:line-through;}
    </style>
</head>
<body>

<block>
    代理网关: ${cfg.url!}
</block>
<block>
    负载策略: ${cfg.policy!}
</block>
<div class="bg14 pad10">
    挂载节点:
</div>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="70px">ID</td>
            <td>资源名称</td>
            <td width="80px">平均响应</td>
            <td width="100px">请求次数</td>
            <td width="120px">最后状态</td>
            <td width="120px">最后检查时间</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list gtws!! as gtw>
            <tr class="${(gtw.service.check_last_state=1)?string('t4 ',' ')}${(gtw.service.is_enabled=0)?string('line1 ',' ')}">

                <td>${(gtw.service.service_id)!}</td>
                <td id="nm_${(gtw.service.service_id)!}" class="left">${(gtw.service.name)!}@${(gtw.service.address)!}</td>
                <td class="right">${(gtw.speed.average)!}</td>
                <td class="right">${(gtw.speed.total_num)!}</td>
                <td>${(gtw.service.check_last_state=0)?string('ok','error')!}</td>
                <td>${gtw.service.check_last_time?string("MM-dd HH:mm:ss")}</td>


            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

<div class="bg14 pad10" style="margin-top: 10px;">
    消费终端:
</div>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="70px">ID</td>
            <td>消费者</td>
            <td width="180px" class="left">消费者参考IP</td>
            <td width="120px" class="left">流量比例</td>
            <td width="120px">最后检查时间</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list csms!! as c>
            <tr class="${(c.chk_last_state=1)?string('t4 ',' ')}${(c.is_enabled=0)?string('line1 ',' ')}">
                <td>${(c.row_id)!}</td>
                <td class="left">${(c.consumer)!}@${c.consumer_address!} - <a class="t2" href="/cfg/gateway/check?s=${(c.consumer)!}@${c.consumer_address!}&upstream=${sev_key}" target="_blank">检查</a></td>
                <td class="left">${(c.consumer_ip)!}</td>
                <td class="left">${c.traffic_per?string("00.00")}% (${c.traffic_num})</td>
                <td>${(c.chk_fulltime)?string("MM-dd HH:mm:ss")}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>