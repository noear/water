<#setting url_escaping_charset='utf-8'>
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 性能监控</title>
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
</head>
<body>
<main>

    <div class="tabs">
        <tabbar>
            <#list tabs as m>
                <#if m.tag == serviceName>
                    <a id="e${m.tag}" class="btn sel">${m.tag}</a>
                <#else>
                    <a id="e${m.tag}" class="btn" href="/mot/speed/inner?tag_name=${tag_name!}&serviceName=${m.tag}">${m.tag}</a>
                </#if>
            </#list>
        </tabbar>
    </div>

    <toolbar>
        <left>
            <form>
                <input type="text" class="w200" name="name" placeholder="接口" id="name"/>
                <input type="hidden" name="serviceName" id="serviceName" value="${serviceName}" />
                <input type="hidden" name="tag" value="${tag!}">
                <button type="submit">查询</button>
            </form>
        </left>
        <right>
            <selector>
                <a class="${(''=tag)?string('sel','')}" href="?serviceName=${serviceName}&tag=">all</a>
                <#list tags as t>
                    <a class="${(t.tag=tag)?string('sel','')}" href="?serviceName=${serviceName}&tag=${t.tag}">${t.tag}(${t.counts})</a>
                </#list>
            </selector>
        </right>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td sort="" class="left">分组::接口</td>
                <td width="80px" sort="total_num">总计数</td>
                <td width="70px" sort="total_num_slow5">异常数</td>
                <td width="100px" sort="gmt_modified">更新时间</td>
                <td width="50px"></td>
            </tr>
            </thead>
            <tbody id="tbody">
            <#list speeds as m>
                <tr ${m.isHighlight()?string("class='t4'","")}>
                    <td class="left break">${m.tag}::${m.name}</td>
                    <td class="right">${m.total_num}</td>
                    <td class="right">${m.total_num_slow5}</td>
                    <td>${m.gmt_modified?string('dd HH:mm')}</td>
                    <td><a href="/mot/speed/charts?tag=${m.tag}&name_md5=${m.name_md5?url}&service=${m.service}" style="color:blue;cursor:pointer;">详情</a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>