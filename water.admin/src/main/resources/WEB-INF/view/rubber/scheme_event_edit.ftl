<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${css}/codemirror/defined.css"/>
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.css"/>
    <link rel="stylesheet" href="${css}/codemirror/show-hint.css"/>
    <link rel="stylesheet" href="${css}/codemirror/fullscreen.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="https://static.kdz6.cn/lib/codemirror.min.js"></script>
    <script src="${js}/codemirror/rubber.js"></script>
    <script src="${js}/codemirror/anyword-hint.js"></script>
    <script src="${js}/codemirror/show-hint.js"></script>
    <script src="${js}/codemirror/jstl-hint.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="${js}/codemirror/matchbrackets.js"></script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
    <script>
        function debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
            }
        }
    </script>
</head>
<body>
<main>
<detail>
    <form>
        <h2>${scheme.name_display}/事件编辑（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
        <hr/>
        <table>
            <tr>
                <td>调试参数</td>
                <td>
                    <input type="text" id="debug_args" value="${scheme.debug_args!}" class="longtxt" placeholder="{user_id:1}"/>
                </td>
            </tr>
            <tr>
                <td>事件处理</td>
                <td><p>
                    <textarea id="input_event" >${scheme.event!}</textarea>
                </p>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <#if is_operator ==1>
                    <button type="button" id="btn_save" >保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>

                    <button type="button" onclick="debug()" class="minor" >调试事件</button>

                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <code>
                        /*
                        c   //上下文对象；
                        m   //当前模型；
                        sm  //当前匹配结果；
                        */
                    </code>
                    <br />
                    <code>
                        /* sm:{scheme,total,value,relation,is_match} */
                    </code>
                </td>
            </tr>
        </table>
    </form>
    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
        <input type="hidden" name="args" id="args">
        <input type="hidden" name="type" value="1">
        <input type="hidden" name="policy" value="1001">
    </form>
</detail>
</main>
</body>
</html>
<script>
    var f = '${f}';
    //保存事件编辑
    $("#btn_save").on("click", function(){
        var event = editor.getValue();

        $.ajax({
            type: "POST",
            url: "/rubber/scheme/event/edit/ajax/save",
            data: {
                "scheme_id": ${scheme.scheme_id},
                "event": event
            },
            success: function (data) {
                if (data.code == 1) {
                    top.layer.msg(data.msg)
                    setTimeout(function () {
                        location.href = "/rubber/scheme/inner?tag_name=${scheme.tag!}"+"&f="+f;
                    }, 1000);
                } else {
                    top.layer.msg(data.msg);
                }
            }
        });
    });


    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.javascript});
    }
    var editor=CodeMirror.fromTextArea(document.getElementById("input_event"),{
        //高亮显示
        mode:"text/javascript",

        //显示行号
        lineNumbers:true,
        //在缩进时，是否需要把 n*tab宽度个空格替换成n个tab字符，默认为false 。
        indentWithTabs: true,
        //自动缩进
        smartIndent: true,
        //光标高度，占满整行为1。（数值范围0~1）
        cursorHeight: 1,


        //设置主题 css文件为defined
        theme:"eclipse",

        //绑定Vim
        // keyMap:"vim",

        //代码折叠
        lineWrapping:true,
        foldGutter: true,
        gutters:["CodeMirror-linenumbers", "CodeMirror-foldgutter"],

        //括号匹配
        matchBrackets:true,

        //智能提示
        extraKeys: {
            "'a'": completeAfter,
            "'b'": completeAfter,
            "'c'": completeAfter,
            "'d'": completeAfter,
            "'e'": completeAfter,
            "'f'": completeAfter,
            "'g'": completeAfter,
            "'h'": completeAfter,
            "'i'": completeAfter,
            "'j'": completeAfter,
            "'k'": completeAfter,
            "'l'": completeAfter,
            "'m'": completeAfter,
            "'n'": completeAfter,
            "'o'": completeAfter,
            "'p'": completeAfter,
            "'q'": completeAfter,
            "'r'": completeAfter,
            "'s'": completeAfter,
            "'t'": completeAfter,
            "'u'": completeAfter,
            "'v'": completeAfter,
            "'w'": completeAfter,
            "'x'": completeAfter,
            "'y'": completeAfter,
            "'z'": completeAfter,
            "'A'": completeAfter,
            "'B'": completeAfter,
            "'C'": completeAfter,
            "'D'": completeAfter,
            "'E'": completeAfter,
            "'F'": completeAfter,
            "'G'": completeAfter,
            "'H'": completeAfter,
            "'I'": completeAfter,
            "'J'": completeAfter,
            "'K'": completeAfter,
            "'L'": completeAfter,
            "'M'": completeAfter,
            "'N'": completeAfter,
            "'O'": completeAfter,
            "'P'": completeAfter,
            "'Q'": completeAfter,
            "'R'": completeAfter,
            "'S'": completeAfter,
            "'T'": completeAfter,
            "'U'": completeAfter,
            "'V'": completeAfter,
            "'W'": completeAfter,
            "'X'": completeAfter,
            "'Y'": completeAfter,
            "'Z'": completeAfter,
            "'.'": completeAfter,
            "'='": completeIfInTag,
            // ,
            // "Ctrl-Space": "autocomplete",
            // "Ctrl-Enter": "autocomplete",

            Tab: function(cm) {
                var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
                cm.replaceSelection(spaces);
            },
            "Esc": function (cm) {
                cm.setOption("fullScreen", !cm.getOption("fullScreen"));
                top.layer.msg("按Esc即可退出或进入编辑！")
            },
            "Ctrl-Enter": function () {
                saveEdit();
            }
        }
    });
    function completeIfInTag(cm) {
        return completeAfter(cm, function() {
            var tok = cm.getTokenAt(cm.getCursor());
            if (tok.type == "string" && (!/['"]/.test(tok.string.charAt(tok.string.length - 1)) || tok.string.length == 1)) return false;
            var inner = CodeMirror.innerMode(cm.getMode(), tok.state).state;
            return inner.tagName;
        });
    }
    function completeAfter(cm, pred) {
        var cur = cm.getCursor();
        if (!pred || pred()) setTimeout(function() {
            if (!cm.state.completionActive)
                cm.showHint({
                    completeSingle: false
                });
        }, 100);
        return CodeMirror.Pass;
    };

    editor.setSize('602px','320px');

</script>