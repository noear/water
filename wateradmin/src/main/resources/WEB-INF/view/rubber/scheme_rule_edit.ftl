<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 规则</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        div[name='select'] >select:nth-child(even){
            width: 52px;
        }

        div[name='select'] >select:nth-child(odd) {
            width: 300px;
        }

        div[name='select'] >input[type='text'],datalist {
            width: 179px;
        }

        select{
            height: 28px;
        }

        tr > td:first-child {
            width:90px;
        }
    </style>
    <script>
        function debug() {
            $('#debug_form').submit();
        }
    </script>
</head>
<body>
<main>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="/rubber/scheme/inner?tag_name=${tag_name!}&f=water" class="noline">计算方案</a> / <a href="#" onclick="javascript:history.back(-1);" class="noline">规则列表</a></h2> / 规则编辑 :: ${name_display!}
    </left>
    <right>
        <#if is_admin ==1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>
<detail>
    <form>

        <datalist id="select_right">
            <#list blocks as item>
                <option label="${item.tag!}/${item.name!}" value="${item.name_display!}"></option>
            </#list>
        </datalist>

        <input type="hidden" id="input_scheme_id" value="${scheme_id}" />
        <input type="hidden" id="input_display" value="${name_display!}" />
        <input type="hidden" id="input_tag_name" value="${tag_name!}" />

        <table id="table_data">
            <tr>
                <input type="hidden" value="${schemeRule.rule_id}" id="input_id">
            </tr>
            <tr>
                <th>显示名</th>
                <td>
                    <input type="text" id="input_name_display" class="longtxt" value="${schemeRule.name_display!}"/>
                </td>
            </tr>
            <tr>
                <th>排序</th>
                <td><input type="text" maxlength="3" id="input_sort" value="${schemeRule.sort}"/>
                </td>
            </tr>
            <tr>
                <th>评估建议</th>
                <td>
                    <select id="select_advice">
                        <option value="0"></option>
                        <option value="1">交易放行</option>
                        <option value="2">审慎审核</option>
                        <option value="3">阻断交易</option>
                    </select>
                    <script>
                        $('#select_advice').val(${schemeRule.advice});
                    </script>
                </td>
            </tr>
            <tr>
                <th>评估分值</th>
                <td>
                    <input type="text" maxlength="8" id="input_score" value="${schemeRule.score}"/>
                </td>
            </tr>
            <tr>
                <th style="padding-top: 16px" valign="top">规则表达式</th>
                <td id="tr_expr">

                </td>
            </tr>

            <tr>
                <th></th>
                <td>
                    <#if is_operator ==1>
                    <button type="button" id="btn_save">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>

                    <#if (debug_args??) && (rule_id>0)>
                        <button type="button" onclick="debug()" class="minor" >调试</button>
                    </#if>
                </td>
            </tr>
        </table>
    </form>
    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="request_id" value="d581b1083fcf4033be4fcd3ad26e6919" />
        <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}" />
        <input type="hidden" name="args" value="${debug_args!}" />
        <input type="hidden" name="type" value="2" />
        <input type="hidden" id="rule" name="rule" value="r${rule_id}" />
        <input type="hidden" name="policy" value="2001" />
    </form>
