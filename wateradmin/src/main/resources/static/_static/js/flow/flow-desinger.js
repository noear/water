function FlowDesigner(diagramDiv) {
    var G = go.GraphObject.make;
    var _this = {};
    var _designer = {};
    var _jsonNewStep = { width:30,height:30, key: guid(), text: "执行", remark: '', fill: "#1CA9C7",figure:"execute" };
    var _otherSelect = {width:30,height:30, key: guid(), text: "排他", remark: '', fill: "#1CA9C7", figure: "Diamond" };
    var _andSelect = { width:30,height:30,key: guid(), text: "并行", remark: '', fill: "#1CA9C7",figure: "Parallelogram2"};
    var _totalSect = { width:30,height:30,key: guid(), text: "汇聚", remark: '', fill: "#1CA9C7",figure: "StopSign"};
    /** --------public method----------------------------------------**/
    /**
     * 初始化图例面板
     * @returns {*}
     */
    this.initToolbar = function(div){
        var myPalette =
            G(go.Palette, div, // 必须是DIV元素
                {
                    maxSelectionCount: 6,
                    nodeTemplateMap: _designer.nodeTemplateMap, // 跟设计图共同一套样式模板
                    model: new go.GraphLinksModel([
                        { width:30,height:30,key: guid(), text: "开始", figure: "Circle", fill: "#7BC726", stepType: 1 },
                        _jsonNewStep,
                        _otherSelect,
                        _andSelect,
                        _totalSect,
                        { width:30,height:30,key: guid(), text: "结束", figure: "Circle", fill: "#DF3A18", stepType: 4 }
                    ])
                });

        return myPalette;
    };

    /**
     * 在设计面板中显示流程图
     * @param flowData  流程图json数据
     */
    this.displayFlow = function (flowData) {

        if(!flowData) return;

        _designer.model = go.Model.fromJson(flowData);

        var pos = _designer.model.modelData.position;
        if (pos) _designer.initialPosition = go.Point.parse(pos);

        // 更改所有连线中间的文本背景色
        setLinkTextBg();
    };

    /**
     * 创建新步骤
     */
    this.createStep = function() {
        var jsonNewStep = {key:_jsonNewStep.key, text:_jsonNewStep.text};
        jsonNewStep.loc = "270 140";// “新步骤”显示的位置
        _designer.model.addNodeData(jsonNewStep);
    };

    /**
     * 获取流程图数据
     * @returns {*}
     */
    this.getFlowData = function () {
        _designer.model.modelData.position = go.Point.stringify(_designer.position);
        return _designer.model.toJson();
    };

    /**
     * 检验流程图是否规范
     */
    this.checkData = function() {
        var errMsg = "";

        // 检查：每个步骤必须包含角色
        if (!_designer.model.nodeDataArray) return '请图';

        $.each(_designer.model.nodeDataArray, function(i, item) {
            if (!item.hasOwnProperty("remark") || item.remark === "") {
                errMsg = "请为步骤【" + item.text + "】设置备注~";
                return false;
            }
        });

        return errMsg;
    };

    /** --------public method-------------end---------------------------**/

    init(diagramDiv);

    /** --------private method----------------------------------------**/

    /**
     * 初始化流程设计器
     * @param divId 设计器Div
     */
    function init(divId) {
        _designer = G(go.Diagram, divId, // must name or refer to the DIV HTML element
                {
                    // grid: G(go.Panel, "Grid",
                    //     G(go.Shape, "LineH", { stroke: "lightgray", strokeWidth: 0.5 }),
                    //     G(go.Shape, "LineH", { stroke: "gray", strokeWidth: 0.5, interval: 10 }),
                    //     G(go.Shape, "LineV", { stroke: "lightgray", strokeWidth: 0.5 }),
                    //     G(go.Shape, "LineV", { stroke: "gray", strokeWidth: 0.5, interval: 10 })
                    // ),
                    allowDrop: true, // must be true to accept drops from the Palette
                    allowTextEdit: false,
                    allowHorizontalScroll: false,
                    allowVerticalScroll: false,
                    // "clickCreatingTool.archetypeNodeData": _jsonNewStep, // 双击创建新步骤
                    "draggingTool.dragsLink": true,
                    "draggingTool.isGridSnapEnabled": true,
                    "linkingTool.isUnconnectedLinkValid": true,
                    "linkingTool.portGravity": 20,
                    "relinkingTool.isUnconnectedLinkValid": true,
                    "relinkingTool.portGravity": 20,
                    "relinkingTool.fromHandleArchetype":
                        G(go.Shape, "Diamond", { segmentIndex: 0, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "tomato", stroke: "darkred" }),
                    "relinkingTool.toHandleArchetype":
                        G(go.Shape, "Diamond", { segmentIndex: -1, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "darkred", stroke: "tomato" }),
                    "linkReshapingTool.handleArchetype":
                        G(go.Shape, "Diamond", { desiredSize: new go.Size(7, 7), fill: "lightblue", stroke: "deepskyblue" }),
                    "undoManager.isEnabled": true
         });

        // 流程图如果有变动，则提示用户保存
        _designer.addDiagramListener("Modified", onDiagramModified);

        // 双击事件
        _designer.addDiagramListener("ObjectDoubleClicked", onObjectDoubleClicked);

        // 流程步骤的样式模板
        _designer.nodeTemplate = makeNodeTemplate();

        // 流程连接线的样式模板
        _designer.linkTemplate = makeLinkTemplate();

    };

    /**
     * 生成GUID
     * @returns {string}
     */
    function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    /**
     * 步骤图的样式模板
     * @returns {*}
     */
    function makeNodeTemplate(){
        return G(go.Node, "Spot",
            { locationSpot: go.Spot.Center },
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            { selectable: true, selectionAdornmentTemplate: makeNodeSelectionAdornmentTemplate() },
            new go.Binding("angle").makeTwoWay(),
            // the main object is a Panel that surrounds a TextBlock with a Shape
            G(go.Panel, "Auto",
                { name: "PANEL" },
                new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                G(go.Shape, "RoundedRectangle", // default figure
                    {
                        portId: "", // the default port: if no spot on link data, use closest side
                        fromLinkable: true,
                        toLinkable: true,
                        cursor: "pointer",
                        fill: "#7e7e7f", // 默认背景色
                        strokeWidth: 1,
                        stroke: "#DDDDDD"
                    },
                    new go.Binding("figure"),
                    new go.Binding("fill")),
                G(go.TextBlock,
                    {
                        font: "bold 11pt Helvetica, Arial, sans-serif",
                        margin: 8,
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        editable: true,
                        stroke: "white"
                    },
                    new go.Binding("text").makeTwoWay()), // the label shows the node data's text
                {
                    toolTip:// this tooltip Adornment is shared by all nodes
                        G(go.Adornment, "Auto",
                            G(go.Shape, { fill: "#FFFFCC" }),
                            G(go.TextBlock, { margin: 4 }, // the tooltip shows the result of calling nodeInfo(data)
                                new go.Binding("text", "", nodeInfo))
                        ),
                    // 绑定上下文菜单
                    contextMenu: makePartContextMenu()
                }
            ),
            // 4个连接点
            makeNodePort("T", go.Spot.Top, false, true),
            makeNodePort("L", go.Spot.Left, true, true),
            makeNodePort("R", go.Spot.Right, true, true),
            makeNodePort("B", go.Spot.Bottom, true, false),
            {
                mouseEnter: function (e, node) { showNodePort(node, true); },
                mouseLeave: function (e, node) { showNodePort(node, false); }
            }
        );
    }

    /**
     * 选中节点的样式
     * @returns {*}
     */
    function makeNodeSelectionAdornmentTemplate(){
        return G(go.Adornment, "Auto",
            G(go.Shape, { fill: null, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2] }),
            G(go.Placeholder)
        );
    }

    /**
     * 创建连接点
     * @param name
     * @param spot
     * @param output
     * @param input
     * @returns {*}
     */
    function makeNodePort(name, spot, output, input) {
        // the port is basically just a small transparent square
        return G(go.Shape, "Circle",
            {
                fill: null, // not seen, by default; set to a translucent gray by showSmallPorts, defined below
                stroke: null,
                desiredSize: new go.Size(6, 6),
                alignment: spot, // align the port on the main Shape
                alignmentFocus: spot, // just inside the Shape
                portId: name, // declare this object to be a "port"
                fromSpot: spot,
                toSpot: spot, // declare where links may connect at this port
                fromLinkable: output,
                toLinkable: input, // declare whether the user may draw links to/from here
                cursor: "pointer" // show a different cursor to indicate potential link point
            });
    };

    /**
     * tooltip上显示的信息
     * @param d
     * @returns {string}
     */
    function nodeInfo(d) {
        return '双击或单击右键可编辑';
    }

    /**
     * 右键菜单
     * @returns {*}
     */
    function makePartContextMenu(){
        return G(go.Adornment, "Vertical",
            makeMenuItem("编辑",
                function(e, obj) { // OBJ is this Button
                    var contextmenu = obj.part; // the Button is in the context menu Adornment
                    var part = contextmenu.adornedPart; // the adornedPart is the Part that the context menu adorns
                    // now can do something with PART, or with its data, or with the Adornment (the context menu)
                    showEditNode(part);
                }),
            makeMenuItem("剪切",
                function(e, obj) { e.diagram.commandHandler.cutSelection(); },
                function(o) { return o.diagram.commandHandler.canCutSelection(); }),
            makeMenuItem("复制",
                function(e, obj) { e.diagram.commandHandler.copySelection(); },
                function(o) { return o.diagram.commandHandler.canCopySelection(); }),
            makeMenuItem("删除",
                function(e, obj) { e.diagram.commandHandler.deleteSelection(); },
                function(o) { return o.diagram.commandHandler.canDeleteSelection(); })
        );
    };

    /**
     * 生成右键菜单项
     * @param text
     * @param action
     * @param visiblePredicate
     * @returns {*}
     */
    function makeMenuItem(text, action, visiblePredicate) {
        return G("ContextMenuButton",
            G(go.TextBlock, text, {
                margin: 5,
                textAlign: "left",
                stroke: "#555555"
            }),
            { click: action },
            // don't bother with binding GraphObject.visible if there's no predicate
            visiblePredicate ? new go.Binding("visible", "", visiblePredicate).ofObject() : {});
    };

    /**
     * 是否显示步骤的连接点
     * @param node
     * @param show
     */
    function showNodePort(node, show) {
        node.ports.each(function (port) {
            if (port.portId !== "") { // don't change the default port, which is the big shape
                port.fill = show ? "rgba(255,0,0,.5)" : null;
            }
        });
    };

    /**
     * 连接线的选中样式
     * @returns {*}
     */
    function makeLinkSelectionAdornmentTemplate(){
        return G(go.Adornment, "Link",
            G(go.Shape,
                // isPanelMain declares that this Shape shares the Link.geometry
                { isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0 }) // use selection object's strokeWidth
        );
    };

    /**
     * 定义连接线的样式模板
     * @returns {*}
     */
    function makeLinkTemplate(){
        return G(go.Link, // the whole link panel
            { selectable: true, selectionAdornmentTemplate: makeLinkSelectionAdornmentTemplate() },
            { relinkableFrom: true, relinkableTo: true, reshapable: true },
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5,
                toShortLength: 4
            },
            G(go.Shape, // 线条
                { stroke: "black" }),
            G(go.Shape, // 箭头
                { toArrow: "standard", stroke: null }),
            G(go.Panel, "Auto",
                G(go.Shape, // 标签背景色
                    {
                        fill: null,
                        stroke: null
                    }, new go.Binding("fill", "pFill")),
                G(go.TextBlock, // 标签文本
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#555555",
                        margin: 4
                    },
                    new go.Binding("text", "text")), // the label shows the node data's text
                {
                    toolTip:// this tooltip Adornment is shared by all nodes
                        G(go.Adornment, "Auto",
                            G(go.Shape, { fill: "#FFFFCC" }),
                            G(go.TextBlock, { margin: 4 }, // the tooltip shows the result of calling nodeInfo(data)
                                new go.Binding("text", "", nodeInfo))
                        ),
                    // this context menu Adornment is shared by all nodes
                    contextMenu: makePartContextMenu()
                }
            )
        );
    };

    /**
     * 流程图元素的双击事件
     * @param ev
     */
    function onObjectDoubleClicked(ev) {
        var part = ev.subject.part;
        showEditNode(part);
    };

    /**
     * 流程图如果有变动，则提示用户保存
     * @param e
     */
    function onDiagramModified(e){
        var button = document.getElementById("btnSaveFlow");
        if (button) button.disabled = !_designer.isModified;
        var idx = document.title.indexOf("*");
        if (_designer.isModified) {
            if (idx < 0) document.title += "*";
        } else {
            if (idx >= 0) document.title = document.title.substr(0, idx);
        }
    };

    /**
     * 编辑节点信息
     */
    function showEditNode(node) {
        if ((node instanceof go.Node) && node.data.figure === 'Circle') {
            return;
        }
        var key = node.data.key;
        var text = node.data.text;

        var figure = node.data.figure;

        if (figure == 'execute') {
            //执行节点
            $.get("/rubber/scheme/flow/excute?node_id="+key+"&scheme_id="+scheme_id, function(result){
                $("#layerdiv").html(result);
            });

            layer.open({
                type: 1,
                title: '执行节点编辑',
                area: ['600px', '500px'],
                shadeClose: false, //点击遮罩关闭
                content: $('#layerdiv'),
                btn: ['确定', '取消'],

                yes: function (index, layero) {//添加人员
                    //做数据校验
                    var name = $('#nodeName').val();
                    var actor = $('#manager').val();
                    if (!name) {
                        layer.msg('节点名称不能为空');
                        return;
                    }

                    var task = $('#taskAll').children().children('select');
                    var count = 0;
                    var tasks = "";
                    task.each(function(){
                        var value = $(this).val();
                        if(count%2 == 0){
                            tasks = tasks + value+",";
                        } else {
                            tasks = tasks + value + ";";
                        }
                        count = count + 1;
                    });
                    var result = saveExcuteNode(scheme_id,key,name,actor,tasks);
                    if (result == true) {
                        updateNodeData(node,name);
                        saveFlow();
                        layer.msg('保存成功');
                        $('#layerdiv').css("display", "none");

                        layer.close(index);
                    } else {
                        layer.msg('保存失败');
                    }

                },
                btn2: function (index, layero) {
                    return;
                },
                cancel: function () {
                    return;
                },
                end: function () {
                    $('#layerdiv').css("display", "none");
                }
            });
        }

        var is_line_from = node.je.from;
        var is_line_to = node.je.to;
        var line_head = _designer.findNodeForKey(is_line_from);
        if (line_head) {
            var _figure = line_head.data.figure;
            if (is_line_from && (_figure == 'Diamond' || _figure == 'Parallelogram2' || _figure == 'StopSign')) {
                var node_key = is_line_from+is_line_to;
                var title = line_head.data.text;
                //分支节点
                $.get("/rubber/scheme/flow/branch?node_key="+node_key+"&scheme_id="+scheme_id, function(result){
                    $("#layerdiv").html(result);
                });
                layer.open({
                    type: 1,
                    title: '分支条件编辑',
                    area: ['600px', '500px'],
                    shadeClose: false, //点击遮罩关闭
                    content: $('#layerdiv'),
                    btn: ['确定', '取消'],

                    yes: function (index, layero) {//
                        var name = $("#input_name_display").val();

                        if(!name){
                            layer.msg("请输入节点名称");
                            return;
                        }
                        var tableRow = $("#tr_expr").find("div");
                        var expressions = new Array();
                        for(i = 0; i < tableRow.length; i++){
                            var row = tableRow[i];
                            var left = $(row).find("#select_left option:selected");
                            var center = $(row).find("#select_center");
                            var right = $(row).find("#input_right");
                            var next = $(row).find("#select_next");

                            var name_dispaly = right.val();
                            var fun_name = "";
                            var right_key = getFunNameByNameDisplay(right.val());
                            if(right_key != null){
                                fun_name = right_key;
                            }

                            var item = {
                                "left": left.val(),
                                "leftValue" : left.text(),
                                "center": center.val(),
                                "right": fun_name,
                                "rightValue" : name_dispaly,
                                "ct" : next.val()
                            };
                            expressions.push(item);
                        }
                        var expressionValue = JSON.stringify(expressions);

                        var result = saveBranchNode(scheme_id,node_key,name,expressionValue);
                        if (result == true) {

                            updateNodeData(node,name);
                            saveFlow();
                            layer.msg("保存成功");
                            $('#layerdiv').css("display", "none");
                            layer.close(index);

                        } else {
                            layer.msg('保存失败');
                        }
                    },
                    btn2: function (index, layero) {
                        return;
                    },
                    cancel: function () {
                        return;
                    },
                    end: function () {
                        $('#layerdiv').css("display", "none");
                    }
                });
            }
        }
    };

    //向后台提交执行节点编辑保存
    function saveExcuteNode(scheme_id,node_id,name,actor,tasks) {
        var flag = false;
        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/saveExcuteNode",
            async: false,
            data:{"scheme_id":scheme_id,"node_id":node_id,"name":name,"actor":actor,"tasks":tasks},
            success:function (data) {
                flag = data;
            }
        });
        return flag;
    };

    //向后台提交分支节点编辑保存
    function saveBranchNode(scheme_id,node_id,name,condition) {
        var flag = false;
        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/saveBranchNode",
            async: false,
            data:{"scheme_id":scheme_id,"node_id":node_id,"name":name,"condition":condition},
            success:function (data) {
                flag = data;
            }
        });
        return flag;
    };

    //保存画板数据
    function saveFlow() {
        areaFlow.value = myDesigner.getFlowData();

        $.ajax({
            type:"POST",
            url:"/rubber/scheme/flow/ajax/savedesign",
            data:{"scheme_id":scheme_id,"details":myDesigner.getFlowData()},
            success:function (data) {

            }
        });
    };


    /**
     * 更新节点信息
     * @param oldData
     * @param newData
     */
    function updateNodeData(node, text) {
        _designer.startTransaction("vacate");
        _designer.model.setDataProperty(node.data, "text", text);
        _designer.commitTransaction("vacate");
    };

    /**
     * 更改所有连线中间的文本背景色
     */
    function setLinkTextBg() {
        _designer.links.each(function (link) {
            _designer.startTransaction("vacate");
            if (link.data.text) {
                _designer.model.setDataProperty(link.data, "pFill", window.go.GraphObject.make(go.Brush, "Radial", {
                    0: "rgb(240, 240, 240)",
                    0.3: "rgb(240, 240, 240)",
                    1: "rgba(240, 240, 240, 0)"
                }));
            }
            _designer.commitTransaction("vacate");
        });
    };

    /** --------private method------------------end----------------------**/

    return this;
};
