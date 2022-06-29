
<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 设置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="${css}/font-awesome-4.7.0/css/font-awesome.min.css" />
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
<main>
    <left>
        <menu>
            <div onclick="$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}"><i class='fa fa-bars'></i></div>
            <items>
                <a class='sel' href='/admin/setting' target="inner">设置</a>
                <br/>
                <br/>
                <a href='/grit/ui/resource/entity/s?spaceCode=wateradmin' target="inner">菜单</a>
                <#if subjectScale gt 0>
                    <a href='/grit/ui/subject/group' target="inner">账号组</a>
                    <a href='/grit/ui/subject/entity' target="inner">账号</a>
                    <a href='/grit/ui/auth?spaceCode=wateradmin' target="inner">授权</a>
                <#else>
                    <a href='/grit/ui/subject/entity/s' target="inner">账号</a>
                    <a href='/grit/ui/auth/s?spaceCode=wateradmin' target="inner">授权</a>
                </#if>

                <br/>
                <br/>
                <a href='/admin/about' target="inner">关于</a>
            </items>
        </menu>
    </left>
    <right class="frm">
        <iframe src="/admin/setting" frameborder="0" name="inner"></iframe>
    </right>
</main>
</body>
</html>






