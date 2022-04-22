<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主题列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function add() {
            location.href = "/msg/topic/add";
        };
    </script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-4">
                <#if is_admin == 1>
                    <button type="button"  onclick="add();" class="edit">新增</button>
                </#if>
            </left>
            <middle class="col-4 center">
                <form>
                    <input type="text" class="w200" name="topic_name" placeholder="主题名称" id="topic_name"/>
                    <input type="hidden"  name="tag_name" value="${tag_name!}" />
                    <button type="submit">查询</button>
                </form>
            </middle>
            <right class="col-4">

            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td class="left" sort="">主题名称</td>
                <td width="120" sort="stat_msg_day_num">近日<br/>消息数量</td>
                <td width="120">最大<br/>消息数量</td>
                <td width="120">最大<br/>派发次数</td>
                <td width="120">最大<br/>同时派发数</td>
                <td width="100">报警模式</td>
                <#if is_admin == 1>
                    <td width="100"></td>
                <#else>
                    <td width="50"></td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody" >
            <#list list as m>
                <tr>
                    <td class="left">${m.topic_name}</td>
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

                    <td class="op">
                        <#if is_admin == 1>
                        <a href="/msg/topic/edit?topic_id=${m.topic_id}" class="t2">编辑</a> |
                        </#if>
                        <a href="/mot/speed/charts?tag=topic&name_md5=${m.topic_md5()}&service=_watermsg" class="t2">监控</a>
                    </td>

                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>

</body>
</html>