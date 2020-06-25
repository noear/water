package wateradmin.dso.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20170301.QueryMetricLastRequest;
import com.aliyuncs.cms.model.v20170301.QueryMetricLastResponse;
import com.aliyuncs.cms.model.v20170301.QueryMetricListRequest;
import com.aliyuncs.cms.model.v20170301.QueryMetricListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.StringUtils;
import wateradmin.models.aliyun.*;
import wateradmin.models.water_cfg.ConfigModel;


import java.util.*;

public class AliyunBlsUtil {


    //获取所有Ecs实例
    public static List<DescribeLoadBalancersResponse.LoadBalancer> getBlsInstances(ConfigModel cfg) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeLoadBalancersRequest describe = new DescribeLoadBalancersRequest();
        describe.setPageSize(100);
        DescribeLoadBalancersResponse response = client.getAcsResponse(describe);
        List<DescribeLoadBalancersResponse.LoadBalancer> res = response.getLoadBalancers();
        return res;
    }

    //    //获取阿里云Bls数据
    public static BlsViewModel getDescribeLoadBalancerAttribute(ConfigModel cfg, String bls_id) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeLoadBalancerAttributeRequest request = new DescribeLoadBalancerAttributeRequest();
        request.setLoadBalancerId(bls_id);
        DescribeLoadBalancerAttributeResponse response = client.getAcsResponse(request);

        BlsViewModel model = new BlsViewModel();
        model.setInstanceId(response.getLoadBalancerId());
        model.setInstanceName((response.getLoadBalancerName()));
        model.setRegionId(response.getRegionId());
        model.setLoadBalancerSpec(response.getLoadBalancerSpec());
        return model;
    }

    //负载均衡请求监控对象初始化
    public static void setMetricInit(Integer type, QueryMetricListRequest request) {
        switch (type) {
            case 0:
                request.setMetric("InstanceMaxConnection"); //并发连接数
                break;
            case 1:
                request.setMetric("InstanceNewConnection"); //新键连接数
                break;
            case 2:
                request.setMetric("Qps"); //QPS
                break;
            case 3:
                request.setMetric("InstanceTrafficTX"); //流量
                break;
            case 4:
                request.setMetric("InstanceTrafficRX"); //流量流入
                break;
            case 5:
                request.setMetric("InstanceInactiveConnection"); //实例非活跃
                break;
            case 6:
                request.setMetric("InstanceActiveConnection"); //实例活跃
                break;
        }
    }


    //负载均衡请求监控对象初始化
    public static String getDataType(Integer type) {
        switch (type) {
            case 0:
               return ("InstanceMaxConnection"); //并发连接数

            case 1:
                return("InstanceNewConnection"); //新键连接数

            case 2:
                return("Qps"); //QPS

            case 3:
                return("InstanceTrafficTX"); //流量

            case 4:
                return("InstanceTrafficRX"); //流量流入

            case 5:
                return("InstanceInactiveConnection"); //实例非活跃

            case 6:
                return("InstanceActiveConnection"); //实例活跃
            default: return null;
        }
    }

    //负载均衡请求监控对象初始化
    public static void setMetricInit(Integer type, QueryMetricLastRequest request) {
        switch (type) {
            case 0:
                request.setMetric("InstanceMaxConnection"); //并发连接数
                break;
            case 1:
                request.setMetric("InstanceNewConnection"); //新键连接数
                break;
            case 2:
                request.setMetric("Qps"); //QPS
                break;
            case 3:
                request.setMetric("InstanceTrafficTX"); //流量
                break;
        }
    }

    //基本信息查询
    public static AliyunElineModel baseQuery(ConfigModel cfg, String instanceId, int dateType, int dataType) throws Exception {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();
        request.setProject("acs_slb_dashboard");
        AliyunCmsUtil.setRequestDateInit(dateType, request);
        setMetricInit(dataType, request);
        JSONObject dim = new JSONObject();
        dim.put("instanceId", instanceId);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);

        IAcsClient client = new DefaultAcsClient(profile);
        QueryMetricListResponse response = client.getAcsResponse(request);
        String res = response.getDatapoints();
        System.out.println(res);
        List<AliyunBlsResponse> list = JSON.parseArray(res, AliyunBlsResponse.class);
        AliyunElineModel avageList = new AliyunElineModel();
        for (AliyunBlsResponse item : list) {
//            if ("80".equals(item.getPort()) && dataType == 2) { //因为返回了443和80端口的数据,但是现在都只要https端口的
//                continue;
//            }

            AliyunEchartModel model = new AliyunEchartModel();
            Date dt = new Date(item.timestamp);
            model.name = getDataType(dataType);

            if(dataType == 2) {
                model.label = item.getPort()+"端口";
            }

            List<String> timelist = new ArrayList<>();
            if (dateType < 3) {
                timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(dt));
            } else {
                timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH").format(dt));
            }
            timelist.add(StringUtils.doubleFormat(item.average));
            model.value = timelist;
            avageList.add(model);
        }
        return avageList;
    }

    //查询最新数据
    public static void baseQueryLast(ConfigModel cfg, String bls_id, int dateType, int dataType) throws Exception {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricLastRequest request = new QueryMetricLastRequest();
        request.setProject("acs_slb_dashboard");
        AliyunCmsUtil.setRequestDateInit(dateType, request);
        setMetricInit(dataType, request);
        JSONObject dim = new JSONObject();
        dim.put("instanceId", bls_id);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);
        IAcsClient client = new DefaultAcsClient(profile);
        QueryMetricLastResponse response = client.getAcsResponse(request);
        String res = response.getDatapoints();
        System.out.println(res);

        List<AliyunBlsResponse> list = JSON.parseArray(res, AliyunBlsResponse.class);
        List<AliyunEchartModel> avageList = new ArrayList<>();
        for (AliyunBlsResponse item : list) {
            AliyunEchartModel model = new AliyunEchartModel();
            Date dt = new Date(item.timestamp);
            model.name = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dt);
            List<String> timelist = new ArrayList<>();
            if (dateType < 3) {
                timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(dt));
            } else {
                timelist.add(new java.text.SimpleDateFormat("yyyy/MM/dd HH").format(dt));
            }
            System.out.println(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(dt));
        }
    }


    //获取所有实例的最新一次的监控数据
    public static List<BlsTrackModel> pullBlsTrack(ConfigModel cfg) throws Exception {

        List<DescribeLoadBalancersResponse.LoadBalancer> blsList = getBlsInstances(cfg);
        List<BlsTrackModel> list = new ArrayList<>();

        for (DescribeLoadBalancersResponse.LoadBalancer item : blsList) {
            BlsTrackModel ecsViewModel = new BlsTrackModel();

            ecsViewModel.instanceId = (item.getLoadBalancerId());
            ecsViewModel.name = (item.getLoadBalancerName());

            list.add(ecsViewModel);
        }

        baseQuery(cfg,0, list);
        baseQuery(cfg,1, list);
        baseQuery(cfg,2, list);
        baseQuery(cfg,3, list);

        return list;
    }


    //基础查询获取参数当前所有
    public static void baseQuery(ConfigModel cfg, Integer dataType, List<BlsTrackModel> ecsModels) {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();
        request.setProject("acs_slb_dashboard");

        setMetricInit(dataType, request);

        request.setStartTime(Datetime.Now().addMinute(-10).toString());
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

            if(list.size() == 0){
                return;
            }

            //找到最新的 时间戳（有多个时间段）

            HashMap<String, Double> tmpHash = new HashMap<>();
            HashMap<String,AliyunResponse> tmpCache = new HashMap<>();
            String tmpKey = null;

            for (AliyunResponse item : list) {
                if(dataType == 2){ //qps
                    tmpKey = item.instanceId+"_"+item.port;

                    if(tmpCache.containsKey(tmpKey)){
                        continue;
                    }else {
                        tmpCache.put(tmpKey, item);
                    }

                    Double val = tmpHash.get(item.instanceId);

                    if(val == null) {
                        val = 0.0;
                    }

                    tmpHash.put(item.instanceId, item.average + val);
                }else {
                    tmpHash.put(item.instanceId, item.average);
                }
            }

            for (BlsTrackModel tmp : ecsModels) {
                if(tmpHash.containsKey(tmp.instanceId)==false){
                    continue;
                }

                switch (dataType) {
                    case 0:
                        tmp.co_conect_num = Double.valueOf(tmpHash.get(tmp.instanceId)).longValue();//cpu
                        break;
                    case 1:
                        tmp.new_conect_num = Double.valueOf(tmpHash.get(tmp.instanceId)).longValue(); //内存
                        break;
                    case 2:
                        tmp.qps = Double.valueOf(tmpHash.get(tmp.instanceId)).longValue(); //公网
                        break;
                    case 3:
                        tmp.traffic_tx = Double.valueOf(tmpHash.get(tmp.instanceId)).longValue(); //
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
