<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 性能监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="//mirror.noear.org/lib/echarts.min.js" async="async"></script><!-- 起到缓存作用 -->
    <script>
        $(function () {
            if (${serviceName!}) {
                $('#${serviceName!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });

        var serviceName = '${serviceName!}';
        function node_onclick(serviceName,obj) {
            serviceName = serviceName
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/mot/speed/inner?serviceName="+serviceName);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list services as m>
                    <#if m.service == serviceName>
                        <li onclick="node_onclick('${m.service}',this)" id="${m.service}" class="sel">${m.service} (${m.counts})</li>
                    </#if>
                    <#if m.service != serviceName>
                        <li onclick="node_onclick('${m.service}',this)" id="${m.service}">${m.service} (${m.counts})</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/mot/speed/inner?serviceName=${serviceName!s}" frameborder="0" id="table"></iframe>
    </right>
</main>

</body>
</html>