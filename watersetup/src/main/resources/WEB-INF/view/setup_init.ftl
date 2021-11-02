<!DOCTYPE HTML>
<html class="frm-y">
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
        body > header label{background-color: #222;}

        section{margin: 10px;}
    </style>
    <script>
        $(function (){
            $('button').click(function (){
                let config = $('#config').val();

                top.layer.load(2);

                $.ajax({
                    type:"POST",
                    url:"/ajax/connect",
                    data: {config:config},
                    success:function (data) {
                        top.layer.closeAll();

                        if(data.code==200) {
                            top.layer.msg('连接成功')
                            setTimeout(function(){
                                location.reload();
                            },800);
                        }else{
                            top.layer.msg(data.description);
                        }
                    },
                    error:function(data){
                        top.layer.closeAll();
                        top.layer.msg('网络请求出错...');
                    }
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
            <h2 class="ln30">连接主数据库</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>配置</th><td>
                            <textarea id="config" rows="5">${config!}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td></td><td>
                            <button type="button">确定</button>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
    </section>

</main>
</body>
</html>