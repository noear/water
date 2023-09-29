<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 消息调试</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/jquery.form.js"></script>
    <script>
        function debug() {
            top.layer.confirm('确定调试该消息', {
                btn: ['确定','取消'] //按钮
            }, function(){
                var id = $('#msg_id').val();
                var msg_key = $('#msg_key').val();
                var topic_name = $('#topic_name').val();
                var dist_count = $('#dist_count').val();
                var content = $('#content').val();
                var receive_key = $('#receive_key').val();
                var url = $('#url').val();
                if(isNaN(id)||id<0){
                    top.layer.msg("输入大于等于0的消息ID");
                    return;
                }
                if (msg_key == null || msg_key == "" || msg_key == undefined) {
                    top.layer.msg("msg_key不能为空");
                    return;
                }
                if (topic_name == null || topic_name == "" || topic_name == undefined) {
                    top.layer.msg("主题不能为空");
                    return;
                }
                if(isNaN(dist_count)||dist_count<0){
                    top.layer.msg("输入大于等于0的累计派发次数");
                    return;
                }
                if (!receive_key) {
                    top.layer.msg("receive_key不能为空");
                    return;
                }
                if (!url) {
                    top.layer.msg("订阅地址不能为空");
                    return;
                }

                //生成签名，然后用浏览器post（可发给localhost。。。）
                $.ajax({
                    type:"POST",
                    url:"/msg/debug/ajax/getSign",
                    data:{"id":id,"msg_key":msg_key,"topic_name":topic_name,"dist_count":dist_count,"content":content,"receive_key":receive_key},
                    success:function(data){
                        $("#msgForm").attr("action", url);
                        $('#id').val(id);
                        $('#key').val(msg_key);
                        $('#topic').val(topic_name);
                        $('#times').val(dist_count);
                        $('#message').val(data.message);
                        $('#sgin').val(data.sign);

                        $("#msgForm").submit();
                    }
                });

                top.layer.close(top.layer.index);
            });
        };

    </script>
</head>
<body>
    <main>
            <toolbar>
                    <form>
                        <select id="broker" style="width: 110px;" title="broker">
                            <option value=""></option>
                            <#list brokerList as broker>
                                <option value="${broker.tag!}">${broker.tag!}</option>
                            </#list>
                        </select>
                        <script>$('#broker').val('${broker!}')</script>
                        <input type="text" name="key" value="${key!}" placeholder="ID or key" style="width: 300px;"/>
                        <button type="submit">查询</button>
                    </form>
            </toolbar>
            <detail>
                <form>
                <table>
                    <tr>
                        <th style="width: 106px;">消息ID</th>
                        <td><input type="text" id="msg_id" value="${msg.msg_id!}"/></td>
                    </tr>
                    <tr>
                        <th>消息key</th>
                        <td><input type="text" id="msg_key" value="${msg.msg_key!}"/></td>
                    </tr>
                    <tr>
                        <th>主题</th>
                        <td><input type="text" id="topic_name" value="${msg.topic_name!}"/></td>
                    </tr>
                    <tr>
                        <th>已派发次数</th>
                        <td><input type="text" id="dist_count" value="${msg.dist_count!}"/></td>
                    </tr>
                    <tr>
                        <th>内容</th>
                        <td><textarea id="content">${msg.content!}</textarea></td>
                    </tr>
                    <tr>
                        <th>签名密钥</th>
                        <td><input type="text" id="receive_key" value="${sub.receive_key!}"/></td>
                    </tr>
                    <tr>
                        <th>接收地址</th>
                        <td><input  class="longtxt" type="text" id="url" value="${sub.receive_url!}"/></td>
                    </tr>
                    <#if is_operator == 1>
                    <tr>
                        <th></th>
                        <td><button type="button" onclick="debug()">调试</button></td>
                    </tr>
                    </#if>
                </table>
                </form>
            </detail>

    </main>
    <form id="msgForm"  method="post" target="_blank" style="display: none;">
        <input name="id" id="id">
        <input name="key" id="key">
        <input name="topic" id="topic">
        <input name="times" id="times">
        <input name="message" id="message">
        <input name="sgin" id="sgin">
    </form>
</body>
</html>