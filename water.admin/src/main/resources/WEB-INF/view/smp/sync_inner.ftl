<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据同步</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
</head>
<script>
    function add() {
        location.href = "/smp/sync/add";
    };

</script>
<body>

<toolbar>
    <left>
        <form>
            名称：<input type="text"  name="sync_name" placeholder="名称" id="sync_name"/>&nbsp;&nbsp;
            <input type="hidden"  name="tag" id="tag" value="${tag!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin = 1>
                        <button onclick="add();" type="button" class="edit">新增</button>
                    </#if>
        </form>
    </left>
    <right>
                <@stateselector items="启用,未启用"/>
    </right>
</toolbar>
<datagrid>
    <table>
        <thead>
        <tr>
            <td width="40">ID</td>
            <td width="130px">名称</td>
            <td width="80px">类型</td>
            <td width="90px">间隔时间（s）</td>
            <td>目标数据</td>
            <td>来源数据</td>
            <td width="60px">是否启用</td>
            <td width="100px">最后同步时间</td>
                    <#if is_admin = 1>
                        <td width="70px">操作</td>
                    </#if>
        </tr>
        </thead>
        <tbody id="tbody">
                <#list synchronous as m>
                <tr>
                    <td>${m.sync_id}</td>
                    <td style="text-align: left;">${m.name!}</td>
                    <td>
                        <#if m.type = 0>增量同步</#if>
                        <#if m.type = 1>更新同步</#if>
                    </td>
                    <td>${m.interval}</td>
                    <td style="text-align: left;word-wrap:break-word;word-break:break-all;">${m.target!}</td>
                    <td style="text-align: left;word-wrap:break-word;word-break:break-all;">${m.source!}</td>
                    <td>
                        <#if m.is_enabled = 1>启用</#if>
                        <#if m.is_enabled = 0>禁用</#if>
                    </td>
                    <td>${(m.last_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <#if is_admin = 1>
                        <td><a href="/smp/sync/edit?sync_id=${m.sync_id}" style="color: blue;cursor: pointer">编辑</a></td>
                    </#if>
                </tr>
                </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>