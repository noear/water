<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function queryForm() {
            location.href = "/smp/log?tableName="+$('#tableName').val()+"&project="+$('#project').val();
        }
        function queryTag() {
            location.href = "/smp/log?project="+$('#project').val();
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
                <select id="project" name="project" onchange="queryTag();">
                        <#if project??>
                            <option value="${project}">${project}</option>
                            <#list tags as m>
                                <#if m.tag != project>
                                    <option value="${m.tag}">${m.tag}</option>
                                </#if>
                            </#list>
                        <#else>
                            <option>选择项目</option>
                            <#list tags as m>
                                <option value="${m.tag}">${m.tag}</option>
                            </#list>
                        </#if>
                </select>
                <select id="tableName" name="tableName" onchange="queryForm();">
                        <#if log??>
                            <option value="${log.logger}">${log.logger}</option>
                            <#list logs as logg>
                                <#if log.logger != logg.logger>
                                    <option value="${logg.logger}">${logg.logger}</option>
                                </#if>
                            </#list>
                        <#else>
                            <option value="">选择服务日志</option>
                            <#list logs as log>
                                <option value="${log.logger}">${log.logger}</option>
                            </#list>
                        </#if>
                </select>&nbsp;&nbsp;
                tagx：<input type="text" style="width: 250px;"  name="tagx" placeholder="tag@tag1@tag2" id="tagx"/>&nbsp;&nbsp;
                log_date：<input type="text"  name="log_date" placeholder="log_date" id="log_date" style="width: 100px;"/>&nbsp;&nbsp;
                log_id：<input type="text"  name="log_id" placeholder="log_id" id="log_id" style="width: 100px;"/>&nbsp;&nbsp;
                <button type="submit">查询</button>
            </form>
        </left>
    </toolbar>

    <div id="content">
        <#list list as log>
            <div class="break">
                <span>${log.log_fulltime?string('yyyy-MM-dd HH:mm:ss')}</span>
                <span>:</span>
                <span>${log.log_id}#${log.tag}@${log.tag1}@${log.tag2}</span>
            </div>
            <div class="break">
                <span>${log.log_fulltime?string('yyyy-MM-dd HH:mm:ss')}</span>
                <span>:</span>
                <span>${log.labelHtml()}</span>
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