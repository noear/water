<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}
    </style>
</head>
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
<body>
<main>
    <#if labelList?size gt 0>
    <div class="tabs">
        <tabbar>
            <#list labelList as m>
                <#if m.tag == label>
                    <a id="e${m.tag}" class="btn sel">${m.tag}(${m.counts})</a>
                <#else>
                    <a id="e${m.tag}" class="btn" href="/cfg/prop/inner?tag_name=${tag_name!}&label=${m.tag}">${m.tag}(${m.counts})</a>
                </#if>
            </#list>
        </tabbar>
    </div>
    </#if>

    <toolbar>
        <flex>
            <left class="col-4">
                <#if is_admin == 1 >
                    <file>
                        <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor">导入</a></label>
                    </file>

                    <button type='button' class="minor" onclick="exp('${tag_name!}')" >导出</button>

                    <#if _state == 0>
                        <button type='button' class="minor" onclick="del(0,'禁用')" >禁用</button>
                    <#else>
                        <button type='button' class="minor" onclick="del(1,'启用')" >启用</button>
                    </#if>
                    <a class="btn edit mar10-l" href="/cfg/prop/edit?tag_name=${tag_name!}">新增</a>
                </#if>
            </left>
            <middle class="col-6 center">
                <form>
                    <input type="hidden"  name="tag_name" value="${tag_name!}"/>
                    <input type="hidden"  name="_state" value="${_state!}"/>
                    <input type="text"  name="key" placeholder="key" value="${key!}" class="w200"/>
                    <button type="submit">查询</button>
                </form>
            </middle>
            <right class="col-4">
                <@stateselector items="启用,未启用"/>
            </right>
        </flex>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <tr>
                <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
                <td width="220px" class="left">key</td>
                <td class="left">配置内容</td>
                <#if is_admin == 1>
                    <td width="50px">操作</td>
                </#if>
            </tr>
            </thead>
            <tbody id="tbody" class="sel_from">
            <#list list as cfg>
                <tr>
                    <td><checkbox><label><input type="checkbox" name="sel_id" value="${cfg.row_id}" /><a></a></label></checkbox></td>
                    <td class="left">${cfg.key!}
                        <#if cfg.type != 0>
                            <n-l>::${cfg.type_str()}</n-l>
                        </#if>
                    </td>
                    <td class="left break">${cfg.value_html()!}</td>
                    <#if is_admin == 1>
                        <td class="op"><a href="/cfg/prop/edit?row_id=${cfg.row_id}" class="t2">编辑</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</main>
</body>
</html>