<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - SQL性能</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>

    <style>
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}
    </style>
    <script>
        function queryDo(startId) {
            if(!startId){
                startId=0;
            }

            UrlQueryByDic({
                serviceName:'${serviceName!}',
                tagx:$('#tagx').val(),
                log_date:$('#log_date').val(),
                startId:startId
            });
        }
    </script>
</head>
<body>

<datalist id="datalist">
    <#list secondList as m>
        <option value="${m.tag}">${m.tag}</option>
    </#list>
</datalist>


<main>

    <div class="tabs">
        <tabbar>
            <#list tabs as m>
                <#if m.tag == serviceName>
                    <a id="e${m.tag}" class="btn sel">${m.tag}</a>
                <#else>
                    <a id="e${m.tag}" class="btn" href="/mot/sql/inner?tag_name=${tag_name!}&serviceName=${m.tag}">${m.tag}</a>
                </#if>
            </#list>
        </tabbar>
    </div>

    <toolbar>
        <left>
            秒数：<input type="text"  id="tagx" placeholder="num" id="tagx" autocomplete="off" list="datalist" style="width: 100px;"/>&nbsp;&nbsp;
            log_date：<input type="text"  id="log_date" placeholder="yyyyMMdd.HH" id="log_date" style="width: 100px;"/>&nbsp;&nbsp;

            <button type="button" onclick="queryDo()">查询</button>
        </left>
        <right>
            <@stateselector items="ALL,SEL,UPD,INS,DEL"/>
        </right>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <td width="90px">
                时间
            </td>
            <td width="60px">毫秒数</td>
            <td class="left">代码</td>
            </thead>
            <tbody>
            <#list list as log>
                <tr ${(log.log_date<refdate)?string("class='t5'","")}>
                    <td>
                        ${(log.log_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}
                    </td>
                    <td>
                        ${log.weight!0}
                    </td>
                    <td class="left break" style="font-size: small">
                        <div>*${log.trace_id!} (${log.from!})</div>
                        <div style="font-size: small">${log.content!}</div>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
    <#if pageSize == listSize>
        <div class="center mar15">
            <a onclick="queryDo(${lastId!0})" class="btn">下一页</a>
        </div>
    </#if>
</main>

</body>
</html>