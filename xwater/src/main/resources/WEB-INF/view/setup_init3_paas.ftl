<!DOCTYPE HTML>
<html class="frm-y">
<head>
    <title>${app} - 助理 ${_version!}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <style>
        body > header label{background-color: #222;}
        pre{border:1px solid #C9C9C9;}
        section{margin: 10px;}
        n-l{display: block;line-height: 24px!important;}
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
                let config = $('#config').val();

                top.layer.load(2);

                $.ajax({
                    type:"POST",
                    url:"/ajax/init/water_paas",
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
    <label title="${_version!}">WATER</label>
</header>
<main>
    <section>
        <blockquote>
            <h2 class="ln30">配置 Water PaaS DB (3/5)</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>配置</th><td>
                            <textarea id="config" class="hidden">${config!}</textarea>
                            <pre style="height:150px; width:600px;"  id="config_edit">${config!}</pre>
                        </td>
                    </tr>
                    <tr>
                        <td></td><td>
                            <button type="button">确定</button>
                        </td>
                    </tr>
                    <tr>
                        <td></td><td>
                            <n-l>
                                对应配置：water/water_paas
                            </n-l>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <hr />
                            <n-l>
                                Water PaaS：
                                <br/>
                                提供了 FaaS（即时接口、定时任务、动态事件）和 RaaS（规则计算）两种动态计算服务
                                <br/>
                                为开发和运维提供了友好的动态计算能力和应急处理能力
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