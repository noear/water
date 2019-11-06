<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 同步任务</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addPlan() {
            location.href="/paas/etl/add";
        }

        function impPaas(tag_name) {
            $.getJSON('/paas/etl/ajax/import?tag='+tag_name,function (rst) {
                if(rst.code){
                    top.layer.msg(rst.msg);
                    setTimeout(location.reload,1000);
                }
                else{
                    top.layer.msg(rst.msg);
                }
            });
        }


    </script>
</head>
<body>
<toolbar>
    <cell>
        <form>
            传输：<input type="text"  name="etl_name" placeholder="任务名" id="etl_name" value="${etl_name!}"/>
            <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name!}"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
            <#if is_admin == 1>
            <button onclick="addPlan()" type="button"  class="edit">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
            <button onclick="impPaas('${tag_name!}')" type="button"  class="minor">导入</button>
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
            <td width="60px">是否抽取</td>
            <td width="60px">是否转换</td>
            <td width="60px">是否装填</td>
            <td width="140px">游标值</td>
            <td width="40px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list etls as m>
            <tr>
                <td>${m.etl_id}</td>
                <td style="text-align: left;">
                    <#if is_admin == 1>
                        <a class="t2" href="/paas/etl/edit?etl_id=${m.etl_id}">${m.etl_name}</a>
                    </#if>
                    <#if is_admin == 0>
                        <a href="/paas/query/code?code_type=2&id=${m.etl_id}">${m.etl_name}</a>
                    </#if>
                </td>
                <td>${m.getBoolean(m.e_enabled)}</td>
                <td>${m.getBoolean(m.t_enabled)}</td>
                <td>${m.getBoolean(m.l_enabled)}</td>
                <td>${m.cursor_str()}</td>
                <td>
                    <a target="_parent" class="t2" href="/smp/log?project=water&tableName=water_log_etl_error&tagx=${m.tag}_${m.etl_name}">日志</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>