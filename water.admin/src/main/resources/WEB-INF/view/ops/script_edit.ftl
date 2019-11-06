
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 脚本资源-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${css}/codemirror/shell.css"/>
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.css"/>
    <link rel="stylesheet" href="${css}/codemirror/fullscreen.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="https://static.kdz6.cn/lib/codemirror.min.js"></script>
    <script src="${js}/codemirror/shell.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
    <script src="https://static.kdz6.cn/lib/vue.min.js"></script>
    <script>
        function saveEdit() {
            var script_id = '${script.script_id}';
            var tag = $('#tag').val();
            var name = $('#name').val();
            var args_json = JSON.stringify(viewModel);
            var type = $('#type').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;
            var code = editor.getValue();

            var obj = document.getElementsByName('env');
            var env = '';
            for(k in obj){
                if(obj[k].checked) {
                    env += obj[k].value;
                }
            }

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签不能为空！");
                return;
            }
            if (name == null || name == "" || name == undefined) {
                top.layer.msg("脚本名称不能为空！");
                return;
            }
            if (type == null || type == "" || type == undefined) {
                top.layer.msg("请选择脚本类型！");
                return;
            }

            if(script_id == null || script_id == "" || script_id == undefined){
                script_id = 0;
            }
            $.ajax({
                type:"POST",
                url:"/ops/script/edit/ajax/save",
                data:{
                       "script_id":script_id,
                       "tag":tag,
                       "name":name,
                       "args_json":args_json,
                       "type":type,
                       "is_enabled":is_enabled,
                       "code":code,
                       "env":env
                    },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/ops/script?tag_name="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };


        function back(tag_name) {
            var tag = $('#tag').val();
            top.layer.confirm('确定返回？代码已保存?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                parent.location.href="/ops/script/?tag_name="+tag_name;
                top.layer.close(top.layer.index);
            });
        }

    </script>
    <style type="text/css">
        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
        img[src]{width: 20px;}
    </style>
</head>
<body>
<detail id="app">
    <form>
    <h2>编辑脚本资源（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
    <hr/>
    <table>
        <tr>
        <tr>
            <td width="70px">标签</td>
            <td width="380px">
                <input type="text" id="tag" value="${script.tag!}"/>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>脚本名称</td>
            <td><input type="text" id="name" value="${script.name!}"/></td>
            <td></td>
        </tr>
        <tr>
            <td>脚本类型</td>
            <td><@enum group="ops_script_type" style="select" id="type" value="${script.type}"/></td>
            <td></td>
        </tr>
        <tr>
            <td>适用环境</td>
            <td>
                <boxlist>
                    <@enum group="ops_script_env" style="checkbox" name="env" value="${script.getEnum()}"/>
                </boxlist>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>是否启用</td>
            <td>
                <switcher>
                    <label><input id="is_enabled" type="checkbox" ${(script.is_enabled=1)?string("checked","")}><a></a></label>
                </switcher>
            </td>
            <td></td>
        </tr>
        <tr>
        <td>参数</td>
        <td style="padding-left: 0px">
                <table v-if="list.length > 0">
                    <tbody>
                    <tr v-for="(item, index) in list" :key="index">
                        <td>
                            <input style="width: 140px;" type="text" placeholder="参数名" v-model="item.param_name">
                        </td>
                        <td>
                            <input style="width: 140px;" type="text" placeholder="参数描述" v-model="item.param_note">
                        </td>
                        <td>
                            <img src="${img}/close.png" alt="" @click="removeItem(index)">
                            <img v-if="index + 1 === list.length" src="${img!}/add.png" alt="" @click="addItem">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <img v-else src="${img!}/add.png" alt="" @click="addItem">
        </td>
        </tr>
        <tr>
            <td>代码</td>
            <td colspan="3">
                <p>
                    <textarea id="code" >${script.code!}</textarea>
                </p>
            </td>
            <td>（按Esc全屏）</td>
        </tr>
        <tr>
            <td></td>
            <td><button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;
                <button type="button" onclick="back('${tag_name!}')">返回</button></td>
        </tr>
    </table>
    </form>
</detail>
<script>
    var viewModel = ${argsList};

    var app = new Vue({
        el: '#app',
        data:viewModel,
        methods: {
            addItem() {
                this.list.push({param_name: '', param_note:''});
            },
            removeItem(index) {
                this.list.splice(index, 1);
            }
        }
    });


        var editor = CodeMirror.fromTextArea(document.getElementById('code'), {
            mode: 'shell',
            lineNumbers: true,
            matchBrackets: true,

            //智能提示
            extraKeys: {
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

    //设置宽高,默认为自动根据每行长度来调整。
    //'width','height'
    editor.setSize('650px','200px');
</script>
</body>
</html>