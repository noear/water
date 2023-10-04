<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="${css}/font-awesome-4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        pre{border:1px solid #C9C9C9;}

        a.clone{display: inline-block; color: #666; width: 26px; height: 26px; line-height: 26px; text-align: center; }
        a.clone:hover{color: #000;}

        boxlist label a{ background:#fff; border-color:#f1f1f1 #f1f1f1 #fff #f1f1f1;}
        boxlist label a:hover{ border-color:#C9C9C9 #C9C9C9 #fff #C9C9C9;}
        boxlist input:checked + a{background:#C9C9C9; border-color:#C9C9C9}
    </style>

    <script>
        var row_id = '${cfg.row_id!}';

        function save() {
            var vm = formToMap('form');
            vm.row_id = row_id;

            if (!vm.tag) {
                top.layer.msg("tag不能为空！");
                return;
            }

            if (!vm.key) {
                top.layer.msg("key不能为空！");
                return;
            }

            if(!vm.label){
                vm.label='';
            }

            let _state = $('#is_disabled').prop('checked') ? 1 : 0;

            $.ajax({
                type: "POST",
                url: "/cfg/prop/edit/ajax/save",
                data: vm,
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/prop?tag_name=" + vm.tag + "&label=" + vm.label + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {
            if (!row_id) {
                return;
            }

            if (!confirm("确定要删除吗？")) {
                return;
            }

            let _state = $('#is_disabled').prop('checked') ? 1 : 0;

            $.ajax({
                type: "POST",
                url: "/cfg/prop/edit/ajax/del",
                data: {"row_id": row_id},
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/cfg/prop?tag_name=${cfg.tag!}" + "&_state=" + _state;
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }


        function loadTypeTml(){
            let jt = $('#type').find("option:selected").text();
            let tml = window.tmls[jt];
            if(tml){
                window.editor.setValue(tml);
                window.editor.moveCursorTo(0, 0);
            }
        }

        var ext_tools = ace.require("ace/ext/language_tools");

        ext_tools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {
                callback(null,
                    [
                        {name: "schema",value: "schema", meta: "",type: "local",score: 1000},
                        {name: "url",value: "url", meta: "",type: "local",score: 1000},
                        {name: "username",value: "username", meta: "",type: "local",score: 1000},
                        {name: "password",value: "password", meta: "",type: "local",score: 1000},
                        {name: "server",value: "server", meta: "",type: "local",score: 1000},
                        {name: "user",value: "user", meta: "",type: "local",score: 1000},
                        {name: "name",value: "name", meta: "",type: "local",score: 1000},
                        {name: "accessKeyId",value: "accessKeyId", meta: "",type: "local",score: 1000},
                        {name: "accessSecret",value: "accessSecret", meta: "",type: "local",score: 1000},
                        {name: "regionId",value: "regionId", meta: "",type: "local",score: 1000},
                        {name: "endpoint",value: "endpoint", meta: "",type: "local",score: 1000},
                        {name: "bucket",value: "bucket", meta: "",type: "local",score: 1000}
                    ]);
            }
        });


        function build_editor(mod){
            if(window.editor){
                window.editor.getSession().setMode("ace/mode/"+mod);
                return
            }

            var editor = ace.edit(document.getElementById('value_edit'));

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
                $('#value').val(value);
            });

            window.editor = editor;
        }

        function editor_shift(mod){
            localStorage.setItem("water_prop_edit_mode", mod);

            build_editor(mod);
        }

        $(function(){
            //编辑模式支持
            var _edit_mode="${cfg.edit_mode!}";
            if(!_edit_mode){
                _edit_mode =localStorage.getItem("water_prop_edit_mode");
            }

            if(!_edit_mode){
                _edit_mode = 'text';
            }

            $("input[name='edit_mode'][value='"+_edit_mode+"']").prop("checked",true);
            build_editor(_edit_mode);


            $('a.clone').click(function () {
                loadTypeTml();
            });

            $('#type').change(function () {
                if(!window.editor.getValue()) {
                    loadTypeTml();
                }
            });

            ctl_s_save_bind(document,save);
        });

        window.tmls = ${config_tml!'{}'};
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">属性配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" class="w80" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>


<detail>
    <form>

        <table>
            <tr>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" autofocus value="${tag_name!}"/></td>
            </tr>
            <tr>
                <th>key*</th>
                <td><input type="text" id="key" value="${cfg.key!}"/></td>
            </tr>
            <tr>
                <th>type</th>
                <td>
                    <@enum group="config_type" style="select" id="type" value="${cfg.type!}" ></@enum>
                    <a class="clone"><i class="fa fa-clone"></i></a>
                </td>
            </tr>
            <tr>
                <th>管理标记</th>
                <td><input type="text" id="label" value="${cfg.label!}"/>
                    <n>（属性多时，用于归类显示）</n>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <checkbox>
                        <label class="mar10-r"><input type="checkbox" id="is_disabled" ${cfg.disabled()?string("checked","")} /><a>禁止使用</a></label>
                    </checkbox>
                </td>
            </tr>
            <tr>
                <th>配置内容</th>
                <td>
                    <div style="line-height: 1em;">
                        <boxlist>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('text')" value="text" /><a>text</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('properties')" value="properties" /><a>properties</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('yaml')" value="yaml" /><a>yaml</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('json')" value="json" /><a>json</a></label>
                        </boxlist>
                    </div>
                    <div>
                        <textarea id="value" class="hidden">${cfg.value!}</textarea>
                        <pre style="height:300px;width:calc(100vw - 260px);"  id="value_edit">${cfg.value!}</pre>
                    </div>
                </td>
            </tr>

        </table>
    </form>

    <div class="right" style="position: absolute; z-index: 1; bottom: 10px; right: 10px;">
        <@versions table="water_cfg_properties" keyName="row_id" keyValue="${cfg.row_id}">
            window.editor.setValue(m.value);
        </@versions>
    </div>
</detail>

</body>
</html>