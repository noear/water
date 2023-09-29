<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 负载监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="//mirror.noear.org/lib/echarts.all.min.js"></script>
    <style>
        toolbar th{color: #666;}
        toolbar td{padding-right: 20px;}
    </style>
</head>
<body>
<main>
    <toolbar>
            <div>实例：${model.instanceName} = ${model.instanceId}（<a onclick="history.back(-1);" class="t2">返回</a>）</div>

            <table>
                <tr>
                    <th>规格：</th><td>${model.loadBalancerSpec}</td>
                    <th>最大连接：</th><td>${model.maxCon}</td>
                    <th>每秒新建连接数 ：</th><td>${model.cps}</td>
                    <th>最大QPS：</th><td>${model.qps}</td>
                    <th>所在区：</th>
                    <td>${model.regionId}</td>
                </tr>
            </table>

    </toolbar>

    <flex class="mar10-b">
        <left class="col-6"><@stateselector items="并发连接数,新键连接数,QPS,流量" clientID="type" onSelect="type_select"/></left>
        <right class="col-6"><@stateselector items="1小时,6小时,1天,3天,7天,14天" clientID="time" onSelect="time_select"/></right>
    </flex>

    <div id="chartsMonth" style="width:100%;height: calc(100vh - 160px);margin: auto;;cursor: default;"></div>
</main>
</body>
</html>
<script>

    var datatype = 0;
    var datetype = 0;
    var chartsMonth = echarts.init(document.getElementById("chartsMonth"));
    var option = {
        grid: {
            left: '3%',
            right: '5%',
            bottom: '3%',
            containLabel: true
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'time',
                boundaryGap: false
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ]
    };
    chartsMonth.setOption(option);

    function loadOption(data) {
        chartsMonth.clear();
        chartsMonth.setOption(option);
        if(datatype<2){
            if(datatype==0){
                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                            }
                        ],
                        series: [
                            {
                                name:'并发数量',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[0]
                            },
                            {
                                name:'非活跃',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[1]
                            },
                            {
                                name:'活跃',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[2]
                            },
                        ]
                    }
                )
            }else{
                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                            }
                        ],
                        series: [
                            {
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[0]
                            }
                        ]
                    }
                )
            }

        }else{
            if(datatype==3){
                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                            }
                        ]
                        ,
                        series: [
                            {
                                name:'流量流出',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[0]
                            },
                            {
                                name:'流量流入',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[1]
                            }
                        ]
                    }
                )
            }else{
                var line_series = [];

                for(var i in data){
                    line_series.push({
                        name:data[i][0].label,
                        type: 'line',
                        smooth: true,
                        showSymbol: false,
                        hoverAnimation: false,
                        data: data[i]
                    });
                }

                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                            }
                        ]
                        ,
                        series:line_series
                    }
                )
            }

        }


    }


    function type_select(idx) {
        datatype = idx;
        $.post('/mot/bls/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instanceId!}'
        }, function (data) {
            loadOption(data);
        })
    }

    function time_select(idx) {
        datetype = idx;
        $.post('/mot/bls/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instanceId!}'
        }, function (data) {
            loadOption(data);
        })
    }


    $(function () {
        $.post('/mot/bls/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instanceId!}'
        }, function (data) {
            loadOption(data);
        })
    })

    parent.window.onMenuHide = function () {
    };

</script>