<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 机器监控</title>
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
            <div>实例：${instance.instanceName} = ${instance.instanceId}（<a onclick="history.back(-1);" class="t2">返回</a>）</div>

            <table>
                <tr>
                    <th>配置：</th>
                    <td>${instance.cpu}核/${instance.memory/1000}G/${size}G
                    </td>
                    <th>带宽：</th>
                    <td>${instance.internetMaxBandwidthOut}Mbps</td>
                    <th>所在区：</th>
                    <td>${instance.zoneId}</td>
                </tr>
            </table>
    </toolbar>

    <flex class="mar10-b">
        <left class="col-6"><@stateselector items="CPU,内存,空间,带宽,TCP" clientID="type" onSelect="type_select"/></left>
        <right class="col-6"><@stateselector items="1小时,6小时,1天,3天,7天,14天" clientID="time" onSelect="time_select"/></right>
    </flex>

    <div id="chartsMonth" style="width:100%;height: 450px;margin: auto;;cursor: default;"></div>

</main>
</body>
</html>
<script type="text/javascript">

    var datatype = 0;
    var datetype = 0;
    var chartsMonth = echarts.init(document.getElementById("chartsMonth"));
    var option = {
        grid: {
            left: '3%',
            right: '4%',
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
                type: 'value',
                splitNumber: 10,
                min: 0,
                max:100,
                axisLabel: {
                    formatter: function (val) {
                        var restmp='';
                        switch (datatype) {
                            case 0:
                                restmp= val + '%';
                                break;
                            case 1:
                                restmp= val + '%';
                                break;
                            case 2:
                                restmp= val + '%';
                                break;
                            case 3:
                                restmp= val + '%';
                                break;
                            case 4:
                                restmp= val ;
                                break;
                        }
                        return restmp;
                    }
                },
            }
        ]
    };
    chartsMonth.setOption(option);

    function loadOption(data) {
        chartsMonth.clear();
        chartsMonth.setOption(option);
        if(datatype<3){
            var option2 = {
                yAxis: [
                    {
                        type: 'value',
                        splitNumber: 10,
                        min: 0,
                        max: 100
                    }
                ],
                series: []
            };

            if(datatype == 2){
                for(var i in data){
                    option2.series.push({
                            name:data[i][0].label,
                            type: 'line',
                            smooth: true,
                            showSymbol: false,
                            hoverAnimation: false,
                            data: data[i]
                        });
                }
            }else {
                option2.series.push({
                        type: 'line',
                        smooth: true,
                        showSymbol: false,
                        hoverAnimation: false,
                        data: data[0]
                    });
            }

            chartsMonth.setOption(option2);
        }else{
            if(datatype==3){
                chartsMonth.setOption(
                    {
                        // yAxis: [
                        //     {
                        //         type: 'value',
                        //         splitNumber: 10,
                        //         min: null,
                        //         max:null,
                        //         axisLabel: {
                        //             formatter: function (val) {
                        //                 return (val).toFixed(2) +'Kbps';
                        //             }
                        //         },
                        //     }
                        // ]
                        // ,
                        yAxis: [
                            {
                                type: 'value',
                                splitNumber: 10,
                                min: 0,
                                max: null
                            }
                        ],
                        series: [
                            {
                                name:'流出宽带',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data[0]
                            },
                            {
                                name:'流入宽带',
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
                var option2 = {
                    yAxis: [
                        {
                            type: 'value',
                            splitNumber: 10,
                            min: null,
                            max:null
                        }
                    ],
                    series: []
                };

                for(var i in data){
                    option2.series.push({
                        name:data[i][0].label,
                        type: 'line',
                        smooth: true,
                        showSymbol: false,
                        hoverAnimation: false,
                        data: data[i]
                    });
                }

                chartsMonth.setOption(option2);
            }
        }
    }


    function type_select(idx) {
        datatype = idx;
        $.post('/mot/ecs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instance.instanceId}'
        }, function (data) {
            loadOption(data);
        })
    }

    function time_select(idx) {
        datetype = idx;
        $.post('/mot/ecs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instance.instanceId}'
        }, function (data) {
            loadOption(data);
        })
    }


    $(function () {
        $.post('/mot/ecs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            instanceId: '${instance.instanceId}'
        }, function (data) {
            loadOption(data);
        })
    })

    parent.window.onMenuHide = function () {
    };


</script>