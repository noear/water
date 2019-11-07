<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 公共模版</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script>
        $(function () {
            if ('${tag!}') {
                $('#${tag!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var tagName = '${tag!}';
        function node_onclick(tg,obj) {
            tagName = tg;
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/paas/tml/inner?tag=" + tagName);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list tags as t>
                    <#if t == tag>
                        <li onclick="node_onclick('${t}',this)" id="${t}" class="sel">${t}</li>
                    </#if>
                    <#if t != tag>
                        <li onclick="node_onclick('${t}',this)" id="${t}">${t}</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/paas/tml/inner?tag=${tag!}&tml_name=${tml_name!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
<script>
</script>
</html>