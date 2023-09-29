<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 查询简报编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/ace/ace.js" ></script>
    <script src="${js}/ace/ext-language_tools.js"></script>
    <style>
        pre{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
    <script>
        $(function () {
            <#if is_admin = 1>
            ctl_s_save_bind(document,saveEdit);
            </#if>
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">数据快报</a></h2> / 编辑
    </left>
    <right class="form">
        <#if is_admin = 1>
            <button type="button" onclick="save()"><u>S</u> 保存</button>
            <#if report.row_id gt 0>
            <button type="button" onclick="del(${report.row_id})" class="minor">删除</button>
            </#if>
        </#if>
    </right>
</toolbar>
<detail>
    <form id="actor_add_edit">
        <input value="${report.row_id}" id="row_id" style="display: none"/>
        <table>
            <tr>
                <th>标签*</th>
                <td>
                    <input type="text" id="tag" name="tag" value="${report.tag!}" id="tag"/>
                </td>
            </tr>
            <tr>
                <th>名称*</th>
                <td><input type="text" id="name" value="${report.name!}" name="name"/></td>
            </tr>
            <tr>
                <th>简报描述</th>
                <td>
                    <input type="text" id="note" value="${report.note!}" class="longtxt" />
                </td>
            </tr>
            <tr>
                <th>查询变量</th>
                <td>
                    <input type="text" id="args" value="${report.args!}" class="longtxt" placeholder="name:value,name:value...多个用逗号隔开"/>
                </td>
            </tr>
            <tr>
                <th>查询代码</th>
                <td><pre id="code" style="width: 100%; height: 200px;">${report.code!}</pre></td>
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
            showPrintMargin:false,
            showLineNumbers:false,
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });

        editor.setHighlightActiveLine(false);
        editor.setOption("wrap", "free")
        editor.setShowPrintMargin(false);
        editor.moveCursorTo(0, 0);

        return editor;
    }

    window.editor = build_editor(document.getElementById("code"),"sql");

    function save() {
        var row_id = $('#row_id').val();
        var tag = $('#tag').val();
        var name = $('#name').val();
        var code = window.editor.getValue();
        var note = $('#note').val();
        var args = $('#args').val();
        if (!tag){
            layer.msg("分类标签不能为空");
            return;
        }
        if (!name){
            layer.msg("查询名称不能为空");
            return;
        }
        if (!code){
            layer.msg("脚本不能为空");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/tool/report/edit/ajax/save",
            data: {
                "row_id": row_id,
                "tag": tag,
                "name": name,
                "code": code,
                "note":note,
                "args":args
            },
            success: function (data) {
                if(data.code==1){
                    top.layer.msg('操作成功');
                    parent.location.href="/tool/report?tag_name="+tag;
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    };
    
    function del(row_id) {

        if(confirm('确定删除吗？') == false){
            return;
        }

        var tag = $('#tag').val();

        $.ajax({
            type: "POST",
            url: "/tool/report/edit/ajax/del",
            data: {
                "row_id": row_id
            },
            success: function (data) {
                if(data.code==1){
                    top.layer.msg('操作成功');
                    parent.location.href="/tool/report?tag_name="+tag;
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    }

</script>
</body>
</html>