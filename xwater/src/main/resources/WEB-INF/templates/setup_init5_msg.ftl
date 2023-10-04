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
                    url:"/ajax/init/water_msg",
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
            <h2 class="ln30">配置 Water Msg Bus Store (5/5)</h2>
        </blockquote>
        <detail>
            <form>
                <table>
                    <tr>
                        <th>配置</th><td>
                            <textarea id="config" class="hidden">${config!}</textarea>
                            <pre style="height:250px; width:600px;"  id="config_edit">${config!}</pre>
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
                            对应配置：water/water_msg_store
                            </n-l>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <hr />
                            <n-l>
                                <div>
                                    Water Msg Bus：
                                    <br/>
                                    为事件驱动开发、为业务水扩展，提供有力的支持
                                    <br/>
                                    同时为分布式环境配置同步、本地缓存同步刷新提供保障
                                </div>

                                <div class="mar10-t">
                                    支持 MySql 或 MongoDB 两种持久化方案
                                </div>
                            </n-l>

                            <br/>
                            <n-l>MongoDB + Local示例：</n-l>
                            <code>
                                store.driverType=mongodb<br/>
                                store.schema=water_msg_store<br/>
                                store.url=mongodb://water:1234@mongodb.water.io<br/>
                                queue.driverType=local<br/>
                            </code>

                        </td>
                    </tr>

                </table>
            </form>
        </detail>
    </section>

</main>
</body>
</html>