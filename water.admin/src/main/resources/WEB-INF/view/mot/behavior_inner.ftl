<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 行为记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
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
            <cell>
                账号：<input type="text"  id="tagx" placeholder="id.name" id="tagx" autocomplete="off" list="datalist" style="width: 100px;"/>&nbsp;&nbsp;
                log_date：<input type="text"  id="log_date" placeholder="yyyyMMdd.hh" id="log_date" style="width: 100px;"/>&nbsp;&nbsp;
                path：<input type="text"  id="path" placeholder="/"  id="path" style="width: 100px;"/>&nbsp;&nbsp;
                <button type="button" onclick="do_query()">查询</button>
            </cell>
            <cell>
                <@stateselector items="ALL,SEL,UPD,INS,DEL"/>
            </cell>
        </toolbar>
    <datagrid>
        <table>
            <thead>
            <td width="90px">
                时间
            </td>
            <td width="90px">操作人</td>
            <td>PATH/代码</td>
            </thead>
            <tbody>
            <#list list as log>
            <tr title="${log.ua!}">
                <td>
                    ${(log.log_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}
                </td>
                <td>${log.operator!}</td>
                <td class="left break">
                    <div>${log.path!} (${log.operator_ip!})</div>
                    <div style="font-size: small">${log.schema!}::${log.cmd_sql!} <br/>$$$ ${log.cmd_arg!}</div>
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