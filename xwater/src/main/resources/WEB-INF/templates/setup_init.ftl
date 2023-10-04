<!DOCTYPE HTML>
<html class="frm-y">
<head>
    <title>${app} - 助理 ${_version!}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="${css}/font-awesome-4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <style>
        body > header label{background-color: #222;}
        pre{border:1px solid #C9C9C9;}
        section{margin: 10px;}
        n-l{display: block;line-height: 24px!important;}
        ul li{line-height: 24px!important;}

        header label sup{color: #00c1de; border-radius: 10px;margin-left: 5px;}
    </style>
    <script>

        function build_editor(mod){
            if(window.editor){
                window.editor.getSession().setMode("ace/mode/"+mod);
                return
            }

            var editor = ace.edit(document.getElementById('config_edit'));

            editor.setTheme("ace/theme/chrome");
            editor.getSession().setMode("ace/mode/"+mod);
            editor.setOptions({
                showFoldWidgets:false,
                showLineNumbers:false,
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });

            editor.setShowPrintMargin(false);
            editor.moveCursorTo(0, 0);

            editor.getSession().on('change', function(e) {
                var value = editor.getValue();
                $('#config').val(value);
            });

            window.editor = editor;
        }

        $(function (){

            build_editor("properties");

            $('button').click(function (){
                let config = '';

                top.layer.load(2);

                $.ajax({
                    type:"POST",
                    url:"/ajax/init/water",
                    data: {config:config},
                    success:function (data) {
                        top.layer.closeAll();

                        if(data.code==200) {
                            top.layer.msg(data.description)
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
    <label>XWATER<sup>${_version!}</sup></label>
</header>
<main>
    <section>
        <blockquote>
            <h2 class="ln30">初始化 Water (1/5)</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th></th>
                        <td>
                            <h2>使用一站式提供服务治理支持平台 - Water。让你轻松工作，早点下班。欢迎加入！</h2>
                            <ul class="mar15-t">
                                <li>1. 配置服务</li>
                                <li>2. 注册与发现服务</li>
                                <li>3. 分布式日志服务</li>
                                <li>4. 消息总线服务（或事件总线）</li>
                                <li>5. 安全名单服务（白名单、黑名单、告警名单之类）</li>
                                <li>6. 跟踪服务</li>
                                <li>7. 监控服务</li>
                                <li>8. FaaS 服务（基于动态技术提供：即时接口、定时任务、动态事件）</li>
                                <li>9. 开发助手</li>
                                <li>a. 告警工具</li>
                                <li>b. 等...</li>
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td></td><td>
                            <button type="button">开始</button>
                        </td>
                    </tr>
                    <tr>
                        <td></td><td>
                            <n-l>
                                对应配置：water/water
                            </n-l>
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
    </section>

</main>
</body>
</html>