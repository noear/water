<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 安全名单</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
    </script>
</head>
<body>
<main>
    <toolbar>
        <left>
            <form>
                <#if is_admin == 1>
                    <a class="btn edit mar10-l" href="/cfg/whitelist/edit?tag_name=${tag_name!}">新增</a>
                </#if>
            </form>
            <#if is_admin == 1 && is_setup !=1 >
                <div>
                    <a class="w60"></a><file>
                        <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor w80">导入</a></label>
                    </file>

                    <button type='button' class="minor w80 mar10-l" onclick="exp()" >导出</button>

                    <#if state==1>
                        <button type='button' class="minor mar10-l" onclick="del(0,'禁用')" >禁用</button>
                    <#else>
                        <button type='button' class="minor mar10-l" onclick="del(1,'启用')" >启用</button>
                        <button type='button' class="minor mar10-l" onclick="del(9,'删除')" >删除</button>
                    </#if>
                </div>
            </#if>
        </left>
        <right>
        </right>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="50px">ID</td>
                <td width="100px">type</td>
                <td>value</td>
                <td width="200px">note</td>
                <td width="60px">操作</td>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from" >

            <#list list as m>
                <tr>
                    <td>${m.row_id}</td>
                    <td class="left">${m.type!}</td>
                    <td class="left">${m.value!}</td>
                    <td class="left">${m.note!}</td>
                    <#if is_admin == 1>
                        <td><a class="t2" href="/cfg/whitelist/edit?id=${m.row_id}">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>