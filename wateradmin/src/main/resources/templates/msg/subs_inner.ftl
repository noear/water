<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 订阅列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}
    </style>
    <script>
        function deleteSubs(subscriber_id){
            var ids = "";
            $('[name=sel_id]').each(function(){
                var $m = $(this);

                if($m.prop('checked')){
                    ids+=($m.val()+',');
                }
            });

            if(ids == ''){
                top.layer.msg('请选择消息');
                return;
            }


            top.layer.confirm('确定删除?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/msg/subs/ajax/delete",
                    data:{"ids":ids},
                    success:function(data){
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        function enabledSubs(is_enabled) {
            var ids = "";
            $('[name=sel_id]').each(function(){
                var $m = $(this);

                if($m.prop('checked')){
                    ids+=($m.val()+',');
                }
            });

            if(ids == ''){
                top.layer.msg('请选择消息');
                return;
            }


            var msgtext = '启用';
            if (is_enabled == 0) {
                msgtext = '禁用';
            }
            top.layer.confirm('确定'+msgtext+'?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/msg/subs/ajax/enabled",
                    data:{
                        "ids":ids,
                        "is_enabled":is_enabled
                    },
                    success:function(data){
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        $(function(){
            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });
        });
    </script>
</head>
<body>
<main>
    <#if tabs_visible!false>
        <div class="tabs">
            <tabbar>
                <#list tabs as m>
                    <#if m.tag == name>
                        <a id="e${m.tag}" class="btn sel">${m.tag!}</a>
                    <#else>
                        <a id="e${m.tag}" class="btn" href="/msg/subs/inner?tag_name=${tag_name!}&name=${m.tag!}">${m.tag!}</a>
                    </#if>
                </#list>
            </tabbar>
        </div>
    </#if>

    <toolbar>
        <flex>
            <left class="col-3">
                <#if is_admin == 1>
                    <#if _state == 1>
                        <button class=" mar10-r minor" onclick="enabledSubs(0)">禁用</button>
                    <#else>
                        <button class=" mar10-r minor" onclick="enabledSubs(1)">启用</button>
                        <button class="minor" onclick="deleteSubs()">删除</button>
                    </#if>

                </#if>
            </left>
            <middle  class="col-6 center">
                <form>
                    <input type="text" class="w200"  name="topic_name" placeholder="主题名称 或 接收地址" id="topic_name"/>
                    <input type="hidden"  name="tag_name" value="${tag_name!}" />
                    <input type="hidden"  name="name" value="${name!}" />
                    <button type="submit">查询</button>
                </form>
            </middle>
            <right  class="col-3">
                <@stateselector items="启用,未启用"/>
            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
                <tr>
                    <td width="20"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                    <td class="left">主题名称</td>
                    <td class="left">订阅者的接收地址</td>
                    <td class="left" width="120px">接收方式</td>
                </tr>
            </thead>
            <tbody id="tbody">
            <#list list as sub>
                <tr class="${sub.trClass()}">
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${sub.subscriber_id}" /><a></a></label></checkbox></td>
                    <td class="left break">${sub.topic_name}</td>
                    <td class="left break">${sub.receive_url}（${sub.name!}#${sub.check_last_state!})</td>
                    <td class="left">
                        <#if sub.receive_way==0>HTTP异步等待</#if>
                        <#if sub.receive_way==1>HTTP同步等待</#if>
                        <#if sub.receive_way==2>HTTP异步不等待</#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>