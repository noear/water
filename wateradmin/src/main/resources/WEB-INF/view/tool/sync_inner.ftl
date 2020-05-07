<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据同步</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
</head>
<body>

<toolbar>
    <left>
        <form>
            名称：<input type="text" class="w300" name="sync_name" placeholder="名称" id="sync_name"/>
            <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name!}"/>
            <button type="submit">查询</button>
            <#if is_admin = 1>
                <a href="/tool/sync/edit" class="btn edit mar10-l">新增</a>
            </#if>
        </form>
    </left>
    <right>
        <@stateselector items="启用,未启用"/>
    </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="40">ID</td>
            <td class="left">名称</td>
            <td width="80px">类型</td>
            <td width="90px">间隔时间</td>
            <td class="left">目标数据</td>
            <td width="60px">是否<br/>启用</td>
            <td width="120px">最后同步时间</td>
                    <#if is_admin = 1>
                        <td width="50px">操作</td>
                    </#if>
        </tr>
        </thead>
        <tbody id="tbody">
                <#list synchronous as m>
                <tr>
                    <td>${m.sync_id}</td>
                    <td class="left">${m.name!}</td>
                    <td>
                        <#if m.type = 0>增量同步</#if>
                        <#if m.type = 1>更新同步</#if>
                    </td>
                    <td>${m.interval}s</td>
                    <td class="left break">${m.target!}</td>
                    <td>
                        <#if m.is_enabled = 1>启用</#if>
                        <#if m.is_enabled = 0>禁用</#if>
                    </td>
                    <td>${(m.last_fulltime?string('MM-dd HH:mm:ss'))!}</td>
                    <#if is_admin = 1>
                        <td class="op"><a href="/tool/sync/edit?sync_id=${m.sync_id}" class="t2">编辑</a></td>
                    </#if>
                </tr>
                </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>