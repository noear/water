<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 存储监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="https://static.kdz6.cn/lib/echarts.all.min.js"></script>

</head>
<body>
<main>
    <toolbar>
        <cell>
            <div>实例：${name!} = ${model.id}（<a onclick="history.back(-1);" class="t2">返回</a>）</div>

            <#if type=='2'>
                <table>
                    <tr>
                        <th>配置：</th>
                        <td>${model.productType!}</td>
                        <th>最大IOPS：</th>
                        <td>${model.iops!}</td>
                        <th>所在区：</th>
                        <td>${model.zoneId!}</td>
                    </tr>
                </table>
            </#if>
            <#if type!='2'>

                <table>
                    <tr>
                        <th>配置：</th>
                        <td>${model.bandWith}MByte</td>
                        <th>内存：</th>
                        <td>${model.capacity}MB</td>
                        <th>最大连接数：</th>
                        <td>${model.maxCon}</td>
                        <th>QPS：</th>
                        <td>${model.qps}</td>
                        <th>所在区：</th>
                        <td>${model.zoneId}</td>
                    </tr>
                </table>

            </#if>


        </cell>
    </toolbar>

    <toolbar class="style2">
        <#if type=='2'>
            <cell><@stateselector items="连接数,CPU,内存,空间" clientID="type" onSelect="type_select"/></cell>
        </#if>
        <#if type!='2'>
            <cell><@stateselector items="连接数,CPU,内存,QPS " clientID="type" onSelect="type_select"/></cell>
        </#if>
        <cell><@stateselector items="1小时,6小时,1天,3天,7天,14天" clientID="time" onSelect="time_select"/></cell>
    </toolbar>

    <div id="chartsMonth" style="width:100%;height: 450px;margin: auto;;cursor: default;"></div>

</main>
</body>
</html>
<script>

    var datatype = 0;
    var datetype = 0;
    var chartsMonth = echarts.init(document.getElementById("chartsMonth"));
    option = {
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
                max: 100
            }
        ]
    };
    chartsMonth.setOption(option);




    function loadOption(data) {
        if('${type}' == '2'){
            chartsMonth.setOption(
                {
                    yAxis: [
                        {
                            type: 'value',
                            min: 0,
                            max: 100,
                            axisLabel: {
                                formatter: function (val) {
                                    var restmp = '';
                                    restmp = val + '%';
                                    return restmp;
                                }
                            },
                        }
                    ],
                    series: [
                        {
                            type: 'line',
                            smooth: true,
                            showSymbol: false,
                            hoverAnimation: false,
                            data: data,
                            markLine: {
                                data: [
                                    {type: 'average', name: '平均值'}
                                ]
                            }
                        }
                    ]
                }
            )
        }else{
            if ( datatype < 3) {
                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                                min: 0,
                                max: 100,
                                axisLabel: {
                                    formatter: function (val) {
                                        var restmp = '';
                                        restmp = val + '%';
                                        return restmp;
                                    }
                                },
                            }
                        ],
                        series: [
                            {
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data,
                                markLine: {
                                    data: [
                                        {type: 'average', name: '平均值'}
                                    ]
                                }
                            }
                        ]
                    }
                )
            } else {
                chartsMonth.setOption(
                    {
                        yAxis: [
                            {
                                type: 'value',
                                min: null,
                                max: null,
                                axisLabel: {
                                    formatter: function (val) {
                                        return val;
                                    }
                                },
                            }
                        ]
                        ,
                        series: [
                            {
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                hoverAnimation: false,
                                data: data,
                                markLine: {
                                    data: [
                                        {type: 'average', name: '平均值'}
                                    ]
                                }
                            }
                        ]
                    }
                )
            }
        }
    }


    function type_select(idx) {
        datatype = idx;
        $.post('/mot/dbs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            inId: '${id}',
            type: '${type}'
        }, function (data) {
            loadOption(data);
        })
    }

    function time_select(idx) {
        datetype = idx;
        $.post('/mot/dbs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            inId: '${id}',
            type: '${type}'
        }, function (data) {
            loadOption(data);
        })
    }


    $(function () {
        $.post('/mot/dbs/charts/ajax/reqtate', {
            dateType: datetype,
            dataType: datatype,
            inId: '${id}',
            type: '${type}'
        }, function (data) {
            loadOption(data);
        })
    })

    parent.window.onMenuHide = function () {
    };

</script>