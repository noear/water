<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 发起部署</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>

        $(function (){
            setToBottom();
        });

        function setToBottom(){
            $('#log').scrollTop( $('#log')[0].scrollHeight);
        }

        function doOperate(task_id, node_id) {
            $.ajax({
                type:"POST",
                url:"/ops/deploy/ajax/exec",
                data:{
                    "task_id":task_id,
                    "node_id":node_id
                },
                success:function(data){
                    if(data.code==1){
                        setInterval(function(){
                            freshLog(task_id);
                        }, 3000);
                    }
                    top.layer.msg(data.msg);
                    // setTimeout(function(){
                    //     location.reload();
                    // },1000);
                }
            });
        }

        var lastId = "${lastId!}";

        function freshLog(task_id) {
            if (lastId==null || lastId == "") {
                lastId = 0;
            }
            $.get('/ops/deploy/ajax/getLog?task_id='+task_id+'&flow_id='+lastId, function (data) {

                if (data.isEnd == 1) {
                    location.reload();
                    return;
                }

                var logs = JSON.parse(data.logs);
                for(var i in logs){
                    var row = "<a>"+logs[i].note+"</a><br>";
                    $('#log code').append(row);
                    lastId = logs[i].flow_id;
                    setToBottom();
                }
            })
        }

    </script>
    <style>
        #log {height: 400px; overflow: scroll; scrollbar-face-color:#70807d; scrollbar-arrow-color:#ffffff; scrollbar-highlight-color:#ffffff; scrollbar-3dlight-color:#70807d; scrollbar-shadow-color:#ffffff; scrollbar-darkshadow-color:#70807d; scrollbar-track-color:#ffffff;}
        #buttonBlock{text-align: center}
        #buttonBlock button{margin-bottom: 30px}
    </style>
</head>
<body>
<detail>
    <toolbar>
        <cell>
              <div>项目：${task.project_name} = ${project.note} /${task.version}（<a onclick="history.back(-1)" class="t2">返回</a>）</div>
              <n>说明：</n><a>${task.note}</a>
        </cell>
    </toolbar>
    <br>
        <flex>
            <block id="log" class="col-10">
                <a id="top-head">操作记录</a>
                <br>
                <br>
                <code>
                    <#list logs as log>
                        <a style="word-wrap: break-word;word-break: break-all">${log.note}</a><br/>
                    </#list>
                </code>
            </block>
            <div id="buttonBlock" class="col-2">
                <#list buttons as button_key,button_value>
                    <button onclick="doOperate('${task.task_id}','${button_value}')">${button_key}</button>
                </#list>
            </div>
        </flex>
</detail>
</body>
</html>