<!doctype html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>${XUtil.cfgGet('article_site_title')!}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <script src="//cdn.jsdelivr.net/npm/jquery@3.4.1/dist/jquery.min.js"></script>
    <style>
        html{background: #f9f9f9; }
        body{padding:0px; margin:0px; display: flex;}
        ul,li{display:block; margin:0px; padding:0px;}
        a{text-decoration:none;}

        aside{width:280px; min-height:100vh;  background:#2c2e2f; position: fixed; top:0px; left:0px;}
        aside header{text-align: center; display:block; height:60px; line-height: 60px; color:#fff; border-bottom:1px solid #313437; margin-bottom:20px;}
        aside header span {font-size:x-large;}
        aside header i{display:none!important;}

        aside li{line-height: 40px; margin: 0px 40px 0px 40px; border-bottom: 1px solid #313437;}
        aside li a{color:#979898; font-size:small; padding: 13px 5px;}
        aside li:hover a{color:#fff;}

        article{width: 100%; min-height:100vh;  padding-left: 300px; overflow-y: auto;}

        article footer{height: 40px; line-height: 40px; margin-left:6px; color:#888; font-size: small;}

        article section {margin-bottom: 10px; }
        article section > header{margin-bottom: 10px; margin-left:6px; color:#555; padding-top: 10px; }

        article section li{ background:#fff; display: inline-block; width: 200px; height: 50px; overflow: hidden; border: 1px solid #e4ecf3;border-radius: 4px;padding: 10px; margin:5px;}
        article section li a{color:#373e4a;}
        article section li b{display:block;}
        article section li i{font-size: x-small; display:block; font-style:normal; color: #979898;  }

        article section li:hover{background:#e4ecf3;}


        @media screen and (max-width: 720px) {
            body{display:block;}
            aside{display:block;width:100%; min-height:auto; height:auto; position: static}
            aside header{ position: relative;}
            aside header span{font-size:xx-large;}
            aside header i{display:inline-block!important; position: absolute; right:10px; top:20px; font-size:1.2rem!important; color:#555;}
            aside ul{display:none;}

            article{padding:10px;height:auto;}
            article section li{width:calc(100vw - 50px);}
        }


    </style>
    <script>
        $(function(){
            $('aside header i').click(function(){
                $('aside ul').toggle("solw");
            });
        });
    </script>
</head>
<body>

<aside>
    <header><span>${XUtil.cfgGet('article_site_home')!}</span> <i class="fa fa-bars" aria-hidden="true"></i></header>
    <ul>
        <#list agroup as g1>
            <li><a href="#g${g1.id}">${g1.title!}</a></li>
        </#list>
    </ul>
</aside>
<article>
    <#list agroup as g1>
        <section>
            <header id="g${g1.id}">${g1.title}</header>

            <ul>
                <#list g1.alist as a1>
                    <li>
                        <a href="/article/${a1.id}" target="_blank">
                            <div>
                                <b>${a1.title!}</b>
                                <i>${a1.summary!}</i>
                            </div>
                        </a>
                    </li>
                </#list>
            </ul>
        </section>
    </#list>
    <footer>
        ${XUtil.cfgGet('article_site_intro')!}
    </footer>
</article>

</body>
</html>

