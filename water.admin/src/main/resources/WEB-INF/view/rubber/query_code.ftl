<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 代码查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${css}/codemirror/defined.css"/>
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.css"/>
    <link rel="stylesheet" href="${css}/codemirror/fullscreen.css"/>
    <script src="${js}/lib.js"></script>
    <script src="https://static.kdz6.cn/lib/codemirror.min.js"></script>
    <script src="${js}/codemirror/etl.js"></script>
    <script src="${js}/codemirror/fullscreen.js"></script>
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
    <style type="text/css">
        .CodeMirror{
            border: 1px solid #C9C9C9;font-size:20px;
            position: fixed;
            top: 60px; left: 10px; right: 10px; bottom: 10px;
            height: auto;
            z-index: 9;
        }
    </style>
</head>
<body>
<main>
        <toolbar>
            <cell>
                <button type='button' onclick="rollback()">返回</button>
            </cell>
        </toolbar>
        <textarea id="code">${code!}</textarea>
</main>
<@footer/>

</main>

<script>
    var editor=CodeMirror.fromTextArea(document.getElementById("code"), {
        //高亮显示
        mode: "text/javascript",

        //显示行号
        lineNumbers: true,
        //在缩进时，是否需要把 n*tab宽度个空格替换成n个tab字符，默认为false 。
        indentWithTabs: true,
        //自动缩进
        smartIndent: true,
        //设置为0，取消光标闪烁。
        cursorHeight: 0,
        readOnly:true,

        //设置主题 css文件为defined
        theme: "eclipse",

    })
    //editor.setSize(1000,500)
</script>
</body>
</html>