function FlowDisplay(diagramDiv) {
    var G = go.GraphObject.make;
    var _this = {};
    var _displayer = {};

    /** --------public method----------------------------------------**/

    /**
     * 显示流程图
     * @param flowData  流程图json数据
     */
    this.loadFlow = function (flowData) {

        if(!flowData) return;

        _displayer.model = go.Model.fromJson(flowData);

        var pos = _displayer.model.modelData.position;
        if (pos) _displayer.initialPosition = go.Point.parse(pos);

        // 更改所有连线中间的文本背景色
        setLinkTextBg();
    };

    /**
     * 获取流程图数据
     * @returns {*}
     */
    this.getFlowData = function () {
        _displayer.model.modelData.position = go.Point.stringify(_displayer.position);
        return _displayer.model.toJson();
    };

    /**
     * 动画显示流程路径状态
     */
    this.animateFlowPath = function(strStepKeys, isCompleted) {
        var stepKeys = strStepKeys.split(',');

        // 查找所有【已完成】步骤：【开始】-> 【已完成】（N个）
        var steps = findFinishedSteps(stepKeys, isCompleted);

        if(steps.length>1){
            showFinishedNodes(steps);

            //【开始】-> 【已完成】（N个）->【待处理】
            // 或
            //【开始】-> 【已完成】（N个）->【结束】
            var lastStep = findLastStep(stepKeys, steps, isCompleted);
            steps.push(lastStep);

            if (!isCompleted) {
                // “待处理”步骤，加上闪烁动画
                loopRunningNode(lastStep);
            }

            // 在连线上加闪烁动画
            var links = findFinishedLinks(steps);
            loopLinks(links);
        }
        // 高亮所有“已完成”步骤

    };
    /** --------public method-------------end---------------------------**/

    init(diagramDiv);

    /** --------private method----------------------------------------**/

    /**
     * 初始化流程设计器
     * @param divId 设计器Div
     */
    function init(divId) {
        _displayer = G(go.Diagram, divId,
            {
                allowDrop: false,
                allowSelect: true,
                allowHorizontalScroll: true,
                allowVerticalScroll: true,
                allowMove: false,
                allowLink: false,
                allowRelink: false,
                "draggingTool.dragsLink": false
            });

        // 流程步骤的样式模板
        _displayer.nodeTemplate = makeNodeTemplate();

        // 流程连接线的样式模板
        _displayer.linkTemplate = makeLinkTemplate();
    };

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
            G(go.Panel, "Auto",
                    { name: "PANEL" },
                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                G(go.Shape, "RoundedRectangle", // default figure
                        {
                            portId: "", // the default port: if no spot on link data, use closest side
                            name: "PIPE",
                            fromLinkable: true,
                            toLinkable: true,
                            cursor: "pointer",
                            fill: "#7e7e7f", // default color
                            strokeWidth: 1,
                            stroke: "#DDDDDD"
                        },
                        new go.Binding("figure"),
                        new go.Binding("stroke"),
                        new go.Binding("strokeDashArray"),
                        new go.Binding("strokeWidth"),
                        new go.Binding("fill")),
                G(go.TextBlock,
                        {
                            font: "bold 11pt Helvetica, Arial, sans-serif",
                            margin: 8,
                            maxSize: new go.Size(160, NaN),
                            wrap: go.TextBlock.WrapFit,
                            stroke: "white"
                        },
                        new go.Binding("text").makeTwoWay()),
                    {
                        toolTip: G(go.Adornment, "Auto",
                            G(go.Shape, { fill: "#FFFFCC" }),
                            G(go.TextBlock, { margin: 4 },
                                new go.Binding("text", "", nodeInfo))
                        )
                    }
                )
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
     * tooltip上显示的信息
     * @param d
     * @returns {string}
     */
    function nodeInfo(d) {
        if (!d.key) return "无key";
        return "编号：" + d.key;
    }

    /**
     * 定义连接线的样式模板
     * @returns {*}
     */
    function makeLinkTemplate(){
        return G(go.Link,
            { selectable: false },
            { relinkableFrom: true, relinkableTo: true, reshapable: true },
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5,
                toShortLength: 4
            },
            new go.Binding("layerName", "color"),
            new go.Binding("zOrder"),
            G(go.Shape, { isPanelMain: true, stroke: "black", strokeWidth: 3 }, new go.Binding("stroke"),new go.Binding("zOrder")),
            G(go.Shape, { isPanelMain: true, stroke: "gray", strokeWidth: 2 }),
            G(go.Shape, { isPanelMain: true, stroke: "white", strokeWidth: 1, name: "PIPE", strokeDashArray: [10, 10] }),
            G(go.Shape,
                { toArrow: "standard", stroke: null }, new go.Binding("stroke"), new go.Binding("fill"), new go.Binding("zOrder")),
            G(go.Panel, "Auto",
                G(go.Shape, {
                    fill: null,
                    stroke: null
                }, new go.Binding("fill", "pFill"), new go.Binding("zOrder")),
                G(go.TextBlock,
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#555555",
                        margin: 4
                    },
                    new go.Binding("text", "text"), new go.Binding("zOrder"))
            )
        );
    };

    /**
     * 返回所有【已完成】的步骤
     * @param stepKeys
     * @param isCompleted
     * @returns {Array}
     */
    function findFinishedSteps(stepKeys, isCompleted) {

        var arrStep = [];

        if (!stepKeys) return arrStep;

        var startStep = findStartStep();// 【开始】步骤
        arrStep.push(startStep);

        // 【已完成】的步骤
        var finishedCount = stepKeys.length - 1;// 不包含最后一个“待处理“步骤
        if (isCompleted) {
            finishedCount = stepKeys.length;// 包含所有步骤
        }
        for (var i = 0; i < finishedCount; i++) {
            var stepKey = stepKeys[i];
            var step = _displayer.findNodeForKey(stepKey);
            if (!step) continue;

            arrStep.push(step);
        }
        return arrStep;
    };

    /**
     * 高亮“已完成”步骤
     * @param steps
     */
    function showFinishedNodes(steps) {

        if (!steps) return;

        for (var i = 0; i < steps.length; i++) {
            var step = steps[i];

            // 步骤
            _displayer.startTransaction("vacate");
            if(step!=null){
                _displayer.model.setDataProperty(step.data, "fill", "#4fba4f");
            }
            _displayer.commitTransaction("vacate");
        }
    };

    /**
     *
     * 查找【开始】节点
     * @param {} steps
     * @returns {}
     */
    function findStartStep() {
        var startStep = null;
        _displayer.nodes.each(function(step) {

            if (step.data.hasOwnProperty('stepType') && step.data.stepType == 1) {
                startStep = step;
                return false;
            }
        });
        return startStep;
    };

    /**
     * 循环闪烁“已完成”步骤之间的连线
     * @param links
     */
    function loopLinks(links) {
        setTimeout(function () {
            showFinishedLinks(links);// “已完成”连线
            loopLinks(links);
        }, 300);
    };

    /**
     * 循环闪烁“待处理”步骤
     * @param node
     */
    function loopRunningNode(node) {
        setTimeout(function () {
            showRunningNode(node);
            loopRunningNode(node);
        }, 200);
    };

    /**
     * 高亮“待处理”步骤
     * @param node
     */
    function showRunningNode(node) {
        if (!node) return;

        _displayer.startTransaction("vacate");
        _displayer.model.setDataProperty(node.data, "fill", (node.data.fill === "#ff9001") ? "#ffB001" : "#ff9001");
        _displayer.commitTransaction("vacate");

        // 边框加上流水动画
        var shape = node.findObject("PIPE");
        var off = shape.strokeDashOffset - 2;
        shape.strokeDashOffset = (off <= 0) ? 20 : off;
    }

    /**
     * 获取最后一个步骤（【待处理】或【结束】）
     * @param stepKeys
     * @param steps
     * @param isCompleted
     * @returns {*}
     */
    function findLastStep(stepKeys, steps, isCompleted) {
        var lastStep;
        if (!isCompleted) {
            // 获取“待处理”步骤
            var lastKey = stepKeys[stepKeys.length - 1];
            var step = _displayer.findNodeForKey(lastKey);
            _displayer.startTransaction("vacate");
            _displayer.model.setDataProperty(step.data, "stroke", "red");
            _displayer.model.setDataProperty(step.data, "strokeWidth", 2);
            _displayer.model.setDataProperty(step.data, "strokeDashArray", [10, 10]);
            _displayer.commitTransaction("vacate");

            //【开始】-> 【已完成】（N个）->【待处理】
            lastStep = step;
        } else {
            // 用最后一根连线获取【结束】步骤
            var lastFinishedStep = steps[steps.length - 1];

            var it = lastFinishedStep.findLinksOutOf();
            var lastLink = it.first();
            var endStep = lastLink.toNode;

            //【开始】-> 【已完成】（N个）->【结束】
            lastStep = endStep;
        }

        return lastStep;
    };

    /**
     * 查找步骤之间的连线
     * @param steps
     * @returns {Array}
     */
    function findFinishedLinks(steps) {

        var arrLinks = [];

        if (!steps || steps.length < 1) return arrLinks;

        var currStep = steps[0];// 【开始】步骤

        for (var i = 0; i < steps.length; i++) {

            var step = steps[i];

            // 连线
            var link = currStep.findLinksBetween(step).first();
            if (!link) continue;
            arrLinks.push(link);

            currStep = step;
        }

        return arrLinks;
    };

    /**
     * 高亮所有“已完成”步骤的连线
     * @param links
     */
    function showFinishedLinks(links) {

        if (!links) return;

        for (var i = 0; i < links.length; i++) {

            // 连线
            var link = links[i];
            _displayer.startTransaction("vacate");
            _displayer.model.setDataProperty(link.data, "stroke", (link.data.stroke === "#4fba4f" ? "red" : "#4fba4f"));
            _displayer.model.setDataProperty(link.data, "fill", (link.data.fill === "#4fba4f" ? "red" : "#4fba4f"));
            _displayer.model.setDataProperty(link.data, "zOrder", 999);
            _displayer.commitTransaction("vacate");

            // 置于最上层，防止被遮挡
            _displayer.startTransaction('modified zOrder');
            _displayer.model.setDataProperty(link.data, "zOrder", 1);
            _displayer.commitTransaction('modified zOrder');

            // 连线加上流水动画
            var shape = link.findObject("PIPE");
            var off = shape.strokeDashOffset - 2;
            shape.strokeDashOffset = (off <= 0) ? 20 : off;
        }
    };

    /**
     * 更改所有连线中间的文本背景色
     */
    function setLinkTextBg() {
        _displayer.links.each(function (link) {
            _displayer.startTransaction("vacate");
            if (link.data.text) {
                _displayer.model.setDataProperty(link.data, "pFill", window.go.GraphObject.make(go.Brush, "Radial", {
                    0: "rgb(240, 240, 240)",
                    0.3: "rgb(240, 240, 240)",
                    1: "rgba(240, 240, 240, 0)"
                }));
            }
            _displayer.commitTransaction("vacate");
        });
    };

    /** --------private method------------------end----------------------**/

    return this;
};
