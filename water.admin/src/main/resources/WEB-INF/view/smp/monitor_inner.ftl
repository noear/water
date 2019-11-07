<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function editTask(monitor_id) {
        location.href="/smp/monitor/edit?monitor_id="+monitor_id;
    }
    function add() {
        location.href = "/smp/monitor/add";
    };
</script>
<body>

        <toolbar>
            <cell>
                <form>
                    项目：<input type="text"  name="monitor_name" placeholder="项目名称" id="monitor_name"/>&nbsp;&nbsp;
                    <input type="hidden"  name="tag" id="tag" value="${tag}"/>
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin = 1>
                        <button onclick="add();" type="button" class="edit">新增</button>
                    </#if>
                </form>
            </cell>
            <cell>
                <@stateselector items="启用,未启用"/>
            </cell>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="40">ID</td>
                    <td>监视项目</td>
                    <td width="60px" nowrap>监视标签</td>
                    <td width="100px">报警手机</td>
                    <td width="80px">报警次数</td>
                    <#if is_admin = 1>
                    <td width="30px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list monitors as monitor>
                <tr>
                    <td>${monitor.monitor_id}</td>
                    <td class="left break">
                        <div>
                            项目名称：${monitor.name} [${monitor.source}]（${monitor.type_str()}）</note>
                        </div>
                        <div>
                            <note>数据采集：${monitor.source_model}</note>
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