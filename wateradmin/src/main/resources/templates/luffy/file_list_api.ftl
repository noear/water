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
            var act = 21;
            var key = $('#key').val();
            if(key.length>0){
                if(key.startsWith("@")){
                    act = 11;
                }
                urlQueryByDic({'key':base64.encode(key), act:act});
            }
            else{
                window.location = "./list?tag_name=${tag}";
            }
        }

        function imp(file) {
            if(confirm("确定要导入吗？") == false){
                return;
            }

            var fromData = new FormData();
            fromData.append("file", file);
            fromData.append("tag","${tag_name!}");

            top.layer.load(2);

            $.ajax({
                type:"POST",
                url:"ajax/import",
                data:fromData,
                processData: false,
                contentType: false,
                success:function (data) {
                    top.layer.closeAll();

                    if(data.code==1) {
                        top.layer.msg('操作成功');
                        setTimeout(function(){
                            location.reload();
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                },
                error:function(data){
                    top.layer.closeAll();
                    top.layer.msg('网络请求出错...');
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

        <flex>
            <left class="col-4">
                <#if is_admin == 1>
                    <file>
                        <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor">导入</a></label>
                    </file>

                    <button type='button' class="minor" onclick="exp()" >导出</button>

                    <#if state!=1>
                        <button type='button' class="minor" onclick="del(1,'禁用')" >禁用</button>
                    <#else>
                        <button type='button' class="minor" onclick="del(0,'启用')" >启用</button>
                    </#if>
                    <a class="btn edit mar10-l" href="edit?tag=${tag}">新增</a>
                </#if>
            </left>
            <middle class="col-4 center">
                <input id="key" class="w200" placeholder="code or @path" type="text" value="${key}" />
                <button type="button" onclick="search()">查询</button>
            </middle>
            <right class="col-4">
                <selector>
                    <a class="${(state !=1)?string('sel','')}" href="./list?tag_name=${tag}&state=0">启用</a>
                    <a class="${(state =1)?string('sel','')}" href="./list?tag_name=${tag}&state=1">未启用</a>
                </selector>
            </right>
        </flex>
    </toolbar>

    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                <td>路径</td>
                <td class="left">标记</td>
                <td class="left" width="80">编辑模式</td>
                <td width="20"></td>
                <#if is_admin == 1>
                    <td width="120"></td>
                <#else>
                    <td width="80"></td>
                </#if>
            </tr>
            </thead>
            <tbody class="sel_from">
            <#list mlist as m1>
                <tr title="${m1.content_type!}">
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${m1.file_id}" /><a></a></label></checkbox></td>
                    <td class="left break"><a href='./code?file_id=${m1.file_id}&_p=${m1.path}' target="_blank">${m1.path!}</a>
                        <a href='./code?file_id=${m1.file_id}&_p=${m1.path}&readonly=1' class="t2" target="_blank">只读</a>

                        <#if (m1.note!'') != ''>
                            <n-l>${m1.note!}</n-l>
                        </#if>
                    </td>
                    <td class="left">${m1.label!}${m1.whitelistLabel()}</td>
                    <td  class="left">${m1.edit_mode!}</td>
                    <td width="20">${m1.staticize()?string("静","")}</td>
                    </td>
                    <td class="op">
                        <#if is_admin == 1>
                            <a class="t2" href='./edit?file_id=${m1.file_id}'>设置</a> |
                        </#if>
                        <a href="/mot/speed/charts?tag=${m1.tag}&name_md5=${m1.pathMd5()}&service=waterfaas" class="t2">监控</a> |
                        <a href="/log/query/inner?tag_name=water&logger=water_log_faas&level=0&tagx=@@${m1.path!}" target="_parent" class="t2">日志</a>
                    </td>
                </tr>
            </#list>
            </tbody>

        </table>
    </datagrid>

</main>
</body>
</html>