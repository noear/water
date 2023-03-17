<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 访问密钥</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <script>
        function del() {
            let key_id = ${m.key_id!0};
            if (key_id < 1) {
                return;
            }

            let vm = formToMap("#form");
            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            top.layer.confirm('确定删除', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/cfg/key/ajax/del",
                    data: {"key_id": key_id},
                    success: function (data) {
                        if (1 == data.code) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                parent.location.href = "/cfg/key?tag_name=" + vm.tag + "&_state=" + _state;
                            }, 800);
                        } else {
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        }

        function save() {
            let vm = formToMap("#form");

            if (!vm.tag || !vm.access_key || !vm.access_secret_key) {
                top.layer.msg("tag 或 access_key 或 access_secret_key 不能为空！");
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            $.ajax({
                type: "POST",
                url: "/cfg/key/edit/ajax/save",
                data: vm,
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/key?tag_name=" + vm.tag + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        var ext_tools = ace.require("ace/ext/language_tools");
        var ext_lang_tips = [];
        <#list tipsList as tip>
        ext_lang_tips.push({name: "${tip}",value: "${tip}", meta: "",type: "local",score: 1000})
        </#list>

        ext_tools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {
                callback(null,ext_lang_tips);
            }
        });


        function build_editor(mod){
            if(window.editor){
                window.editor.getSession().setMode("ace/mode/"+mod);
                return
            }

            var editor = ace.edit(document.getElementById('metainfo_edit'));

            editor.setTheme("ace/theme/chrome");
            editor.getSession().setMode("ace/mode/"+mod);
            editor.setOptions({
                showFoldWidgets:false,
                showLineNumbers:false,
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });

            editor.setShowPrintMargin(false);
            editor.moveCursorTo(0, 0);

            editor.getSession().on('change', function(e) {
                var value = editor.getValue();
                $('#metainfo').val(value);
            });

            window.editor = editor;
        }

        function editor_shift(mod){
            localStorage.setItem("water_prop_edit_mode", mod);

            build_editor(mod);
        }

        $(function () {
            ctl_s_save_bind(document,save);

            //编辑模式支持
            build_editor("properties");
        })
    </script>
    <style>
        pre{border:1px solid #C9C9C9;}

        boxlist label a{ background:#fff; border-color:#f1f1f1 #f1f1f1 #fff #f1f1f1;}
        boxlist label a:hover{ border-color:#C9C9C9 #C9C9C9 #fff #C9C9C9;}
        boxlist input:checked + a{background:#C9C9C9; border-color:#C9C9C9}

        .disabled{color:#888;background-color:#f9f9fa}
    </style>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">访问密钥</a></h2> / 编辑
        </left>
        <right class="form">
            <n>ctrl + s 可快捷保存</n>
            <button type="button" onclick="save()">保存</button>
            <#if is_admin == 1>
                <button type="button" class="minor" onclick="del()">删除</button>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form id="form">
            <input type="hidden" id="key_id" value="${m.key_id!0}">
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value="${tag_name!}" autofocus/></td>
            </tr>
            <tr>
                <th>label</th>
                <td><input type="text" id="label" value="${m.label!}" /></td>
            </tr>
            <tr>
                <th>access_key</th>
                <td><input class="disabled" type="text" id="access_key" value="${m.access_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_key</th>
                <td><input class="disabled" type="text" id="access_secret_key" value="${m.access_secret_key!}" /></td>
            </tr>
            <tr>
                <th>access_secret_salt</th>
                <td><input class="disabled" type="text" id="access_secret_salt" value="${m.access_secret_salt!}" /></td>
            </tr>
            <tr>
                <th>metainfo</th>
                <td>
                    <div style="line-height: 1em;">
                        <boxlist>
                            <label><input type="radio" name="edit_mode" value="properties" checked/><a>properties</a></label>
                        </boxlist>
                    </div>
                    <div>
                        <textarea id="metainfo" class="hidden">${m.metainfo!}</textarea>
                        <pre style="height:100px;width:600px;"  id="metainfo_edit">${m.metainfo!}</pre>
                    </div>
                </td>
            </tr>
            <tr>
                <th>启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" value="1" type="checkbox" ${(m.is_enabled = 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
        </table>
        </form>
    </detail>
</main>

</body>
</html>