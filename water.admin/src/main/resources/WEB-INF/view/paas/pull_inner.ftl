<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 公共模版</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addPull() {
            location.href="/paas/pull/edit";
        }
        
        function issuePull(pull_id) {
            $.ajax({
                type: "POST",
                url: "/paas/pull/ajax/issue",
                data: {
                    pull_id: pull_id
                },
                success: function (data) {
                    top.layer.msg(data.msg);
                    if (1 == data.code) {
                        location.reload();
                    }
                }
            });
        }

    </script>
</head>
<body>
<toolbar>
    <cell>
        <form>
            拉取：<input type="text"  name="pull_name" placeholder="名称" value="${tml_name!}"/>
            <input type="hidden"  name="tag" value="${tag!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_admin == 1>
                <button onclick="addPull()" type="button" class="edit">新增</button>
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
            <td>名称</td>
            <td></td>
            <td width="70">状态</td>
            <td width="80">最后处<br/>理时间</td>
            <#if is_admin == 1>
                <td width="70">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list list as t>
            <tr>
                <td>${t.pull_id}</td>
                <td class="left">${t.pull_name!}</td>
                <td class="left">
                    <div>来源：<a href="${t.source!}" target="_blank">${t.source!}</a></div>
                    <div>存储：${t.target_dir!}
                    <#if (t.target_url?? && t.target_url != '')>
                        （<a href="${t.target_url}/${t.tag}/${t.pull_name}.htm" class="t2" target="_blank">浏览</a>）
                    </#if>
                    </div>
                </td>
                <td>
                    ${t.state_str()}
                </td>
                <td>${(t.last_fulltime?string('dd hh:mm:ss'))!}</td>
                <#if is_admin == 1>
                    <td>
                    <a href="/paas/pull/edit?pull_id=${t.pull_id}" class="t2">编辑</a>
                        |
                        <a href="javascript:issuePull(${t.pull_id})" class="t2">拉取</a>
                    </td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>