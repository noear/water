<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视-查看</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        pre{border:1px solid #C9C9C9;}
    </style>

    <script>
        window.vm = {};

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
            <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">数据监视</a></h2> / 查看
        </left>
    </toolbar>

    <detail>
        <form>
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value = "${monitor.tag!}"/></td>
            </tr>
            <tr>
                <th>项目名称*</th>
                <td><input type="text" id="name" value = "${monitor.name!}"/></td>
            </tr>
            <tr>
                <th>监视类型</th>
                <td>
                    <select id="type" >
                        <#if monitor.type=0>
                            <option value="0" selected="selected">数据预警</option>
                            <option value="1">数据简报</option>
                        </#if>
                        <#if monitor.type=1>
                            <option value="0">数据预警</option>
                            <option value="1" selected="selected">数据简报</option>
                        </#if>
                    </select>
                </td>
            </tr>
            <tr>
                <th>数据采集</th>
                <td>
                    <pre style="height:100px;width:600px;" jt-sql id="source_query">${monitor.source_query!}</pre>
                </td>
            </tr>
            <tr>
                <th>触发规则</th>
                <td>
                    <pre style="height:50px;width:600px;" jt-js id="rule">${monitor.rule!}</pre>
                </td>
            </tr>
            <tr>
                <th>重复标识</th>
                <td><input type="text" id="task_tag_exp" class="longtxt" value="${monitor.task_tag_exp!}" /><n>（重复的标识不报警）</n></td>
            </tr>

            <tr>
                <th>报警手机</th>
                <td><input type="text" id="alarm_mobile" class="longtxt" value = "${monitor.alarm_mobile!}"/><n>（多个手机号用逗号分隔）</n></td>
            </tr>
            <tr>
                <th>报警签名</th>
                <td><input type="text" id="alarm_sign"  value = "${monitor.alarm_sign!}"/></td>
            </tr>
            <tr>
                <th>报警说明</th>
                <td><textarea class="h50" id="alarm_exp">${monitor.alarm_exp!}</textarea></td>
            </tr>
            <tr>
                <th>是否启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(monitor.is_enabled=1)?string("checked","")}><a></a></label>
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
                    <#list cfgs as cfg>
                    {name: "--${cfg.tag}/${cfg.key}",value: "--${cfg.tag}/${cfg.key}::", meta: "db",type: "local",score: 1000}<#if cfg_has_next>,</#if>
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