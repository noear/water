<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 代码查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/clipboard.min.js"></script>
    <script src="${js}/layer/layer.js"></script>

</head>
<body>

<main>

    <toolbar>
        <form>
            <select id="code_type" name="code_type">
                <option value="0">模型构造代码</option>
                <option value="1">字段动态代码</option>
                <option value="2">计算事件代码</option>
                <option value="3">数据扫描代码</option>
            </select>：<input type="text" placeholder="代码片断" id="code" name="code"/>
            <button type="submit">查询</button>
        </form>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="50px">ID</td>
                <td width="120px" class="left">分类</td>
                <td width="300px" class="left">名称</td>
                <td class="left">备注</td>
                <td width="110px">操作</td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list list as m>
                <tr>
                    <td>${m.id}</td>
                    <td class="left">${m.tag}</td>
                    <td class="left">${m.name}</td>
                    <td class="left">${m.note}</td>
                    <td class="op">
                        <a href="/rubber/query/code?code_type=${m.code_type}&id=${m.id}" class="t2">查看代码</a> |
                        <a href="#" onclick="edit('${m.code_type}','${m.tag}',${m.pid},${m.id})" class="t2">编辑</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</main>

</main>
</body>
<script>
    function edit(code_type,tag,pid,id) {
        if (code_type == 0) {
            //接口代码
            location.href = "/rubber/model?tag_name="+tag+"&model_id="+id;
        } else if (code_type == 1) {
            //定时任务
            location.href = "/rubber/model?tag_name="+tag+"&model_id="+pid+"&field_id="+id;
        } else if (code_type == 2) {
            //同步任务
            location.href = "/rubber/scheme?tag_name="+tag+"&scheme_id="+id;
        }else if (code_type == 3) {
            //同步任务
            location.href = "/rubber/block?tag_name="+tag+"&block_id="+id;
        }
    }
</script>
</html>