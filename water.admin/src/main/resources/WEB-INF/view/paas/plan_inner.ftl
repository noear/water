<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计划任务</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addPlan() {
            location.href="/paas/plan/add";
        }

        function impPaas(tag_name) {
            $.getJSON('/paas/plan/ajax/import?tag='+tag_name,function (rst) {
                if(rst.code){
                    top.layer.msg(rst.msg);
                    setTimeout(location.reload,1000);
                }
                else{
                    top.layer.msg(rst.msg);
                }
            });
        }

        function resetPlan(id) {
            $.ajax({
                type: "POST",
                url: "/paas/plan/ajax/reset",
                data: {
                    plan_id: id
                },
                success: function (data) {
                    top.layer.msg(data.msg);

                    setTimeout(function () {
                        location.reload();
                    }, 500);
                }
            });
        }

    </script>
</head>
<body>
        <toolbar>
            <cell>
                <form>
                    任务：<input type="text"  name="plan_name" placeholder="任务名" id="plan_name" value="${plan_name!}"/>
                          <input type="hidden"  name="tag_name" id="tag_name" value="${tag_name}"/>
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                    <button onclick="addPlan()" type="button" class="edit">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <button onclick="impPaas('${tag_name}')" type="button" class="minor">导入</button>
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
                        <td width="100px">开始时间</td>
                        <td width="40px">间隔<br/>时间</td>
                        <td width="110px">最后执行时间</td>
                        <td width="50px">最后执<br/>行备注</td>
                        <td width="50px">最多重<br/>复次数</td>
                        <td width="50px">已经执<br/>行次数</td>
                        <td width="70px">操作</td>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    <#list plans as plan>
                        <tr>
                            <td>${plan.plan_id}</td>
                            <td style="text-align: left;">
                                <#if is_admin == 1>
                                    <a href="/paas/plan/edit?plan_id=${plan.plan_id}" class="t2">${plan.plan_name}</a>
                                </#if>
                                <#if is_admin == 0>
                                    <a href="/paas/query/code?code_type=1&id=${plan.plan_id}">${plan.plan_name}</a>
                                </#if>
                            </td>
                            <td>${(plan.begin_time?string('yyyy-MM-dd'))!}</td>
                            <td>${plan.repeat_interval}</td>
                            <td>${(plan.last_exec_time?string('MM-dd HH:mm'))!}</td>
                            <td>${plan.last_exec_note!}</td>
                            <td>${plan.repeat_max}</td>
                            <td>${plan.repeat_count}</td>
                            <td>
                                <a target="_parent" class="t2" href="/smp/log?project=water&tableName=water_log_sev_error&tagx=pln@${plan.plan_id}">日志</a>
                                |
                                <a href="javascript:resetPlan(${plan.plan_id})" class="t2">重置</a>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </datagrid>
</body>
</html>