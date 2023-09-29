<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 负载监控</title>
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
                <input type="text" class="w200"  name="name" placeholder="实例" id="name"/>
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
                <td sort="co_conect_num" width="70px">并发<br/>连接数</td>
                <td sort="new_conect_num" width="70px">新建<br/>连接数</td>
                <td sort="qps" width="70px">QPS</td>
                <td sort="traffic_tx" width="70px">流量<br/>(kbs)</td>
                <td sort="gmt_modified" width="100px">更新时间</td>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody>
            <#list list as m>
                <tr>
                    <td class="left">${m.tag}::${m.name}<n>（${m.iaas_key!} ::${m.iaas_attrs!}）</n></td>
                    <td class="right${(m.co_conect_num>2000)?string(' t4','')}">${m.co_conect_num}</td>
                    <td class="right">${m.new_conect_num}</td>
                    <td class="right">${m.qps}</td>
                    <td class="right">${m.traffic_tx}</td>
                    <td>${m.gmt_modified?string('dd HH:mm:ss')}</td>
                    <td><a href="bls/inner?instanceId=${m.iaas_key}" class="t2">详情</a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>