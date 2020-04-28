<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 代码生成</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <style>
    </style>
    <script>
        $(function () {
            if ('${tag_name!}') {
                $('#${tag_name}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });
        var tagName = '${tag_name!}';
        function node_onclick(tag_name,obj) {
            tagName = tag_name;
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/dev/code/inner/"+tagName);
        }
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
        <iframe src="/dev/code/inner/${tag_name!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>