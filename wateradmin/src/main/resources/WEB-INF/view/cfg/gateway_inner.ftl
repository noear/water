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
<script>

    <#if is_admin == 1>
        function set_enabled(service_id, is_enabled, obj) {
            $.ajax({
                type: "POST",
                url: "/cfg/gateway/ajax/enabled",
                data: {
                    service_id: service_id,
                    is_enabled: is_enabled
                },
                success: function (data) {
                    top.layer.msg(data.msg);
                    if (1 == data.code) {
                        if (1 == is_enabled) {
                            $(obj).html('禁用');
                            $(obj).attr('onclick', "set_enabled(" + service_id + ", 0, this);");
                        } else {
                            $(obj).html('启用');
                            $(obj).attr('onclick', "set_enabled(" + service_id + ", 1, this);");
                        }
                    }
                }
            });
        }
    </#if>

</script>
<body>

<#if is_admin == 1>
<toolbar>
    <a class='btn' href="/cfg/gateway/edit/${sev_key!}" >修改</a>
    <a class='btn edit mar10-l' href="/cfg/gateway/add" >新增</a>
</toolbar>
</#if>

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

            <#if is_admin == 1>
                <td width="70px">最后状态</td>
                <td width="120px">最后检查时间</td>
                <td width="40px">操作</td>
            <#else>
                <td width="120px">最后状态</td>
                <td width="120px">最后检查时间</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list gtws as gtw>
            <tr class="${(gtw.service.check_last_state=1)?string('t4 ',' ')}${(gtw.service.is_enabled=0)?string('line1 ',' ')}">

                <td>${(gtw.service.service_id)!}</td>
                <td id="nm_${(gtw.service.service_id)!}" class="left">${(gtw.service.name)!}@${(gtw.service.address)!}</td>
                <td class="right">${(gtw.speed.average)!}</td>
                <td class="right">${(gtw.speed.total_num)!}</td>
                <td>${(gtw.service.check_last_state=0)?string('ok','error')!}</td>
                <td>${gtw.service.check_last_time?string("MM-dd HH:mm:ss")}</td>

                <#if is_admin == 1>
                    <td>
                        <#if gtw.service.is_enabled == 1>
                            <a class="t2" href="javascript:void(0);" onclick="set_enabled(${gtw.service.service_id}, 0, this);">禁用</a>
                        <#else>
                            <a class="t2" href="javascript:void(0);" onclick="set_enabled(${gtw.service.service_id}, 1, this);">启用</a>
                        </#if>
                    </td>
                </#if>
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
            <td width="180px">消费者参考IP</td>
            <td width="120px">流量比例</td>
            <td width="120px">最后检查时间</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list csms!! as c>
            <tr>
                <td>${(c.row_id)!}</td>
                <td class="left">${(c.consumer)!}@${c.consumer_address!}</td>
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