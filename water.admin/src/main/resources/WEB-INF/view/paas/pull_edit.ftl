<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑内容拉取</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>

    <script>
        var pull_id = ${pull.pull_id!};
        function save() {

            var tag = $("#tag").val();

            if (!tag) {
                top.layer.msg("输入tag!");
                return;
            }

            if (!$("#pull_name").val()) {
                top.layer.msg("输入名称!");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/paas/pull/ajax/save",
                data: {
                    pull_id: pull_id,
                    tag: tag,
                    pull_name: $("#pull_name").val(),
                    source:$('#source').val(),
                    target_dir: $('#target_dir').val(),
                    target_url: $('#target_url').val(),
                    is_enabled: $('#is_enabled').prop('checked')?1:0
                },
                success: function (data) {
                    top.layer.msg(data.msg);
                    if (1 == data.code) {
                        if(pull_id==0) {
                            setTimeout(function () {
                                parent.location.href = "/paas/pull?tag_name=" + tag;
                            }, 500);
                        }
                    }
                }
            });
        }

    </script>
</head>
<body>

<flex>
    <left class="col-6"><h2>拉取编辑（<a onclick="history.back()" class="t2">返回</a>）</h2></left>
    <right class="col-6">

    </right>
</flex>
<hr/>
<detail>
    <form>

        <table>
            <tr>
            <tr>
                <td>tag*</td>
                <td>
                    <input type="text" id="tag" value="${pull.tag!}"/>
                </td>
            </tr>
            <tr>
                <td>名称*</td>
                <td><input type="text" id="pull_name" value="${pull.pull_name!}"/></td>
            </tr>
            <tr>
                <td>内容来源</td>
                <td><input type="text" id="source" value="${pull.source!}" class="longtxt"/></td>
            </tr>
            <tr>
                <td>存储位置</td>
                <td>
                    <input type="text" id="target_dir" value="${pull.target_dir!}" class="longtxt"/>
                </td>
            </tr>
            <tr>
                <td>浏览位置</td>
                <td>
                    <input type="text" id="target_url" value="${pull.target_url!}" class="longtxt"/>
                </td>
            </tr>
            <tr>
                <td>是否启用</td>
                <td>
                    <switcher>
                        <label><input id="is_enabled" type="checkbox" ${(pull.is_enabled == 1)?string("checked","")}><a></a></label>
                    </switcher>
                </td>
            </tr>

            <tr>
                <td></td>
                <td><button type="button" onclick="save()">保存</button></td>
            </tr>
        </table>
    </form>
</detail>

</body>
</html>