<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 规则计算</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/flow2/layer.js"></script>
    <script src="${js}/flow2/go.js"></script>
    <script>
        var project_id = '${project_id}';
        var deploy_id = '${deploy.deploy_id}';
    </script>
    <script src="${js}/flow2/flow-desinger.js?v267"></script>
    <script src="${js}/flow2/flow-display.js?v2"></script>
</head>
<body>
<main class="frm">
    <h2>${deploy.name!}（<a onclick="history.back(-1)" class="t2">返回</a>）</h2>
    <hr />

    <toolbar>
        <left>
            <!--  控件 -->
            <div id="myPaletteDiv" style="height: 80px;width: 800px; text-align: left;"></div>
        </left>
        <right style="vertical-align: middle">
                <#if is_operator ==1>
                <button id="btnSave" onclick="saveDesigner()" class="edit">保存</button>
                </#if>
        </right>
    </toolbar>

    <detail>
        <!--  设计面板 -->
        <div id="myFlowDesignerDiv" style="border: solid 1px black; height: 400px;"></div>

        <!--  其它附助 -->
        <textarea id="mySavedModel" style="width:100%;height:300px;display: none;">${deploy.design_detail!}</textarea>

        <div id="layerdiv" style="display: none;"></div>
    </detail>
</main>
</body>
<script type="text/javascript">
    var areaFlow = document.getElementById('mySavedModel');

    // 流程图设计器
    var myDesigner= new FlowDesigner('myFlowDesignerDiv');
    myDesigner.initToolbar('myPaletteDiv');// 初始化控件面板
    myDesigner.displayFlow(areaFlow.value);// 在设计面板中显示流程图


    function showFlowPath() {
        var flowPath =  $.trim($('#txtFlowPath').val());
        var isCompleted = $('#chkIsCompleted').is(':checked');
        myDisplay.loadFlow(areaFlow.value);
        myDisplay.animateFlowPath(flowPath, isCompleted);
    };

    function saveDesigner() {
        areaFlow.value = myDesigner.getFlowData();
        $.ajax({
            type:"POST",
            url:"/ops/project/deploy_design/ajax/save",
            data:{
                "project_id":project_id,
                "deploy_id":deploy_id,
                "details":myDesigner.getFlowData()
            },
            success:function (data) {
                layer.msg(data.msg);
            }
        });
    };

</script>
</html>