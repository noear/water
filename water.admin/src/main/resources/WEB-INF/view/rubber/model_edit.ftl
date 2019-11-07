<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据模型-编辑</title>
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
        var model_id = '${model.model_id}';
        var f = '${f}';
            function saveEdit() {
                var init_expr = editor.getValue();
                var tag = $('#tag').val();
                var name = $('#name').val();
                var name_display = $('#name_display').val();
                var debug_args = $('#debug_args').val();
                var related_db = $('#related_db').val();

                if (!tag){
                    top.layer.msg("分类标签不能为空！");
                    return;
                }

                if (!name){
                    top.layer.msg("代号不能为空！");
                    return;
                }

                if (!name_display){
                    top.layer.msg("显示名不能为空！");
                    return;
                }

                if(!model_id){
                    model_id=0;
                }
                $.ajax({
                    type:"POST",
                    url:"/rubber/model/edit/ajax/save",
                    data:{
                        "model_id":model_id,
                        "tag":tag,
                        "name":name,
                        "name_display":name_display,
                        "init_expr":init_expr,
                        "debug_args":debug_args,
                        "related_db":related_db
                    },
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                if('water'==f) {
                                    parent.location.href="/rubber/model?tag="+tag;
                                } else if ('sponge'==f){
                                    parent.location.href='${backUrl!}'+"push/model?tag="+tag;
                                }

                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            };

        //删除模型
        function del() {
            var tag = $('#tag').val();
            top.layer.confirm('确定要删除吗？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type: "POST",
                    url: "/rubber/model/edit/ajax/del",
                    data: {
                        "model_id": model_id
                    },
                    success: function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                if('water'==f) {
                                    parent.location.href="/rubber/model?tag="+tag;
                                } else if ('sponge'==f){
                                    parent.location.href='${backUrl!}'+"push/model?tag="+tag;
                                }
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });
        };

        function impField() {
            var tag = $('#tag').val();
            top.layer.confirm('确定导入？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.getJSON('/rubber/model/ajax/import?model_id=${model.model_id}',function (rst) {
                    if(rst.code){
                        top.layer.msg(rst.msg);
                        setTimeout(parent.location.href="/rubber/model?tag="+tag,1000);
                    }
                    else{
                        top.layer.msg(rst.msg);
                    }
                });
            });
        };

        //另存为
        function saveAs() {
            var init_expr = editor.getValue();
            var tag = $('#tag').val();
            var name = $('#name').val();
            var name_display = $('#name_display').val();
            var debug_args = $('#debug_args').val();
            var related_db = $('#related_db').val();

            if (name == '${model.name!}') {
                top.layer.msg('请重命名代号！');
                return;
            }

            top.layer.confirm('确定另存为操作', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/rubber/model/edit/ajax/saveAs",
                    data:{
                        "model_id":model_id,
                        "tag":tag,
                        "name":name,
                        "name_display":name_display,
                        "init_expr":init_expr,
                        "debug_args":debug_args,
                        "related_db":related_db
                    },
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                if('water'==f) {
                                    parent.location.href="/rubber/model?tag="+tag;
                                } else if ('sponge'==f){
                                    parent.location.href='${backUrl!}'+"push/model?tag="+tag;
                                }
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });


        };

        function  debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
            }
        }
    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>
<toolbar>
    <cell><h2>编辑数据模型（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2></cell>
    <cell>
                <#if (is_admin == 1) && (model.model_id > 0) >
                    <button type="button" onclick="del()" class="minor">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <button onclick="impField()" type="button"  class="minor">导入</button>
                </#if>
    </cell>
</toolbar>

<detail>
    <form>

        <table>
            <tr>
                <td>分类标签</td>
                <td><input type="text" id="tag" value = "${model.tag!}"/></td>
            </tr>
            <tr>
                <td>代号</td>
                <td><input type="text" id="name" value = "${model.name!}"/></td>
            </tr>
            <tr>
                <td>显示名</td>
                <td><input type="text" id="name_display" value = "${model.name_display!}"/></td>
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
                        $('#related_db').val('${model.related_db!}');
                    </script><note>（查询时使用）</note>
                </td>
            </tr>
            <tr>
                <td>调试参数</td>
                <td><input type="text" id="debug_args" value = "${model.debug_args!}" class="longtxt" placeholder="{user_id:1}"/></td>
            </tr>
            <tr>
                <td>构造表达式</td>
                <td><p>
                    <textarea id="code" >${model.init_expr!}</textarea>
                </p>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <#if is_operator ==1>
                        <button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" onclick="saveAs()" class="minor">另存为</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <button onclick="debug()" type="button"  class="minor">调试</button>
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
    editor.setSize('601px','180px');
</script>
</body>
</html>