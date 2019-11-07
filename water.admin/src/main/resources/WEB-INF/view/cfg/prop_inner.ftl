<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function editTask(id) {
        location.href="/cfg/prop/edit?id="+id;
    };

    function add() {
        location.href = "/cfg/prop/add";
    };

    function impCfg(tag) {
        $.getJSON('/cfg/prop/ajax/import?tag='+tag,function (rst) {
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
<body>
        <toolbar>
            <left>
                <form>
                    key：<input type="text"  name="key" placeholder="key" id="key"/>
                    <input type="hidden"  name="tag" id="tag" value="${tag}"/>
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                        <button type='button' class="edit" onclick="add();" >新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type='button' class="minor" onclick="impCfg('${tag}')" >导入</button>
                    </#if>
                </form>
            </left>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="70px" >tag</td>
                    <td width="80px" nowrap>key</td>
                    <td width="80px" nowrap>type</td>
                    <td>url</td>
                    <td width="90px">user</td>
                    <td width="90px">explain</td>
                    <#if is_admin == 1>
                        <td width="30px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list cfgs as cfg>
                    <tr>
                        <td>${cfg.tag}</td>
                        <td style="text-align: left;">${cfg.key!}</td>
                        <td>${cfg.type_str()}</td>
                        <td style="text-align:left;word-wrap:break-word;word-break:break-all;!important;">${cfg.url!}</td>
                        <td style="word-wrap:break-word;word-break:break-all;text-align: left;">${cfg.user!}</td>
                        <td style="word-wrap:break-word;word-break:break-all;text-align: left;">${cfg.explain!}</td>
                        <#if is_admin == 1>
                            <td><a onclick="editTask('${cfg.id}')" style="color: blue;cursor: pointer">编辑</a></td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>

</body>
</html>