<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算资源</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<script>
    function exec_operate(operate_id,operate_name){
        top.layer.confirm('确定执行 '+operate_name+' 操作?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/ops/server/extra/operate/ajax/exec",
                data:{"operate_id":operate_id},
                success:function(data){
                    top.layer.msg(data.msg);
                    setTimeout(function(){
                        location.reload();
                    },1000);
                }
            });
            top.layer.close(top.layer.index);
        });
    }

    function restart(domain, service_id) {

        if (domain == null || domain == undefined || domain == '') {
            top.layer.msg("域名未知，无法重启");
            return;
        }

        top.layer.confirm('确定重启', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/ops/server/extra/service/ajax/restart",
                data:{"ip":'${server.address}', "domain":domain, "service_id":service_id},
                success:function(data){
                    top.layer.msg(data.msg);
                    setTimeout(function(){
                        location.reload();
                    },1000);
                }
            });
            top.layer.close(top.layer.index);
        });
    };
</script>
<style>
    ul{
        text-decoration: none;
        list-style-type: none;
    }
    .button_list>li{
        float: left;
        font-size: 0;
        margin-right: 20px;
        margin-top: 4px;
    }
    .button_list>li>button{
        object-fit: cover;
        white-space:nowrap;
        width: auto;
        height: 30px;
        padding: 3px
    }
</style>
<body>
<toolbar>
    <cell>
        <div>实例：${instance.instanceName} = ${instance.instanceId}（<a onclick="history.back(-1)" class="t2">返回</a>）</div>
        <table>
            <tr>
                <th>配置：</th>
                <td>${instance.cpu}核/<fmt:formatNumber type="number" value="${instance.memory/1000}"
                                                       maxFractionDigits="0"/>G/${size}G
                </td>
                <th>带宽：</th>
                <td>${instance.internetMaxBandwidthOut}Mbps</td>
                <th>所在区：</th>
                <td>${instance.zoneId}</td>
            </tr>
        </table>
    </cell>
</toolbar>
<toolbar>
    <cell>
        <a>可操作命令</a>
        <button class="edit" onclick="location.href='/ops/server/extra/operate?server_id='+${server.server_id}">编辑</button>
        <ul class="button_list">
            <#list operateList as operate>
                <li>
                    <button onclick="exec_operate('${operate.operate_id}','${operate.name}')">${operate.name}</button>
                </li>
            </#list>
        </ul>
    </cell>
</toolbar>
<datagrid>
    <table>
        <thead>
        <tr>
            <td width="60px">序号</td>
            <td width="200px">项目</td>
            <td>域名</td>
            <td width="80px">端口</td>
            <td width="50px">最后检<br/>查状态</td>
            <#if is_admin == 1>
                <td width="80px">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody" >
        <#list services as m>
            <#if m.check_last_state == 1>
                <tr style="color: red">
            </#if>
            <td>${m_index+1}</td>
            <td class="left">${m.name!}</td>
            <td class="left">${domains[m.name]!}</td>
            <td class="left">${m.port!}</td>
            <td>
                <#if m.check_last_state == 0>
                    ok
                </#if>
                <#if m.check_last_state == 1>
                    error
                </#if>
            </td>
            <#if is_admin == 1>
                <td >
                    <a class="t2" onclick="restart('${domains[m.name]!}','${m.service_id!}')">重启</a>
                </td>
            </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>