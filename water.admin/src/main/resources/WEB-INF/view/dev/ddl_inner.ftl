<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - DDL查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${css}/codemirror/defined.css"/>
    <link rel="stylesheet" href="https://static.kdz6.cn/lib/codemirror.min.css"/>
    <link rel="stylesheet" href="${css}/codemirror/show-hint.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="https://static.kdz6.cn/lib/codemirror.min.js"></script>
    <script src="${js}/codemirror/sql.js"></script>
    <script src="${js}/codemirror/sql-hint.js"></script>
    <style>
        .header {height: 50px;}
        .header li {display: inline-block;padding-left: 10px;padding-right: 10px;}
        .header a.sel{color: blue;}
        .header a:hover{color: blue;}

        .left {width: 200px;}
        .left ul{display: block; height: 100%; overflow-y: scroll; overflow-x:hidden;}
        .left li {line-height: 24px; overflow: hidden;}
        .left a.sel{color: blue;}
        .left a:hover{color: blue;}

        .CodeMirror{border: 1px solid #C9C9C9;font-size:14px;}
    </style>
    <script>

        $(function () {
            $('.header').find('a').first().click();
        });

        function get_tbs(e, key) {
            $('.header').find('a.sel').removeClass('sel');
            $(e).addClass('sel');

            $.ajax({
                type:"POST",
                url:"/dev/ddl/ajax/tb",
                data:{
                    tag: '${tag}',
                    key: key
                },
                success:function (data) {
                    if (1 === data.code) {
                        var r = "";
                        for (var idx in data.tbs) {
                            var tb = data.tbs[idx];
                            r += '<li><a href="javascript:void(0);" onclick="get_ddl(this, \'' + key + '\', \'' + tb + '\')">' + tb + '</a></li>';
                        }
                        $("#tb-tbs").html(r);
                        $(".left").find('a').first().click();
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function get_ddl(e, key, tb) {
            $('.left').find('a.sel').removeClass('sel');
            $(e).addClass('sel');

            $.ajax({
                type:"POST",
                url:"/dev/ddl/ajax/getddl",
                data:{
                    tag: '${tag!}',
                    key: key,
                    tb: tb
                },
                success:function (data) {
                    if (1 === data.code) {
                        //$("#code").html(data.ddl);
                        editor.setValue(data.ddl);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

    </script>
</head>
<body>

<table style="width: 100%; height: calc(100vh - 20px);">
    <tr>
        <td class="header" colspan="2">
            <block>
                <ul>
                    <#list cfgs as c>
                        <li><a href="javascript:void(0);" onclick="get_tbs(this, '${c.key}')">${c.key}</a></li>
                    </#list>
                </ul>
            </block>
        </td>
    </tr>
    <tr>
        <td class="left" valign="top">
            <ul id="tb-tbs"></ul>
        </td>
        <td valign="top">
            <textarea id="code" style="height: 100%; width: 100%"></textarea>
        </td>
    </tr>
</table>

<script>
    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.javascript});
    }
    var editor=CodeMirror.fromTextArea(document.getElementById("code"),{
        //高亮显示
        mode:"text/x-mssql",
        lineNumbers:true,
        readOnly:true,
        indentWithTabs: true,
        smartIndent: true,
        matchBrackets : true,
        autofocus: true,
        extraKeys: {"Ctrl-Space": "autocomplete"},
        hintOptions: {tables: {
                users: ["name", "score", "birthDate"],
                countries: ["name", "population", "size"]
            }}

    });

    //设置宽高,默认为自动根据每行长度来调整。
    //'width','height'
    editor.setSize('auto','100%');
</script>
</body>
</html>