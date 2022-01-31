<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 计算方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/flow/go.js" async="async"></script>
    <script>
        $(function () {
            if ('${tag_name!}') {
                $('#${tag_name!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var tagName = '${tag_name!}';
        function node_onclick(tag_name,obj) {
            tagName = tag_name
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/rubber/scheme/inner?tag_name="+tagName+"&f=water");
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list tags as m>
                    <#if m.tag == tag_name>
                        <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}" class="sel">${m.tag} (${m.counts})</li>
                    </#if>
                    <#if m.tag != tag_name>
                        <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}">${m.tag} (${m.counts})</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/rubber/scheme/inner?tag_name=${tag_name!}&scheme_id=${scheme_id!}&f=water" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>