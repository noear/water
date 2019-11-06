<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑操作</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/number.js"></script>
</head>
<script>
    function saveOperate() {
        var operate_id = '${operate.operate_id!}';
        var server_id = '${server_id!}';
        var name = $('#name').val();
        var rank = $('#rank').val();
        var args = document.getElementsByName('args');
        var script_id = $('#sel_script').val();

        var argList = [];
        for(var i=0;i<args.length;i++){
            var arg = args[i].value;
            argList.push(arg);
        }

        if (name == null || name == "" || name == undefined) {
            top.layer.msg("请输入操作名称！");
            return;
        }
        if (name == null || name == "" || name == undefined) {
            top.layer.msg("请输入操作排序！");
            return;
        }
        if (script_id == null || script_id == 0) {
            top.layer.msg("请选择使用脚本！");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/ops/server/extra/operate/ajax/save",
            traditional: true,
            data: {
                "server_id": server_id,
                "operate_id": operate_id,
                "name": name,
                "args":argList,
                "rank": rank,
                "script_id": script_id
            },
            success: function (data) {
                if (data.code == 1) {
                    top.layer.msg(data.msg);
                    setTimeout(function () {
                        parent.location.reload();
                    }, 200);
                } else {
                    top.layer.msg(data.msg);
                }
            }
        });
    }

    var cateList;

    function getScriptByTag(script_id) {
        var tag = $('#sel_tag').val();
        $.ajax({
            type: "POST",
            url: "/ops/server/extra/operate/ajax/getScript",
            data: {"tag": tag},
            success: function (data) {
                cateList = data.scripts;
                addOption(data.scripts, script_id);
                contribute_args(script_id);
            }
        });
    }

    function addOption(cateList, script_id) {
        var cateField = document.getElementById("sel_script");
        cateField.innerText = '';
        var option_1 = document.createElement("option");
        option_1.value = '';
        option_1.innerHTML = "请选择标签";
        cateField.appendChild(option_1);
        for (var idx in cateList) {
            var option = document.createElement("option");
            option.value = cateList[idx].script_id;
            option.innerHTML = cateList[idx].name;
            if (script_id != null && option.value == script_id) {
                option.selected = true;
                contribute_args(script_id);
            }
            cateField.appendChild(option);
        }
    }


    function getScript(id) {
        var script_id = $('#sel_script').val();
        for (var idx in cateList) {
            if (script_id == cateList[idx].script_id) {

                return;
            }
        }
    }

    function contribute_args(script_id) {
        var args_content = '';
        $.ajax({
            type: "POST",
            url: "/ops/server/extra/operate/ajax/getArgs",
            data: {
                "script_id": script_id,
                "operate_id": '${operate.operate_id!}',
            },
            success: function (data) {
                var argsList = data.argList;
                args_content += "<td>参数</td>";
                if (argsList.length == 0) {
                    args_content += "<td>该脚本无需参数</td>";
                } else {
                    args_content += "<td colspan='3' style='padding-left: 0px;width: 200px'><table><tbody>";
                    for (var i in argsList) {
                            args_content += "<td>" + argsList[i].param_name + "<td> = </td></td><td><input name='args' type='text' placeholder='"+argsList[i].param_note+"' value='"+argsList[i].param_value+"'/></td></tr>"
                    }
                    args_content += "</tbody></table></td>";
                }
                $('#args_tr').html(args_content);
            }
        });

    }

    $(function () {
        document.getElementById('sel_tag').value = "${operate.script_tag!}";
        getScriptByTag('${operate.script_id!}');
    })

    $(document).ready(function () {
        $('#sel_script').change(function () {
            var script_id = $(this).children('option:selected').val();
            contribute_args(script_id);
        })
    })

</script>
<style>
    select {
        min-width: 50px
    }

    datagrid td {
        min-height: 30px
    }
</style>
<body>
<detail>
    <table id="edit_table">
        <tr>
            <td width="90px">操作名称</td>
            <td><input type="text" id="name" value="${operate.name!}"/></td>
        </tr>
        <tr>
            <td>排序</td>
            <td><input type="text" id="rank" value="${operate.rank!}" onkeyup="javaScript:RepNumber(this)"/></td>
        </tr>
        <tr>
            <td>使用脚本</td>
            <td>
                <select id="sel_tag" onchange="getScriptByTag()">
                    <option value="">请选择标签</option>
                    <#list scriptTags as tag>
                        <option value="${tag.tag}">${tag.tag}</option>
                    </#list>
                </select>&nbsp;
                <select id="sel_script">
                    <option value="">请选择脚本</option>
                </select>
            </td>
        </tr>
        <tr id="args_tr">
        </tr>
        <tr>
            <td></td>
            <td colspan="3">
                <button type="button" onclick="saveOperate()">保存</button>
            </td>
        </tr>
    </table>
</detail>
</body>
</html>