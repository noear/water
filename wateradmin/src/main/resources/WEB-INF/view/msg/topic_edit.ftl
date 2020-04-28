<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} -主题-新增</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveAdd() {
            var topic_name = $('#topic_name').val();
            var topic_id = ${topic.topic_id};
            var max_msg_num = $('#max_msg_num').val();
            var max_distribution_num = $('#max_distribution_num').val();
            var max_concurrency_num = $('#max_concurrency_num').val();
            var alarm_model = $('#alarm_model').val();

            if(isNaN(max_msg_num)||max_msg_num<0){
                top.layer.msg("最大消息数量必须为大于等于0的数字");
                return;
            }
            if(isNaN(max_distribution_num)||max_distribution_num<0){
                top.layer.msg("最大派发次数必须为大于等于0的数字");
                return;
            }
            if(isNaN(max_concurrency_num)||max_concurrency_num<0){
                top.layer.msg("最大同时派发数必须为大于等于0的数字");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/msg/topic/edit/ajax/save",
                data:{"topic_name":topic_name,"topic_id":topic_id,"max_msg_num":max_msg_num,"max_distribution_num":max_distribution_num,"max_concurrency_num":max_concurrency_num,"alarm_model":alarm_model},
                success:function (data) {
                    if (data.code == 1) {
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            location.href = "/msg/topic";
                        },1000);
                    } else {
                        top.layer.msg = data.msg;
                    }
                }
            });
        };
    </script>
</head>
<body>
<main>
    <blockquote>
        <h2 class="ln30">主题编辑</h2>
    </blockquote>
    <detail>
        <form>
            <table>
                <tr>
                    <th style="width: 150px;">主题名称</th>
                    <td><input type="text" id="topic_name" value="${topic.topic_name!}" ${(topic.topic_id>0)?string('disabled','')} /></td>
                </tr>
                <tr>
                    <th>最大消息数量</th>
                    <td><input type="text" id="max_msg_num" value="${topic.max_msg_num}"/>（0, 表示不限）</td>
                </tr>
                <tr>
                    <th>最大派发次数</th>
                    <td><input type="text" id="max_distribution_num" value="${topic.max_distribution_num}"/></td>
                </tr>
                <tr>
                    <th>最大同时派发数</th>
                    <td><input type="text" id="max_concurrency_num" value="${topic.max_concurrency_num}"/>（0, 表示不限）</td>
                </tr>
                <tr>
                    <th>报警模式</th>
                    <td>
                        <select id="alarm_model">
                            <#if topic.alarm_model == 0>
                                <option value="0" selected="selected">普通模式</option>
                                <option value="1">不报警</option>
                            </#if>
                            <#if topic.alarm_model == 1>
                                <option value="0">普通模式</option>
                                <option value="1" selected="selected">不报警</option>
                            </#if>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td><button type='button' onclick="saveAdd()">保存</button></td>
                </tr>
            </table>
        </form>
    </detail>
</main>
</body>
</html>