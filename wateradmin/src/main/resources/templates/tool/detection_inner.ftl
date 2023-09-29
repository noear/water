<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据监视</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>
    <script>
        function del(act,hint){
            var vm = formToMap(".sel_from");

            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            if(confirm("确定要"+hint+"吗？") == false) {
                return;
            }

            $.ajax({
                type:"POST",
                url:"ajax/batch",
                data:{act: act, ids: vm.sel_id},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function(){
            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });
        });
    </script>
</head>
<body>

<toolbar>
    <flex>
        <left class="col-4">
            <#if is_admin = 1>
                <#if _state == 0>
                    <button type='button' class="minor" onclick="del(0,'禁用')" >禁用</button>
                <#else>
                    <button type='button' class="minor" onclick="del(1,'启用')" >启用</button>
                </#if>

                <a class="btn edit mar10-l" href="/tool/detection/edit?tag=${tag_name!}&detection_id=0" >新增</a>
            </#if>
        </left>
        <middle class="col-4 center">
            <form>
                <input type="text"  class="w200" name="detection_name" placeholder="项目名称" />
                <input type="hidden"  name="tag_name" value="${tag_name}"/>
                <button type="submit">查询</button>
            </form>
        </middle>
        <right class="col-4">
            <@stateselector items="启用,未启用"/>
        </right>
    </flex>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
            <td class="left">应用名称</td>
            <td width="80px">检测间隔</td>
            <td width="120px">检测情况</td>
            <td width="130px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as m>
            <#if m.check_last_state == 1>
            <tr style="color: red">
            <#else>
                <tr>
            </#if>
            <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.detection_id}" /><a></a></label></checkbox></td>
            <td class="left">
                <div>${m.name!}</div>
                <n-l>${m.protocol!}://${m.address!}</n-l>
            </td>
            <td>
                ${m.check_interval}s
            </td>
            <td style='${m.isAlarm()?string("color:red","")}'>
                ${(m.check_last_time?string('dd HH:mm'))!}
                <#if m.check_last_state == 0>
                    - ok
                <#else>
                    - no
                </#if>
            </td>

            <td class="op">
                <a href="/tool/detection/edit?detection_id=${m.detection_id}" class="t2" ><#if is_admin = 1>编辑<#else>查看</#if></a>
                |
                <a href="/log/query/inner?tag_name=water&logger=water_log_sev&level=0&tagx=detapp@${m.detection_id}" target="_parent" class="t2">日志</a>
                |
                <a href="/mot/speed/charts?tag=${m.tag}&name_md5=${m.track_md5()}&service=_waterdet" class="t2">监控</a>
            </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>