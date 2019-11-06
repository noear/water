<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - ${fun_name}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<body>
<@header/>
<main>
    <#if fun_type = 0>
        <left>
            <@leftmenu/>
        </left>
        <right class="frm">
            <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
        </right>
    <#else>
        <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
    </#if>
</main>
<@footer/>
</body>
</html>






