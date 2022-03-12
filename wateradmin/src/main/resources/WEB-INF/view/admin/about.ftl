<!DOCTYPE HTML>
<html class="frm10-y">
<head>
    <title>${app} - 关于</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        body  detail{ width: 800px; margin: 0 auto;}
        form > table{width: 800px;}
        form > table th{width: 150px;}
        form > table td > h2{line-height: 40px;}
    </style>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left class="ln30">
            <h2>关于</h2>
        </left>
        <right class="form">

        </right>
    </toolbar>

    <detail>
        <table>
            <tr><td>版本：</td><td>${_version!}</td></tr>
            <tr><td>协议：</td><td>Apache-2.0</td></tr>
            <tr><td>描述：</td><td>为分布式服务开发和治理，提供一站式解决方案（或者，支持套件）</td></tr>
        </table>
    </detail>
</main>

</body>
</html>