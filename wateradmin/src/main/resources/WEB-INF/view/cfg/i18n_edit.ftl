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
    <script src="${js}/vue.js"></script>
    <script>
        var nameOld = "${model.name!}";
        var tagOld = "${tag_name!}";
        var viewModel = {items: ${langs}};

        function del(){
            if(!nameOld){
                return;
            }

            if(!confirm("确定要删除吗？")){
                return;
            }

            var bundle = $('#bundle').val().trim();
            var name = $('#name').val().trim();

            viewModel.tag = $('#tag').val().trim();

            if (!bundle) {
                top.layer.msg("语言包名不能为空！");
                return;
            }

            if (!name) {
                top.layer.msg("状态码不能为空！");
                return;
            }

            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/cfg/i18n/ajax/del",
                    data:{"tag":viewModel.tag, "bundle":bundle, "name":name, "nameOld":nameOld},
                    success:function(data){
                        if(1==data.code) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                if(window.tagOld){
                                    location.href="/cfg/i18n/inner?tag_name="+viewModel.tag+"&bundle="+bundle;
                                }else{
                                    parent.location.href = "/cfg/i18n?tag_name=" + vm.tag;
                                }
                            }, 800);
                        }else{
                            top.layer.msg(data.msg);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };


        function save() {
            var bundle = $('#bundle').val().trim();
            var name = $('#name').val().trim();
            var items = JSON.stringify(viewModel.items);

            viewModel.tag = $('#tag').val().trim();

            if(viewModel.items.length <1){
                top.layer.msg("描述配置不能为空！");
                return;
            }

            if (!bundle) {
                top.layer.msg("语言包名不能为空！");
                return;
            }

            if (!name) {
                top.layer.msg("键值不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/i18n/edit/ajax/save",
                data:{"tag":viewModel.tag, "bundle":bundle, "name":name, "nameOld":nameOld, "items":items},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            if(window.tagOld){
                                location.href="/cfg/i18n/inner?tag_name="+viewModel.tag+"&bundle="+bundle;
                            }else{
                                parent.location.href="/cfg/i18n?tag_name="+viewModel.tag;
                            }
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        };

        $(function () {
            ctl_s_save_bind(document,save);
        })
    </script>
    <style>
        #app li{margin-bottom: 4px;}
    </style>
</head>
<body>

<datalist id="lang_list">
    <#list lang_type as m1>
        <option value="${m1.value}">${m1.title}</option>
    </#list>
</datalist>

<main>
    <toolbar class="blockquote">
        <left>
            <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">多语言包</a></h2> / 编辑
        </left>
        <right class="form">
            <n>ctrl + s 可快捷保存</n>
            <button type="button" onclick="save()">保存</button>
            <#if is_admin == 1>
                <button type="button" class="minor" onclick="del()">删除</button>
            </#if>
        </right>
    </toolbar>

    <detail>
        <form id="form">
            <input type="hidden" id="row_id" value="${model.row_id!0}">
        <table>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" value="${tag_name!}" autofocus/></td>
            </tr>
            <tr>
                <th>语言包名</th>
                <td><input type="text" id="bundle" value="${bundle!}" /></td>
            </tr>
            <tr>
                <th>键值</th>
                <td><input type="text" id="name" value="${model.name!}" /></td>
            </tr>
            <tr>
                <th class="top" style="padding-top: 45px;">描述配置</th>
                <td id="app">
                    <div>
                        <left><n class="w100" style="display: inline-block">语言</n></left>
                        <right><n class="longtxt" style="display: inline-block">描述</n></right>
                    </div>
                    <ul>
                        <li v-for="m in items">
                            <left><input class="w100" type="text" list="lang_list" autocomplete="off" v-model="m.lang"></left>
                            <right><input type="text" class="longtxt" v-model="m.value"/></right>
                        </li>
                    </ul>
                    <div>
                        <button type="button" @click="add" class="minor">添加</button>
                    </div>
                </td>
            </tr>
        </table>
        </form>
    </detail>
</main>

<script>
    var app = new Vue({
        el: '#app',
        data: viewModel,
        methods:{
            add: function (){
                this.items.push({lang:"",value:""})
            }
        }
    })
</script>

</body>
</html>