<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 消息调试</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/jquery.form.js"></script>
    <script>
        function debug() {
            top.layer.confirm('确定测试该消息', {
                btn: ['确定','取消'] //按钮
            }, function(){
                var id = $('#msg_id').val();
                var msg_key = $('#msg_key').val();
                var topic_name = $('#topic_name').val();
                var dist_count = $('#dist_count').val();
                var content = $('#content').val();
                var access_key = $('#access_key').val();
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
                if (access_key == null || access_key == "" || access_key == undefined) {
                    top.layer.msg("access_key不能为空");
                    return;
                }
                if (url == null || url == "" || url == undefined) {
                    top.layer.msg("订阅地址不能为空");
                    return;
                }

                //后台提交表单
               /* $.ajax({
                    type:"POST",
                    url:"/msg/debug/ajax/submitDebug",
                    data:{"id":id,"msg_key":msg_key,"topic_name":topic_name,"dist_count":dist_count,"content":content,"url":url},
                    success:function(data){
                        top.layer.msg(data);
                    }
                });*/

                $.ajax({
                    type:"POST",
                    url:"/msg/debug/ajax/getSign",
                    data:{"id":id,"msg_key":msg_key,"topic_name":topic_name,"dist_count":dist_count,"content":content,"access_key":access_key},
                    success:function(data){
                        $("#msgForm").attr("action", url);
                        $('#id').val(id);
                        $('#key').val(msg_key);
                        $('#topic').val(topic_name);
                        $('#times').val(dist_count);
                        $('#message').val(data.message);
                        $('#sgin').val(data.sign);

                        $("#msgForm").ajaxSubmit(function(data) {
                            // 提交成功后处理，message为提交页面的返回内容
                            top.layer.msg(data);
                        });
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
                <left>
                    <form>
                        ID/key：<input type="text" name="key" value="${key!}" style="width: 275px;"/>&nbsp;&nbsp;&nbsp;
                        <button type="submit">查询</button>
                    </form>
                </left>
            </toolbar>
            <detail>
                <form>
                <table>
                    <tr>
                        <td>消息ID</td>
                        <td><input type="text" id="msg_id" value="${msg.msg_id}"/></td>
                    </tr>
                    <tr>
                        <td>消息key</td>
                        <td><input type="text" id="msg_key" value="${msg.msg_key!}"/></td>
                    </tr>
                    <tr>
                        <td>主题</td>
                        <td><input type="text" id="topic_name" value="${msg.topic_name!}"/></td>
                    </tr>
                    <tr>
                        <td>已派次数</td>
                        <td><input type="text" id="dist_count" value="${msg.dist_count!}"/></td>
                    </tr>
                    <tr>
                        <td>内容</td>
                        <td><textarea id="content">${msg.content!}</textarea></td>
                    </tr>
                    <tr>
                        <td>签名密钥</td>
                        <td><input type="text" id="access_key" value="${sub.access_key!}"/></td>
                    </tr>
                    <tr>
                        <td>接收地址</td>
                        <td><input  class="longtxt" type="text" id="url" value="${sub.receive_url!}"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><button type="button" onclick="debug()">测试</button></td>
                    </tr>
                </table>
                </form>
            </detail>

    </main>
    <form id="msgForm"  method="post" style="display: none;">
        <input name="id" id="id">
        <input name="key" id="key">
        <input name="topic" id="topic">
        <input name="times" id="times">
        <input name="message" id="message">
        <input name="sgin" id="sgin">
    </form>
</body>
</html>