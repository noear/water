<div>
<style>
    #taskAll .taskType{width: 70px}
    #taskAll .task{width: 225px;}
</style>

<table id="excuteNode" style="margin-top: 10px;">
    <tr>
        <td width="110px;" class="right"><label style="color: red">*</label>名称：</td>
        <td><input type="text" style="width:300px;height:30px;" name="nodeName" id="nodeName" value="${node.name!}"></td>
    </tr>
    <tr>
        <td class="right">参与人员：</td>
        <td>
            <select name="manager" id="manager" style="width:300px;">
                <#list actors as m>
                    <#if (m.name_display!'') == (node.actor_display!'')>
                        <#if (m.name_display??) == false>
                            <option value="" selected></option>
                        </#if>
                        <#if m.name_display?? >
                            <option value="${m.tag}/${m.name!},${m.name_display!}" selected>${m.tag}/${m.name_display!}</option>
                        </#if>
                    <#else>
                        <option value="${m.tag}/${m.name!},${m.name_display!}">${m.tag}/${m.name_display!}</option>
                    </#if>
                </#list>
            </select>
        </td>
    </tr>
    <tr>
        <td valign="top"  class="right" style="padding-top: 15px;">执行任务：</td>
        <td id="excuteTask">
            <div id="taskAll">
                <#list schemeNode as m>
                <div>
                    <select name="nodeType" class="taskType" onchange="changeTaskType(this)">
                        <option value="R" selected>计算方案</option>
                    </select>
                    <select name="task" class='task'>
                        <#list m.name_resp as m1>
                            <#if m1.select == m1.name>
                                <#if (m1.select??)=false>
                                    <option value="" selected></option>
                                </#if>
                                <#if m1.select??>
                                    <option value="${m1.name}" selected>${m1.tag}/${m1.display_name}</option>
                                </#if>
                            </#if>
                            <#if m1.select != m1.name >
                                <option value="${m1.name}">${m1.tag}/${m1.display_name}</option>
                            </#if>
                        </#list>
                    </select>
                    <label style="color: red" onclick="delTask(this,${m_index})">X</label>
                </div>
            </#list>
            </div>
        </td>
        <td valign="bottom">
            <button class="edit pad0" style="margin-bottom: 3px;line-height: 27px!important; height: 27px!important; width: 50px; min-width: auto!important;" onclick="addTask()" >添加</button>
        </td>
    </tr>
</table>


<script>
    function addTask(index) {
        var htmm = `<div>
                    <select class="taskType" onchange="changeTaskType(this)"><option value="R">计算方案</option></select>
                    <select class="task"><option value=""></option>`;

        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/getTask",
            async: false,
            data:{"taskType":"R","scheme_id":${node.scheme_id}},
            success:function (data) {
                $.each(data, function (n, value) {
                        htmm = htmm + '<option value="' + value.tag+'/'+value.name + '">' + value.tag + '/' + value.name_display + '</option>';
                    }
                );
                htmm = htmm + '</select><label style="color: red;" onclick="delTask(this)"> X</label>';
            }
        });

        htmm+="</div>";
        $("#taskAll").append(htmm);
    };

    function getSchemeNodeOption() {
        //var htmm = '<option value="" selected></option>';
        var htmm = '<option value="" selected></option>';
        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/getTask",
            async: false,
            data:{"taskType":"R"},
            success:function (data) {
                $.each(data, function (n, value) {
                        htmm = htmm + '<option value="' + value.tag+'/'+value.name + '">' + value.tag+'/'+ value.name_display + '</option>';
                    }
                );
            }
        });
        return htmm;
    };

    //删除执行任务
    function delTask(that,index){
        if (index == 0) {
            var option = getSchemeNodeOption();
            $(that).prev('select').html(option);
        } else {
            $(that).prev('select').remove();
            $(that).prev('select').remove();
            $(that).prev('br').remove();
            $(that).prev('br').remove();
            $(that).remove();
        }
    };

    function changeTaskType(that) {

        var taskType = $(that).val();
        //var htmm = "<option value=\"\"></option>";
        var htmm = "<option value=''></option>";
        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/getTask",
            async: false,
            data:{"taskType":taskType,"scheme_id":${node.scheme_id}},
            success:function (data) {
                var taskValue = $(that).next('select');
                taskValue.find("option").remove();

                $.each(data, function (n, value) {
                        if(taskType == 'R'){
                            htmm = htmm + '<option value="' + value.tag+'/'+value.name + '">' + value.tag +'/'+ value.name_display + '</option>';
                        } else if (taskType == "F"){
                            htmm = htmm + '<option value="' + value.tag+'/'+value.fun_name + '">' + value.tag +'/'+ value.name_display + '</option>';
                        }
                    }
                );
                taskValue.append(htmm);
            }
        });
    };
</script>
</div>