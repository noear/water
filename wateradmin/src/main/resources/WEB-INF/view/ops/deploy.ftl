<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 项目发布</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<body>

<toolbar>
    <left>
        <button class="edit" type="button">新建</button>
    </left>
    <right>
        <@stateselector items="全部,要我处理的,我新建的,已关闭"/>
    </right>
</toolbar>

<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="40px" >ID</td>
            <td width="130px" nowrap>项目名称</td>
            <td width="40px" nowrap>类型</td>
            <td>源码地址（git）</td>
            <#if is_admin == 1>
                <td  width="100px">操作</td>
            </#if>
        </tr>
        </thead>
        <tbody id="tbody">

        </tbody>
    </table>
</datagrid>

</body>
</html>