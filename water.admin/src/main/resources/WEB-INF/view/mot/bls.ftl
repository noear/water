<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 负载监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
</head>
<body>
<main>
    <toolbar>
        <left>
            实例：<input type="text"  name="name" placeholder="实例名" id="name"/>&nbsp;&nbsp;
            <button type="button" onclick="UrlQueryBy('name',$('#name').val())">查询</button>&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" onclick="location.reload();">刷新</button>&nbsp;&nbsp;
        </left>
        <right>
            <select name="tag_name" id="tag_name" onchange="UrlQueryBy('tag_name',$(this).val())">
                <#list tags as m>
                    <option value="${m.tag}">${m.tag} (${m.counts})</option>
                </#list>
            </select>
        </right>
    </toolbar>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td sort="">实例</td>
                <td sort="co_conect_num" width="70px">并发<br/>连接数</td>
                <td sort="new_conect_num" width="70px">新建<br/>连接数</td>
                <td sort="qps" width="70px">QPS</td>
                <td sort="traffic_tx" width="70px">流量<br/>(kbs)</td>
                <td sort="last_updatetime" width="100px">更新时间</td>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody>
            <#list list as m>
                <tr>
                    <td class="left">${m.tag}::${m.name}<n>（${m.iaas_key}）</n></td>
                    <td class="right${(m.co_conect_num>2000)?string(' t4','')}">${m.co_conect_num}</td>
                    <td class="right">${m.new_conect_num}</td>
                    <td class="right">${m.qps}</td>
                    <td class="right">${m.traffic_tx}</td>
                    <td>${m.last_updatetime?string('dd HH:mm:ss')}</td>
                    <td><a href="bls/inner?id=${m.iaas_key}" class="t2">详情</a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>