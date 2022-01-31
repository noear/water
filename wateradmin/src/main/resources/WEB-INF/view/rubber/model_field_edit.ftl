<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据模型字段-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <script src="${js}/jteditor.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        pre{border:1px solid #C9C9C9;}
    </style>

    <script>
        var field_id = '${field.field_id}';
        var model_id = '${model_id}';
        var f = '${f}';
        function saveEdit(is_back) {
            var expr = window.editor.getValue();
            var note = $('#note').val();
            var name = $('#name').val();
            var name_display = $('#display').val();
            var is_pk = $("input[name='is_pk']:checked").val();

            if (!name){
                top.layer.msg("代号不能为空！");
                return;
            }

            if (!name_display){
                top.layer.msg("显示名不能为空！");
                return;
            }

            if(!field_id){
                field_id=0;
            }
            $.ajax({
                type:"POST",
                url:"/rubber/model/field/edit/ajax/save",
                data:{
                    "model_id":model_id,
                    "field_id":field_id,
                    "expr":expr,
                    "name":name,
                    "name_display":name_display,
                    "note":note,
                    "is_pk":is_pk
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg);
                        if(is_back) {
                            setTimeout(function () {
                                location.href = "/rubber/model/field?model_id=" + model_id+"&f="+f;
                            }, 1000);
                        }
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        //删除字段
        function del() {
            top.layer.confirm('确定删除字段', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/rubber/model/field/del/ajax/save",
                    data:{"field_id":field_id,"model_id":model_id},
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                               location.href = "/rubber/model/field?model_id=" + model_id+"&f="+f;
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        function debug() {
            var field = $('#name').val();
            var txt = $('#debug_args').val().trim();

            if(!field){
                top.layer.msg('请输入字段代号并保存');
                return;
            }

            if(!txt){
                top.layer.msg('请输入调试参数');
                return;
            }

            if(field && txt) {
                $('#args').val(txt);
                $('#field').val(field);
                $('#debug_form').submit();
            }
        }

        $(function(){

            $('pre[jt-js]').each(function(){
                window.editor = build_editor(this,'javascript');
            });
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left class="ln30">
        <h2><a href="/rubber/model/inner?tag_name=${model.tag!}&f=water" class="noline">数据模型</a> / <a onclick="history.back(-1)" href="#" class="noline">字段列表</a></h2> / 字段编辑 :: ${model.name_display!}
    </left>
    <right>
        <#if is_admin ==1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>
<detail>
    <form>
        <table>
            <tr>
                <td>代号</td>
                <td><input type="text" id="name" value = "${field.name!}"/></td>
            </tr>
            <tr>
                <td>显示名</td>
                <td><input type="text" id="display" class="longtxt" value = "${field.name_display!}"/></td>
            </tr>
            <tr>
                <td>备注</td>
                <td><input type="text" id="note" class="longtxt" value="${field.note!}" /></td>
            </tr>
            <tr>
                <td>是否主键</td>
                <td>
                    <radio>
                    <label><input type="radio" name="is_pk" value="0" <#if field.is_pk==0>checked</#if>/><a>否</a></label>&nbsp;&nbsp;
                    <label><input type="radio" name="is_pk" value="1" <#if field.is_pk==1>checked</#if>/><a>是</a></label>
                    </radio>
                </td>
            </tr>
            <tr>
                <td>调试参数</td>
                <td><input type="text" id="debug_args" value = "${model.debug_args!}" class="longtxt" placeholder="{user_id:1}"/></td>
            </tr>
            <tr>
                <td>动态表达式</td>
                <td>
                    <pre style="height:260px;width:600px;" jt-js id="code">${field.expr!}</pre>
                </td>
                <td>（按Esc全屏）</td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <#if is_operator ==1>
                        <button type="button" onclick="saveEdit(false)">保存</button>&nbsp;&nbsp;
                        <button type="button" onclick="saveEdit(true)">保存并返回</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <#if field.field_id??>
                    <button type="button" onclick="debug()" class="minor" >调试字段</button>
                    </#if>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <code>
                        /*
                        this.$c //上下文对象；
                        this._x //字段私有值；
                        this.x() //字段公有值；
                        */
                    </code>
                </td>
            </tr>
        </table>
    </form>

    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="model" value="${model.tag!}/${model.name!}">
        <input type="hidden" name="args" id="args">
        <input type="hidden" id="field" name="field">
    </form>
</detail>
</body>
</html>