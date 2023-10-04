<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 上游监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}
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
                if (1 == data.code) {
                    location.reload();
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    }
    </#if>

    var gateway_id = '${gateway_id!}';
    function node_onclick(tn,obj) {
        gateway_id = tn;
        $('li.sel').removeClass('sel');
        $(obj).addClass("sel");
        $("#table").attr('src',"/mot/gw/inner?gateway_id=" + gateway_id);
    };

</script>
<body>

<div class="tabs">
    <tabbar>
        <#list tabs as m>
            <#if m.gateway_id == gateway_id>
                <a id="e${m.gateway_id}" class="btn sel">${m.name}</a>
            <#else>
                <a id="e${m.gateway_id}" class="btn" href="/mot/gw/inner?gateway_id=${m.gateway_id}&tag_name=${tag_name!}">${m.name}</a>
            </#if>
        </#list>
    </tabbar>
</div>

<block>
    代理网关: ${cfg.agent!}
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
                <td width="80px">最后状态</td>
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
            <td width="180px" class="left">消费者参考IP</td>
            <td width="120px" class="left">流量比例</td>
            <td width="120px">最后检查时间</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list csms!! as c>
            <tr class="${(c.chk_last_state=1)?string('t4 ',' ')}${(c.is_enabled=0)?string('line1 ',' ')}">
                <td>${(c.row_id)!}</td>
                <td class="left">${(c.consumer)!}@${c.consumer_address!}
                    <#if c.consumer_address?contains(":")>
                    - <a class="t2" href="/mot/gw/check?s=${(c.consumer)!}@${c.consumer_address!}&upstream=${cfg.name!}" target="_blank">检查</a>
                    </#if>
                </td>
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