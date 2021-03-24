<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 代码查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <script src="${js}/jteditor.js"></script>
    <style type="text/css">
        pre{border:1px solid #C9C9C9;
            width: calc(100vw - 20px);
            height: calc(100vh - 30px  - 50px);}
    </style>
    <script>
        function rollback() {
            if(history.length>0) {
                history.back(-1);
            }else{
                self.opener=null;
                self.close();
            }
        };

    </script>

</head>
<body>
<main>
    <toolbar>
        <button type='button' onclick="rollback()">返回</button>
    </toolbar>
    <pre jt-js id="code">${code!}</pre>
    <script>
        window.editor = build_editor(document.getElementById('code'),'javascript');
    </script>
</main>

</main>
</body>
</html>