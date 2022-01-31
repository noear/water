<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function  debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
            }
        }
    </script>
</head>
<body>
<main>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">计算方案</a></h2> / 编辑
    </left>
    <right>
        <#if (scheme.scheme_id > 0)>
            <#if (is_admin == 1) && (scheme.scheme_id > 0)>
                <button type="button" onclick="del()" class="minor mar10-l">删除</button>
            </#if>
        </#if>
    </right>
</toolbar>

<detail>
    <form>
        <table>
            <tr>
                <input type="hidden" value="${scheme.scheme_id}" id="input_id">
            </tr>
            <tr>
                <th>分类标签</th>
                <td>
                    <input type="text" id="input_tag" value="${scheme.tag!}"/>
                </td>
            </tr>
            <tr>
                <th>代号</th>
                <td><input type="text" id="input_name" value="${scheme.name!}"/>
                </td>
            </tr>
            <tr>
                <th>显示名</th>
                <td>
                    <input type="text" id="input_name_display" value="${scheme.name_display!}"/>
                </td>
            </tr>
            <tr>
                <th>关联模型</th>
                <td>
                    <select id="select_model">
                        <option value=""></option>
                        <#list models as model>
                            <option value="${model.tag!}/${model.name!}">${model.tag!}/${model.name_display!}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>引用指标</th>
                <td>
                    <input type="text" id="related_block" value="${scheme.related_block!}" placeholder="tag/*;tag/block..."/>（指标库）
                </td>
            </tr>
            <tr>
                <th>调试参数</th>
                <td>
                    <input type="text" id="debug_args" value="${scheme.debug_args!}" class="longtxt" placeholder="{user_id:1}"/>
                </td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <#if (is_operator ==1 )>
                    <button type="button" id="btn_save" >保存</button>&nbsp;&nbsp;&nbsp;&nbsp;

                    </#if>
                    <#if (scheme.scheme_id > 0)>
                        <button type="button" id="saveAs" class="minor" >另存为</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>
                    <button type="button" onclick="debug()"  class="minor">调试</button>
                </td>
            </tr>
        </table>
    </form>
    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
        <input type="hidden" name="args" id="args">
        <input type="hidden" name="policy" value="2001">
    </form>
</detail>
</main>
</body>
</html>
<script>
    var f = '${f}';
    $(function(){
        $("#select_model").val("${scheme.related_model!}");
    });

    $("#btn_save").on("click", function(){
        var id = $('#input_id').val();
        var tag = $('#input_tag').val();
        var name = $('#input_name').val();
        var name_display = $('#input_name_display').val();
        var related_model = $('#select_model option:selected');
        var related_block = $('#related_block').val();
        var debug_args = $('#debug_args').val();

        if (!tag) {
            top.layer.msg("标签名称不能为空！");
            return;
        }
        if (!name) {
            top.layer.msg("配置项关键字不能为空！");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/rubber/scheme/edit/ajax/save",
            data: {
                "id": id,
                "tag": tag,
                "name": name,
                "name_display": name_display,
                "related_model": related_model.val(),
                "related_model_display": related_model.text(),
                "related_block": related_block,
                "debug_args": debug_args
            },
            success: function (data) {
                if (data.code == 1) {
                    top.layer.msg(data.msg)
                    setTimeout(function () {
                        if('water'==f) {
                            parent.location.href = "/rubber/scheme?tag_name="+tag+"&f="+f;
                        } else if ('sponge'==f){
                            parent.location.href = '${backUrl!}'+"push/scheme?tag_name="+tag+"&f="+f;
                        }
                    }, 800);
                } else {
                    top.layer.msg(data.msg);
                }
            }
        });
    });

    //另存为
    $("#saveAs").on("click", function(){
        var scheme_id = $('#input_id').val();
        var name = $('#input_name').val();
        var name_display = $('#input_name_display').val();
        var debug_args = $('#debug_args').val();
        var tag = $('#input_tag').val();

        if (!name) {
            top.layer.msg("配置项关键字不能为空！");
            return;
        }
        if (name == '${scheme.name!}') {
            top.layer.msg("请重命名代号！");
            return;
        }

        top.layer.confirm('确定另存为操作', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                url: "/rubber/scheme/edit/ajax/saveAs",
                data: {
                    "scheme_id": scheme_id,
                    "name":name,
                    "name_display":name_display,
                    "debug_args": debug_args,
                    "tag": tag
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg('操作成功');
                        setTimeout(function () {
                            if('water'==f) {
                                parent.location.href = "/rubber/scheme?tag_name=${scheme.tag!}&f="+f;
                            } else if ('sponge'==f){
                                parent.location.href = '${backUrl!}'+"push/scheme?tag_name=${scheme.tag!}&f="+f;
                            }
                        }, 800);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        });

    });

    //删除计算方案
    function del() {
        var scheme_id = $('#input_id').val();

        top.layer.confirm('确定要删除吗？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                url: "/rubber/scheme/edit/ajax/del",
                data: {
                    "scheme_id": scheme_id
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg(data.msg)
                        setTimeout(function () {
                            if('water'==f) {
                                parent.location.href = "/rubber/scheme?tag_name=${scheme.tag!}&f="+f;
                            } else if ('sponge'==f){
                                parent.location.href = '${backUrl!}'+"push/scheme?tag_name=${scheme.tag!}&f="+f;
                            }
                        }, 1000);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        });
    };

    //导入
    function imp() {
        var scheme_id = $('#input_id').val();

        top.layer.confirm('确定导入？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                url: "/rubber/scheme/edit/ajax/import",
                data: {
                    "scheme_id": scheme_id
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg(data.msg)
                        setTimeout(function () {
                            if('water'==f) {
                                parent.location.href = "/rubber/scheme?tag_name=${scheme.tag!}&f="+f;
                            } else if ('sponge'==f){
                                parent.location.href = '${backUrl!}'+"push/scheme?tag_name=${scheme.tag!}&f="+f;
                            }
                        }, 1000);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        });
    };

</script>