package wateradmin.dso.wrap.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20170301.QueryMetricLastRequest;
import com.aliyuncs.cms.model.v20170301.QueryMetricListRequest;
import com.aliyuncs.cms.model.v20170301.QueryMetricListResponse;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.water.protocol.model.monitor.ETimeType;
import org.noear.water.utils.Datetime;
import org.noear.water.protocol.model.monitor.EChartModel;
import org.noear.water.protocol.model.monitor.ELineModel;
import org.noear.water.utils.TextUtils;
import wateradmin.models.aliyun.AliyunResponse;
import wateradmin.models.aliyun.EcsTrackModel;
import wateradmin.models.water_cfg.ConfigModel;

import java.util.*;

@Slf4j
public class AliyunCmsUtil {

    //阿里云时间转换 输入1525652700000L
    public static String translateAliyunTime(Long input) {
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(input));
        return date;
    }

    //阿里云监控请求时间初始化
    public static void setRequestDateInit(ETimeType timeType, QueryMetricListRequest request) {
        switch (timeType) {
            case hour1:
                request.setStartTime(Datetime.Now().addHour(-1).toString());
                request.setPeriod("60");//一分钟监控一次
                break;        //六小时
            case hour6:
                request.setStartTime(Datetime.Now().addHour(-6).toString());
                request.setPeriod("300");//5分钟监控一次
                break;        //1天
            case day1:
                request.setStartTime(Datetime.Now().addDay(-1).toString());
                request.setPeriod("1800");//半小时监控一次
                break;        //3天
            case day3:
                request.setStartTime(Datetime.Now().addDay(-3).toString());
                request.setPeriod("3600");//1小时监控一次
                break;        //7天
            case day7:
                request.setStartTime(Datetime.Now().addDay(-7).toString());
                request.setPeriod("3600");//1小时监控一次
                break;       //15天
            case day14:
                request.setStartTime(Datetime.Now().addDay(-14).toString());
                request.setPeriod("3600");//1小时监控一次
                break;        //30天
        }
    }

    //阿里云监控请求时间初始化
    public static void setRequestDateInit(ETimeType timeType, QueryMetricLastRequest request) {
        switch (timeType) {
            case hour1:
                request.setStartTime(Datetime.Now().addHour(-1).toString());
                request.setPeriod("60");//一分钟监控一次
                break;        //六小时
            case hour6:
                request.setStartTime(Datetime.Now().addHour(-6).toString());
                request.setPeriod("300");//5分钟监控一次
                break;        //1天
            case day1:
                request.setStartTime(Datetime.Now().addDay(-1).toString());
                request.setPeriod("1800");//半小时监控一次
                break;        //3天
            case day3:
                request.setStartTime(Datetime.Now().addDay(-3).toString());
                request.setPeriod("3600");//1小时监控一次
                break;        //7天
            case day7:
                request.setStartTime(Datetime.Now().addDay(-7).toString());
                request.setPeriod("3600");//1小时监控一次
                break;       //15天
            case day14:
                request.setStartTime(Datetime.Now().addDay(-14).toString());
                request.setPeriod("3600");//1小时监控一次
                break;        //30天
        }
    }

    //阿里云请求监控对象初始化 //https://help.aliyun.com/document_detail/28619.html
    public static void setMetricInit(Integer type, QueryMetricListRequest request) {
        switch (type) {
            case 0:
                request.setMetric("CPUUtilization"); //cpu
                break;
            case 1:
                request.setMetric("memory_usedutilization"); //内存
                break;
            case 2:
                request.setMetric("diskusage_utilization"); //
                break;
            case 3:
                request.setMetric("InternetOutRate_Percent");//request.setMetric("InternetOutRate"); //公网 //bit/s
                break;
            case 4:
                request.setMetric("net_tcpconnection"); //tcp
                break;
            case 5:
                request.setMetric("InternetInRate_Percent");//request.setMetric("InternetInRate"); //公网流入带宽 //bit/s
                break;
            case 6:
                request.setMetric("InternetOutRate_Percent"); //公网流出带宽使用率 //%
                break;
        }
    }

    //基础查询获取参数
    public static ELineModel baseQuery(ConfigModel cfg, String instanceId, ETimeType timeType, Integer dataType) {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();
        request.setProject("acs_ecs_dashboard");

        setMetricInit(dataType, request);
        setRequestDateInit(timeType, request);

        request.setEndTime(Datetime.Now().toString());
        JSONObject dim = new JSONObject();
        dim.put("instanceId", instanceId);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);

        IAcsClient client = new DefaultAcsClient(profile);
        try {
            QueryMetricListResponse response = client.getAcsResponse(request);

            String res = response.getDatapoints();
            System.out.println(res);
            List<AliyunResponse> list = JSON.parseArray(res, AliyunResponse.class);
            ELineModel avageList = new ELineModel();
            for (AliyunResponse item : list) {

                if (dataType == 4) { //这里特殊处理, 因为网卡返回的数据有三种, 我们只需要Total的.
                    EChartModel model = new EChartModel();
                    Date dt = new Date(item.timestamp);
                    model.label = item.state;
                    model.name = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dt);
                    List<String> timelist = new ArrayList<>();

                    if (timeType.code < 3) {
                        timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(dt));
                    } else {
                        timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH").format(dt));
                    }

                    timelist.add(TextUtils.doubleFormat(item.average));
                    model.value = timelist;
                    avageList.add(model);

                } else {
                    EChartModel model = new EChartModel();
                    Date dt = new Date(item.timestamp);
                    model.name = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dt);
                    List<String> timelist = new ArrayList<>();

                    if (timeType.code < 3) {
                        model.label = item.diskname; //增加多硬盘支持 //外部可根据group进行硬盘分组

                        timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(dt));
                    } else {
                        timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH").format(dt));
                    }

                    //公网流出3,流入5（bit/s）
