<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        tbody input {
            margin-right: 5px;
            padding:0 2px;
            height: 20px!important;
        }

    </style>
</head>
<body>
<blockquote>
    <h2 class="ln30"><a href="/rubber/scheme/inner?tag_name=${tag_name!}&f=water" class="noline">计算方案</a></h2> / 规则列表 :: ${name_display}
</blockquote>
<main class="frm">
    <toolbar>
        <left>
            <input type="hidden" name="tag_name" id="tag_name" value="${tag_name!}"/>
            <input type="hidden" id="input_scheme_id" value="${scheme_id}"/>
            <input type="hidden" id="input_name_display" value="${name_display!}"/>
            <input type="hidden" id="input_tag_name" value="${tag_name!}"/>
            规则关系:
            <select id="rule_relation">
                <option value="0" <#if scheme.rule_relation == 0>selected</#if>>并且关系</option>
                <option value="1" <#if scheme.rule_relation == 1>selected</#if>>或者关系</option>
            </select>
            <#if is_operator ==1>
                <button type="button" class="edit" id="btn_save">保存</button>
            </#if>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <#if is_operator == 1>
                <button id="btn_add" type="button" class="edit">添加</button>&nbsp;&nbsp;&nbsp;&nbsp;
            </#if>
        </left>
        <right>
            <#if related_db?has_content>
                <form action="${raas_uri}/debug" target="_blank" method="get">
                    <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
                    <input type="hidden" name="type" value="11">
                    <input type="hidden" name="limit" value="10">
                    <button type="submit" >调试查询</button>
                </form>
                &nbsp;&nbsp;
                <form action="${raas_uri}/preview.js" target="_blank" method="get">
                    <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
                    <input type="hidden" name="type" value="11">
                    <input type="hidden" name="limit" value="10">
                    <button type="submit" >预览查询</button>
                </form>
            <#else>
                <form action="${raas_uri!}/debug" target="_blank" method="get">
                    <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
                    <input type="text" name="args" value="${scheme.debug_args!}" placeholder="{user_id:1}">
                    <input type="hidden" name="policy" value="2001">
                    <input type="hidden" name="type" value="2">
                    <button type="submit" >调试规则</button>
                </form>
                &nbsp;&nbsp;
                <form action="${raas_uri!}/preview.js" target="_blank" method="get">
                    <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
                    <button type="submit">预览规则</button>
                </form>
            </#if>
        </right>
    </toolbar>

    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20px"></td>
                <td width="200px">显示名</td>
                <td>规则</td>
                <td width="40px">评估<br/>分值</td>
                <td width="40px">评估<br/>建议</td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list rules as item>
                <tr name="data_row" target="${item.rule_id}" title="r${item.rule_id}">
                    <td class="center"><checkbox>
                            <label><input type="checkbox" ${(item.is_enabled == 1)?string('checked' , '')} /><a></a></label>
                        </checkbox>
                    </td>
                    <td class="left"><a id="item_scheme" target="${item.rule_id}" class="t2">${item.name_display!}</a></td>
                    <td class="left" id="td_expr">${item.expr_display!}</td>
                    <td class="center">${item.score}</td>
                    <td class="center">${item.advice}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>
<script>

    $(function () {
        $("td[id='td_expr']").each(function () {
            var innerText = this.innerText;
            var inputReg = /{\S*}/g;

            var innerHTML = innerText;
            if (innerText.match(inputReg) != null) {
                innerText.match(inputReg).forEach(function (value, index, array) {
                    var id = value.substring(value.indexOf("{") + 1, value.lastIndexOf(":"));
                    var content = value.substring(value.lastIndexOf(":") + 1, value.lastIndexOf("}"));

                    var width = getInputWidthByVal(content);
                    innerHTML = innerHTML.replace(value, "<input name='input_right' id='" + id + "' style='width: " + width + "px' type='text' value=\"" + content + "\" oninput='setInputRightWidth(this)'/>");
                });
                this.innerHTML = innerHTML;
            }

            if(innerText.indexOf(" IN ")>0){
                innerHTML = innerHTML.replace(" IN "," <mark>IN</mark> ");
                this.innerHTML = innerHTML;
            }
        })
    });

    function setInputRightWidth(obj) {
        var width = getInputWidthByVal($(obj).val());
        $(obj).width(width);
    };

    function getInputWidthByVal(val) {
        var pattern_char = /[0-9a-zA-Z''\.]/g;
        var pattern_chin = /[\u4e00-\u9fa5]/g;
        var char_length = 0;
        var chin_length = 0;
        if (val.match(pattern_char)) {
            char_length = val.match(pattern_char).length;
        }
        if (val.match(pattern_chin)) {
            chin_length = val.match(pattern_chin).length;
        }

        var width = (char_length * 6) + (chin_length * 11) + 8;
        return width;
    }

    $("#btn_search").on("click", function () {
        location.href = "/rubber/scheme/rule/design?scheme_id=" + $("#input_scheme_id").val()
            + "&tag_name=" + $("#input_tag_name").val() + "&name_display=" + $("#input_name_display").val() + "&name=" + $("#input_search_name").val();
    });

    $("#btn_save").on("click", function () {
        var expr = {};
        $("tr[name='data_row']").each(function (index, value) {
            var is_enabled = $(this).find("input[type='checkbox']").is(":checked");
            var rule_id = $(this).attr("target");
            var tr_expr = $(this).find("input[name='input_right']");

            var item = {is_enabled:is_enabled};
            tr_expr.each(function (itemIndex, itemValue) {
                var expr_id = $(this).attr("id");
                item[expr_id] = $(this).val();
            });

            expr[rule_id] = item;
        });

        var scheme_id = ${scheme.scheme_id};
        var rule_relation = $('#rule_relation').val();

        $.ajax({
            type: "POST",
            url: "/rubber/scheme/rule/expr/edit/ajax/save",
            data: {
                "exprs": JSON.stringify(expr),
                "scheme_id":scheme_id,
                "rule_relation":rule_relation

            },
            success: function (data) {
                if (data.code == 1) {
                    top.layer.msg(data.msg);
                    // setTimeout(function () {
                    //     location.href = "/rubber/scheme/rule/design?scheme_id=" + $("#input_scheme_id").val()
                    //         + "&tag_name=" + $("#input_tag_name").val() + "&name_display=" + $("#input_name_display").val() + "&name=" + $("#input_search_name").val();
                    // }, 1000);
                } else {
                    top.layer.msg(data.msg);
                }
            },
            error: function(){
                top.layer.msg("保存异常");
            }
        });
    });

    function addorEditScheme(rule_id) {
        var params = "";
        if (rule_id) {
            params = "&rule_id=" + rule_id;
        }
        var url = "/rubber/scheme/rule/edit?scheme_id=" + $("#input_scheme_id").val() + "&tag_name=" +
            $("#input_tag_name").val() + "&name_display=" + $("#input_name_display").val()+"&f=${f}";
        location.href = url + params;
    }

    $("#btn_add").on("click", function () {
        addorEditScheme();
    });

    $("a[id='item_scheme']").on("click", function () {
        addorEditScheme(this.target)
    });

</script>