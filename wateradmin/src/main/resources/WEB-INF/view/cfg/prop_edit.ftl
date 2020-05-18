<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="//static.kdz6.cn/lib/ace/ace.js" ></script>
    <script src="//static.kdz6.cn/lib/ace/ext-language_tools.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        pre{border:1px solid #C9C9C9;}

        a.clone{display: inline-block; color: #666; width: 26px; height: 26px; line-height: 26px; text-align: center; }
        a.clone:hover{color: #000;}
    </style>

    <script>
        var row_id = '${cfg.row_id!}';

        function save() {
            var tag = $('#tag').val();
            var key = $('#key').val();
            var value = $('#value').val();
            var type = $('#type').val();


            if (!tag) {
                top.layer.msg("tag不能为空！");
                return;
            }

            if (!key) {
                top.layer.msg("key不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/prop/edit/ajax/save",
                data:{"row_id":row_id,"name":name,"tag":tag,"key":key,"type":type,"value":value},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/prop?tag_name="+tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {
            if(!row_id){
                return;
            }

            if(!confirm("确定要删除吗？")){
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/prop/edit/ajax/del",
                data:{"row_id":row_id},
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/prop?tag_name=${cfg.tag!}";
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function build_editor(elm,mod){
            var ext_tools = ace.require("ace/ext/language_tools");

            ext_tools.addCompleter({
                getCompletions: function(editor, session, pos, prefix, callback) {
                    callback(null,
                        [
                            {name: "schema",value: "schema", meta: "",type: "local",score: 1000},
                            {name: "url",value: "url", meta: "",type: "local",score: 1000},
                            {name: "username",value: "username", meta: "",type: "local",score: 1000},
                            {name: "password",value: "password", meta: "",type: "local",score: 1000},
                            {name: "server",value: "server", meta: "",type: "local",score: 1000},
                            {name: "user",value: "user", meta: "",type: "local",score: 1000},
                            {name: "name",value: "name", meta: "",type: "local",score: 1000},
                            {name: "accessKeyId",value: "accessKeyId", meta: "",type: "local",score: 1000},
                            {name: "accessSecret",value: "accessSecret", meta: "",type: "local",score: 1000}
                        ]);
                }
            });

            var editor = ace.edit(elm);

            editor.setTheme("ace/theme/chrome");
            editor.getSession().setMode("ace/mode/"+mod);
            editor.setOptions({
                showFoldWidgets:false,
                showLineNumbers:false,
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });

            editor.setShowPrintMargin(false);
            editor.moveCursorTo(0, 0);

            editor.getSession().on('change', function(e) {
                var value = editor.getValue();
                $('#value').val(value);
            });

            window.editor = editor;
        }

        function loadTypeTml(){
            let jt = $('#type').find("option:selected").text();
            let tml = window.tmls[jt];
            if(tml){
                window.editor.setValue(tml);
                window.editor.moveCursorTo(0, 0);
            }
        }

        $(function(){
            $('pre[jt-ini]').each(function(){
                build_editor(this,'ini');
            });

            $('pre[jt-yaml]').each(function(){
                build_editor(this,'yaml');
            });

            $('a.clone').click(function () {
                loadTypeTml();
            });

            $('#type').change(function () {
                if(!window.editor.getValue()) {
                    loadTypeTml();
                }
            });

            ctl_s_save_bind(document,save);
        });

        window.tmls = {
            "database":"schema=\nurl=\nusername=\npassword=",
            "mongodb":"schema=\nurl=\nusername=\npassword=",
            "memcached":"server=\nuser=\npassword=",
            "redis":"server=\nuser=\npassword=",
            "elasticsearch":"url=\nusername=\npassword=",
            "aliyun_ram":"regionId=\naccessKeyId=\naccessSecret=\nendpoint=",
            "iaas_ram":"regionId=\naccessKeyId=\naccessSecret=\nendpoint=",
            "ram":"regionId=\naccessKeyId=\naccessSecret=\nendpoint=",
        };
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">属性配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" class="w80" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>


<detail>
    <form>

        <table>
            <tr>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" autofocus value="${tag_name!}"/></td>
            </tr>
            <tr>
                <th>key*</th>
                <td><input type="text" id="key" value="${cfg.key!}"/></td>
            </tr>
            <tr>
                <th>type</th>
                <td>
                    <@enum group="config_type" style="select" id="type" value="${cfg.type!}" ></@enum>
                    <a class="clone"><i class="fa fa-clone"></i></a>
                </td>
            </tr>
            <tr>
                <th>value</th>
                <td>
                    <textarea id="value" class="hidden">${cfg.value!}</textarea>
                    <pre style="height:300px;width:calc(100vw - 260px);"  jt-ini id="value_edit">${cfg.value!}</pre>
                </td>
            </tr>

        </table>
    </form>
</detail>

</body>
</html>