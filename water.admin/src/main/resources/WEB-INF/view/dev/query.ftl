<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据库查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.css"/>
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.js"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css" />
    <script src="${js}/lib.js" ></script>
    <style>
        td {
            text-align: center;
        }
        .CodeMirror{
            border: 1px solid #C9C9C9;font-size:20px;
            position: fixed;
            top: 50px; left: 0px; right: 0; bottom: 0;
            height: auto;
            z-index: 9;
        }
    </style>
    <script>
    $(function () {
        $('#btn_query').click(function () {
            $.post("/dev/query/ajax/do",{code:$('#code').val()},function(rst){
                $("#query_rst").html(rst);
            });
        });
    });
    </script>
</head>
<body>

<main>
    <toolbar >
        <cell>
            <input id="code" type="text" style="width: 100%" placeholder="tag/db::sql">
        </cell>
        <cell style="width: 70px;">
            <button id="btn_query" type="button">查询</button>
        </cell>
    </toolbar>
    <datagrid>
        <div id="query_rst" style="overflow-x: scroll"></div>
    </datagrid>
</main>

</body>
</html>
