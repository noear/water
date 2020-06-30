<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主题列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function add() {
            location.href = "/msg/topic/add";
        };
    </script>
</head>
<body>
<main>
    <toolbar>
        <form>
            主题：<input type="text" class="w200" name="topic_name" placeholder="主题名称" id="topic_name"/>&nbsp;&nbsp;
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_admin == 1>
                <button type="button"  onclick="add();" class="edit mar20-l">新增</button>
            </#if>
        </form>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="70">主题ID</td>
                <td class="left" sort="">主题名称</td>
                <td width="120" sort="stat_msg_day_num">近日<br/>消息数量</td>
                <td width="120">最大<br/>消息数量</td>
                <td width="120">最大<br/>派发次数</td>
                <td width="120">最大<br/>同时派发数</td>
                <td width="100">报警模式</td>
                <#if is_admin == 1>
                    <td width="50">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody" >
            <#list list as m>
                <tr>
                    <td>${m.topic_id}</td>
                    <td style="text-align: left">${m.topic_name}</td>
                    <td>${m.stat_msg_day_num!0}</td>
                    <td>${m.max_msg_num}</td>
                    <td>${m.max_distribution_num}</td>
                    <td>${m.max_concurrency_num}</td>
                    <td>
                        <#if m.alarm_model == 0>
                            普通模式
                        </#if>
                        <#if m.alarm_model == 1>
                            不报警
                        </#if>
                    </td>
                    <#if is_admin == 1>
                        <td class="op"><a href="/msg/topic/edit?topic_id=${m.topic_id}" class="t2">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>

</body>
</html>