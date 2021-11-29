<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 权限管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js" ></script>
    <script src="${js}/layer.js"></script>
    <style>
        body > header aside a{display:inline-block; height:100%; padding:0 15px; }
        body > header aside .split{border-left:1px solid #444;}
    </style>
    <script>
        $(function (){
            $("menu a").click(function (){
                $("a.sel").removeClass("sel");
                $(this).addClass("sel");
            });
        });
    </script>
</head>
<body>
<main>
    <left>
        <menu>
            <div onclick="$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}"><i class='fa fa-bars'></i></div>
            <items>
                <a class='sel' href='/grit/resource/space' target="dock">资源空间</a>
                <a href='/grit/resource/group' target="dock">资源组</a>
                <a href='/grit/resource/entity' target="dock">资源</a>
                <br /><br />
                <a href='/grit/subject/group' target="dock">主体组</a>
                <a href='/grit/subject/entity' target="dock">主体</a>
                <br /><br />
                <a href='/grit/auth' target="dock">授权</a>
            </items>
        </menu>
    </left>
    <right class="frm">
        <iframe src="/grit/resource/space" frameborder="0" name="dock"></iframe>
    </right>
</main>
</body>
</html>






