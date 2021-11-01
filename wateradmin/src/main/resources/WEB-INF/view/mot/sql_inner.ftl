<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - SQL性能</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>

    <style>
        body > header agroup{font-size: 16px;}
    </style>
    <script>
        function do_query() {
            UrlQueryByDic({tag_name:'${tag_name!}',tagx:$('#tagx').val(),log_date:$('#log_date').val()});
        }
    </script>
</head>
<body>

<main>
    <datalist id="datalist">
        <#list tag2s as m>
            <option value="${m.tag}">${m.tag}</option>
        </#list>
    </datalist>

    <toolbar>
        <left>
            秒数：<input type="text"  id="tagx" placeholder="num" id="tagx" autocomplete="off" list="datalist" style="width: 100px;"/>&nbsp;&nbsp;
            log_date：<input type="text"  id="log_date" placeholder="yyyyMMdd.HH" id="log_date" style="width: 100px;"/>&nbsp;&nbsp;

            <button type="button" onclick="do_query()">查询</button>
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
    <div>
        <a>上一页</a>
        <a>下一页</a>
    </div>
</main>

</body>
</html>