<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志查询</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/laydate/laydate.js"></script>
    <script>
        function queryForm() {
            location.href = "/log/query/inner?logger="+$('#logger').val()+"&tag_name=${tag_name}";
        }

        function queryTag() {
            location.href = "/log/query/inner?tag_name=${tag_name}";
        }

        $(function (){
            $(".log a").click(function (){
                let tagx = $(this).attr('tagx');
                if(tagx){
                    UrlQueryBy("tagx",tagx,'page');
                    return
                }

                let time = $(this).attr('time');
                if(time){
                    UrlQueryBy("time",time,'page');
                    return;
                }
            });
        });
    </script>
    <style>
        body > header agroup{font-size: 16px;}

        .level5,.level5 a{color:red;}
        .level4,.level4 a{color:orange;}
        .level3,.level3 a{color:green;}
        .level2,.level2 a{color:blue;}

        .log a{text-decoration:underline; cursor: default;}
    </style>
</head>
<body>

<main>

    <toolbar>
        <form accept-charset="UTF-8">


            <div class="center">
                <input type="text" class="w350"  name="tagx" placeholder="Tag@Tag1@Tag2@Tag3@Tag4 or *TraceId or $Key" id="tagx"/>&nbsp;&nbsp;
                <button type="submit">查询</button>
            </div>

            <div>
                <left>

                    <input type="hidden" name="tag_name" value="${tag_name}">
                    <select id="logger" name="logger" onchange="queryForm();">
                        <option value="">选择服务日志</option>
                        <#list logs as m>
                            <option value="${m.logger}">${m.logger} (${m.row_num_today})</option>
                        </#list>
                    </select>&nbsp;&nbsp;

                    <input type="text"  name="time"  id="time"
                           jt-laydate="datetime"
                           placeholder="yyyy-MM-dd HH:mm:ss.SSS"
                           autocomplete="off"
                           style="width: 165px;"/>&nbsp;&nbsp;

                    <script>
                        <#if log??>
                        $('#logger').val('${logger!}');
                        </#if>
                    </script>

                </left>
                <right>
                    <@stateselector stateKey="level" items="ALL,TRACE,DEBUG,INFO,WARN,ERROR"/>
                </right>
            </div>
        </form>
    </toolbar>

    <div id="content">
        <#list list as log>
            <div class="break log">
                ${formater.html(log)!}
            </div>
            <br>
        </#list>
    </div>
</main>

</body>
</html>