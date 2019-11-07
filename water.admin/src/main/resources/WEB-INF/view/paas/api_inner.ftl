<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 接口列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/clipboard.min.js"></script>
    <script src="${js}/layer.js"></script>
    <script>

        function addPaas(tag) {
            location.href="/paas/api/edit?tag="+tag;
        }

        function impPaas(tag) {
            $.getJSON('/paas/api/ajax/import?tag='+tag,function (rst) {
                if(rst.code){
                    top.layer.msg(rst.msg);
                    setTimeout(location.reload,1000);
                }
                else{
                    top.layer.msg(rst.msg);
                }
            });
        }

        //复制功能
        $(document).ready(function(){
            var clipboard1 = new Clipboard('.a');
            clipboard1.on('success', function(e) {
                top.layer.msg("复制成功，去粘贴吧");
            });
        });
    </script>
</head>
<body>
        <toolbar>
            <left>
                <form>
                    接口：<input type="text"  name="api_name" placeholder="接口名" id="api_name" value="${api_name!}"/>
                          <input type="hidden"  name="tag" id="tag" value="${tag}"/>
                    <button type="submit">查询</button>&nbsp;&nbsp;
                    <#if is_admin == 1>
                    <button onclick="addPaas('${tag}')" class="edit" type="button">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <button onclick="impPaas('${tag}')" class="minor" type="button">导入</button>
                    </#if>
                </form>
            </left>
            <right>
                <@stateselector items="启用,未启用"/>
            </right>
        </toolbar>
            <datagrid>
                <table>
                    <thead>
                    <tr>
                        <td width="40px">ID</td>
                        <td>名称</td>
                        <td width="40px">缓存<br />时间</td>
                        <td>参数提示</td>
                        <td>备注</td>
                        <td width="100px">操作</td>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    <#list apis as api>
                        <tr>
                            <td>${api.api_id}</td>
                            <td class="left break">
                                <#if is_admin == 1>
                                    <a href="/paas/api/edit?api_id=${api.api_id}" class="t2">${api.api_name}</a>
                                </#if>
                                <#if is_admin == 0>
                                    <a href="/paas/query/code?code_type=0&id=${api.api_id}">${api.api_name}</a>
                                </#if>
                            </td>
                            <td class="break">${api.cache_time!}</td>
                            <td class="left break">${api.args!}</td>
                            <td class="left break" title="${api.note_hint()!}">${api.methods()!}::${api.note_str()!}</td>
                            <td><a class="a t2" data-clipboard-text="${url_start!}/${tag!}/${api.api_name!}" >复制地址</a>
                                <span> | </span>
                                <a target="_parent" class="t2" href="/smp/log?project=water&tableName=water_log_paas_error&tagx=@${api.api_id}">日志</a>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </datagrid>
</body>
</html>