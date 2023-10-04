<!doctype html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>源码：${m1.path!}</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/base64.js" ></script>
    <script src="${js}/jtadmin.js?v=4"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/monaco-editor/min/vs/loader.js" ></script>
<#--    <script src="//cdn.jsdelivr.net/npm/monaco-editor@0.25.2/min/vs/loader.js" ></script>-->

    <style>
        html,body{margin:0px;padding:0px;overflow:hidden;}
        main{margin:10px;}
        main > pre{border:1px solid #C9C9C9; margin: 0px;}

        em{color:#999;font-style:normal;}
    </style>
    <script>
        var base64 = new Base64();

        function file_save() {
            var fc = window.editor.getValue();
            var fc64 = base64.encode(fc);

            ajaxPost({url:"./code/ajax/save", data:{'fc64':fc64, 'id':${id}, 'path':'${m1.path!}'}});
        }
        <#if is_admin = 1>
        ctl_s_save_bind(document, file_save);
        </#if>
    </script>
</head>
<body>
<main>
    <pre id="editor" style="height: calc(100vh - 80px); "></pre>

    <flex style="margin-top: 18px;">
        <left class="col-6 form">
            <#if is_admin = 1>
                <button type="button" onclick="file_save()">保存 <u>S</u></button>
            </#if>
            <#if is_operator = 1>
            <a href="${faas_uri}${m1.path!}?_debug=1" class="btn minor mar10-l" onclick="return confirm('确定要调试吗？')" target="_blank">调试</a>
            </#if>
            <n class="sml mar10-l"> 编辑器的脚本库首次加载会比较慢...</n>
        </left>
        <right class="col-6">
            <@versions table="luffy_file" keyName="file_id" keyValue="${m1.file_id}">
                window.editor.setValue(m.content);
            </@versions>
        </right>
    </flex>


    <script>
        var code64 = "${code64}";

        //require.config({ paths: { 'vs': '//cdn.jsdelivr.net/npm/monaco-editor@0.25.2/min/vs' }});
        require.config({ paths: { 'vs': '${js}/monaco-editor/min/vs' }});
        require(['vs/editor/editor.main'], function() {
            $.get("/_luffy.d.txt?v=1",(rst)=>{
                monaco.languages.typescript.javascriptDefaults.addExtraLib(rst);
            });

            window.editor = monaco.editor.create(document.getElementById('editor'), {
                value:base64.decode(code64),
                language: '${edit_mode}'
            });
        });
    </script>
</main>
</body>
</html>