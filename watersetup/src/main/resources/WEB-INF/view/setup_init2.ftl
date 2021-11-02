<!DOCTYPE HTML>
<html class="frm-y">
<head>
    <title>${app} - 安装工具</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body > header label{background-color: #222;}

        section{margin: 10px;}
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
    <label>WATER</label>
</header>
<main>
    <section>
        <blockquote>
            <h2 class="ln30">1 主数据库初始化</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>配置</th><td>
                            <textarea rows="5">${rdb_water_tml!}</textarea>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
    </section>

    <section>
        <blockquote>
            <h2 class="ln30">2 日志存储初始化</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>类型</th><td>
                            <boxlist>
                                <label><input type="radio" name="log_drive_type" /><a>MySQL</a></label>
                                <label><input type="radio" name="log_drive_type" /><a>MongoDB</a></label>
                                <label><input type="radio" name="log_drive_type" /><a>Elasticsearch</a></label>
                            </boxlist>
                        </td>
                    </tr>
                    <tr>
                        <th>配置</th><td>
                            <textarea rows="5"></textarea>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
    </section>

    <section>
        <blockquote>
            <h2 class="ln30">3 消息存储初始化</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>类型</th><td>
                            <boxlist>
                                <label><input type="radio" name="msg_drive_type" /><a>MySQL</a></label>
                                <label><input type="radio" name="msg_drive_type" /><a>MongoDB</a></label>
                            </boxlist>
                        </td>
                    </tr>
                    <tr>
                        <th>配置</th><td>
                            <textarea rows="5"></textarea>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
    </section>

</main>
</body>
</html>