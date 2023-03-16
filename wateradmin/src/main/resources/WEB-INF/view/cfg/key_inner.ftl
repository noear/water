<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 访问密钥</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function imp(file) {
            if(confirm("确定要导入吗？") == false){
                return;
            }

            var fromData = new FormData();
            fromData.append("file", file);
            fromData.append("tag","${tag_name!}");

            $.ajax({
                type:"POST",
                url:"ajax/import",
                data:fromData,
                processData: false,
                contentType: false,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function exp() {
            var vm = formToMap(".sel_from");
            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            window.open("ajax/export?tag=${tag_name!}&ids=" + vm.sel_id, "_blank");
        }

        function del(act,hint){
            var vm = formToMap(".sel_from");

            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            if(confirm("确定要"+hint+"吗？") == false) {
                return;
            }

            $.ajax({
                type:"POST",
                url:"ajax/batch",
                data:{act: act, ids: vm.sel_id},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function(){
            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });

            $("#imp_file").change(function () {
                imp(this.files[0]);
            })
        });
    </script>
</head>
<body>
<main>
    <toolbar>
        <flex>
            <left class="col-6">
                <#if is_admin == 1 >
                    <file>
                        <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor">导入</a></label>
                    </file>

                    <button type='button' class="minor" onclick="exp()" >导出</button>

                    <#if _state == 0>
                        <button type='button' class="minor" onclick="del(0,'禁用')" >禁用</button>
                    <#else>
                        <button type='button' class="minor" onclick="del(1,'启用')" >启用</button>
                    </#if>
                    <a class="btn edit mar10-l" href="/cfg/key/edit?tag_name=${tag_name!}">新增</a>
                </#if>
            </left>
            <right class="col-6">
                <@stateselector items="启用,未启用"/>
            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                <td width="50px">ID</td>
                <td width="280px" class="left">access_key</td>
                <td width="100px" class="left">label</td>
                <td class="left">metainfo</td>
                <td width="60px">操作</td>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from" >

            <#list list as m>
                <tr>
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.key_id}" /><a></a></label></checkbox></td>
                    <td>${m.key_id}</td>
                    <td class="left">${m.access_key!}</td>
                    <td class="left">${m.label!}</td>
                    <td class="left">${m.metainfo!}</td>
                    <#if is_admin == 1>
                        <td><a class="t2" href="/cfg/key/edit?id=${m.key_id}">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>