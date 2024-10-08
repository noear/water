<!DOCTYPE HTML>
<html class="mar10">
<head>
    <title>${app} - DDL查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <style>
        .header {height: 50px;}
        .header li {display: inline-block;padding-left: 10px;padding-right: 10px;}
        .header a.sel{color: blue;}
        .header a:hover{color: blue;}

        .left {width: 200px;}
        .left ul{display: block; height: 100%; overflow-y: scroll; overflow-x:hidden;}
        .left li {line-height: 24px; overflow: hidden;}
        .left a.sel{color: blue;}
        .left a:hover{color: blue;}

        pre{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
    <script>

        $(function () {
            $('.header').find('a').first().click();
        });

        function get_tbs(e, key) {
            $('.header').find('a.sel').removeClass('sel');
            $(e).addClass('sel');

            $.ajax({
                type:"POST",
                url:"/dev/ddl/ajax/tb",
                data:{
                    tag: '${tag_name}',
                    key: key
                },
                success:function (data) {
                    if (1 === data.code) {
                        var r = "";
                        for (var idx in data.tbs) {
                            var tb = data.tbs[idx];
                            r += '<li><a href="javascript:void(0);" onclick="get_ddl(this, \'' + key + '\', \'' + tb + '\')">' + tb + '</a></li>';
                        }
                        $("#tb-tbs").html(r);
                        $(".left").find('a').first().click();
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function get_ddl(e, key, tb) {
            $('.left').find('a.sel').removeClass('sel');
            $(e).addClass('sel');

            $.ajax({
                type:"POST",
                url:"/dev/ddl/ajax/getddl",
                data:{
                    tag: '${tag_name!}',
                    key: key,
                    tb: tb
                },
                success:function (data) {
                    if (1 === data.code) {
                        //$("#code").html(data.ddl);
                        window.editor.setValue(data.ddl);
                        window.editor.moveCursorTo(0, 0);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

    </script>
</head>
<body>

<table style="width: 100%; height: 100%;">
    <tr>
        <td class="header" colspan="2">
            <block class="ln30">
                <ul>
                    <#list cfgs as c>
                        <li><a href="javascript:void(0);" onclick="get_tbs(this, '${c.key}')">${c.key}</a></li>
                    </#list>
                </ul>
            </block>
        </td>
    </tr>
    <tr>
        <td class="left" valign="top">
            <ul id="tb-tbs"></ul>
        </td>
        <td valign="top">
            <pre id="editor" style="width: 100%; height: calc(100vh - 81px);"></pre>
        </td>
    </tr>
</table>

<script>
    function build_editor(elm,mod){
        ace.require("ace/ext/language_tools");

        var editor = ace.edit(elm);

        editor.setTheme("ace/theme/chrome");
        editor.getSession().setMode("ace/mode/"+mod);
        editor.setOptions({

            showFoldWidgets:false,
            showLineNumbers:true,
            enableBasicAutocompletion: false,
            enableSnippets: false,
            enableLiveAutocompletion: false
        });

        editor.setHighlightActiveLine(false);
        editor.setReadOnly(true);
        editor.setShowPrintMargin(false);
        editor.moveCursorTo(0, 0);

        return editor;
    }

    window.editor = build_editor(document.getElementById("editor"),"sql");
</script>
</body>
</html>