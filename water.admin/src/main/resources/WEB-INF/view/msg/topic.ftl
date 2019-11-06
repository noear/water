<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主题列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
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
        <cell>
            <form>
                主题：<input type="text"  name="topic_name" placeholder="主题名称" id="topic_name"/>&nbsp;&nbsp;
                <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                        <button type="button" onclick="add();" class="edit">新增</button>
                    </#if>
            </form>
        </cell>
    </toolbar>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td>主题ID</td>
                <td>主题名称</td>
                <td>最大消息数量</td>
                <td>最大派发次数</td>
                <td>最大同时派发数</td>
                <td>报警模式</td>
                    <#if is_admin == 1>
                        <td>操作</td>
                    </#if>
            </tr>
            </thead>
            <tbody id="tbody" >
                <#list list as m>
                <tr>
                    <td>${m.topic_id}</td>
                    <td style="text-align: left">${m.topic_name}</td>
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
                                        <td><a href="/msg/topic/edit?topic_id=${m.topic_id}" style="color: blue;cursor: pointer;">编辑</a></td>
                                    </#if>
                </tr>
                </#list>
            </tbody>
        </table>
    </datagrid>
</main>

</body>
</html>