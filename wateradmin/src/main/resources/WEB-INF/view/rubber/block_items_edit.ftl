<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 编辑数据块</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function save() {
            var data = {};
            $('tbody input').each(function (i,m) {
                data[m.id]=m.value;
            })

            $.ajax({
                type:"POST",
                url:"/rubber/block/item/edit/ajax/save",
                data:{
                    "block_id":'${block.block_id}',
                    "item_key":'${item_key!}',
                    "data":JSON.stringify(data)
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg);
                        setTimeout(function(){
                            location.href="/rubber/block/items?block_id=${block.block_id}";
                        },500);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

    </script>
</head>
<body>
<main>
    <toolbar class="blockquote">
        <left>
            <h2><a href="/rubber/block/inner?tag_name=${block.tag!}" class="noline">指标仓库</a> / <a href="#" onclick="javascript:history.back(-1);" class="noline">内容列表</a></h2> / 编辑 :: ${block.name_display!}
        </left>
        <right>
            <#if (block.block_id > 0)>
                <#if (is_admin == 1) && (block.block_id != 0)>
                    <button type="button" onclick="del()" class="minor">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <button type="button" onclick="imp()" class="minor">导入</button>
                </#if>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form>

            <table>
                <tbody>
                    <#list cols as k,v>
                        <tr>
                            <th>${v}</th>
                            <td>
                                <input type="text" id="${k}" value="${item.get(k)!}" />
                            </td>
                        </tr>
                    </#list>
                </tbody>
                <tfoot>
                    <tr>
                        <th></th>
                        <td>
                            <#if is_operator ==1>
                                <button type="button" onclick="save();">保存</button>
                            </#if>
                        </td>
                    </tr>
                </tfoot>

            </table>
        </form>
    </detail>
</main>
</body>
</html>