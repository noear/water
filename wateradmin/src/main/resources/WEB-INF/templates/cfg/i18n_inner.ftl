<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 多语言包</title>
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
            fromData.append("bundle","${bundle!}");

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

        function exp(fmt) {
            var vm = formToMap(".sel_from");
            if(!vm.sel_id){
                alert("请选择..");
                return;
            }

            let baseUrl = "ajax/export?tag=${tag_name!}&bundle=${bundle!}";
            window.open(baseUrl + "&fmt=" + fmt + "&ids=" + vm.sel_id, "_blank");
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
    <style>
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}

        .btn-link:hover{text-decoration: underline;}
    </style>
</head>
<body>
<div class="tabs">
    <tabbar>
        <#list bundles as m>
            <#if m.tag == bundle>
                <a id="e${m.tag}" class="btn sel">${m.tag}(${m.counts})</a>
            <#else>
                <a id="e${m.tag}" class="btn" href="/cfg/i18n/inner?tag_name=${tag_name!}&bundle=${m.tag}">${m.tag}(${m.counts})</a>
            </#if>
        </#list>
    </tabbar>
</div>

<toolbar>
    <div class="center">
        <form>
            <input type="hidden" name="tag_name" value="${tag_name!}">
            <input type="hidden" name="bundle" value="${bundle!}">
            <input type="text"  name="name" placeholder="键值"  value="${name!}"class="w200"/>
            <button type="submit">查询</button>
        </form>

        <#if is_admin == 1>
            <a class="btn edit mar10-l" href="/cfg/i18n/edit?tag_name=${tag_name!}&bundle=${bundle!}">新增</a>
        </#if>
    </div>
    <div>
        <left>
            <#if is_admin == 1>
                <file>
                    <label><input id="imp_file" type="file" accept=".yml,.json,.properties,.jsond"/><a class="btn minor">导入</a></label>
                </file>

                <div class="btn-group">
                    <a class="btn minor" >导出</a>
                    <div class="btn-dropdown w150">
                        <a class="btn-link mar10" onclick="exp('yml')">导出为 Yml</a>
                        <a class="btn-link mar10" onclick="exp('json')">导出为 Json</a>
                        <a class="btn-link mar10" onclick="exp('properties')">导出为 Properties</a>
                        <hr class="mar10-l mar10-r"/>
                        <a class="btn-link mar10" onclick="exp('jsond')">导出为 JsonD</a>
                    </div>
                </div>

                <button type='button' class="minor" onclick="del(9,'删除')" >删除</button>
            </#if>
        </left>
        <right>
            <selector>
                <#list langs as l>
                    <a class='noline ${(l.tag == lang)?string("sel","") }' href="./inner?lang=${l.tag}&tag_name=${tag_name!}&bundle=${bundle!}">${l.tag}(${l.counts})</a>
                </#list>
            </selector>
        </right>
    </div>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
            <td width="150px" class="left">键值</td>
            <td width="80px" class="left">语言</td>
            <td class="left">描述信息</td>
            <td width="60px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from" >

        <#list list as m>
            <tr>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m.row_id}" /><a></a></label></checkbox></td>
                <td class="left">${m.name}</td>
                <td class="left">${m.lang!}</td>
                <td class="left break">${m.value!}</td>
                <#if is_admin == 1>
                    <td><a class="t2" href="/cfg/i18n/edit?id=${m.row_id}">编辑</a></td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</body>
</html>