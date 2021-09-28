<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - ES查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" >
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <style>
        html,body,main,left,right,pre{margin: 0px; padding: 0px; overflow: hidden;}

        main > * {overflow: hidden; display: inline-block; margin: 10px 0 10px 10px;width:calc(50vw - 18px);}
        pre{height: calc(100vh - 20px)}

        #btn_query{position: absolute; z-index: 2; top: 20px; left: calc(50vw - 50px); border: none;background: transparent; color:green;outline: none;}
        #btn_query:hover{opacity: 0.8;}
    </style>
    <style>
        pre{border:1px solid #C9C9C9;}
    </style>
    <script>
        var ext_tools = ace.require("ace/ext/language_tools");

        ext_tools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {
                callback(null,
                    [
                        <#list cfgs as cfg>
                        {name: "${cfg.tag}/${cfg.key}",value: "${cfg.tag}/${cfg.key}", meta: "db",type: "local",score: 1000}<#if cfg_has_next>,</#if>
                        </#list>
                    ]);
            }
        });

        function build_editor(elm,mod){
            var editor = ace.edit(elm);

            editor.setTheme("ace/theme/chrome");
            editor.getSession().setMode("ace/mode/"+mod);
            editor.setOptions({
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });

            editor.setHighlightActiveLine(false);
            editor.setShowPrintMargin(false);
            editor.moveCursorTo(0, 0);

            editor.getSession().on('change', function(e) {
                var value = editor.getValue();
                $('#value').val(value);
            });

            return editor;
        }

        $(function () {
            $('#btn_query').click(function () {
                var code = window.editor.getValue();

                var _load_idx = top.layer.load(2);
                $.post("/dev/query_es/ajax/do",{code:code},function(rst){
                    window.editor2.setValue(rst);
                    window.editor2.moveCursorTo(0, 0);
                    top.layer.close(_load_idx);
                });
            });

            window.editor = build_editor($('#code')[0],'ini');
            window.editor2 = build_editor($('#query_rst')[0],'json');
            window.editor2.setReadOnly(true);
        });
    </script>
</head>
<body>

<button id="btn_query"><i class="fa fa-play fa-2x"></i></button>

<main>
    <left>
        <pre id="code" jt-ini >#tag/key
GET _search
{
  "query": {
    "match_all": {}
  }
}</pre>
    </left>
    <right>
        <pre id="query_rst"  jt-json ></pre>
    </right>
</main>

</body>
</html>
