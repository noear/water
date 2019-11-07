
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 计算资源-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function saveEdit() {
            var server_id = '${server.server_id}';
            var tag = $('#tag').val();
            var name = $('#name').val();
            var address = $('#address').val();
            var address_local = $('#address_local').val();
            var hosts_local = $('#hosts_local').val();
            var note = $('#note').val();
            var env_type = $('#env_type').val();
            var is_enabled =  $('#is_enabled').prop('checked')?1:0;
            var iaas_type = $('#iaas_type').val();
            var iaas_key = $('#iaas_key').val();
            var iaas_account = $('#iaas_account').val();

            if (!tag) {
                top.layer.msg("标签不能为空！");
                return;
            }
            if (!name) {
                top.layer.msg("服务器名称不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/ops/server/edit/ajax/save",
                data:{"server_id":server_id,"tag":tag,"name":name,
                       "address":address,"address_local":address_local,
                        iaas_type:iaas_type,
                        iaas_key:iaas_key,
                        iaas_account:iaas_account,
                      "hosts_local":hosts_local,"note":note,"is_enabled":is_enabled,"env_type":env_type},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/ops/server?tag="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

    </script>
</head>
<body>
<detail>
    <form>
    <h2>编辑计算资源（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
    <hr/>
    <table>
        <tr>
        <tr>
            <td>标签</td>
            <td>
                <input type="text" id="tag" value="${server.tag!}"/>
            </td>
        </tr>
        <tr>
            <td>资源名称</td>
            <td><input type="text" id="name" value="${server.name!}"/></td>
        </tr>
        <tr>
            <td>IAAS 实例</td>
            <td>
                <input type="text" id="iaas_key" value="${server.iaas_key!}"/>
            </td>
        </tr>
        <tr>
            <td>IAAS 类型</td>
            <td>
                <select id="iaas_type">
                    <option value="0">ECS</option>
                    <option value="1">LBS</option>
                    <option value="2">RDS</option>
                    <option value="3">Redis</option>
                    <option value="4">Memcached</option>
                    <option value="5">DRDS</option>
                </select>
                <script>
                    $('#iaas_type').val(${server.iaas_type});
                </script>
            </td>
        </tr>
        <tr>
            <td>IAAS 账号</td>
            <td>
                <select id="iaas_account">
                    <option value=""></option>
                    <#list accounts as m>
                    <option value="${m.tag}/${m.key}">${m.tag}/${m.key}</option>
                    </#list>
                </select>
                <script>
                    $('#iaas_account').val('${server.iaas_account!}');
                </script>
            </td>
        </tr>

        <tr>
            <td>外网地址</td>
            <td>
                <input type="text" id="address" class="longtxt" value="${server.address!}"/>
            </td>
        </tr>
        <tr>
            <td>内网地址</td>
            <td>
                <input type="text" id="address_local" class="longtxt" value="${server.address_local!}"/>
            </td>
        </tr>
        <tr style="display: none;">
            <td>本地映射</td>
            <td>
                <textarea id="hosts_local" style="height: 60px;">${server.hosts_local!}</textarea>
            </td>
        </tr>
        <tr style="display: none;">
            <td>备注</td>
            <td><input type="text" id="note" value="${server.note!}"/></td>
        </tr>
        <tr>
            <td>环境类型</td>
            <td>
                <select id="env_type">
                    <#if server.env_type == 0>
                        <option value="0" selected="selected">测试环境</option>
                        <option value="1">预生产环境</option>
                        <option value="2">生产环境</option>
                    </#if>
                    <#if server.env_type == 1>
                        <option value="1"  selected="selected">预生产环境</option>
                        <option value="0">测试环境</option>
                        <option value="2">生产环境</option>
                    </#if>
                    <#if server.env_type == 2>
                        <option value="2" selected="selected">生产环境</option>
                        <option value="0">测试环境</option>
                        <option value="1">预生产环境</option>
                    </#if>
                </select>
            </td>
        </tr>
        <tr>
            <td>是否启用</td>
            <td>
                <switcher>
                    <label><input id="is_enabled" type="checkbox" ${(server.is_enabled = 1)?string("checked","")}><a></a></label>
                </switcher>
            </td>
        </tr>

        <tr>
            <td></td>
            <td><button type="button" onclick="saveEdit();">保存</button></td>
        </tr>
    </table>
    </form>
</detail>
</body>
</html>