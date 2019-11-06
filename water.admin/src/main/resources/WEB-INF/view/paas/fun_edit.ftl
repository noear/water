<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑函数</title>
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
    <script src="${js}/codemirror/jstl.js"></script>
    <script src="${js}/codemirror/anyword-hint.js"></script>
    <script src="${js}/codemirror/show-hint.js"></script>
    <script src="${js}/codemirror/jstl-hint.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="${js}/codemirror/matchbrackets.js"></script>

    <script>
        var fun_id = ${fun.fun_id}
        function saveEdit() {
            var code = editor.getValue();
            var tag = $('#tag').val();
            var fun_name = $('#fun_name').val();
            var name_display = $('#name_display').val();
            var args = $('#args').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;
            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (fun_name == null || fun_name == "" || fun_name == undefined) {
                top.layer.msg("函数名称不能为空！");
                return;
            }

                if(fun_id==null){
                    fun_id=0
                }
            $.ajax({
                type:"POST",
                url:"/paas/fun/edit/ajax/save",
                data:{"fun_id":fun_id,"code":code,"tag":tag,"fun_name":fun_name,"name_display":name_display,"note":'',"is_enabled":is_enabled,"args":args},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function back(tag_name) {
            var tag = $('#tag').val();
            top.layer.confirm('确定返回？代码已保存?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                parent.location.href="/paas/fun?tag_name="+tag_name;
                top.layer.close(top.layer.index);
            });
        }

        function preview() {
            var tag = $('#tag').val();
            var name = $('#fun_name').val();

            if(!tag){
                top.layer.msg('请输入标签');
                return;
            }

            if(!name){
                top.layer.msg('请输入名称');
                return;
            }

            window.open("${url_start!}/preview.js?fun="+tag+'/'+name);
        }
    </script>
    <style type="text/css">
       .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>

<flex>
    <left class="col-6"><h2>函数编辑（<a onclick="history.back(-1)" class="t2">返回</a>）</h2></left>
    <right class="col-6">
        <@versions table="paas_fun" keyName="fun_id" keyValue="${fun.fun_id}">
            $('#form1').setForm(m);
            editor.getDoc().setValue(m.code);
        </@versions>
    </right>
</flex>
<hr/>
        <detail>
            <form>
            <table>
                <tr>
                <tr>
                    <td>标签*</td>
                    <td>
                        <input type="text" id="tag" value="${fun.tag!}"/>（字母或_或数字组成）
                    </td>
                </tr>
                <tr>
                    <td>名称*</td>
                    <td><input type="text" id="fun_name" value="${fun.fun_name!}"/></td>
                </tr>
                <tr>
                    <td>显示名</td>
                    <td><input type="text" id="name_display" value="${fun.name_display!}"/></td>
                </tr>
                <tr>
                    <td>参数</td>
                    <td><input type="text" id="args" value="${fun.args!}"/>（参数之间逗号分隔）</td>
                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(fun.is_enabled == 1)?string("checked","")}><a></a></label>
                        </switcher>
                    </td>
                </tr>
                <tr>
                    <td>代码</td>
                    <td><p>
                         <textarea id="code" >${fun.code!}</textarea>
                    </p>
                    </td>
                    <td>（按Esc全屏）</td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;
                        <button type="button" onclick="back('${tag_name!}')">返回</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" onclick="preview()" class="minor">预览</button>
                    </td>
                </tr>
            </table>
            </form>
        </detail>

<script>
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
    editor.setSize('700px','200px');
</script>

</body>
</html>