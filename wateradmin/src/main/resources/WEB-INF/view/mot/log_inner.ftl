<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志统计</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>
</head>
<body>
    <datagrid  class="list">
        <table>
            <thead>
            <tr>
                <td width="100px" sort="">tag</td>
                <td class="left">logger</td>
                <td width="90px" sort="row_num">总数量</td>
                <td width="90px" sort="row_num_today">今日数量</td>
                <td width="90px" sort="row_num_yesterday">昨日数量</td>
                <td width="90px" sort="row_num_beforeday">前日数量</td>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list loggers as m>
            <tr ${m.isHighlight()?string("class='t4'","")}>
                <td>${m.tag}</td>
                <td style="text-align: left;"><a href="/log/query/inner?logger=${m.logger}&tag_name=${m.tag}" target="_parent">${m.logger}</a></td>
                <td style="text-align: right;">${m.row_num}</td>
                <td style='text-align: right;'>${m.row_num_today}</td>
                <td style='text-align: right;'>${m.row_num_yesterday}</td>
                <td style='text-align: right;'>${m.row_num_beforeday}</td>
                <td class="op">
                    <a href="/mot/speed/charts?tag=logger&name_md5=${m.logger_md5()}&service=waterlog" class="t2">监控</a>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</body>
</html>