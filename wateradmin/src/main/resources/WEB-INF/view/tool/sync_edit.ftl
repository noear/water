<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据同步-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <style>
        pre{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
    <script>
        function saveEdit() {
            var syn_id = ${syn.sync_id};
            var type = $('#type').val();
            var name = $('#name').val();
            var tag = $('#tag').val();
            var interval = $('#interval').val();
            var target = $('#target').val();
            var target_pk = $('#target_pk').val();
            var source_model = window.editor.getValue();
            var alarm_mobile = $('#alarm_mobile').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;

            if (name == null || name == "" || name == undefined) {
                top.layer.msg("名称不能为空");
                return;
            }
            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("名称不能为空");
                return;
            }

            if(isNaN(interval)||interval<=0){
                top.layer.msg("间隔时间必须为大于0的数字");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/tool/sync/edit/ajax/save",
                data:{"syn_id":syn_id,"type":type,"name":name,"tag":tag,"interval":interval,"target":target,"target_pk":target_pk,"source_model":source_model,"alarm_mobile":alarm_mobile,"is_enabled":is_enabled},
                success:function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            parent.location.href = "/tool/sync?tag_name="+tag;
                        },800);
                    } else {
                        top.layer.msg = data.msg;
                    }
                }
            });
        };

        function del(syn_id){
            if(confirm('确定删除吗？') == false){
                return;
            }

            var tag = $('#tag').val();

            $.ajax({
                type:"POST",
                url:"/tool/sync/edit/ajax/del",
                data:{"syn_id":syn_id},
                success:function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            parent.location.href = "/tool/sync?tag_name="+tag;
                        },800);
                    } else {
                        top.layer.msg = data.msg;
                    }
                }
            });
        }

        $(function () {
            <#if is_admin = 1>
            ctl_s_save_bind(document,saveEdit);
            </#if>
        });
    </script>
</head>
<body>
    <datalist id="db_list">
        <#list cfgs as cfg>
            <option value="${cfg.tag}/${cfg.key}::">${cfg.tag}/${cfg.key}::</option>
        </#list>
    </datalist>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">数据同步</a></h2> / 编辑
        </left>
        <right class="form">
            <#if is_admin = 1>
                <button type="button" onclick="saveEdit()"><u>S</u> 保存</button>
                <#if syn.sync_id gt 0>
                    <button type="button" onclick="del(${syn.sync_id})" class="minor">删除</button>
                </#if>
            </#if>
        </right>
    </toolbar>

    <detail>

        <form>
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value="${syn.tag!}"/></td>
            </tr>
            <tr>
                <th>名称*</th>
                <td><input type="text" id="name" value="${syn.name!}"/></td>
            </tr>
            <tr>
                <th>同步类型</th>
                <td>
                    <select id="type" >
                        <#if syn.type = 0>
                            <option value="0" selected>增量同步</option>
                            <option value="1">更新同步</option>
                        </#if>
                        <#if syn.type = 1>
                            <option value="0">增量同步</option>
                            <option value="1" selected>更新同步</option>
                        </#if>
                    </select>
                </td>
            </tr>
            <tr>
                <th>间隔时间(s)</th>
                <td><input type="text" id="interval" value="${syn.interval}"/></td>
            </tr>
            <tr>
                <th>目标数据表</th>
                <td><input type="text" id="target" list="db_list" autocomplete="off" placeholder="tag/key::table" value="${syn.target!}"/></td>
            </tr>
            <tr>
                <th>目标数据主键</th>
                <td><input type="text" id="target_pk" value="${syn.target_pk!}"/></td>
            </tr>
            <tr>
                <th>来源数据模型</th>
                <td><pre id="source_model" style="width: 100%; height: 100px;">${syn.source_model!}</pre>
                    <n-l class="sml">模型输出,需包含'目标数据主键'；@key,表示上次记录的主键值</n-l>
                </td>
            </tr>
            <tr>
                <th>报警手机<br/></th>
                <td><input type="text" id="alarm_mobile" value="${syn.alarm_mobile!}" class="longtxt" /></td>
            </tr>
            <tr>
                <th>是否启用</th>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(syn.is_enabled = 1)?string("checked","")}><a></a></label>
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

    window.editor = build_editor(document.getElementById("source_model"),"sql");
</script>
</body>
</html>