<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        $(function () {
            document.getElementById('source').value="${monitor.source!}";
        });

        function saveEdit() {
            var monitor_id = ${monitor.monitor_id}
            var name = $('#name').val();
            var tag = $('#tag').val();
            var type = $('#type').val();
            var source = $('#source').val();
            var source_model = $('#source_model').val();
            var rule = $('#rule').val();
            var task_tag_exp = $('#task_tag_exp').val();
            var alarm_mobile = $('#alarm_mobile').val();
            var alarm_exp = $('#alarm_exp').val();
            var alarm_sign = $('#alarm_sign').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;

            if (name == null || name == "" || name == undefined) {
                top.layer.msg("名称不能为空");
                return;
            }

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("tag不能为空");
                return;
            }

            //验证手机
            if(alarm_mobile == null || alarm_mobile == "" || alarm_mobile == undefined) {
               //允许为空
            }else{
                //非空时判断格式
                var reg = "1[345780]\\d{9}(?=,|$)";
                var re = new RegExp(reg);
                if (re.test(alarm_mobile)) {
                }
                else {
                    top.layer.msg('请输入正确的手机号！');
                    return;
                }
            }

            $.ajax({
                type:"POST",
                url:"/smp/monitor/edit/ajax/save",
                data:{"monitor_id":monitor_id,"name":name,"tag":tag,"type":type,"source":source,"source_model":source_model,"rule":rule,"source":source,"task_tag_exp":task_tag_exp,"alarm_mobile":alarm_mobile,"alarm_exp":alarm_exp,"is_enabled":is_enabled,"alarm_sign":alarm_sign},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/smp/monitor?tag="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>
        <detail>
            <form>
            <h2>编辑数据监视（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
            <hr/>
            <table>
                <tr>
                    <td>tag*</td>
                    <td><input type="text" id="tag" value = "${monitor.tag!}"/></td>
                </tr>
                <tr>
                    <td>项目名称*</td>
                    <td><input type="text" id="name" value = "${monitor.name!}"/></td>
                </tr>
                <tr>
                    <td>监视类型</td>
                    <td>
                        <select id="type" >
                            <#if monitor.type=0>
                                <option value="0" selected="selected">数据表预警</option>
                                <option value="1">数据简报</option>
                            </#if>
                            <#if monitor.type=1>
                                <option value="0">数据表预警</option>
                                <option value="1" selected="selected">数据简报</option>
                            </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>数据源</td>
                    <td>
                        <select id="source">
                            <option value=""></option>
                            <#list option_sources as sss>
                                <option value="${sss}">${sss}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>数据采集</td>
                    <td><textarea id="source_model" >${monitor.source_model!}</textarea></td>
                </tr>
                <tr>
                    <td>触发规则</td>
                    <td><textarea id="rule">${monitor.rule!}</textarea></td>
                </tr>
                <tr>
                    <td>监视标签说明</td>
                    <td><input type="text" id="task_tag_exp" class="longtxt" value="${monitor.task_tag_exp!}" /></td>
                </tr>

                <tr>
                    <td>报警手机<br/></td>
                    <td><input type="text" id="alarm_mobile" class="longtxt" value = "${monitor.alarm_mobile!}"/>（多个手机号用逗号分隔）</td>
                </tr>
                <tr>
                    <td>报警签名<br/></td>
                    <td><input type="text" id="alarm_sign"  value = "${monitor.alarm_sign!}"/></td>
                </tr>
                <tr>
                    <td>报警说明</td>
                    <td><textarea id="alarm_exp">${monitor.alarm_exp!}</textarea></td>
                </tr>
                <tr>
                    <td>是否启用</td>
                    <td>
                        <switcher>
                            <label><input id="is_enabled" type="checkbox" ${(monitor.is_enabled=1)?string("checked","")}><a></a></label>
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