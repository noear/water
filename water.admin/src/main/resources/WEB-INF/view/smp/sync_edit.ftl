<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据同步-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveEdit() {
            var syn_id = ${syn.sync_id};
            var type = $('#type').val();
            var name = $('#name').val();
            var tag = $('#tag').val();
            var interval = $('#interval').val();
            var target = $('#target').val();
            var target_pk = $('#target_pk').val();
            var source = $('#source').val();
            var source_model = $('#source_model').val();
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
                url:"/smp/sync/edit/ajax/save",
                data:{"syn_id":syn_id,"type":type,"name":name,"tag":tag,"interval":interval,"target":target,"target_pk":target_pk,"source":source,"source_model":source_model,"alarm_mobile":alarm_mobile,"is_enabled":is_enabled},
                success:function (data) {
                    if (data.code == 1) {
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            parent.location.href = "/smp/sync?tag="+tag;
                        },1000);
                    } else {
                        top.layer.msg = data.msg;
                    }
                }
            });
        };
    </script>
</head>
<body>

        <detail>
            <form>
            <h2>编辑数据同步（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
            <hr/>
            <table>
                <tr>
                    <td>同步类型</td>
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
                    <td>名称*</td>
                    <td><input type="text" id="name" value="${syn.name!}"/></td>
                </tr>
                <tr>
                    <td>tag*</td>
                    <td><input type="text" id="tag" value="${syn.tag!}"/></td>
                </tr>
                <tr>
                    <td>间隔时间(s)</td>
                    <td><input type="text" id="interval" value="${syn.interval!}"/></td>
                </tr>
                <tr>
                    <td>目标数据</td>
                    <td><input type="text" id="target" value="${syn.target!}"/>（数据表::tag.key.table）</td>
                </tr>
                <tr>
                    <td>目标数据主键</td>
                    <td><input type="text" id="target_pk" value="${syn.target_pk!}"/></td>
                </tr>
                <tr>
                    <td>来源数据</td>
                    <td><input type="text" id="source" value="${syn.source!}"/>（数据表::tag.key.table）</td>
                </tr>
                <tr>
                    <td>来源数据模型</td>
                    <td><textarea id="source_model">${syn.source_model!}</textarea></td>
                </tr>
                <tr>
                    <td>报警手机<br/></td>
                    <td><input type="text" id="alarm_mobile" value="${syn.alarm_mobile!}" class="longtxt" /></td>
                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(syn.is_enabled = 1)?string("checked","")}><a></a></label>
                        </switcher>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <#if is_admin = 1>
                            <button type="button" onclick="saveEdit()">保存</button>
                        </#if>
                    </td>
                </tr>
            </table>
            </form>
        </detail>
</body>
</html>