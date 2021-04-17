<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 性能监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="//mirror.noear.org/lib/echarts.min.js"></script>
    <style>
        tbody td{text-align: left;}
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>
    <base target="_blank">
</head>
<body>
<toolbar>
    <left>
    ${tag}::${name!}&nbsp;&nbsp;<a onClick="javascript :history.back(-1);" class="t2">(返回)</a>
    </left>
    <right>
        <button type="button" onclick="location.reload()">刷新</button>
    </right>
</toolbar>
<block>
    <toolbar>
        <div style="padding: 5px;overflow: hidden;">
            <@stateselector items="请求次数,1s+次数,2s+次数,5s+次数,平均响应,最快响应,最慢响应" clientID="chart1" onSelect="chart1_select"/>
        </div>
        <div id="chartsReqTate" style="width:100%;height: 220px;margin: auto;;cursor: default;"></div>
    </toolbar>
</block>
<block>
    <toolbar>
        <div id="chartsMonth" style="width:100%;height: 230px;margin: auto;;cursor: default;"></div>
    </toolbar>
</block>

</body>
<script>
    var hoursList=['0', '1', '2', '3', '4', '5', '6','7', '8', '9', '10', '11', '12', '13','14', '15', '16', '17', '18', '19', '20','21', '22', '23'];

    function chart1_select(type) {
        $.ajax({
            url:"/mot/speed/charts/ajax/reqtate",
            data:{tag:'${tag}', name_md5:'${name_md5}', service:'${service}', type:type},
            success:function(data){
                chartsReqTate.setOption({
                    series: [
                        {
                            name: '今日',
                            data: data.speedReqTate.today
                        },
                        {
                            name: '昨日',
                            data: data.speedReqTate.yesterday
                        },
                        {
                            name: '前日',
                            data: data.speedReqTate.beforeday
                        }
                    ]
                });
            }
        });
    }

    //3天的请求频率
    var speedReqTate_vm = ${speedReqTate};
    var chartsReqTate=echarts.init(document.getElementById("chartsReqTate"));
    option1 = {
        title: {
            text: '近况：'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['今日','昨日','前日']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            data: hoursList
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                type:'line',
                name:'今日',
                data:speedReqTate_vm.today
            },
            {
                type:'line',
                name:'昨日',
                data:speedReqTate_vm.yesterday
            },
            {
                type:'line',
                name:'前日',
                data:speedReqTate_vm.beforeday
            }
        ]
    };
    chartsReqTate.setOption(option1);

    /////////////////////////////////////////
    //30天接口响应速度情况
    var speeds_vm = ${speeds};
    var chartsMonth=echarts.init(document.getElementById("chartsMonth"));
    option2 = {
        title: {
            text: '趋势：'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['平均响应','最快响应','最慢响应','请求次数','1s+次数','2s+次数','5s+次数']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            //x轴间隔5个单位显示
            axisLabel :{
                interval: 'auto'
            },
            data: speeds_vm.dates
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                type:'line',
                name:'平均响应',
                data:speeds_vm.average
            },
            {
                type:'line',
                name:'最快响应',
                data:speeds_vm.fastest
            },
            {
                type:'line',
                name:'最慢响应',
                data:speeds_vm.slowest
            },
            {
                type:'line',
                name:'请求次数',
                data:speeds_vm.total_num
            }
            ,
            {
                type:'line',
                name:'1s+次数',
                data:speeds_vm.total_num_slow1
            },
            {
                type:'line',
                name:'2s+次数',
                data:speeds_vm.total_num_slow2
            },
            {
                type:'line',
                name:'5s+次数',
                data:speeds_vm.total_num_slow5
            }
        ]
    };
    chartsMonth.setOption(option2);

    parent.window.onMenuHide=function () { };
</script>
</html>