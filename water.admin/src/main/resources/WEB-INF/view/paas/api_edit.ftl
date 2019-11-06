<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑接口</title>
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
    <script src="${js}/codemirror/jstl.js?v=1"></script>
    <script src="${js}/codemirror/anyword-hint.js"></script>
    <script src="${js}/codemirror/show-hint.js"></script>
    <script src="${js}/codemirror/jstl-hint.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="${js}/codemirror/matchbrackets.js"></script>

    <script>
        var api_id = ${api.api_id}
        function saveEdit() {
            var code = editor.getValue();
            var tag = $('#tag').val();
            var api_name = $('#api_name').val();
            var args = $('#args').val();
            var note = $('#note').val();
            var cache_time = $('#cache_time').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;

            var is_get = $('#is_get').prop('checked')?1:0;
            var is_post = $('#is_post').prop('checked')?1:0;

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (api_name == null || api_name == "" || api_name == undefined) {
                top.layer.msg("接口名称不能为空！");
                return;
            }

            if (is_get == 0 && is_post == 0) {
                top.layer.msg("GET和POST方法至少选中一种");
                return;
            }
            if(api_id==null){
                api_id=0
            }
            $.ajax({
                type:"POST",
                url:"/paas/api/edit/ajax/save",
                data:{"api_id":api_id,"code":code,"tag":tag,"api_name":api_name,"note":note,"cache_time":cache_time,"is_enabled":is_enabled,"args":args,"is_get":is_get,"is_post":is_post},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg);
                        if(api_id == 0){
                            setTimeout(function(){
                                parent.location.href="/paas/api?tag_name="+tag;
                            },1000);
                        }
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
                parent.location.href="/paas/api?tag_name="+tag_name;
                top.layer.close(top.layer.index);
            });
        };

        function recache() {
            var url = "${url_start!}/run/clear/?api=${api.tag!}::${api.api_name!}";

            $.get(url,function (data) {
                top.layer.msg(data);
            });
        }

        $("input:radio").click(function(){
            var domName = $(this).attr('name');//获取当前单选框控件name 属性值
            var checkedState = $(this).attr('checked');//记录当前选中状态
            $("input:radio[name='" + domName + "']").attr('checked',false);//1.
            $(this).attr('checked',true);//2.
            if(checkedState == 'checked'){
                $(this).attr('checked',false); //3.
            }
        });

        function debug() {
            var tag = $('#tag').val();
            var api_name = $('#api_name').val();

            if(!tag){
                top.layer.msg('请输入标签');
                return;
            }

            if(!api_name){
                top.layer.msg('请输入名称');
                return;
            }

            window.open("${url_start!}"+'/'+tag+'/'+api_name);
        }

        function preview() {
            var tag = $('#tag').val();
            var name = $('#api_name').val();

            if(!tag){
                top.layer.msg('请输入标签');
                return;
            }

            if(!name){
                top.layer.msg('请输入名称');
                return;
            }

            window.open("${url_start!}/preview.js?api="+tag+'/'+name);
        }
    </script>

    <style type="text/css">
       .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>

<flex>
    <left class="col-6"><h2>接口编辑（<a onclick="history.back(-1)" class="t2">返回</a>）</h2></left>
    <right class="col-6">
        <@versions table="paas_api" keyName="api_id" keyValue="${api.api_id}">
            $('#form1').setForm(m);
            editor.getDoc().setValue(m.code);
        </@versions>
    </right>
</flex>

<hr/>
        <detail>
            <form id="form1">

            <table>
                <tr>
                <tr>
                    <td>标签*</td>
                    <td>
                        <input type="text" id="tag" value="${api.tag!}"/>
                    </td>
                </tr>
                <tr>
                    <td>名称*</td>
                    <td><input type="text" id="api_name" value="${api.api_name!}"/>
                        <checkbox>
                            <label><input type="checkbox" id="is_get"  ${(api.is_get == 1)?string("checked","")}/><a>GET</a></label>&nbsp;&nbsp;&nbsp;
                            <label><input type="checkbox" id="is_post" ${(api.is_post == 1)?string("checked","")}/><a>POST</a></label>&nbsp;&nbsp;&nbsp;
                        </checkbox>
                        （缓存<input type="text" id="cache_time" value="${api.cache_time}" class="w30 center"/>s）
                        <a target="_blank" href="javascript:;" onclick="recache()">刷新缓存</a>
                    </td>
                </tr>
                <tr>
                    <td>参数提示</td>
                    <td><input type="text" id="args" value="${api.args!}"/>（参数之间逗号分隔）</td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input type="text" id="note" value="${api.note!}"/></td>
                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(api.is_enabled == 1)?string("checked","")}><a></a></label>
                        </switcher>
                    </td>
                </tr>
                <tr>
                    <td>代码</td>
                    <td><p>
                         <textarea id="code" >${api.code!}</textarea>
                    </p>
                    </td>
                    <td>（按Esc全屏）</td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;
                        <button type="button" onclick="back('${tag_name!}')">返回</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" onclick="debug()" class="minor">调试</button>&nbsp;&nbsp;
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