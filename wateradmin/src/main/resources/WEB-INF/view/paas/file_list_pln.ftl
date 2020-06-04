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

        function reset() {
            var vm = formToMap(".sel_from");
            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            ajaxPost({
                url: "ajax/reset", data: {ids: vm.sel_id}, success: (rst) => {
                    location.reload();
                }
            });
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
        <left>
            <div>
                <a class="w60">关键字：</a><input id="key" class="w350" placeholder="code or @path" type="text" value="${key}" />
                <button type="button" onclick="search()">查询</button>
                <#if is_admin == 1>
                    <a class="btn edit mar10-l" href="edit?tag=${tag}">新增</a>
                </#if>
            </div>
            <#if is_admin == 1>
                <div>
                    <a class="w60"></a><file>
                        <label><input id="imp_file" type="file" accept=".jsonx"/><a class="btn minor w80">导入</a></label>
                    </file>

                    <button type='button' class="minor w80 mar10-l" onclick="exp()" >导出</button>

                    <#if state!=1>
                        <button type='button' class="edit w80 mar10-l" onclick="reset()" >立即执行</button>
                        <button type='button' class="minor mar10-l" onclick="del(1,'禁用')" >禁用</button>
                    <#else>
                        <button type='button' class="minor mar10-l" onclick="del(0,'启用')" >启用</button>
                        <button type='button' class="minor mar10-l" onclick="del(9,'删除')" >删除</button>
                    </#if>
                </div>
            </#if>
        </left>
        <right>
            <selector>
                <a class="${(state !=1)?string('sel','')}" href="./list?tag_name=${tag}&state=0">启用</a>
                <a class="${(state =1)?string('sel','')}" href="./list?tag_name=${tag}&state=1">未启用</a>
            </selector>
        </right>
    </toolbar>

    <datagrid class="list">
        <table>

                <thead>
                <tr>
                    <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                    <td>路径</td>
                    <td width="100" class="left">开始（间隔）</td>
                    <td width="160" class="left">最后执行（时长）</td>
                    <td width="50">状态</td>
                    <td width="50">次数</td>
                    <td width="120"></td>
                </tr>
                </thead>
                <tbody class="sel_from">
                <#list mlist as m1>
                    <tr>
                        <td><checkbox><label><input type="checkbox" name="sel_id" value="${m1.file_id}" /><a></a></label></checkbox></td>
                        <td class="left"><a href='./code?file_id=${m1.file_id}&_p=${m1.path}'  class="t2" target="_blank">${m1.path!}</a>
                            <#if (m1.note!'') != ''>
                                <n class="mar10">::${m1.note!}</n>
                            </#if>
                        </td>
                        <td  class="left">${(m1.plan_begin_time?string('MM.dd'))!}(${m1.plan_interval!})</td>
                        <td  class="left">
                            <#if m1.plan_last_timespan?default(0) gt 1000 >
                                ${(m1.plan_last_time?string('MM.dd HH:mm'))!}(${m1.plan_last_timespan/1000}s)
                            <#else>
                                ${(m1.plan_last_time?string('MM.dd HH:mm'))!}(${m1.plan_last_timespan!0}ms)
                            </#if>
                        </td>

                        <td style='${(m1.plan_state=8)?string("color:red","")}'>
                            <#if m1.plan_state=9>
                                成功
                            <#elseif m1.plan_state=8>
                                出错
                            <#elseif m1.plan_state=2>
                                处理中
                            <#else>
                                待处理
                            </#if>
                        </td>
                        <td class="right">${m1.plan_count?default(0)%1000}/${m1.plan_max!}</td>
                        <td class="op">
                            <a class="t2" href='./edit?file_id=${m1.file_id}'>设置</a>
                            |
                            <a href="/log/query/inner?tag_name=water&logger=water_log_paas&level=5&tagx=@@${m1.path!}" target="_parent" class="t2">日志</a>
                            |
                            <a class="t2" href="${paas_uri}${m1.path!}?_debug=1" target="_blank">调试</a>

                        </td>
                    </tr>
                </#list>
                </tbody>

        </table>
    </datagrid>

</main>
</body>
</html>