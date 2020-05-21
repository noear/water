<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 异常记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function fresh() {
            location.reload();
        };

        function act(act) {
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

            $.ajax({
                type:"POST",
                url:"/msg/ajax/" + act,
                data:{"ids":ids},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        //派发
        function distribute(){
            act('distribute');
        };

        //修复订阅
        function repairSubs(){
            act('repair');
        };

        //取消派发
        function cancelSend() {
            act('cancelSend');
        }

        function search(){
            var key = $('#key').val();
            if(key){
                urlQueryByDic({'key':key});
            }
        }

        $(function(){
            $("#key").keydown(function(e){
                if(e.keyCode==13){
                    search();
                }
            });

            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });
        });
    </script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-4">
                <button type='button' onclick="fresh()">刷新</button>
                <#if _m!=2>
                    <button type='button' class="edit mar10-l" onclick="distribute()" >立即派发</button>
                    <button type="button" class="minor  mar10-l" onclick="cancelSend()">取消</button>
                    <button type="button" class="minor  mar10-l" onclick="repairSubs()">修复</button>
                </#if>
            </left>
            <middle class="col-4 center">
                <input type="text" id="key" value="${key!}" placeholder="ID or Topic" class="w200"/>&nbsp;&nbsp;
                <button type='button' onclick="search()">查询</button>
            </middle>
            <right class="col-4">
                <selector>
                    <a class="${(_m =0)?string('sel','')}" href="?_m=0">异常的</a>
                    <a class="${(_m =1)?string('sel','')}" href="?_m=1">处理中</a>
                    <a class="${(_m =2)?string('sel','')}" href="?_m=2">已成功</a>
                    <a class="${(_m =3)?string('sel','')}" href="?_m=3">其它的</a>
                </selector>
            </right>
        </flex>
    </toolbar>

    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                <td width="70px">消息ID</td>
                <td class="left">消息主题</td>
                <td class="left">内容</td>
                <td class="left" width="60px">下次<br/>时间</td>
                <td class="left" width="40px">已派<br/>次数</td>
                <td class="left" width="120px">发起时间</td>
                <#if is_admin == 1>
                    <td width="50px">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list list as msg>
                <tr>
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${msg.msg_id}" /><a></a></label></checkbox></td>
                    <td>${msg.msg_id}</td>
                    <td class="left">${msg.topic_name}</td>
                    <td class="left break">${msg.content}</td>
                    <td>${msg.nexttime(currTime)}</td>
                    <td>${msg.dist_count}</td>
                    <td class="left">${msg.log_fulltime?string('MM-dd HH:mm:ss')}</td>
                    <#if is_admin == 1>
                        <td class="op">
                            <a href="/log/query/inner?tag_name=water&logger=water_log_msg&level=5&tagx=@${msg.msg_id}" class="t2">日志</a>
                        </td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</main>
</body>
</html>