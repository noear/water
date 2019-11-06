<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志配置-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        $(function () {
            document.getElementById('source').value="${log.source!}";
        });

        var logger_id = '${log.logger_id}';
        function saveEdit() {
            var tag = $('#tag').val();
            var logger = $('#logger').val();
            var keep_days = $('#keep_days').val();
            var source = $('#source').val();
            var note = $('#note').val();

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if(logger_id==null){
                logger_id=0;
            }
            $.ajax({
                type:"POST",
                url:"/cfg/logger/edit/ajax/save",
                data:{
                    "logger_id":logger_id,
                    "tag":tag,
                    "logger":logger,
                    "keep_days":keep_days,
                    "source":source,
                    "note":note
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/cfg/logger?tag_name="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>
        <detail>
            <form>
            <h2>编辑日志配置（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
            <hr/>
            <table>
                <tr>
                <tr>
                    <td>tag*</td>
                    <td><input type="text" id="tag" autofocus value="${log.tag!}"/></td>
                </tr>
                <tr>
                    <td>logger*</td>
                    <td><input type="text" id="logger" value="${log.logger!}"/></td>
                </tr>
                <tr>
                    <td>保留天数</td>
                    <td><input type="text" id="keep_days" value="${log.keep_days!}"/></td>
                </tr>
                <tr>
                    <td>数据源</td>
                    <td>
                        <select id="source">
                            <option value=""></option>
                            <#list option_sources as sss>
                                <option value="${sss}">${sss}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td><input type="text" id="note" value="${log.note!}"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="button" onclick="saveEdit()">保存</button></td>
                </tr>
            </table>
            </form>
        </detail>
</body>
</html>