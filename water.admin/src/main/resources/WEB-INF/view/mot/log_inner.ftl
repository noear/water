<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志统计</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>
</head>
<body>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td width="100px" sort="">tag</td>
                <td width="250px">logger</td>
                <td width="80px" sort="row_num">总数量</td>
                <td width="80px" sort="row_num_today">今日数量</td>
                <td width="80px" sort="row_num_yesterday">昨日数量</td>
                <td width="80px" sort="row_num_beforeday">前日数量</td>
                <td>备注</td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list loggers as m>
            <tr ${m.isHighlight()?string("class='t4'","")}>
                <td>${m.tag}</td>
                <td style="text-align: left;"><a href="/smp/log?tableName=${m.logger}&project=${m.tag}" target="_parent">${m.logger}</a></td>
                <td style="text-align: right;">${m.row_num}</td>
                <td style='text-align: right;'>${m.row_num_today}</td>
                <td style='text-align: right;'>${m.row_num_yesterday}</td>
                <td style='text-align: right;'>${m.row_num_beforeday}</td>
                <td style="text-align:left;word-wrap:break-word;word-break:break-all;!important;">${m.note!}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</body>
</html>