<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function queryForm() {
            location.href = "/log/query/inner?logger="+$('#logger').val()+"&tag_name=${tag_name}";
        }

        function queryTag() {
            location.href = "/log/query/inner?tag_name=${tag_name}";
        }
    </script>
    <style>
        body > header agroup{font-size: 16px;}
    </style>
</head>
<body>

<main>
    <toolbar>
        <left>
            <form accept-charset="UTF-8">
                <input type="hidden" name="tag_name" value="${tag_name}">
                <select id="logger" name="logger" onchange="queryForm();">
                    <option value="">选择服务日志</option>
                    <#list logs as m>
                        <option value="${m.logger}">${m.logger} (${m.row_num})</option>
                    </#list>
                </select>&nbsp;&nbsp;
                标签：<input type="text" class="w200"  name="tagx" placeholder="tag@tag1@tag2@tag3" id="tagx"/>&nbsp;&nbsp;
                时间：<input type="text"  name="log_date" placeholder="yyyyMMdd" id="log_date" class="w70"/>&nbsp;&nbsp;
                ID：<input type="text"  name="log_id" placeholder="" id="log_id" class="w70"/>&nbsp;&nbsp;
                <button type="submit">查询</button>
                <script>
                    <#if log??>
                    $('#logger').val('${logger!}');
                    </#if>
                </script>
            </form>
        </left>
        <right>
            <@stateselector stateKey="level" items="ALL,TRACE,DEBUG,INFO,WARN,ERROR"/>
        </right>

    </toolbar>



    <div id="content">
        <#list list as log>
            <div class="break">
                <span>${log.log_fulltime?string('yyyy-MM-dd HH:mm:ss')}</span>
                <span>:</span>
                <span>${log.log_id}-${log.levelHtml()}#${log.tag}@${log.tag1}@${log.tag2}@${log.tag3}#${log.from!}</span>
            </div>
            <div class="break">
                <span>${log.log_fulltime?string('yyyy-MM-dd HH:mm:ss')}</span>
                <span>:</span>
                <span>${log.summaryHtml()}</span>
            </div>
            <div class="break">
                <span>${log.log_fulltime?string('yyyy-MM-dd HH:mm:ss')}</span>
                <span>:</span>
                <span>${log.contentHtml()}</span>
            </div>
            <br>
        </#list>
    </div>
</main>

</body>
</html>