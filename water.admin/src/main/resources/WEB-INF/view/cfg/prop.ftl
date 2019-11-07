<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 配置列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script>
        $(function () {
            if ('${tag!}') {
                $('#${tag}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });
        var tagName = '${tag}';
        function node_onclick(tag,obj) {
            tagName = tag
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/cfg/prop/inner?tag="+tagName);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
                <ul>
                    <#list resp as m>
                        <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}">${m.tag} (${m.counts})</li>
                    </#list>
                </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/cfg/prop/inner?tag=${tag!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>