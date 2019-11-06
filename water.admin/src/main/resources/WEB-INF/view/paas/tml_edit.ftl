<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑公共模版</title>
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
    <script src="${js}/codemirror/xml.js"></script>
    <script src="${js}/codemirror/show-hint.js"></script>
    <script src="${js}/codemirror/xml-hint.js"></script>
    <script src="${js}/codemirror/html-hint.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="${js}/codemirror/matchbrackets.js"></script>

    <script>
        var tml_id=${tml.tml_id!};

        function save() {

            var tag = $("#tag").val();

            if (!tag) {
                top.layer.msg("输入tag!");
                return;
            }

            if (!$("#tml_name").val()) {
                top.layer.msg("输入名称!");
                return;
            }


            $.ajax({
                type: "POST",
                url: "/paas/tml/ajax/save",
                data: {
                    tml_id: tml_id,
                    tag: tag,
                    tml_name: $("#tml_name").val(),
                    name_display:$('#name_display').val(),
                    code: editor.getValue(),
                    is_enabled: $('#is_enabled').prop('checked')?1:0
                },
                success: function (data) {
                    top.layer.msg(data.msg);
                    if (1 == data.code) {
                        if(tml_id == 0) {
                            setTimeout(function () {
                                parent.location.href="/paas/tml?tag_name="+tag;
                            }, 500);
                        }
                    }
                }
            });
        }

    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>

<flex>
    <left class="col-6"><h2>模版编辑（<a onclick="history.back()" class="t2">返回</a>）</h2></left>
    <right class="col-6">
        <@versions table="paas_tml" keyName="tml_id" keyValue="${tml.tml_id}">
            $('#form1').setForm(m);
        </@versions>
    </right>
</flex>
<hr/>
<detail>
    <form id="form1">

        <table>
            <tr>
            <tr>
                <td>tag*</td>
                <td>
                    <input type="text" id="tag" value="${tml.tag!}"/>
                </td>
            </tr>
            <tr>
                <td>名称*</td>
                <td><input type="text" id="tml_name" value="${tml.tml_name!}"/></td>
            </tr>
            <tr>
                <td>显示名</td>
                <td><input type="text" id="name_display" value="${tml.name_display!}"/></td>
            </tr>
            <tr>
                <td>是否启用</td>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(tml.is_enabled == 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
            <tr>
                <td>代码</td>
                <td><p>
                        <textarea id="code" >${tml.code!}</textarea>
                    </p>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <td></td>
                <td><button type="button" onclick="save()">保存</button></td>
            </tr>
        </table>
    </form>
</detail>

<script>
    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.javascript});
    };
    var editor=CodeMirror.fromTextArea(document.getElementById("code"),{
        //高亮显示
        mode:"text/html",

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
    //设置宽高,默认为自动根据每行长度来调整。
    //'width','height'
    editor.setSize('700px','220px');
</script>

</body>
</html>