<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 异常记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/clipboard.min.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        /* tooltip */
        #tooltip{
            position:absolute;
            border:1px solid #aaa;
            background:#eee;
            padding:1px;
            color:#333;
            display:none;
            font-size: small;
        }

        .msg a{text-decoration:underline; cursor: default;}
    </style>
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
            var broker = $('#broker').val();
            if(key || broker){
                urlQueryByDic({'key':key, 'broker':broker});
            }
        }

        function selectorClick(m){
            var broker = $('#broker').val();

            urlQueryByDic({'_m':m, 'broker':broker});
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

            //复制功能
            let clipboard1 = new Clipboard('.dct');
            clipboard1.on('success', function(e) {
                top.layer.msg("复制成功，去粘贴吧");
            });

            $(".msg a").click(function (){
                let tagx = $(this).attr('tagx');
                if(tagx){
                    $('#key').val(tagx);
                    search();
                }
            });
        });

        $(function(){
            var x = 10;
            var y = 20;
            $("tr[title]").mouseover(function(e){
                this.myTitle = this.title.replace(/；/g,"<br/>")
                this.title = "";
                var tooltip = "<div id='tooltip'>"+ this.myTitle +"<\/div>"; //创建 div 元素 文字提示
                $("body").append(tooltip);    //把它追加到文档中
                $("#tooltip").css({
                                    "top": (e.pageY+y) + "px",
                                    "left": (e.pageX+x)  + "px"
                                }).show();      //设置x坐标和y坐标，并且显示
            }).mouseout(function(){
                this.title = this.myTitle;
                $("#tooltip").remove();   //移除
            }).mousemove(function(e){
                $("#tooltip").css({
                        "top": (e.pageY+y) + "px",
                        "left": (e.pageX+x)  + "px"
                    });
            });
        });
    </script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-3">
                <#if _m!=3 && is_admin == 1>
                    <button type="button" class="minor" onclick="cancelSend()">取消</button>
                    <button type="button" class="minor" onclick="repairSubs()">修复</button>
                    <button type='button' class="edit mar10-l" onclick="distribute()" >立即派发</button>
                </#if>
            </left>
            <middle class="col-6 center">
                <select id="broker" class="w100" title="broker">
                    <option value=""></option>
                    <#list brokerList as broker>
                    <option value="${broker.tag!}">${broker.tag!}</option>
                    </#list>
                </select>
                <script>$('#broker').val('${broker!}')</script>
                <input type="text" id="key" value="${key!}" placeholder="*TraceId or ID or Topic or @Tags" class="w250"/>
                <button type='button' onclick="search()">查询</button>
                <button type='button' class="mar10-l" onclick="fresh()">刷新</button>
            </middle>
            <right class="col-3">
                <selector>
                    <a class="${(_m =0)?string('sel','')}" onclick="selectorClick(0)" >异常的</a>
                    <a class="${(_m =1)?string('sel','')}" onclick="selectorClick(1)" >等待中</a>
                    <a class="${(_m =2)?string('sel','')}" onclick="selectorClick(2)" >处理中</a>
                    <a class="${(_m =3)?string('sel','')}" onclick="selectorClick(3)" >已成功</a>
                    <a class="${(_m =4)?string('sel','')}" onclick="selectorClick(4)" >其它的</a>
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
                <td class="left">消息主题 - 标签</td>
                <td class="left">内容</td>
                <td width="60px">下次<br/>时间</td>
                <td class="left" width="40px">已派<br/>次数</td>
                <td class="left" width="120px">发起时间</td>
                <td width="50px">跟踪<br/>标识</td>
                <td width="50px">操作</td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list list as msg>
                <tr class="msg" title="状态代码：${msg.stateStr()}；变更时间：${msg.last_fulltime?string('MM-dd HH:mm:ss')}；跟踪标识：${msg.trace_id!}">
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${msg.msg_id}" /><a></a></label></checkbox></td>
                    <td>${msg.msg_id}</td>
                    <td class="left"><a tagx="${msg.topic_name}">${msg.topic_name}</a>
                    <#if msg.tags?? && msg.tags?length gt 0>
                        - <a tagx="@${msg.tags!}">@${msg.tags!}</a>
                    </#if>
                    </td>
                    <td class="left break">${msg.content}</td>
                    <td>${msg.nexttime(currTime)}</td>
                    <td>${msg.dist_count}</td>
                    <td class="left">${msg.log_fulltime?string('MM-dd HH:mm:ss')}</td>
                    <td class="op">
                        <a class="t2 dct" style="cursor: pointer;" data-clipboard-text="*${msg.trace_id!}" >复制</a>
                    </td>
                    <td class="op">
                        <a href="/log/query/inner?tag_name=water&logger=water_log_msg&level=0&tagx=@${msg.msg_id}" class="t2">日志</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</main>
</body>
</html>