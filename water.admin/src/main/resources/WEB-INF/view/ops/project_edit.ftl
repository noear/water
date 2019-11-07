<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 项目配置-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>

        //三列有值选择框的数量
        var production_value_size = 0;
        var prepare_value_size = 0;
        var test_value_size = 0;

        //三列所有选择框的数量
        var production_total_size = 0;
        var prepare_total_size = 0;
        var test_total_size = 0;

        $(function (){
            var resource_production = JSON.parse('${resource_production!}');
            var resource_prepare = JSON.parse('${resource_prepare!}');
            var resource_test = JSON.parse('${resource_test!}');

            production_value_size = resource_production.length;
            prepare_value_size = resource_prepare.length;
            test_value_size = resource_test.length;

            var listSize = [production_value_size,prepare_value_size,test_value_size];

            //获取最大行数
            var maxRow = Math.max.apply(Math, listSize);
            //构造tr
            for(var i=0;i<maxRow;i++){
                $('#resource_table').append("<tr><td></td><td></td><td></td><td></td></tr>");
            }
            for(var i=0;i<production_value_size;i++){
                addNextSelect(1,i+1);
                var item = document.getElementsByClassName('machine_production');
                item[i].value = resource_production[i].server_id;
            }
            for(var i=0;i<prepare_value_size;i++){
                addNextSelect(2,i+1);
                var item = document.getElementsByClassName('machine_prepare');
                item[i].value = resource_prepare[i].server_id;
            }
            for(var i=0;i<test_value_size;i++){
                addNextSelect(3,i+1);
                var item = document.getElementsByClassName('machine_test');
                item[i].value = resource_test[i].server_id;
            }
            $('#developer').val('${project.developer!}');
        });

        function checkTr(index){
            var class_name = "";
            if(index==1){
                class_name = 'machine_production';
                production_value_size = 0;
                production_total_size = 0;
            }else if(index==2){
                class_name = 'machine_prepare';
                prepare_value_size = 0;
                prepare_total_size = 0;
            }else{
                class_name = 'machine_test';
                test_value_size = 0;
                test_total_size = 0;
            }

            $('.'+class_name).each(function() {
                var select_value = $(this).val();
                if(index==1){
                    production_total_size += 1;
                    if (select_value!=null && select_value!=""){
                        production_value_size += 1;
                    }
                }else if(index==2){
                    prepare_total_size += 1;
                    if (select_value!=null && select_value!=""){
                        prepare_value_size += 1;
                    }
                }else{
                    test_total_size += 1;
                    if (select_value!=null && select_value!=""){
                        test_value_size += 1;
                    }
                }
            });


            var listSize = [production_value_size,prepare_value_size,test_value_size];
            //获取所需最大行数
            var maxRow = Math.max.apply(Math, listSize);
            var resource_table = document.getElementById("resource_table");
            var rows = resource_table.rows.length;

            //行数不足时 新增行
            if((rows-3)<=maxRow){
                $('#resource_table').append("<tr><td></td><td></td><td></td><td></td></tr>");
            }

            //行数多余时 删除行
            if((rows-3)==maxRow+2){
                $("#resource_table tr:last").remove();
            }

            if(index==1){
                //需要新增select
                if (production_total_size==production_value_size){
                    addNextSelect(index,production_value_size);
                }
                //需要删除select
                if (production_total_size>=production_value_size+2){
                    removeSelect(index,production_value_size+1);
                }
            }else if(index==2){
                //需要新增select
                if (prepare_total_size==prepare_value_size){
                    addNextSelect(index,prepare_value_size);
                }
                //需要删除select
                if (prepare_total_size>=prepare_value_size+2){
                    removeSelect(index,prepare_value_size+1);
                }
            }else{
                //需要新增select
                if (test_total_size==test_value_size){
                    addNextSelect(index,test_value_size);
                }
                //需要删除select
                if (test_total_size>=test_value_size+2){
                    removeSelect(index,test_value_size+1);
                }
            }
        }

        function removeSelect(index,num){
            var class_name = "";
            if(index==1){
                class_name = 'machine_production';
            }else if(index==2){
                class_name = 'machine_prepare';
            }else{
                class_name = 'machine_test';
            }

            var resource_table = document.getElementById("resource_table");
            var cell = resource_table.rows[num+3].cells[index];
            var cNode = $('.'+class_name)[num];
            cell.removeChild(cNode);
        }

        function addNextSelect(index,num){
            var class_name = "";
            if(index==1){
                class_name = 'machine_production';
            }else if(index==2){
                class_name = 'machine_prepare';
            }else{
                class_name = 'machine_test';
            }

            var resource_table = document.getElementById("resource_table");
            var cell = resource_table.rows[num+3].cells[index];
            var cNode = $('.'+class_name)[0].cloneNode(true);
            cell.appendChild(cNode);
        }

        function saveEdit() {
            var project_id = '${project.project_id}';
            var tag = $('#tag').val();
            var name = $('#name').val();
            var git_url = $('#git_url').val();
            var note = $('#note').val();
            var developer = $('#developer').val();
            var production_host = $('#domain_production').val();
            var prepare_host = $('#domain_prepare').val();
            var test_host = $('#domain_test').val();

            var production_port = $('#port_production').val();
            var prepare_port = $('#port_prepare').val();
            var test_port = $('#port_test').val();

            var type = $('#type').val();

            var productionList = [];
            var prepareList = [];
            var testList = [];

            var productionValue = [];
            var prepareValue = [];
            var testValue = [];

            $('.machine_production').each(function(){
                var select_value = $(this).val();
                if(select_value!=null && select_value!=""){
                    productionList.push(select_value);
                    productionValue.push($(this).find("option:selected").text());
                }
            });
            $('.machine_prepare').each(function(){
                var select_value = $(this).val();
                if(select_value!=null && select_value!=""){
                    prepareList.push(select_value);
                    prepareValue.push($(this).find("option:selected").text());
                }
            });
            $('.machine_test').each(function(){
                var select_value = $(this).val();
                if(select_value!=null && select_value!=""){
                    testList.push(select_value);
                    testValue.push($(this).find("option:selected").text());
                }
            });

            if((isNull(production_host)==false || isNull(production_port)==false) && productionList.length==0){
                layer.msg("请选择生产环境的资源主机！");
                return;
            }
            if((isNull(prepare_host)==false || isNull(prepare_port)==false) && prepareList.length==0){
                layer.msg("请选择预发环境的资源主机！");
                return;
            }
            if((isNull(test_host)==false || isNull(test_port)==false) && testList.length==0){
                layer.msg("请选择测试环境的资源主机！");
                return;
            }

            if((isNull(test_host) || isNull(test_port)) && testList.length>0) {
                layer.msg("请填写完整测试环境的域名和端口参数！");
                return;
            }
            if((isNull(production_host) || isNull(production_port)) && productionList.length>0){
                layer.msg("请填写完整生产环境的域名和端口参数！");
                return;
            }
            if((isNull(prepare_host) || isNull(prepare_port)) && prepareList.length>0){
                layer.msg("请填写完整预发环境的域名和端口参数！");
                return;
            }

            if (isNull(tag)) {
                top.layer.msg("标签不能为空！");
                return;
            }
            if (isNull(name)) {
                top.layer.msg("项目名称不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/ops/project/edit/ajax/save",
                data:{
                    "project_id":project_id,"tag":tag,"name":name,"git_url":git_url,"note":note,"type":type,"developer":developer,
                    "production_host":production_host,"prepare_host":prepare_host,"test_host":test_host,
                    "production_port":production_port,"prepare_port":prepare_port,"test_port":test_port,
                    "productionList":productionList,"prepareList":prepareList,"testList":testList,
                    "productionValue":productionValue,"prepareValue":prepareValue,"testValue":testValue
                },
                traditional: true,
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg(data.msg)
                        setTimeout(function(){
                            parent.location.href="/ops/project?tag="+tag;
                        },1000);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function isNull(str){
            if (str == "" || str == null || typeof(str) == "undefined") {
                return true;
            }else{
                return false;
            }
        }
    </script>
    <style>
        #resource_table select{width: 165px}
        #resource_table input{width: 160px}
        #type {width:80px}
    </style>
</head>
<body>
<detail>
    <form>
    <h2>编辑项目配置（<a href="#" onclick="javascript:history.back(-1);" class="t2 noline">返回</a>）</h2>
    <hr/>
    <table>
        <tr>
        <tr>
            <td>标签</td>
            <td width="300px">
                <input type="text" id="tag" value="${project.tag!}"/>
            </td>
        </tr>
        <tr>
            <td>项目名称</td>
            <td><input type="text" id="name" value="${project.name!}"/></td>
            <td>
                <select id="type">
                    <#if project.type == 0>
                        <option value="0">api服务</option>
                        <option value="1">web服务</option>
                    </#if>
                    <#if project.type == 1>
                        <option value="1">web服务</option>
                        <option value="0">api服务</option>
                    </#if>
                </select>
            </td>
        </tr>
        <tr>
            <td>描述</td>
            <td><input type="text" id="note" value="${project.name!}"/></td>
        </tr>
        <tr>
            <td>开发人</td>
            <td>
                <select id="developer">
                    <option value="">选择开发人</option>
                    <#list users! as user>
                        <option value="${user.cn_name!}">${user.cn_name!}</option>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td>git地址</td>
            <td colspan="2">
                <input type="text" id="git_url" value="${project.git_url!}" class="longtxt"/>
            </td>
        </tr>
        <tr>
            <td>资源配置</td>
            <td colspan="2">
                <datagrid>
                    <table id="resource_table">
                        <thead>
                            <tr>
                                <th>项目</th>
                                <th>生产环境</th>
                                <th>预发环境</th>
                                <th>测试环境</th>
                            </tr>
                        </thead>
                        <tbody id="machine">
                            <tr>
                                <td>域名</td>
                                <td><input type="text" id="domain_production" value="${production.domain!}"/></td>
                                <td><input type="text" id="domain_prepare" value="${prepare.domain!}"/></td>
                                <td><input type="text" id="domain_test" value="${test.domain!}"/></td>
                            </tr>
                            <tr>
                                <td>端口</td>
                                <td><input type="text" id="port_production" value="${production.port_plan!}"/></td>
                                <td><input type="text" id="port_prepare" value="${prepare.port_plan!}"/></td>
                                <td><input type="text" id="port_test" value="${test.port_plan!}"/></td>
                            </tr>
                            <tr>
                                <td>机器</td>
                                <td>
                                    <select class="machine_production" onchange="checkTr(1)">
                                        <option value="">选择资源</option>
                                        <#list productionList as production>
                                            <option value="${production.server_id}">${production.name}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td>
                                    <select class="machine_prepare" onchange="checkTr(2)">
                                        <option value="">选择资源</option>
                                        <#list prepareList as prepare>
                                            <option value="${prepare.server_id}">${prepare.name}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td>
                                    <select class="machine_test" onchange="checkTr(3)">
                                        <option value="">选择资源</option>
                                        <#list testList as test>
                                            <option value="${test.server_id}">${test.name}</option>
                                        </#list>
                                    </select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </datagrid>
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