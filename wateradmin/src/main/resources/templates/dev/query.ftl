<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 数据库查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css" />
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js" ></script>
    <style>
        td {
            text-align: center;
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
        <left style="width:calc(100vw - 110px)">
            <input id="code" type="text" style="width: 100%" placeholder="tag/db::sql">
        </left>
        <right>
            <button id="btn_query" type="button">查询</button>
        </right>
    </toolbar>
    <datagrid>
        <div id="query_rst" style="overflow-x: scroll"></div>
    </datagrid>
</main>

</body>
</html>
