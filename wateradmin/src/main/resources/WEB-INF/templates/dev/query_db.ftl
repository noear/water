<!DOCTYPE HTML>
<html class="mar10">
<head>
    <title>${app} - 数据库查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css" />
    <link rel="stylesheet" href="${css}/font-awesome-4.7.0/css/font-awesome.min.css" >
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js" ></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <style>
        datagrid > div{height: calc(100vh - 122px)}
        datagrid tbody td { text-align: left;}
        datagrid, pre{ border: 1px solid #C9C9C9; margin: 0px; padding: 0px;}

        #btn_query{position: absolute; z-index: 2; top: 22px; right: 20px; border: none;background: transparent; color:green;outline: none;}
        #btn_query:hover{opacity: 0.8;}

        #btn_history{position: absolute; z-index: 2; top: 62px; right: 32px; border: none;background: transparent; color:#888;outline: none;}
        #btn_history:hover{opacity: 0.8;}
        #btn_history i{font-size: 20px;}
    </style>
    <script>
    $(function () {
        $('#btn_query').click(function () {
            var code = window.editor.getValue();

            var _load_idx = top.layer.load(2);
            $.post("/dev/query_db/ajax/do",{code:code},function(rst){
                $("#query_rst").html(rst);
                top.layer.close(_load_idx);
            });
        });
    });
    </script>
</head>
<body>

    <div class="mar10-b">
        <pre id="code" type="text" style="width: 100%; height: 90px;">--tag/key::
select * from tb limit 1</pre>
        <button id="btn_query"><i class="fa fa-play fa-2x"></i></button>
        <a id="btn_history" href="/log/query/inner?tag_name=water&logger=water_log_admin&level=0&tagx=dev_query_sqldb"><i class="fa fa-history fa-2x"></i></a>
    </div>
    <datagrid>
        <div id="query_rst" style="overflow-x: scroll"></div>
    </datagrid>

<script>
    var ext_tools = ace.require("ace/ext/language_tools");

    ext_tools.addCompleter({
        getCompletions: function(editor, session, pos, prefix, callback) {
            callback(null,
                [
                    <#list cfgs as cfg>
                    {name: "--${cfg.tag}/${cfg.key}",value: "--${cfg.tag}/${cfg.key}::", meta: "db",type: "local",score: 1000}<#if cfg_has_next>,</#if>
                    </#list>
                ]);
        }
    });

    function build_editor(elm,mod){
        ace.require("ace/ext/language_tools");

        var editor = ace.edit(elm);


        editor.setTheme("ace/theme/chrome");
        editor.getSession().setMode("ace/mode/"+mod);
        editor.setOptions({
            showFoldWidgets:false,
            showLineNumbers:false,
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });

        editor.setHighlightActiveLine(false);
        editor.setShowPrintMargin(false);
        editor.moveCursorTo(0, 0);

        return editor;
    }

    window.editor = build_editor(document.getElementById("code"),"sql");
</script>

</body>
</html>
