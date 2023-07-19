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
                let config = $('#config').val();

                top.layer.load(2);

                $.ajax({
                    type:"POST",
                    url:"/ajax/init/water_log",
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
            <h2 class="ln30">配置 Water Log Service Store (4/5)</h2>
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
                                对应配置：water/water_log_store
                            </n-l>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <hr />
                            <n-l>
                                <div>
                                    Water Log，分布式日志服务：
                                    <br/>
                                    基于统一收集、统一查询界面的原则。提倡日志业务，记录日志的同时把上下文数据也带着。
                                </div>
                                <div class="mar10-t">
                                    支持 MySql 或 MongoDB 或 ElasticSearch（支持内容搜索） 三种持久化方案
                                </div>
                            </n-l>
                            <br/>
                            <n-l>MongoDB 示例：</n-l>
                            <code>
                                driverType=mongodb<br/>
                                schema=water_log_store<br/>
                                url=mongodb://water:1234@mongodb.water.io<br/>
                            </code>
                            <br/>
                            <n-l>ElasticSearch 示例：（支持内容搜索）</n-l>
                            <code>
                                driverType=elasticsearch<br/>
                                url=localhost:1212<br/>
                            </code>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <hr />
                            <n-l>
                                Water Log DB（for mysql），支持 MySql 8.0、5.7、5.6（建议用 8.0）。如果有需要，可以创建个空库：
                                <br />
                                <code lang="sql">
                                    CREATE DATABASE water_log_store <br/>
                                    DEFAULT CHARACTER SET utf8mb4 <br/>
                                    DEFAULT COLLATE utf8mb4_general_ci;
                                </code>
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