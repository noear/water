
<!DOCTYPE HTML>
<html>
<head>
    <title>${title!}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js" ></script>
    <script src="${js}/layer/layer.js"></script>
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
<header>
    <label>GRIT</label>
    <nav>
    </nav>
    <aside>
        <a><i class='fa fa-user'></i> 权限管理员</a>
        <a class='split' href='/'><i class='fa fa-fw fa-paper-plane'></i></a>
        <a class='split' href='/grit/login'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>
    </aside>
</header>
<main>
    <left>
        <menu>
            <div onclick="$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}"><i class='fa fa-bars'></i></div>
            <items>
                <a class='sel' href='/grit/ui/resource/space' target="dock">资源空间</a>
                <a href='/grit/ui/resource/group' target="dock">资源组</a>
                <a href='/grit/ui/resource/entity' target="dock">资源</a>
                <br /><br />
                <a href='/grit/ui/subject/group' target="dock">主体组</a>
                <a href='/grit/ui/subject/entity' target="dock">主体</a>
                <a href='/grit/ui/subject/entity/s' target="dock">主体s</a>
                <br /><br />
                <a href='/grit/ui/auth' target="dock">授权</a>
                <a href='/grit/ui/auth/s' target="dock">授权s</a>
            </items>
        </menu>
    </left>
    <right class="frm">
        <iframe src="/grit/ui/resource/space" frameborder="0" name="dock"></iframe>
    </right>
</main>
</body>
</html>






