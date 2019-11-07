<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志配置</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

    </style>
</head>
<script>
    function editTask(id) {
        location.href="/cfg/logger/edit?id="+id;
    }
    function add() {
        location.href = "/cfg/logger/add";
    };
    function deleteTask(id,is_enabled) {
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
                data:{"id":id,"is_enabled":is_enabled},
                success:function(data){

                    if (data == true) {
                        top.layer.msg("操作成功");
                        setTimeout(function(){
                            location.reload();
                        },1000);
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
                <form>
                    <#if is_admin == 1>
                        <button class="edit" onclick="add();" type="button">新增</button>
                    </#if>
                </form>
            </left>
            <right><@stateselector items="启用,未启用"/></right>
        </toolbar>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <td width="90px">tag</td>
                    <td width="250px">logger</td>
                    <td width="60px">保留天数</td>
                    <td width="80px">行数</td>
                    <td width="80px">今日行数</td>
                    <td>数据源</td>
                    <#if is_admin == 1>
                        <td width="80px">操作</td>
                    </#if>
                </tr>
                </thead>
                <tbody id="tbody">
                <#list loggers as logger>
                    <tr ${logger.isHighlight()?string("class='t4'","")}>
                        <td>${logger.tag}</td>
                        <td class="left"><a href="/smp/log?tableName=${logger.logger}&project=${logger.tag}" target="_parent">${logger.logger}</a></td>
                        <td class="center">${logger.keep_days!}</td>
                        <td class="right">${logger.row_num!}</td>
                        <td class="right">${logger.row_num_today!}</td>
                        <td class="left break">${logger.source!}</td>

                        <#if is_admin == 1>
                            <td>
                                <a  onclick="editTask('${logger.id}')" style="color: blue;cursor: pointer">编辑</a>&nbsp;&nbsp;
                                <a  onclick="deleteTask('${logger.id}','${logger.is_enabled}')" style="color: blue;cursor: pointer">
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