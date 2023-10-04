<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 上游配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>

    </style>

    <script>

        let gateway_id = ${cfg.gateway_id!0};

        function save() {
            let tag = $("#tag").val();
            let name = $("#name").val();

            if(!tag){
                top.layer.msg("tag不能为空！");
                return;
            }

            if(!name){
                top.layer.msg("service不能为空！");
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            $.ajax({
                type: "POST",
                url: "/sev/gateway/ajax/save",
                data: {
                    gateway_id: gateway_id,
                    tag: tag,
                    name: name,
                    agent: $("#agent").val(),
                    policy: $("#policy").val(),
                    is_enabled:($('#is_enabled').prop("checked")?1:0)
                },
                success: function (data) {
                    if (1 == data.code) {
                        top.layer.msg('操作成功');

                        setTimeout(function () {
                            location.href = "/sev/gateway/inner?tag_name=" + tag  + "&_state=" + _state;
                        }, 800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del(){
            if(gateway_id < 1){
                return;
            }

            let _state = $('#is_enabled').prop('checked') ? 0 : 1;

            let tag = $("#tag").val();

            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/sev/gateway/ajax/del",
                    data:{
                        "gateway_id":gateway_id
                    },
                    success:function(data){
                        if(1==data.code) {
                            top.layer.msg('操作成功');

                            setTimeout(function () {
                                location.href = "/sev/gateway/inner?tag_name=" + tag + "&_state=" + _state;
                            }, 800);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        $(function(){
            ctl_s_save_bind(document,save);
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">上游配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>

<detail>

    <form>
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" autofocus value="${cfg.tag!}"/></td>
            </tr>
            <tr>
                <th>service*</th>
                <td><input type="text" id="name" autofocus value="${cfg.name!}"/></td>
            </tr>
            <tr>
                <th>代理网关</th>
                <td><input type="text" id="agent" value="${cfg.agent!}"/>
                <n-l>比如在k8s环境，可以设为k8s svc；再比如指到某一个特点的节点上；或者指到本地用于调试</n-l>
                </td>
            </tr>
            <tr>
                <th>负载策略</th>
                <td>
                    <select id="policy">
                        <option value="polling" <#if (cfg.policy!) == 'polling'>selected</#if>>轮询</option>
                        <option value="weight" <#if (cfg.policy!) == 'weight'>selected</#if>>权重轮询</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>启用</th>
                <td>
                    <switcher><label><input type="checkbox" id="is_enabled" value="1" ${(cfg.is_enabled = 1)?string("checked","")} /><a></a></label></switcher>
                </td>
            </tr>
        </table>
    </form>
</detail>

</body>
</html>