//                    if(dataType == 3 || dataType == 5){
//                        timelist.add(StringUtil.doubleFormat((item.average/1024/8)));
//                    }else{
//                        timelist.add(StringUtil.doubleFormat(item.average));
//                    }

                    timelist.add(TextUtils.doubleFormat(item.average));

                    model.value = timelist;
                    avageList.add(model);
                }
            }
            return avageList;
        } catch (ServerException e) {
            log.error("{}",e);
            return null;
        } catch (ClientException e) {
            log.error("{}",e);
            return null;
        }

    }


    //获取所有Ecs实例
    public static List<DescribeInstancesResponse.Instance> getEcsInstances(ConfigModel cfg) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(100);
        DescribeInstancesResponse response = client.getAcsResponse(describe);
        List<DescribeInstancesResponse.Instance> res = response.getInstances();
        return res;
    }


    //获取当个Ecsid
    public static DescribeInstancesResponse.Instance getInstanceInfo(ConfigModel cfg, String instanceId) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        ONode oNode = new ONode();
        oNode.asArray();
        oNode.add(instanceId);
        describe.setInstanceIds(oNode.toJson());
        describe.setPageSize(100);
        DescribeInstancesResponse response = client.getAcsResponse(describe);
        List<DescribeInstancesResponse.Instance> res = response.getInstances();
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return new DescribeInstancesResponse.Instance();
        }
    }

    //获取磁盘实例
    public static DescribeDisksResponse.Disk getEcsDiskInfo(ConfigModel cfg,String instanceId) throws ClientException{
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeDisksRequest describe=new DescribeDisksRequest();
        //describe.setPortable(false);
        describe.setInstanceId(instanceId);
       // describe.setPageSize(100);
        DescribeDisksResponse response = client.getAcsResponse(describe);
        List<DescribeDisksResponse.Disk> res = response.getDisks();
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return null;
        }
    }



    //获取所有实例的最新一次的监控数据
    public static List<EcsTrackModel> pullEcsTrack(ConfigModel cfg) throws Exception {

        List<DescribeInstancesResponse.Instance> ecsList = getEcsInstances(cfg);
        List<EcsTrackModel> list = new ArrayList<>();

        for (DescribeInstancesResponse.Instance item : ecsList) {
            EcsTrackModel ecsViewModel = new EcsTrackModel();

            ecsViewModel.instanceId = (item.getInstanceId());
            ecsViewModel.name     = (item.getInstanceName());

            list.add(ecsViewModel);
        }

        baseQuery(cfg,0, list);
        baseQuery(cfg,1, list);
        baseQuery(cfg,2, list);
        baseQuery(cfg,6, list);
        baseQuery(cfg,4, list);

        return list;
    }

    //基础查询获取参数当前所有
    public static void baseQuery(ConfigModel cfg,Integer dataType, List<EcsTrackModel> ecsModels) {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();
        request.setProject("acs_ecs_dashboard");

        setMetricInit(dataType, request);

        request.setStartTime(Datetime.Now().addMinute(-5).toString());
        request.setEndTime(Datetime.Now().toString());

        JSONObject dim = new JSONObject();

        // dim.put("instanceId", input);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);

        IAcsClient client = new DefaultAcsClient(profile);

        try {
            QueryMetricListResponse response = client.getAcsResponse(request);

            String res = response.getDatapoints();

            List<AliyunResponse> list = JSON.parseArray(res, AliyunResponse.class);

            HashMap<String, Double> tmpHash = new HashMap<>();
            for (AliyunResponse item : list) {
                if(dataType == 2){
                    if(tmpHash.containsKey(item.instanceId)){ //处理多硬盘的最大值
                        double tmp = tmpHash.get(item.instanceId);
                        if(item.average>tmp){
                            tmpHash.put(item.instanceId, item.average);
                        }
                    }else{
                        tmpHash.put(item.instanceId, item.average);
                    }
                }else {
                    tmpHash.put(item.instanceId, item.average);
                }
            }

            for (EcsTrackModel tmp : ecsModels) {
                if(tmpHash.containsKey(tmp.instanceId)==false){
                    continue;
                }

                switch (dataType) {
                    case 0:
                        tmp.cpu = tmpHash.get(tmp.instanceId); //cpu
                        break;
                    case 1:
                        tmp.memory = tmpHash.get(tmp.instanceId); //内存
                        break;
                    case 2:
                        tmp.disk = tmpHash.get(tmp.instanceId); //
                        break;
                    case 6:
                        tmp.broadband = tmpHash.get(tmp.instanceId); //公网
                        break;
                    case 4:
                        tmp.tcp = tmpHash.get(tmp.instanceId); //tcp
                        break;
                }
            }
        } catch (Exception e) {
            log.error("{}",e);
        }
    }
}
