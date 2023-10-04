<!doctype html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>源码：${m1.path!}</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/base64.js" ></script>
    <script src="${js}/jtadmin.js?v=4"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>

    <style>
        html,body{margin:0px;padding:0px;overflow:hidden;}
        main{margin:10px;}

        main > pre{border:1px solid #C9C9C9; margin: 0px;}

        em{color:#999;font-style:normal;}
    </style>
    <script>
        var base64 = new Base64();

        function file_save() {
            var fc = window.editor.getValue();
            var fc64 = base64.encode(fc);

            ajaxPost({url:"./code/ajax/save", data:{'fc64':fc64, 'id':${id}, 'path':'${m1.path!}'}});
        }
        <#if is_admin = 1>
        ctl_s_save_bind(document, file_save);
        </#if>
    </script>
</head>
<body>
<main>
    <pre id="editor" style="height: calc(100vh - 80px);"></pre>

    <flex style="margin-top: 18px;">
        <left class="col-6 form">
            <#if is_admin = 1>
            <button type="button" onclick="file_save()">保存 <u>S</u></button>
            </#if>
            <#if is_operator = 1>
            <a href="${faas_uri}${m1.path!}?_debug=1" class="btn minor mar10-l" onclick="return confirm('确定要调试吗？')" target="_blank">调试</a>
            </#if>
        </left>
        <right class="col-6">
            <@versions table="luffy_file" keyName="file_id" keyValue="${m1.file_id}">
                window.editor.setValue(m.content);
            </@versions>
        </right>
    </flex>


    <script>
        var code64 = "${code64}";
        var ext_tools = ace.require("ace/ext/language_tools");

        ext_tools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {

                obj = editor.getSession().getTokenAt(pos.row, pos.column- prefix.length);
                console.log(obj);

                callback(null,
                    [
                        {name: "db",value: "db", meta: "DbContext",type: "local",score: 1000},
                        {name: "cache",value: "cache", meta: "ICacheServiceEx",type: "local",score: 1000},
                        {name: "ctx",value: "ctx", meta: "Context",type: "local",score: 1000},
                        {name: "localCache",value: "localCache", meta: "ICacheServiceEx",type: "local",score: 1000},

                        {name: "XMsg",value: "XMsg", meta: "XMsg",type: "local",score: 1000},
                        {name: "XFun",value: "XFun", meta: "XFun",type: "local",score: 1000},
                        {name: "XUtil",value: "XUtil", meta: "XUtil",type: "local",score: 1000},
                        {name: "XLock",value: "XLock", meta: "XLock",type: "local",score: 1000},

                        {name: "water",value: "water", meta: "WaterClient",type: "local",score: 1000},
                        {name: "rock",value: "rock", meta: "RockClient",type: "local",score: 1000},

                        {name: "requireX",value: "requireX", meta: "requireX(path)",type: "local",score: 1000},
                        {name: "modelAndView",value: "modelAndView", meta: "modelAndView(path,model)",type: "local",score: 1000},

                    ]);
            }
        });

        window.editor = ace.edit("editor");

        window.editor.setTheme("ace/theme/chrome");
        window.editor.getSession().setMode("ace/mode/${edit_mode}");
        window.editor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });

        window.editor.setShowPrintMargin(false);
        window.editor.setValue(base64.decode(code64));
        window.editor.moveCursorTo(0, 0);
    </script>
</main>
</body>
</html>