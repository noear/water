<div>
    <datalist id="select_right">
        <#list functions as item>
            <option label="${item.tag!}/${item.name!}" value="${item.name_display!}"></option>
        </#list>
    </datalist>
    <input type="hidden" value="${hasCondition!}" id="input_id"/>
    <table id="table_data" style="margin-top: 10px;">
        <tr>
            <td width="90px;" class="right">名称:</td>
            <td>
                <input type="text" id="input_name_display" value="${schemeNode.name!}" style="width: 200px;"/>
            </td>
        </tr>
        <tr>
            <td style="padding-top: 16px" valign="top" class="right">规则表达式:</td>
            <td id="tr_expr">

            </td>
        </tr>
    </table>
    <script>
        var leftList = '${leftList!}';
        var editList = '${expr!}';

        $(function(){
            if($("#input_id").val() > 0){
                var list = JSON.parse(editList);
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
            var inputRight = $("<input id='input_right' type='text' placeholder='输入阀值或选择数据' list='select_right' value='"+value+"' />");
            var selectNext = createSelectNext(nextValue);

            tr.append(selectLeft).append("\n");
            tr.append(selectCenter).append("\n");
            tr.append(inputRight).append("\n");
            tr.append(selectNext);

            $("#tr_expr").append(tr);
        }

        function createSelectLeft(defaultValue){
            if(leftList) {
                var list = JSON.parse(leftList);
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

        function getFunNameByNameDisplay(val){
            var fun_name = null;
            $("#select_right").children().each(function(index,value){
                if($(this).val() == val){
                    fun_name = $(this).attr("label");
                }
            });
            return fun_name;
        }

        function getNameDisplayByFunName(val){
            var fun_name = null;
            $("#select_right").children().each(function(index,value){
                if($(this).attr("label") == val){
                    fun_name = $(this).val();
                }
            });
            return fun_name;
        }

    </script>
</div>
