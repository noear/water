<!DOCTYPE HTML>
<html class="frm10-y">
<head>
    <title>${app} - 运行设置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body  detail{ width: 800px; margin: 0 auto;}
        form > table{width: 800px;}
        form > table th{width: 150px;}
        form > table td > h2{line-height: 40px;}
    </style>
    <script>
        function save(){
           let vm = formToMap("form");

            $.ajax({
                type:"POST",
                url:"/admin/ajax/save",
                data:{json: JSON.stringify(vm)},
                success:function (data) {
                    if(data.code == 1) {
                        top.layer.msg('操作成功');
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>

<main>
    <toolbar class="blockquote">
        <left class="ln30">
            <h2>设置</h2>
        </left>
        <right class="form">
            <#if is_admin == 1>
                <n>ctrl + s 可快捷保存</n>
                <button type="button" onclick="save()">保存</button>
            </#if>
        </right>
    </toolbar>

        <detail>
            <form>
                <table>

                    <tr><td colspan="2"> <h2>控制台</h2> <hr/></td></tr>

                    <tr>
                        <th>服务注册数量规模</th>
                        <td>
                            <boxlist>
                                <label><input type="radio" name="water.setting.scale.service" value="0" checked /><a>小</a></label>
                                <label><input type="radio" name="water.setting.scale.service" value="1" /><a>中</a></label>
                                <label><input type="radio" name="water.setting.scale.service" value="2" /><a>大</a></label>
                            </boxlist>
                            <n-l>
                                会根据情况调整部分控制台界面布局
                            </n-l>
                            <script>
                                $("input[name='water.setting.scale.service'][value='${sets["water.setting.scale.service"]!}']").attr("checked",true);
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <th>消息主题数量规模</th>
                        <td>
                            <boxlist>
                                <label><input type="radio" name="water.setting.scale.topic" value="0" checked /><a>小</a></label>
                                <label><input type="radio" name="water.setting.scale.topic" value="1" /><a>中</a></label>
                                <label><input type="radio" name="water.setting.scale.topic" value="2" /><a>大</a></label>
                            </boxlist>
                            <n-l>
                                会根据情况调整部分控制台界面布局
                            </n-l>
                            <script>
                                $("input[name='water.setting.scale.topic'][value='${sets["water.setting.scale.topic"]!}']").attr("checked",true);
                            </script>
                        </td>
                    </tr>


                    <tr><td colspan="2"> <h2>其它</h2> <hr/></td></tr>

                    <tr>
                        <th>告警签名</th>
                        <td>
                            <input id="alarm_sign" type="text" value="${sets["alarm_sign"]!}">
                        </td>
                    </tr>
                </table>
            </form>
        </detail>
</main>

</body>
</html>