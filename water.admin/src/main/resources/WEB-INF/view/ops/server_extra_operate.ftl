<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 操作配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<script>

    function deleteOperate(operate_id,operate_name){
        top.layer.confirm('确定删除操作：'+operate_name+' ?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/ops/server/extra/operate/ajax/delete",
                data:{"operate_id":operate_id},
                success:function(data){
                    top.layer.msg(data.msg);
                    setTimeout(function(){
                        location.reload();
                    },200);
                }
            });
            top.layer.close(top.layer.index);
        });
    };

   function editOperate(operate_id) {
       layer.open({
           type: 2,
           title: '编辑操作',
           shadeClose: true,
           shade: 0.1,
           area: ['555px', '540px'],
           content: '/ops/server/extra/operate/edit?server_id=${server_id!}&operate_id=' + operate_id
       })
   }

</script>
<style>
    select {min-width: 50px}
    datagrid td{min-height: 30px}
    .define_input {text-align: center}
</style>
<body>
<toolbar>
    <cell>
        <div>实例：${instance.instanceName!} = ${instance.instanceId!}（<a onclick="history.back(-1)" class="t2">返回</a>）</div>
        <table>
            <tr>
                <th>配置：</th>
                <td>${instance.cpu!}核/<fmt:formatNumber type="number" value="${instance.memory/1000!}"
                                                       maxFractionDigits="0"/>G/${size!}G
                </td>
                <th>带宽：</th>
                <td>${instance.internetMaxBandwidthOut!}Mbps</td>
                <th>所在区：</th>
                <td>${instance.zoneId!}</td>
            </tr>
        </table>
    </cell>
</toolbar>
<toolbar>
    <cell>
        <button type="button" onclick="editOperate(0)">新增操作</button>
    </cell>
</toolbar>
<datagrid>
    <table>
        <thead>
            <th width="180px">操作名称</th>
            <th>使用脚本</th>
            <th width="60px">排序</th>
            <th width="80px">操作</th>
        </thead>
        <tbody>
        <#list operateList as operate>
        <tr id="col${operate_index+1}">
            <td><a>${operate.name}</a></td>
            <td><a>${operate.script_name}</a></td>
            <td><a>${operate.rank}</a></td>
            <td>
                <a style="cursor: pointer;color: blue;" onclick="editOperate('${operate.operate_id}')">编辑</a><span> | </span>
                <a style="cursor: pointer;color: blue;" onclick="deleteOperate('${operate.operate_id}','${operate.name}')">删除</a>
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>