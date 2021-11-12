<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 网关配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script>
        $(function () {
            if ('${gateway_id!}') {
                $('#e${gateway_id!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var gateway_id = '${gateway_id!}';
        function node_onclick(tn,obj) {
            gateway_id = tn;
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/cfg/gateway/inner?gateway_id=" + gateway_id);
        };
    </script>
    <style>
        .line1{text-decoration:line-through;}
    </style>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list sevs as m>
                    <#if m.gateway_id == gateway_id>
                        <li class="${(m.is_enabled=0)?string('line1 ','')}" onclick="node_onclick('${m.gateway_id}',this)" id="e${m.gateway_id}" class="sel">${m.name}</li>
                    </#if>
                    <#if m.gateway_id != gateway_id>
                        <li class="${(m.is_enabled=0)?string('line1 ','')}" onclick="node_onclick('${m.gateway_id}',this)" id="e${m.gateway_id}">${m.name}</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/cfg/gateway/inner?gateway_id=${gateway_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
<script>
</script>
</html>