</detail>
</main>
</body>
</html>
<script>

    var leftList = ${leftList!};
    var editList = ${expr!};
    var f = '${f!}';

    $(function(){
        if($("#input_id").val()){
            var list = editList;
            for(j = 0; j < list.length; j++){
                var item = list[j];
                createTr(item.l, item.op, item.r , item.ct);
            }
        }else{
            createTr();
        }
    });

    $("select[id='select_next']").on("change", function () {
        selectNextFunc($(this));
    });

    function selectNextFunc(obj) {
        var selectDiv = $("div[name='select']");
        var index  = selectDiv.index($(obj).parent()) + 1;
        if (obj.val() != "" && index == selectDiv.length) {
            createTr();
        }else if(obj.val() == ""){
            clearItem(index);
        }
    }

    function createTr(leftValue,centerValue,rightValue,nextValue){
        var tr = $("<div name='select'></div>");

        var selectLeft = createSelectLeft(leftValue);
        var selectCenter = createSelectCenter(centerValue);
        var value = "";
        if(rightValue){
            value = getNameDisplayByFunName(rightValue);
            if(!value){
                value = rightValue;
            }
        }
        var inputRight = $("<input id='input_right' type='text' placeholder='输入阀值或选择数据' list='select_right' value=\""+value+"\" />");
        var selectNext = createSelectNext(nextValue);

        tr.append(selectLeft).append("\n");
        tr.append(selectCenter).append("\n");
        tr.append(inputRight).append("\n");
        tr.append(selectNext);

        $("#tr_expr").append(tr);
    }

    function createSelectLeft(defaultValue){
        if(leftList) {
            var list = leftList;
            var selectLeft = $("<select id='select_left'></select>");
            // 添加选项
            selectLeft.append($("<option value=''>选择字段</option>"));
            for (i = 0; i < list.length; i++) {
                var item = list[i];
                var selected = "";
                if(item.name == defaultValue){
                    selected = "selected"
                }
                selectLeft.append($("<option value='" + item.name + "' "+selected+">" + item.name_display + "</option>"));
            }
        }
        return selectLeft;
    }

    function createSelectCenter(centerValue){
        var selectContent = $("<select id='select_center'></select>");

        var center = ["=","!=",">","<",">=","<=","LIKE","IN"];
        selectContent.append($("<option></option>"));
        for(i = 0; i < center.length; i++){
            var text = center[i];
            var selected = "";
            if(text == centerValue){
                selected = "selected"
            }
            selectContent.append($("<option "+selected+">"+ text +"</option>"));
        }
        return selectContent;
    }

    function createSelectNext(nextValue){
        var selectNext = $("<select id='select_next'></select>");
        selectNext.on("change",function(){
            selectNextFunc($(this));
        });
        // 添加选项
        selectNext.append($("<option></option>"));
        var array = new Array();
        var item1 = { value : "&&", text : "并且"};
        var item2 = { value : "||", text : "或者"};
        array.push(item1);
        array.push(item2);
        for(i = 0; i < array.length; i++){
            var item = array[i];
            var selected = "";
            if(item.value == nextValue){
                selected = "selected";
            }
            selectNext.append($("<option value='"+item.value +"' "+selected+">"+item.text+"</option>"));
        }
        return selectNext;
    }

    function clearItem(index){
        var tableRow = $("#tr_expr").find("div");
        for(i = index; i < tableRow.length; i++){
            var row = tableRow[i];
            row.remove();
        }
    }

    $("#input_sort,#input_score").on("input",function(){
        var reg = /^[0-9]*[0-9][0-9]*$/;
        var reg2 = /^-?[0-9]*$/;
        if(!$("#input_sort").val().match(reg)){
            $("#input_sort").val("");
        }
        if(!$("#input_score").val().match(reg2)){
            $("#input_score").val("");
        }
    });

    $("#btn_save").on("click",function(){
        var sort = $("#input_sort").val();
        var score = $("#input_score").val();
        var advice = $('#select_advice').val();
        var scheme_id = $("#input_scheme_id").val();
        var name_display = $("#input_name_display").val();
        var rule_id = $("#input_id").val() ? $("#input_id").val() : 0;

        if(!name_display){
            top.layer.msg("请输入规则的显示名！");
            return;
        }else if(!sort){
            top.layer.msg("排序不能为空！");
            return;
        }else if(!score){
            top.layer.msg("评估分值不能为空！");
            return;
        }

        var tableRow = $("#tr_expr").find("div");
        var expressions = new Array();
        for(i = 0; i < tableRow.length; i++){
            var row = tableRow[i];
            var left = $(row).find("#select_left option:selected");
            var center = $(row).find("#select_center");
            var right = $(row).find("#input_right");
            var next = $(row).find("#select_next");

            var name_dispaly = right.val();
            var fun_name = "";
            var right_key = getFunNameByNameDisplay(right.val());
            if(right_key != null){
                fun_name = right_key;
            }

            var item = {
                "left": left.val(),
                "leftValue" : left.text(),
                "center": center.val(),
                "right": fun_name,
                "rightValue" : name_dispaly,
                "ct" : next.val()
            };
            expressions.push(item);
        }
        var expressionValue = JSON.stringify(expressions);
        $.ajax({
            type: "POST",
            url: "/rubber/scheme/rule/edit/ajax/save",
            data: {
                "rule_id": rule_id,
                "scheme_id": scheme_id,
                "expr": expressionValue,
                "advice":advice,
                "score": score,
                "sort": sort,
                "name_display": name_display
            },
            success: function (data) {
                if (data.code == 1) {
                    top.layer.msg('操作成功');
                    setTimeout(function () {
                        location.href = "/rubber/scheme/rule/design?scheme_id="+ $("#input_scheme_id").val() + "&tag_name="+
                                $("#input_tag_name").val() +"&name_display=" + $("#input_display").val()+"&f="+f;
                    }, 1000);
                } else {
                    top.layer.msg(data.msg);
                }
            }
        });
    });

    function getFunNameByNameDisplay(val){
        var fun_name = null;
        $("#select_right").children().each(function(index,value){
            if($(this).val() == val){
                fun_name = $(this).attr("label");
            }
        });
        return fun_name;
    };

    function getNameDisplayByFunName(val){
        var fun_name = null;
        $("#select_right").children().each(function(index,value){
            if($(this).attr("label") == val){
                fun_name = $(this).val();
            }
        });
        return fun_name;
    };

    //删除规则
    function del() {
        top.layer.confirm('确定删除该规则', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/rubber/scheme/rule/del/ajax/save",
                data:{"rule_id":'${schemeRule.rule_id}',"scheme_id":'${scheme_id}'},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.href="/rubber/scheme/rule/design?scheme_id=${scheme_id}&tag_name=${tag_name}"
                                    + "&name_display=${name_display}";

                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
            top.layer.close(top.layer.index);
        });
    };

</script>