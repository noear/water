<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑同步任务</title>
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
    <script src="${js}/codemirror/etl.js?t=1"></script>
    <script src="${js}/codemirror/anyword-hint.js"></script>
    <script src="${js}/codemirror/show-hint.js"></script>
    <script src="${js}/codemirror/jstl-hint.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="${js}/codemirror/matchbrackets.js"></script>

    <script>

        var etl_id = ${etl.etl_id}
        function saveEdit() {
            var code = editor.getValue();
            var tag = $('#tag').val();
            var alarm_mobile = $('#alarm_mobile').val();
            var cursor = $('#cursor').val();
            var cursor_type = $('#cursor_type').val();
            var etl_name = $('#etl_name').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;
            var e_enabled = $('#e_enabled').prop('checked')?1:0;
            var t_enabled = $('#t_enabled').prop('checked')?1:0;
            var l_enabled = $('#l_enabled').prop('checked')?1:0;
            var is_update = $('input[name="is_update"]:checked').val();

            var e_max_instance = $('#e_max_instance').val();
            var t_max_instance = $('#t_max_instance').val();
            var l_max_instance = $('#l_max_instance').val();

            if (!tag) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if (!etl_name) {
                top.layer.msg("配置项关键字不能为空！");
                return;
            }

            if(etl_id==null){
                etl_id=0
            }

            if (!e_max_instance || e_max_instance==0) {
                top.layer.msg("抽取器集群数输入正确的数字！");
                return;
            }
            if (!t_max_instance || t_max_instance==0) {
                top.layer.msg("转换器集群数输入正确的数字！");
                return;
            }
            if (!l_max_instance || l_max_instance==0) {
                top.layer.msg("加载器集群数输入正确的数字！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/paas/etl/edit/ajax/save",
                data:{
                    "etl_id":etl_id,
                    "code":code,
                    "tag":tag,
                    "etl_name":etl_name,
                    "is_enabled":is_enabled,
                    "e_enabled":e_enabled,
                    "t_enabled":t_enabled,
                    "l_enabled":l_enabled,
                    "is_update":is_update,
                    "alarm_mobile":alarm_mobile,
                    "cursor":cursor,
                    "cursor_type":cursor_type,

                    "e_max_instance":e_max_instance,
                    "t_max_instance":t_max_instance,
                    "l_max_instance":l_max_instance
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        if(etl_id==0) {
                            setTimeout(function () {
                                parent.location.href = "/paas/etl?tag_name=" + tag;
                            }, 1000);
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
                parent.location.href="/paas/etl?tag_name=" + tag_name;
                top.layer.close(top.layer.index);
            });
        }
    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
        checkbox input[type='text']{padding: 0px;height: 18px; vertical-align: middle;margin-right: 10px;}
        checkbox input[type='text']:last-child{margin-right: 0px;}

        .max_instance{width: 20px !important;text-align: center;padding: 0px !important;}
    </style>
</head>
<body>

<flex>
    <left class="col-6"><h2>传输编辑（<a onclick="history.back(-1)" class="t2">返回</a>）</h2></left>
    <right class="col-6">
        <@versions table="paas_etl" keyName="etl_id" keyValue="${etl.etl_id}">
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
                    <input type="text" id="tag" value="${etl.tag!}"/>
                </td>
            </tr>
            <tr>
                <td>名称*</td>
                <td><input type="text" id="etl_name" value="${etl.etl_name!}"/>
                    （ <checkbox>
                    <label><input id="e_enabled" type="checkbox" value="1" ${(etl.e_enabled == 1)?string("checked","")} /><a>启用抽取器</a></label><input id="e_max_instance" value="${etl.e_max_instance}" type="text" class="max_instance" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                    <label><input id="t_enabled" type="checkbox" value="1" ${(etl.t_enabled == 1)?string("checked","")} /><a>启用转换器</a></label><input id="t_max_instance" value="${etl.t_max_instance}" type="text" class="max_instance" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                    <label><input id="l_enabled" type="checkbox" value="1" ${(etl.l_enabled == 1)?string("checked","")} /><a>启用加载器</a></label><input id="l_max_instance" value="${etl.l_max_instance}" type="text" class="max_instance" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                    </checkbox>）

                </td>
            </tr>
            <tr>
                <td>游标值</td>
                <td>
                    <select style="width: 60px;" id="cursor_type">
                        <option value="0">时间</option>
                        <option value="1">数值</option>
                    </select>
                    <input style="width: 235px;" type="text" id="cursor" value="${etl.cursor_str()}"/>
                    （
                    <radio>
                    <label><input type="radio" name="is_update" value="0" /><a>清空</a></label>
                    <label><input type="radio" name="is_update" value="1" /><a>更新</a></label>
                    </radio>）

                    <script>
                        $('#cursor_type').val(${etl.cursor_type});
                    </script>
                </td>
            </tr>
            <tr style="display: none">
                <td>报警手机</td>
                <td>
                    <input type="text" id="alarm_mobile" value="${etl.alarm_mobile!}"/>
                </td>
            </tr>
            <tr>
                <td>是否启用</td>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(etl.is_enabled == 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
            <tr>
                <td>代码</td>
                <td><p>
                    <textarea id="code" >${etl.code!}</textarea>
                </p>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <td></td>
                <td><button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;
                    <button type="button" onclick="back('${tagName!}')">返回</button></td>
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
    editor.setSize('700px','300px');
</script>

</body>
</html>