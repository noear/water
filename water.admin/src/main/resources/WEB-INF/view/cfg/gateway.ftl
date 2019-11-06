<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 网关配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script>
        $(function () {
            if ('${sev_key!}') {
                $('#${sev_key}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var tagName = '${sev_key}';
        function node_onclick(sev_key,obj) {
            tagName = sev_key;
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/cfg/gateway/inner?sev_key=" + tagName);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list sevs as m>
                    <#if m.key == sev_key>
                        <li onclick="node_onclick('${m.key}',this)" id="${m.key}" class="sel">${m.key}</li>
                    </#if>
                    <#if m.key != sev_key>
                        <li onclick="node_onclick('${m.key}',this)" id="${m.key}">${m.key}</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/cfg/gateway/inner?sev_key=${sev_key!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
<script>
</script>
</html>