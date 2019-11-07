<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 接口列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function addPaas(tag) {
            location.href="/paas/fun/add?tag="+tag;
        }

        function impPaas(tag) {
            $.getJSON('/paas/fun/ajax/import?tag='+tag,function (rst) {
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
                    函数：<input type="text"  name="fun_name" placeholder="函数名" id="fun_name" value="${fun_name!}"/>
                          <input type="hidden"  name="tag" id="tag" value="${tag!}"/>
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                    <button onclick="addPaas('${tag!}')" type="button" class="edit">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <button onclick="impPaas('${tag!}')" type="button" class="minor">导入</button>
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
                        <td width="40px">ID</td>
                        <td width="250px">名称</td>
                        <td>显示名</td>
                        <td width="250px">参数</td>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    <#list funs as fun>
                        <tr>
                            <td>${fun.fun_id}</td>
                            <td class="left break">
                                <#if is_admin == 1>
                                    <a href="/paas/fun/edit?fun_id=${fun.fun_id}" class="t2">${fun.fun_name!}</a>
                                </#if>
                                <#if is_admin == 0>
                                    <a href="/paas/query/code?code_type=3&id=${fun.fun_id}">${fun.fun_name!}</a>
                                </#if>
                            </td>
                            <td class="left">${fun.name_display!}</td>
                            <td class="left break">${fun.args!}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </datagrid>
</body>
</html>