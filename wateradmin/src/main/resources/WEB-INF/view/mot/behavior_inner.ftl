<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 行为记录</title>
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
            UrlQueryByDic({
                tag_name:'${tag_name}',
                tagx:$('#tagx').val(),
                log_date:$('#log_date').val(),
                path:$('#path').val()});
        }
    </script>
</head>
<body>

<main>
    <datalist id="datalist" >
        <#list tag2s as m>
            <option value="${m.tag}">${m.tag}</option>
        </#list>
    </datalist>
        <toolbar>
            <left>
                账号：<input type="text"  id="tagx" placeholder="id.name" id="tagx" autocomplete="off" list="datalist" class="w100"/>&nbsp;&nbsp;
                log_date：<input type="text"  id="log_date" placeholder="yyyyMMdd.hh" id="log_date" class="w100"/>&nbsp;&nbsp;
                path：<input type="text"  id="path" placeholder="/"  id="path" class="w150"/>&nbsp;&nbsp;
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
            <td width="90px">操作人</td>
            <td class="left">PATH/代码</td>
            </thead>
            <tbody>
            <#list list as log>
            <tr title="${log.ua!}">
                <td>
                    ${(log.log_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}
                </td>
                <td>${log.operator!}</td>
                <td class="left break">
                    <div>*${log.trace_id!} ${log.path!} (${log.operator_ip!})</div>
                    <div style="font-size: small">${log.schema!}::${log.cmd_sql!}
                    <n-l>$$$ ${log.cmd_arg!}</n-l>
                    </div>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
    <@pagebar pageSize="${pageSize}" rowCount="${rowCount}"/>
</main>

</body>
</html>