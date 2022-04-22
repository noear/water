<!doctype html>
<html class="frm10">
<head>
    <title>文件</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/base64.js"></script>
    <script>
        var base64 = new Base64();

        function search(){
            var key = $('#key').val();
            if(key.length>0){
                urlQueryByDic({'key':base64.encode(key), act:22});
            }
            else{
                window.location = "./query";
            }
        }

        function reset(id){

        }

        $(function(){
            $("#key").keydown(function(e){
                if(e.keyCode==13){
                    search();
                }
            });
        });
    </script>
</head>
<body>
<main>
    <toolbar>
        <div class="center">
            <input id="key" class="w250" placeholder="code" type="text" value="${key}" />
            <button type="button" onclick="search()">查询</button>
        </div>
    </toolbar>

    <datagrid class="list">
        <table>
                <thead>
                <tr>
                    <td class="left" width="80">tag</td>
                    <td>路径</td>
                    <td class="left" width="80">类型</td>
                    <td class="left" width="80">编辑模式</td>
                    <td colspan="2"></td>
                    <#if is_admin == 1>
                        <td width="80"></td>
                    <#else>
                        <td width="40"></td>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <#list mlist as m1>
                    <tr>
                        <td class="left">${m1.tag!}</td>
                        <td class="left"><a href='./file/${m1.typeStr()}/code?file_id=${m1.file_id}&_p=${m1.path}' class="t2" target="_blank">${m1.path!}</a>
                            <#if (m1.note!'') != ''>
                                <n class="mar10">::${m1.note!}</n>
                            </#if>
                            <#if (m1.link_to!'') != ''>
                                <n class="mar10">（${m1.link_to!}）</n>
                            </#if>
                        </td>
                        <td class="left">${m1.typeStr()}</td>
                        <td  class="left">${m1.edit_mode!}</td>
                        <td width="20"></td>
                        <td width="20"></td>
                        </td>
                        <td class="op">
                            <#if is_admin == 1>
                            <a class="t2" href='./file/${m1.typeStr()}/edit?file_id=${m1.file_id}'>设置</a>
                            |
                            </#if>
                            <a class="t2" href="${faas_uri}${m1.path!}" onclick="return confirm('确定要调试吗？')" target="_blank">调试</a>
                        </td>
                    </tr>
                </#list>
                </tbody>

        </table>
    </datagrid>

</main>
</body>
</html>