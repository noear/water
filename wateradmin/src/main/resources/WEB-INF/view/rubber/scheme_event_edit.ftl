<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑方案</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <script src="${js}/jteditor.js"></script>
    <style type="text/css">
        pre{border:1px solid #C9C9C9;}
    </style>
    <script>
        var f = '${f}';
        //保存事件编辑
        function saveEdit(){
            var event = window.editor.getValue();

            $.ajax({
                type: "POST",
                url: "/rubber/scheme/event/edit/ajax/save",
                data: {
                    "scheme_id": ${scheme.scheme_id},
                    "event": event
                },
                success: function (data) {
                    if (data.code == 1) {
                        top.layer.msg(data.msg)
                        setTimeout(function () {
                            location.href = "/rubber/scheme/inner?tag_name=${scheme.tag!}"+"&f="+f;
                        }, 1000);
                    } else {
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function debug() {
            var txt = $('#debug_args').val().trim();

            if(txt) {
                $('#args').val(txt);
                $('#debug_form').submit();
            }else{
                top.layer.msg('请输入调试参数');
            }
        }
        $(function(){

            $('pre[jt-js]').each(function(){
                window.editor = build_editor(this,'javascript');
            });
        });
    </script>
</head>
<body>
<main>

    <blockquote>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back(-1);" class="noline">计算方案</a></h2> / 事件编辑 :: ${scheme.name_display}
    </blockquote>

<detail>
    <form>
        <table>
            <tr>
                <th>调试参数</th>
                <td>
                    <input type="text" id="debug_args" value="${scheme.debug_args!}" class="longtxt" placeholder="{user_id:1}"/>
                </td>
            </tr>
            <tr>
                <th>事件处理</th>
                <td>
                    <pre style="height:320px;width:600px;" jt-js id="input_event">${scheme.event!}</pre>
                </td>
                <td>（按Esc全屏）</td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <#if is_operator ==1>
                    <button type="button" onclick="saveEdit()" >保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </#if>

                    <button type="button" onclick="debug()" class="minor" >调试事件</button>

                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <code>
                        /*
                        c   //上下文对象；
                        m   //当前模型；
                        sm  //当前匹配结果；
                        */
                    </code>
                    <br />
                    <code>
                        /* sm:{scheme,total,value,relation,is_match} */
                    </code>
                </td>
            </tr>
        </table>
    </form>
    <form id="debug_form" action="${raas_uri!}/debug" target="_blank" method="get">
        <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
        <input type="hidden" name="args" id="args">
        <input type="hidden" name="type" value="1">
        <input type="hidden" name="policy" value="1001">
    </form>
</detail>
</main>
</body>
</html>