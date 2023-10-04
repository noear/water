<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
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
    </style>

    <script>
        window.vm = {};

        function saveEdit() {
            let monitor_id = ${model.monitor_id}
            let name = $('#name').val();
            let tag = $('#tag').val();
            let source_query = window.vm.source_query;
            let rule = window.vm.rule;
            let task_tag_exp = $('#task_tag_exp').val();
            let alarm_exp = $('#alarm_exp').val();
            let is_enabled = $('#is_enabled').prop('checked') ? 1 : 0;

            if (!name) {
                top.layer.msg("名称不能为空");
                return;
            }

            if (!tag) {
                top.layer.msg("tag不能为空");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/tool/monitor/edit/ajax/save",
                data: {
                    "monitor_id": monitor_id,
                    "name": name,
                    "tag": tag,
                    "source_query": source_query,
                    "rule": rule,
                    "task_tag_exp": task_tag_exp,
                    "alarm_mobile": '',
                    "alarm_exp": alarm_exp,
                    "is_enabled": is_enabled
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/tool/monitor?tag_name=" + tag + "&_state=" + (is_enabled == 1 ? 0 : 1);
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del(monitor_id) {
            if (confirm('确定删除吗？') == false) {
                return;
            }

            let tag = $('#tag').val();
            let is_enabled = $('#is_enabled').prop('checked') ? 1 : 0;

            $.ajax({
                type: "POST",
                url: "/tool/monitor/edit/ajax/del",
                data: {"monitor_id": monitor_id,},
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功')
                        setTimeout(function () {
                            parent.location.href = "/tool/monitor?tag_name=" + tag + "&_state=" + (is_enabled == 1 ? 0 : 1);
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function(){
            $('pre[jt-sql]').each(function(){
                build_editor(this,'sql');
            });
            $('pre[jt-js]').each(function(){
                build_editor(this,'javascript');
            });
            <#if is_admin = 1>
            ctl_s_save_bind(document,saveEdit);
            </#if>
        });
    </script>
</head>
<body>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">数据监视</a></h2> / 编辑
        </left>
        <right class="form">
            <#if is_admin = 1>
                <button type="button" onclick="saveEdit()"><u>S</u> 保存</button>
                <#if model.monitor_id gt 0>
                    <button type="button" onclick="del(${model.monitor_id})" class="minor">删除</button>
                </#if>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form>
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value = "${model.tag!}"/></td>
            </tr>
            <tr>
                <th>数据名称*</th>
                <td><input type="text" id="name" value = "${model.name!}"/></td>
            </tr>
            <tr>
                <th>数据采集</th>
                <td>
                    <pre style="height:150px;width:600px;" jt-sql id="source_query">${model.source_query!}</pre>
                </td>
            </tr>
            <tr>
                <th>触发规则</th>
                <td>
                    <pre style="height:50px;width:600px;" jt-js id="rule">${model.rule!}</pre>
                </td>
            </tr>

            <tr>
                <th>告警标识</th>
                <td><input type="text" id="task_tag_exp" class="longtxt" value="${model.task_tag_exp!}" />
                    <n-l>重复的标识不重复告警（!开头的除外）。{{x}}表示嵌入变量</n-l>
                </td>
            </tr>
            <tr>
                <th>告警描述</th>
                <td>
                    <textarea class="h50" id="alarm_exp">${model.alarm_exp!}</textarea>
                    <n-l>{{x}}表示嵌入变量</n-l>
                </td>
            </tr>

            <tr>
                <th>启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(model.is_enabled=1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>
        </table>
        </form>
    </detail>
<script>
    var ext_tools = ace.require("ace/ext/language_tools");

    ext_tools.addCompleter({
        getCompletions: function(editor, session, pos, prefix, callback) {
            callback(null,
                [
                    {name: "--faas()",value: "--faas()::", meta: "faas",type: "local",score: 1000},
                    {name: "return",value: "return ", meta: "faas",type: "local",score: 1000},
                    {name: "water.faasAsJson",value: "water.faasAsJson('path');", meta: "faas",type: "local",score: 1000},
                    {name: "water.callFile",value: "water.callFile('path');", meta: "faas",type: "local",score: 1000}
                    <#list cfgs as cfg>
                    ,{name: "--${cfg.tag}/${cfg.key}",value: "--${cfg.tag}/${cfg.key}::", meta: "db",type: "local",score: 1000}
                    </#list>
                ]);
        }
    });

    function build_editor(elm,mod){
        ace.require("ace/ext/language_tools");

        var editor = ace.edit(elm);

        editor.setTheme("ace/theme/chrome");
        editor.getSession().setMode("ace/mode/"+mod);
        editor.setOptions({
            showFoldWidgets:false,
            showLineNumbers:false,
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });

        editor.setHighlightActiveLine(false);
        editor.setShowPrintMargin(false);
        editor.moveCursorTo(0, 0);

        editor.getSession().on('change', function(e) {
            var $my = $(elm);
            var name = $my.prop('id');
            var value = editor.getValue();

            window.vm[name] = value;
        });
    }
</script>

</body>
</html>