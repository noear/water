<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 数据模型</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        $(function () {
            if ('${tag!}') {
                $('#${tag}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var tagName = '${tag!}';
        function node_onclick(tag,obj) {
            tagName = tag
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/rubber/model/inner?tag="+tagName+"&f=water");
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list tags as m>
                    <#if m.tag == tag>
                        <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}" class="sel"> ${m.tag} (${m.counts})</li>
                    </#if>
                    <#if m.tag != tag>
                        <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}"> ${m.tag} (${m.counts})</li>
                    </#if>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/rubber/model/inner?tag=${tag!}&model_id=${model_id!}&field_id=${field_id!}&f=water" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>