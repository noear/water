<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 行为记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script src="${js}/laydate/laydate.js"></script>

    <style>
        .tabs{padding-bottom: 10px;}
        .tabs a.btn{margin: 0 5px 5px 5px!important;}

        .log a{text-decoration:underline; cursor: default;}
    </style>
    <script>
        function queryDo(startId) {
            if(!startId){
                startId=0;
            }

            urlQueryByDic({
                serviceName:'${serviceName!}',
                operator:$('#operator').val(),
                time:$('#time').val(),
                path:$('#path').val(),
                startId:startId
            });
        }

        $(function (){
            $(".log a").click(function (){
                let path = $(this).attr('path');
                if(path){
                    urlQueryBy("path",path,'page');
                    return
                }

                let time = $(this).attr('time');
                if(time){
                    urlQueryBy("time",time,'page');
                    return;
                }
            });

            bindQueryStringAndSort();
        });
    </script>
</head>
<body>

<datalist id="datalist" >
    <#list peratorList as m>
        <option value="${m.tag}">${m.tag}</option>
    </#list>
</datalist>

<main>
    <div class="tabs">
        <tabbar>
            <#list tabs as m>
                <#if m.tag == serviceName>
                    <a id="e${m.tag}" class="btn sel">${m.tag}</a>
                <#else>
                    <a id="e${m.tag}" class="btn" href="/mot/behavior/inner?tag_name=${tag_name!}&serviceName=${m.tag}">${m.tag}</a>
                </#if>
            </#list>
        </tabbar>
    </div>

    <toolbar>
        <left>
            <input type="text"  name="time"  id="time"
                   jt-laydate="datetime"
                   placeholder="yyyy-MM-dd HH:mm:ss"
                   autocomplete="off"
                   class="w150 sml"/>
            <input type="text"  id="operator" placeholder="操作人" autocomplete="off" list="datalist" class="w100"/>
            <input type="text"  id="path" placeholder="路径"  class="w200"/>
            <button type="button" onclick="queryDo()">查询</button>
        </left>
        <right>
            <@stateselector items="ALL,SEL,UPD,INS,DEL"/>
        </right>
    </toolbar>
    <datagrid class="list">
        <table>
            <thead>
            <td width="90px">
                时间
            </td>
            <td width="90px">操作人</td>
            <td class="left">路径/代码</td>
            </thead>
            <tbody>
            <#list list as log>
                <tr class="log">
                    <td>
                        <a time="${(log.log_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}">${(log.log_fulltime?string('yyyy-MM-dd HH:mm:ss'))!}</a>
                    </td>
                    <td>${log.tag2!}</td>
                    <td class="left break">
                        <div>*${log.trace_id!} <a path="${log.tag1!}">${log.tag1!}</a> (${log.from!})</div>
                        <div style="font-size: small">${log.content!}</div>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
    <#if pageSize == listSize>
        <div class="center mar15">
            <a onclick="queryDo(${lastId!0})" class="btn">下一页</a>
        </div>
    </#if>
</main>

</body>
</html>