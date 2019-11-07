<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑数据块</title>
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
</head>
<body>
<main>
    <toolbar class="style2">
        <cell><h2>D-Block编辑（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2></cell>
        <cell>
                <#if (block.block_id > 0) >
                    <form action="${raas_uri!}/preview.js" target="_blank" method="get">
                        <input type="hidden" name="block" value="${block.tag!}/${block.name!}">
                        <button type="submit">预览</button>
                    </form>
                    &nbsp;&nbsp;
                    <form action="${raas_uri!}/debug" target="_blank" method="get">
                        <input type="hidden" name="block" value="${block.tag!}/${block.name!}">
                        <input type="hidden" name="args" value="{cmd:null,x:'xxx'}">
                        <button type="submit">调试</button>
                    </form>
                </#if>
        </cell>
    </toolbar>
    <hr/>
    <detail>

        <form>
            <table>
                <tr>
                    <input type="hidden" value="${block.block_id}" id="block_id">
                </tr>
                <tr>
                    <td>分类标签</td>
                    <td>
                        <input type="text" id="tag" value="${block.tag!}"/>
                    </td>
                </tr>
                <tr>
                    <td>代号</td>
                    <td><input type="text" id="name" value="${block.name!}"/>
                    </td>
                </tr>
                <tr>
                    <td>显示名</td>
                    <td>
                        <input type="text" id="name_display" value="${block.name_display!}"/>
                        <checkbox>
                        <label><input type="checkbox" id="is_editable" ${(block.is_editable == 1)?string("checked","")}><a>可以编辑</a></label>
                        </checkbox>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input type="text" id="note" value="${block.note!}"/>
                    </td>
                </tr>
                <tr>
                    <td>相关数据库</td>
                    <td>
                        <select id="related_db">
                            <option value=""></option>
                            <#list option_sources as sss>
                                <option value="${sss}">${sss}</option>
                            </#list>
                        </select>
                        <script>
                            $('#related_db').val('${block.related_db!}');
                        </script>
                        <span style="width: 50px;display: inline-block;text-align: center;">相关表</span>
                        <input type="text" id="related_tb" style="width: 237px;" value="${block.related_tb!}"/>
                    </td>
                </tr>
                <tr>
                    <td>数据结构</td>
                    <td>
                        <input type="text" id="struct" class="longtxt" value="${block.struct!}" placeholder="{col:'name'...}"/>&nbsp;&nbsp;
                    </td>
                    <td>（第1列须是唯一约束）</td>
                </tr>
                <tr>
                    <td>扫描表达式</td>
                    <td><p>
                        <textarea id="code" >${block.app_expr!}</textarea>
                        </p>
                    </td>
                    <td>（按Esc全屏）</td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <#if is_operator ==1>
                            <button type="button" onclick="save(false);">保存</button>
                            &nbsp;&nbsp;
                            <button type="button" onclick="save(true);">保存并返回</button>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <code>
                            /*
                            x //参数
                            cmd //指令(默认:(x)->bool；例map:(x)->item/list; 例count:(x)->num)
                            */
                        </code>
                    </td>
                </tr>
            </table>
        </form>
    </detail>
</main>
</body>
</html>
<script>
    function save(is_back) {
        var block_id = '${block.block_id}';
        var tag = $("#tag").val();
        var name = $("#name").val();
        var name_display = $("#name_display").val();
        var note = $("#note").val();
        var related_db = $("#related_db").val();
        var related_tb = $("#related_tb").val();
        var struct = $("#struct").val();
        var is_editable =  $("#is_editable").is(":checked");

        if(is_editable==true){
            is_editable = 1;
        } else {
            is_editable = 0;
        }
        var app_expr = editor.getValue();

        if(!tag){
            top.layer.msg('分类标签不能为空！');
        }
        if(!name){
            top.layer.msg('代号不能为空！');
        }
        if(!name_display){
            top.layer.msg('显示名不能为空！');
        }
        if(!tag){
            top.layer.msg('分类标签不能为空！');
        }

        if(related_db){
            if(!related_tb){
                top.layer.msg('相关表不能为空！');
            }
        }

        if(!struct){
            top.layer.msg('数据结构不能为空！');
        }

        $.ajax({
            type:"POST",
            url:"/rubber/block/edit/ajax/save",
            data:{
                "block_id":block_id,
                "tag":tag,
                "name":name,
                "name_display":name_display,
                "note":note,
                "related_db":related_db,
                "related_tb":related_tb,
                "struct":struct,
                "is_editable":is_editable,
                "app_expr":app_expr
            },
            success:function (data) {
                if(data.code==1) {
                    top.layer.msg(data.msg);

                    if(is_back) {
                        setTimeout(function () {
                            parent.location.href = "/rubber/block?tag=" + tag;
                        }, 500);
                    }
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    };


    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.javascript});
    }
    var editor=CodeMirror.fromTextArea(document.getElementById("code"),{
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
    editor.setSize('601px','150px');
</script>