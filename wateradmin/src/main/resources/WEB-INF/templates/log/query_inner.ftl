<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志查询</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/laydate/laydate.js"></script>

    <script>
        function queryForm() {
            location.href = "/log/query/inner?logger="+$('#logger').val()+"&tag_name=${tag_name}";
        }

        function queryTag() {
            location.href = "/log/query/inner?tag_name=${tag_name}";
        }

        function queryDo(startId){
            if(!startId){
                startId=0;
            }

            urlQueryByDic({startId:startId});
        }

        $(function (){
            $(".log a").click(function (){
                let tagx = $(this).attr('tagx');
                if(tagx){
                    urlQueryBy("tagx",tagx,'page');
                    return
                }

                let time = $(this).attr('time');
                if(time){
                    urlQueryBy("time",time,'page');
                    return;
                }
            });

            bindQueryStringAndSort();
        });
    </script>
    <style>
        body > header agroup{font-size: 16px;}

        .level5,.level5 a{color:red;}
        .level4,.level4 a{color:orange;}
        .level3,.level3 a{color:green;}
        .level2,.level2 a{color:blue;}
        .level1,.level1 a{color:#666;}

        .log a{text-decoration:underline; cursor: default;}
    </style>
</head>
<body>

<main>

    <toolbar>
        <form accept-charset="UTF-8">
            <div>
                <left>

                    <input type="hidden" name="tag_name" value="${tag_name}">
                    <select id="logger" name="logger" onchange="queryForm();">
                        <option value="">选择日志器</option>
                        <#list logs as m>
                            <option value="${m.logger}">${m.logger} (${m.row_num_today})</option>
                        </#list>
                    </select>

                    <script>
                        <#if log??>
                        $('#logger').val('${logger!}');
                        </#if>
                    </script>

                    <input type="text"  name="time"  id="time"
                           jt-laydate="datetime"
                           placeholder="yyyy-MM-dd HH:mm:ss"
                           autocomplete="off"
                           class="w150 sml"/>

                    <input type="text" name="tagx" id="tagx"
                           class="w350 sml"
                           placeholder="Tag@Tag1@Tag2@Tag3@Tag4 or *TraceId ${allowSearch?string('or $Key','')}" />

                    <button type="submit">查询</button>

                </left>
                <right class="sml">
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

    <#if pageSize == listSize>
        <div class="center mar15">
            <a onclick="queryDo(${lastId!0})" class="btn">下一页</a>
        </div>
    </#if>
</main>

</body>
</html>