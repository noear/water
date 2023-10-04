<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 存储监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-4">
            </left>
            <middle class="col-4 center">
                <input type="text" class="w200" name="name" placeholder="实例" id="name"/>
                <button type="button" onclick="UrlQueryBy('name',$('#name').val())">查询</button>
            </middle>
            <right class="col-4">
                <select name="tag_name" id="tag_name" onchange="UrlQueryBy('tag_name',$(this).val())">
                    <#list tags as m>
                        <option value="${m.tag}">${m.note} (${m.counts})</option>
                    </#list>
                </select>
            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td sort="" class="left">实例</td>
                <td sort="connect_usage" width="70px">连接数</td>
                <td sort="cpu_usage" width="70px">CPU</td>
                <td sort="memory_usage" width="70px">内存</td>
                <td sort="disk_usage" width="70px">硬盘</td>
                <td sort="gmt_modified"  width="100px">更新时间</td>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody>
            <#list list as m>
            <tr>
                <td class="left">${m.tag!}::${m.name!}<n> ::${m.iaas_type_str()}</n><n>（${m.iaas_key!} ::${m.iaas_attrs!}）</n></td>
                <td class="right${(m.connect_usage>50)?string(" t4","")}">${m.connect_usage}%</td>
                <td class="right${(m.cpu_usage>50)?string(" t4","")}">${m.cpu_usage}%</td>
                <td class="right${(m.memory_usage>70)?string(" t4","")}">${m.memory_usage}%</td>
                <td class="right${(m.disk_usage>60)?string(" t4","")}">${m.disk_usage}%</td>
                <td>${(m.gmt_modified?string('dd HH:mm:ss'))!}</td>
                <td><a href="dbs/inner?instanceId=${m.iaas_key!}&type=${m.iaas_type!}&name=${m.name!}" class="t2">详情</a></td>
            </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>