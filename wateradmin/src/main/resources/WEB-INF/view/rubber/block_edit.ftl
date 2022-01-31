<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑数据块</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <script src="${js}/jteditor.js"></script>
    <style type="text/css">
        pre{border:1px solid #C9C9C9;}
    </style>
    <script>
        var block_id = '${block.block_id}';

        function  debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
            }
        };

        function save(is_back) {
            var block_id = '${block.block_id}';
            var tag = $("#tag").val();
            var name = $("#name").val();
            var name_display = $("#name_display").val();
            var note = $("#note").val();
            var related_db = $("#related_db").val();
            var related_tb = $("#related_tb").val();
            var struct = $("#struct").val();
            var is_editable =  $("#is_editable").is(":checked");

            if(is_editable==true){
                is_editable = 1;
            } else {
                is_editable = 0;
            }
            var app_expr = window.editor.getValue();

            if(!tag){
                top.layer.msg('分类标签不能为空！');
            }
            if(!name){
                top.layer.msg('代号不能为空！');
            }
            if(!name_display){
                top.layer.msg('显示名不能为空！');
            }
            if(!tag){
                top.layer.msg('分类标签不能为空！');
            }

            if(related_db){
                if(!related_tb){
                    top.layer.msg('相关表不能为空！');
                }
            }

            if(!struct){
                top.layer.msg('数据结构不能为空！');
            }

            $.ajax({
                type:"POST",
                url:"/rubber/block/edit/ajax/save",
                data:{
                    "block_id":block_id,
                    "tag":tag,
                    "name":name,
                    "name_display":name_display,
                    "note":note,
                    "related_db":related_db,
                    "related_tb":related_tb,
                    "struct":struct,
                    "is_editable":is_editable,
                    "app_expr":app_expr
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');

                        if(is_back) {
                            setTimeout(function () {
                                parent.location.href = "/rubber/block?tag_name=" + tag;
                            }, 800);
                        }
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        //删除模型
        function del() {
            var tag = $('#tag').val();

            top.layer.confirm('确定要删除吗？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type: "POST",
                    url: "/rubber/block/edit/ajax/del",
                    data: {
                        "block_id": block_id
                    },
                    success: function (data) {
                        if(data.code==1) {
                            top.layer.msg('操作成功');

                            setTimeout(function () {
                                parent.location.href = "/rubber/block?tag_name=" + tag;
                            }, 800);

                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });
        };

        $(function(){

            $('pre[jt-js]').each(function(){
                window.editor = build_editor(this,'javascript');
            });
        });
    </script>
</head>
<body>

<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">指标仓库</a></h2> / 编辑
    </left>
    <right>
        <#if (is_admin == 1) && (block.block_id > 0) >
            <button type="button" onclick="del()" class="minor mar10-l">删除</button>
        </#if>
    </right>
</toolbar>

<main>
    <detail>

        <form>
            <table>
                <tr>
                    <input type="hidden" value="${block.block_id}" id="block_id">
                </tr>
                <tr>
                    <th>分类标签</th>
                    <td>
                        <input type="text" id="tag" value="${block.tag!}"/>
                    </td>
                </tr>
                <tr>
                    <th>代号</th>
                    <td><input type="text" id="name" value="${block.name!}"/>
                    </td>
                </tr>
                <tr>
                    <th>显示名</th>
                    <td>
                        <input type="text" id="name_display" value="${block.name_display!}"/>
                        <checkbox>
                        <label><input type="checkbox" id="is_editable" ${(block.is_editable == 1)?string("checked","")}><a>可以编辑</a></label>
                        </checkbox>
                    </td>
                </tr>
                <tr>
                    <th>备注</th>
                    <td><input type="text" id="note" value="${block.note!}"/>
                    </td>
                </tr>
                <tr>
                    <th>相关数据库</th>
                    <td>
                        <select id="related_db">
                            <option value=""></option>
                            <#list option_sources as sss>
                                <option value="${sss}">${sss}</option>
                            </#list>
                        </select>
                        <script>
                            $('#related_db').val('${block.related_db!}');
                        </script>
                        <span class="w55 center">相关表</span>
                        <input type="text" id="related_tb" style="width: 237px;" value="${block.related_tb!}"/>
                    </td>
                </tr>
                <tr>
                    <th>数据结构</th>
                    <td>
                        <input type="text" id="struct" class="longtxt" value="${block.struct!}" placeholder="{col:'name'...}"/>&nbsp;&nbsp;
                    </td>
                    <td>（第1列为唯一约束）</td>
                </tr>
                <tr>
                    <th>扫描表达式</th>
                    <td>
                        <pre style="height:150px;width:600px;" jt-js id="code">${block.app_expr!}</pre>
                    </td>
                    <td>（按Esc全屏）</td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <#if is_operator ==1>
                            <button type="button" onclick="save(false);">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" onclick="save(true);">保存并返回</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        </#if>
                        <button onclick="debug()" type="button"  class="minor">调试</button>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td colspan="2">
                        <code>
                            /*
                            x //参数
                            cmd //指令(默认:(x)->bool；例map:(x)->item/list; 例count:(x)->num)
                            */
                        </code>
                    </td>
                </tr>
            </table>
        </form>
        <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get" class="inline">
            <input type="hidden" name="block" value="${block.tag!}/${block.name!}">
            <input type="hidden" name="args" value="{cmd:null,x:'xxx'}">
        </form>
    </detail>
</main>
</body>
</html>