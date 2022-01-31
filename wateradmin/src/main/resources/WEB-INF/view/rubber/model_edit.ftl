<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据模型-编辑</title>
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
        var model_id = '${model.model_id}';
        var f = '${f}';
        function saveEdit() {
            var init_expr = window.editor.getValue();
            var tag = $('#tag').val();
            var name = $('#name').val();
            var name_display = $('#name_display').val();
            var debug_args = $('#debug_args').val();
            var related_db = $('#related_db').val();

            if (!tag){
                top.layer.msg("分类标签不能为空！");
                return;
            }

            if (!name){
                top.layer.msg("代号不能为空！");
                return;
            }

            if (!name_display){
                top.layer.msg("显示名不能为空！");
                return;
            }

            if(!model_id){
                model_id=0;
            }
            $.ajax({
                type:"POST",
                url:"/rubber/model/edit/ajax/save",
                data:{
                    "model_id":model_id,
                    "tag":tag,
                    "name":name,
                    "name_display":name_display,
                    "init_expr":init_expr,
                    "debug_args":debug_args,
                    "related_db":related_db
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            if('water'==f) {
                                parent.location.href="/rubber/model?tag_name="+tag;
                            } else if ('sponge'==f){
                                parent.location.href='${backUrl!}'+"push/model?tag_name="+tag;
                            }

                        },800);
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
                    url: "/rubber/model/edit/ajax/del",
                    data: {
                        "model_id": model_id
                    },
                    success: function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                if('water'==f) {
                                    parent.location.href="/rubber/model?tag_name="+tag;
                                } else if ('sponge'==f){
                                    parent.location.href='${backUrl!}'+"push/model?tag_name="+tag;
                                }
                            },800);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });
        };


        //另存为
        function saveAs() {
            var init_expr = editor.getValue();
            var tag = $('#tag').val();
            var name = $('#name').val();
            var name_display = $('#name_display').val();
            var debug_args = $('#debug_args').val();
            var related_db = $('#related_db').val();

            if (name == '${model.name!}') {
                top.layer.msg('请重命名代号！');
                return;
            }

            top.layer.confirm('确定另存为操作', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/rubber/model/edit/ajax/saveAs",
                    data:{
                        "model_id":model_id,
                        "tag":tag,
                        "name":name,
                        "name_display":name_display,
                        "init_expr":init_expr,
                        "debug_args":debug_args,
                        "related_db":related_db
                    },
                    success:function (data) {
                        if(data.code==1) {
                            top.layer.msg(data.msg);
                            setTimeout(function(){
                                if('water'==f) {
                                    parent.location.href="/rubber/model?tag_name="+tag;
                                } else if ('sponge'==f){
                                    parent.location.href='${backUrl!}'+"push/model?tag_name="+tag;
                                }
                            },1000);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
            });


        };

        function  debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
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
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">数据模型</a></h2> / 编辑
    </left>
    <right>
        <#if (is_admin == 1) && (model.model_id > 0) >
            <button type="button" onclick="del()" class="minor mar10-l">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form>

        <table>
            <tr>
                <th>分类标签</th>
                <td><input type="text" id="tag" value = "${model.tag!}"/></td>
            </tr>
            <tr>
                <th>代号</th>
                <td><input type="text" id="name" value = "${model.name!}"/></td>
            </tr>
            <tr>
                <th>显示名</th>
                <td><input type="text" id="name_display" value = "${model.name_display!}"/></td>
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
                        $('#related_db').val('${model.related_db!}');
                    </script><note>（查询时使用）</note>
                </td>
            </tr>
            <tr>
                <th>调试参数</th>
                <td><input type="text" id="debug_args" value = "${model.debug_args!}" class="longtxt" placeholder="{user_id:1}"/></td>
            </tr>
            <tr>
                <th>构造表达式</th>
                <td>
                    <pre style="height:180px;width:600px;" jt-js id="code">${model.init_expr!}</pre>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <#if is_operator ==1>
                        <button type="button" onclick="saveEdit()">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" onclick="saveAs()" class="minor">另存为</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <button onclick="debug()" type="button"  class="minor">调试</button>
                </td>
            </tr>
            <tr>
                <th></th>
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
    </form>
</detail>
</body>
</html>