<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑计划任务</title>
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
        $(function(){
            document.getElementById('source').value="${plan.source!}";
        });

        var plan_id = ${plan.plan_id}
        function saveEdit() {
            var code = editor.getValue();
            var tag = $('#tag').val();
            var plan_name = $('#plan_name').val();
            var source = $('#source').val();
            var begin_time = $('#begin_time').val();
            var repeat_interval = $('#repeat_interval').val();
            var last_exec_time = $('#last_exec_time').val();
            var repeat_max = $('#repeat_max').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (plan_name == null || plan_name == "" || plan_name == undefined) {
                top.layer.msg("配置项关键字不能为空！");
                return;
            }

                if(plan_id==null){
                    plan_id=0
                }
            $.ajax({
                type:"POST",
                url:"/paas/plan/edit/ajax/save",
                data:{"plan_id":plan_id,"code":code,"tag":tag,"plan_name":plan_name,"source":source,
                    "repeat_max":repeat_max,
                    "begin_time":begin_time,"repeat_interval":repeat_interval,
                    "last_exec_time":last_exec_time,"is_enabled":is_enabled},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        if(plan_id==0) {
                            setTimeout(function () {
                                parent.location.href = "/paas/plan?tag=" + tag;
                            }, 1000);
                        }
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function back(tag) {
            var tag = $('#tag').val();
            top.layer.confirm('确定返回？代码已保存?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                parent.location.href="/paas/plan?tag=" + tag;
                top.layer.close(top.layer.index);
            });
        }

        function preview() {
            var tag = $('#tag').val();
            var name = $('#plan_name').val();

            if(!tag){
                top.layer.msg('请输入标签');
                return;
            }

            if(!name){
                top.layer.msg('请输入名称');
                return;
            }

            window.open("${url_start!}/preview.js?pln="+tag+'/'+name);
        }
    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
</head>
<body>

<flex>
    <left class="col-6"><h2>任务编辑（<a onclick="history.back(-1)" class="t2">返回</a>）</h2></left>
    <right class="col-6">
        <@versions table="paas_plan" keyName="plan_id" keyValue="${plan.plan_id}">
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
                    <td>tag*</td>
                    <td>
                        <input type="text" id="tag" value="${plan.tag!}"/>
                    </td>
                </tr>
                <tr>
                    <td>名称*</td>
                    <td><input type="text" id="plan_name" value="${plan.plan_name!}"/></td>
                </tr>
                <tr>
                    <td>数据源</td>
                    <td>
                        <select id="source">
                            <option value=""></option>
                           <#list option_sources as sss>
                            <option value="${sss}">${sss}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>开始时间</td>
                    <td><input type="datetime-local" id="begin_time" value="${begin_time!}"/>
                        最后执行 <input  type="datetime-local" id="last_exec_time" value="${last_exec_time!}" style="width: 230px;"/>
                    </td>
                </tr>
                <tr>
                    <td>重复次数</td>
                    <td><input type="text" id="repeat_max" value="${plan.repeat_max!}" title="（0表示不限数次）"/> 间隔时间
                        <input style="width: 50px;" type="text" id="repeat_interval" value="${plan.repeat_interval!}"/>（m分钟；h小时；d天；M月）</td>
                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(plan.is_enabled == 1)?string("checked","")}><a></a></label>
                        </switcher>
                    </td>
                </tr>
                <tr>
                    <td>代码</td>
                    <td><p>
                         <textarea id="code" >${plan.code!}</textarea>
                    </p>
                    </td>
                    <td>（按Esc全屏）</td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;
                        <button type="button" onclick="back('${tagName!}')">返回</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
    editor.setSize('700px','220px');
</script>

</body>
</html>