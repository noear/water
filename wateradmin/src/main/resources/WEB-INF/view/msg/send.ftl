<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 消息发送</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/jquery.form.js"></script>
    <script>
        function dist() {
            top.layer.confirm('确定发送该消息', {
                btn: ['确定','取消'] //按钮
            }, function(){
                var broker = $('#broker').val();
                var topic = $('#topic').val();
                var tags = $('#tags').val();
                var message = $('#message').val();

                if (!topic || topic == "") {
                    top.layer.msg("主题不能为空");
                    return;
                }

                if (!message  || message == "") {
                    top.layer.msg("消息内容不能为空");
                    return;
                }

                $.ajax({
                    type:"POST",
                    url:"/msg/send/ajax/dosend",
                    data:{"broker":broker, "topic":topic, "message":message, "tags":tags},
                    success:function(data){
                        top.layer.msg('操作成功');
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

    </script>
</head>
<body>

<main>
    <blockquote>
        <h2 class="ln30">消息发送</h2>
    </blockquote>
    <detail>
        <form>
        <table>
            <tr>
                <th>broker</th>
                <td>
                    <select id="broker" title="broker">
                        <option value=""></option>
                        <#list brokerList as broker>
                            <option value="${broker.tag!}">${broker.tag!}</option>
                        </#list>
                    </select>
                    <script>$('#broker').val('${broker!}')</script>
                    <n>（默认为空）</n>
                </td>
            </tr>
            <tr>
                <th>主题</th>
                <td><input type="text" id="topic" value=""/></td>
            </tr>
            <tr>
                <th>标签</th>
                <td><input type="text" id="tags" value=""/></td>
            </tr>
            <tr>
                <th>消息内容</th>
                <td><textarea  class="longtxt" type="text" id="message" ></textarea></td>
            </tr>
            <#if is_operator == 1>
            <tr>
                <th></th>
                <td><button type="button" onclick="dist()">发送</button></td>
            </tr>
            </#if>
        </table>
        </form>
    </detail>
</main>

</body>
</html>