package org.noear.water.admin.plugin_aliyun.dso;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20170301.QueryMetricListRequest;
import com.aliyuncs.cms.model.v20170301.QueryMetricListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import org.noear.water.admin.plugin_aliyun.model.aliyun.*;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.tools.Datetime;
import org.noear.water.tools.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AliyunDbsUtil {

    //RDS请求监控对象初始化  连接数,CPU,内存,空间,QPS
    public static void setRdsMetricInit(Integer type, QueryMetricListRequest request) {
        switch (type) {
            case 0:
                request.setMetric("ConnectionUsage"); //连接数使用率
                break;
            case 1:
                request.setMetric("CpuUsage"); //CPU使用率
                break;
            case 2:
                request.setMetric("MemoryUsage"); //QPS
                break;
//            case 3:
//                request.setMetric("Qps"); //磁盘使用率
//                break;
            case 3:
                request.setMetric("DiskUsage"); //磁盘使用率
                break;
            case 4:
                request.setMetric("UsedQps"); //磁盘使用率
                break;

        }
    }

    //基础信息获取
    public static AliyunElineModel baseQuery(ConfigModel cfg, String id, int dateType, int dataType, int type) throws Exception {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();

        switch (type) {
            case 2:
                request.setProject("acs_rds_dashboard");
                break;  //RDS
            case 4:
                request.setProject("acs_kvstore");
                break;       //memcache
            case 3:
                request.setProject("acs_kvstore");
                break;        //redis
        }

        AliyunCmsUtil.setRequestDateInit(dateType, request);

        if(dataType==3&&type!=2){
            dataType=4;
        }
        setRdsMetricInit(dataType, request);
        JSONObject dim = new JSONObject();
        dim.put("instanceId", id);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);

        IAcsClient client = new DefaultAcsClient(profile);
        QueryMetricListResponse response = client.getAcsResponse(request);
        String res = response.getDatapoints();
        System.out.println(res);
        List<AliyunBlsResponse> list = JSON.parseArray(res, AliyunBlsResponse.class);
        AliyunElineModel avageList = new AliyunElineModel();
        for (AliyunBlsResponse item : list) {
            if ("80".equals(item.getPort()) && dataType == 2) {
                continue;
            }
            AliyunEchartModel model = new AliyunEchartModel();
            Date dt = new Date(item.timestamp);
            model.name = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dt);
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

    //获取所有Rds实例
    public static List<DescribeDBInstancesResponse.DBInstance> getRdsInstances(ConfigModel cfg) throws ClientException {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeDBInstancesRequest describe = new DescribeDBInstancesRequest();
        describe.setPageSize(100);
        DescribeDBInstancesResponse response = client.getAcsResponse(describe);
        List<DescribeDBInstancesResponse.DBInstance> res = response.getItems();
        return res;
    }


    //    //获取阿里云Bls数据
    public static DbsViewModel getDescribeDbAttribute(ConfigModel cfg, String dbId) throws ClientException {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
        request.setDBInstanceId(dbId);
        DescribeDBInstancesResponse response = client.getAcsResponse(request);
        List<DescribeDBInstancesResponse.DBInstance> instance = response.getItems();
        DbsViewModel model = new DbsViewModel();
        model.id = dbId;
        model.setType(0);
        if (instance.size() > 0) {
            model.setZoneId(instance.get(0).getZoneId());
            model.setVersion(instance.get(0).getEngineVersion());
            model.setdBInstanceClass(instance.get(0).getDBInstanceClass());
        }
        return model;
    }

    //获取所有redis和memchace实例
    public static DbsViewModel getKvInstance(ConfigModel cfg, String id, String type) throws ClientException {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest request = new com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest();
        request.setInstanceIds(id);

        if("4".equals(type)){
            request.setInstanceType("Memcache");
        }


        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse response = client.getAcsResponse(request);
        List<DescribeInstancesResponse.KVStoreInstance> res = response.getInstances();
        DbsViewModel model=new DbsViewModel();
        if(res.size()>0){
            model.setBandWith(res.get(0).getBandwidth()+"");
            model.setMaxCon(res.get(0).getConnections()+"");
            model.setQps(res.get(0).getQPS()+"");
            model.setType(1);
            model.setZoneId(res.get(0).getZoneId());
            model.setCapacity(res.get(0).getCapacity());
        }
        return model;
    }


    //获取所有redis和memchace实例
    public static List<DescribeInstancesResponse.KVStoreInstance> getKvInstances(ConfigModel cfg, int type) throws ClientException {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest request = new com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest();
        if (type > 0) {
            request.setInstanceType("Memcache");  //默认为redis不需要设置
        }
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse response = client.getAcsResponse(request);
        List<DescribeInstancesResponse.KVStoreInstance> res = response.getInstances();
        return res;
    }

    //获取Rds所有实例的最新一次的监控数据
    public static List<DbsTrackModel> pullRdsTrack(ConfigModel cfg) throws Exception {

        List<DescribeDBInstancesResponse.DBInstance> ecsList = getRdsInstances(cfg);
        List<DbsTrackModel> list = new ArrayList<>();

        for (DescribeDBInstancesResponse.DBInstance item : ecsList) {
            DbsTrackModel ecsViewModel = new DbsTrackModel();

            ecsViewModel.instanceId = (item.getDBInstanceId());
            ecsViewModel.name = (item.getDBInstanceId());

            list.add(ecsViewModel);
        }
        baseQuery(cfg,0, list, 0);
        baseQuery(cfg,1, list, 0);
        baseQuery(cfg,2, list, 0);
        baseQuery(cfg,3, list, 0);
        return list;
    }


    //获取Rds所有实例的最新一次的监控数据
    public static List<DbsTrackModel> pullRedisTrack(ConfigModel cfg) throws Exception {

        List<DescribeInstancesResponse.KVStoreInstance> kvList = getKvInstances(cfg,0);
        List<DbsTrackModel> list = new ArrayList<>();

        for (DescribeInstancesResponse.KVStoreInstance item : kvList) {
            DbsTrackModel ecsViewModel = new DbsTrackModel();

            ecsViewModel.instanceId = (item.getInstanceId());
            ecsViewModel.name = (item.getInstanceName());

            list.add(ecsViewModel);
        }
        baseQuery(cfg,0, list, 2);
        baseQuery(cfg,1, list, 2);
        baseQuery(cfg,2, list, 2);
        return list;
    }

    //获取Rds所有实例的最新一次的监控数据
    public static List<DbsTrackModel> pullMemcacheTrack(ConfigModel cfg) throws Exception {

        List<DescribeInstancesResponse.KVStoreInstance> kvList = getKvInstances(cfg, 1);
        List<DbsTrackModel> list = new ArrayList<>();

        for (DescribeInstancesResponse.KVStoreInstance item : kvList) {
            DbsTrackModel ecsViewModel = new DbsTrackModel();

            ecsViewModel.instanceId = (item.getInstanceId());
            ecsViewModel.name = (item.getInstanceName());

            list.add(ecsViewModel);
        }
        baseQuery(cfg, 0, list, 1);
        baseQuery(cfg, 1, list, 1);
        baseQuery(cfg, 2, list, 1);
        return list;
    }

    //基础查询获取参数当前所有
    public static void baseQuery(ConfigModel cfg, Integer dataType, List<DbsTrackModel> ecsModels, int type) {

        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();
        switch (type) {
            case 0:
                request.setProject("acs_rds_dashboard");
                break;  //RDS
            case 1:
                request.setProject("acs_memcache");
                break;       //memcache
            case 2:
                request.setProject("acs_kvstore");
                break;        //redis
        }

        setRdsMetricInit(dataType, request);

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
            System.out.println(res);
            List<AliyunResponse> list = JSON.parseArray(res, AliyunResponse.class);
            HashMap<String, Double> tmpHash = new HashMap<>();
            for (AliyunResponse item : list) {
                tmpHash.put(item.instanceId, item.average);
            }
            for (DbsTrackModel tmp : ecsModels) {
                if(tmpHash.containsKey(tmp.instanceId)==false){
                    continue;
                }

                switch (dataType) {
                    case 0:
                        tmp.connect_usage = tmpHash.get(tmp.instanceId);
                        break;
                    case 1:
                        tmp.cpu_usage = tmpHash.get(tmp.instanceId);
                        break;
                    case 2:
                        tmp.memory_usage =  tmpHash.get(tmp.instanceId);
                        break;
                    case 3:
                        tmp.disk_usage = tmpHash.get(tmp.instanceId);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
