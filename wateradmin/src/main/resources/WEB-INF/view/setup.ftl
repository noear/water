<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 安装模式</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        header aside a{display:inline-block; height:100%; border-left:1px solid #444; padding:0 15px; margin-left:15px;}
    </style>
</head>
<body>
<header>
    <label>Water console setup mode ...</label>
    <nav>
        <a href="/cfg/prop" class="sel" target="dock">属性配置</a>
        <a href="/cfg/whitelist" target="dock">安全名单</a>
    </nav>
</header>
<main>
    <iframe src="/cfg/prop" frameborder="0" name="dock"></iframe>
</main>
</body>
</html>