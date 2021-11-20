<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 助理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        header aside a{display:inline-block; height:100%; border-left:1px solid #444; padding:0 15px; margin-left:15px;}
        header label{background-color: #222;}
    </style>
    <script>
    $(function (){
        $('nav a').each(function (){
           $(this).click(function (){
               $("a.sel").removeClass("sel");
               $(this).addClass("sel");
           });
        });
    });
    </script>
</head>
<body>
<header>
    <label title="${_version!}">WATER</label>
    <nav>
        <a href="/cfg/prop" class="sel" target="dock">属性配置</a>
        <a href="/cfg/whitelist" target="dock">安全名单</a>
        <a href="/cfg/logger" target="dock">日志存储</a>
        <a href="/cfg/broker" target="dock">消息管道</a>
        <a href="/log/query" target="dock">日志查询</a>
    </nav>
</header>
<main>
    <iframe src="/cfg/prop" frameborder="0" name="dock"></iframe>
</main>
</body>
</html>