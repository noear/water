<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 规则计算</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/flow/layer.js"></script>
    <script src="${js}/flow/go.js"></script>
    <script>
        var scheme_id = '${scheme.scheme_id}';
    </script>
    <script src="${js}/flow/flow-desinger.js?v2"></script>
    <script src="${js}/flow/flow-display.js?v2"></script>
    <style>
    </style>
</head>
<body>
<blockquote class="ln30">
    <h2><a href="#" onclick="history.back(-1)" class="noline">计算方案</a></h2> / 流程设计 :: ${scheme.name_display!}
</blockquote>
<main class="frm">

    <toolbar>
        <left>
            <!--  控件 -->
            <div id="myPaletteDiv" style="height: 80px;width: 650px; text-align: left;"></div>
        </left>
        <right style="vertical-align: middle">
                <#if is_operator ==1>
                <button id="btnSave" onclick="saveDesigner()" class="edit">保存</button>
                </#if>
                <#if scheme.debug_args??>
                    &nbsp;&nbsp;
                    <form action="${raas_uri!}/debug" target="_blank" method="get">
                        <input type="hidden" name="scheme" value="${scheme.tag!}/${scheme.name!}">
                        <input type="hidden" name="args" value="${scheme.debug_args!}">
                        <input type="hidden" name="type" value="3">
                        <input type="hidden" name="policy" value="2001">

                        <button type="submit" >调试流程</button>
                    </form>
                </#if>
        </right>
    </toolbar>

    <detail>
        <!--  设计面板 -->
        <div id="myFlowDesignerDiv" style="border: solid 1px black; height: 400px;"></div>

        <!--  其它附助 -->
        <textarea id="mySavedModel" style="width:100%;height:300px;display: none;">${design.details!}</textarea>

        <div id="layerdiv" style="display: none;"></div>
    </detail>
</main>
</body>
<script type="text/javascript">
    var areaFlow = document.getElementById('mySavedModel');

    // 流程图设计器
    var  myDesigner= new FlowDesigner('myFlowDesignerDiv');
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
            url:"/rubber/scheme/flow/ajax/savedesign",
            data:{"scheme_id":${scheme.scheme_id},"details":myDesigner.getFlowData()},
            success:function (data) {
                top.layer.msg(data.msg);
            }
        });
    };

</script>
</html>