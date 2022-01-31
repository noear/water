<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function edit(logger_id) {
        location.href="/cfg/logger/edit?tag_name=${tag_name!}&logger_id="+logger_id;
    }

    function del(logger_id,is_enabled) {
        var text = '禁用';
        if (is_enabled == 0) {
            text = '启用';
            is_enabled = 1;
        } else {
            is_enabled = 0;
        }
        top.layer.confirm('确定'+text, {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                type:"POST",
                url:"/cfg/logger/isEnable",
                data:{"logger_id":logger_id,"is_enabled":is_enabled},
                success:function(data){
                    if (data.code == 1) {
                        top.layer.msg("操作成功");
                        setTimeout(function(){
                            location.reload();
                        },800);
                    } else {
                        top.layer.msg("操作失败");
                    }

                }
            });
            top.layer.close(top.layer.index);
        });
    }
</script>
<body>
        <toolbar>
            <left>
                <#if is_admin == 1>
                    <button class="edit" onclick="edit(0);" type="button">新增</button>
                </#if>
            </left>
            <right><@stateselector items="启用,未启用"/></right>
        </toolbar>
        <datagrid class="list">
            <table>
                <thead>
                <tr>
                    <td width="220px" class="left">logger</td>
                    <td width="60px">保留<br/>天数</td>
                    <td width="120px" class="right">今日<br/>记录数</td>
                    <td width="120px" class="right">今日<br/>错误数</td>
                    <td>数据源</td>
                    <td width="60">启用<br/>报警</td>
                    <#if is_admin == 1>
                        <td width="100px" rowspan="2">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list loggers as logger>
                    <tr ${logger.isHighlight()?string("class='t4'","")}>
                        <td class="left"><a href="/log/query/inner?logger=${logger.logger}&tag_name=${logger.tag}" target="_parent">${logger.logger}</a></td>
                        <td class="center">${logger.keep_days}</td>
                        <td class="right">${logger.row_num_today}</td>
                        <td class="right">${logger.row_num_today_error}</td>

                        <td class="left break">${logger.source}</td>

                        <td>
                            <#if logger.is_alarm?default(0) gt 0>
                                是
                            </#if>
                        </td>

                        <#if is_admin == 1>
                            <td>
                                <a  onclick="edit('${logger.logger_id}')" style="color: blue;cursor: pointer">编辑</a>&nbsp;&nbsp;
                                <a  onclick="del('${logger.logger_id}','${logger.is_enabled}')" style="color: blue;cursor: pointer">
                                    <#if logger.is_enabled == 0>启用</#if>
                                    <#if logger.is_enabled == 1>禁用</#if>
                                </a>
                            </td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </datagrid>

</body>
</html>