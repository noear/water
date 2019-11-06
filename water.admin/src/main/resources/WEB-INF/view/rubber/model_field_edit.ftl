<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据模型字段-编辑</title>
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
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        var field_id = '${field.field_id}';
        var model_id = '${model_id}';
        var f = '${f}';
        function saveEdit(is_back) {
            var expr = editor.getValue();
            var note = $('#note').val();
            var name = $('#name').val();
            var name_display = $('#display').val();
            var is_pk = $("input[name='is_pk']:checked").val();

            if (!name){
                top.layer.msg("代号不能为空！");
                return;
            }

            if (!name_display){
                top.layer.msg("显示名不能为空！");
                return;
            }

            if(!field_id){
                field_id=0;
            }
            $.ajax({
                type:"POST",
                url:"/rubber/model/field/edit/ajax/save",
                data:{
                    "model_id":model_id,
                    "field_id":field_id,
                    "expr":expr,
                    "name":name,
                    "name_display":name_display,
                    "note":note,
                    "is_pk":is_pk
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg);
                        if(is_back) {
                            setTimeout(function () {
                                location.href = "/rubber/model/field?model_id=" + model_id+"&f="+f;
                            }, 1000);
                        }
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        //删除字段
        function del() {
            top.layer.confirm('确定删除字段', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/rubber/model/field/del/ajax/save",
                    data:{"field_id":field_id,"model_id":model_id},
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                               location.href = "/rubber/model/field?model_id=" + model_id+"&f="+f;
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        function debug() {
            var field = $('#name').val();
            var txt = $('#debug_args').val().trim();

            if(!field){
                top.layer.msg('请输入字段代号并保存');
                return;
            }

            if(!txt){
                top.layer.msg('请输入调试参数');
                return;
            }

            if(field && txt) {
                $('#args').val(txt);
                $('#field').val(field);
                $('#debug_form').submit();
            }
        }
    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>
<detail>
    <form>
        <h2>${model_name!}/字段编辑（<a class="t2" onclick="history.back(-1)">返回</a>）</h2>
        <hr/>
        <table>
            <tr>
                <td>代号</td>
                <td><input type="text" id="name" value = "${field.name!}"/></td>
            </tr>
            <tr>
                <td>显示名</td>
                <td><input type="text" id="display" class="longtxt" value = "${field.name_display!}"/></td>
            </tr>
            <tr>
                <td>备注</td>
                <td><input type="text" id="note" class="longtxt" value="${field.note!}" /></td>
            </tr>
            <tr>
                <td>是否主键</td>
                <td>
                    <radio>
                    <label><input type="radio" name="is_pk" value="0" <#if field.is_pk==0>checked</#if>/><a>否</a></label>&nbsp;&nbsp;
                    <label><input type="radio" name="is_pk" value="1" <#if field.is_pk==1>checked</#if>/><a>是</a></label>
                    </radio>
                </td>
            </tr>
            <tr>
                <td>调试参数</td>
                <td><input type="text" id="debug_args" value = "${model.debug_args!}" class="longtxt" placeholder="{user_id:1}"/></td>
            </tr>
            <tr>
                <td>动态表达式</td>
                <td><p>
                    <textarea id="code" >${field.expr!}</textarea>
                </p>
                </td>
                <td>（按Esc全屏）</td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <#if is_operator ==1>
                        <button type="button" onclick="saveEdit(false)">保存</button>&nbsp;&nbsp;
                        <button type="button" onclick="saveEdit(true)">保存并返回</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <#if is_admin ==1>
                    <button type="button" class="minor" onclick="del()">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <#if field.field_id??>
                    <button type="button" onclick="debug()" class="minor" >调试字段</button>
                    </#if>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <code>
                        /*
                        this.$c //上下文对象；
                        this._x //字段私有值；
                        this.x() //字段公有值；
                        */
                    </code>
                </td>
            </tr>
        </table>
    </form>

    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="model" value="${model.tag!}/${model.name!}">
        <input type="hidden" name="args" id="args">
        <input type="hidden" id="field" name="field">
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
    editor.setSize('602px','260px');
</script>
</body>
</html>