package wateradmin.dso.wrap.aliyun;

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
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import org.noear.water.protocol.model.monitor.EChartModel;
import org.noear.water.protocol.model.monitor.ELineModel;
import org.noear.water.protocol.model.monitor.ETimeType;
import org.noear.water.utils.TextUtils;
import wateradmin.models.aliyun.*;
import wateradmin.models.water_cfg.ConfigModel;
import org.noear.water.utils.Datetime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AliyunDbsUtil {

    //RDS请求监控对象初始化  连接数,CPU,内存,空间,QPS
    public static void setRdsMetricInit(int type, Integer dataType, QueryMetricListRequest request) {
        switch (dataType) {
            case 0:
                request.setMetric("ConnectionUsage"); //连接数使用率
                break;
            case 1:
                request.setMetric("CpuUsage"); //CPU使用率
                break;
            case 2:
                request.setMetric("MemoryUsage"); //内存使用率
                break;
            case 3:
                request.setMetric("DiskUsage"); //磁盘使用率
                break;
            case 4:
                request.setMetric("UsedQps"); //Qps使用率，UsedQps已不支持
                break;

        }
    }

    //基础信息获取
    public static ELineModel baseQuery(ConfigModel cfg, String id, ETimeType timeType, int dataType, int type) throws Exception {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        QueryMetricListRequest request = new QueryMetricListRequest();

        switch (type) {
            case 2:
                request.setProject("acs_rds_dashboard");
                break;  //RDS
            case 4:
                request.setProject("acs_memcache");
                break;       //memcache
            case 3:
                request.setProject("acs_kvstore");
                break;        //redis
        }

        AliyunCmsUtil.setRequestDateInit(timeType, request);

        if (dataType == 3 && type != 2) {
            dataType = 4;
        }
        setRdsMetricInit(type, dataType, request);
        JSONObject dim = new JSONObject();
        dim.put("instanceId", id);
        request.setDimensions(dim.toJSONString());
        request.setAcceptFormat(FormatType.JSON);

        IAcsClient client = new DefaultAcsClient(profile);
        QueryMetricListResponse response = client.getAcsResponse(request);
        String res = response.getDatapoints();
        System.out.println(res);
        List<AliyunBlsResponse> list = JSON.parseArray(res, AliyunBlsResponse.class);
        ELineModel avageList = new ELineModel();
        for (AliyunBlsResponse item : list) {
            if ("80".equals(item.getPort()) && dataType == 2) {
                continue;
            }
            EChartModel model = new EChartModel();
            Date dt = new Date(item.timestamp);
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
    public static DbsViewModel getDescribeDbAttribute(ConfigModel cfg, String instanceId) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeDBInstanceAttributeRequest request = new DescribeDBInstanceAttributeRequest();
        request.setDBInstanceId(instanceId);

        DescribeDBInstanceAttributeResponse response = client.getAcsResponse(request);
        List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> instance = response.getItems();

        DbsViewModel model = new DbsViewModel();
        model.instanceId = instanceId;
        model.setType(0);

        if (instance.size() > 0) {
            DescribeDBInstanceAttributeResponse.DBInstanceAttribute dm = instance.get(0);

            model.setDBInstanceStorage(dm.getDBInstanceStorage());
            model.setDBInstanceCPU(dm.getDBInstanceCPU());
            model.setDBInstanceMemory(dm.getDBInstanceMemory());

            model.setMaxCon(dm.getMaxConnections());
            model.setMaxIOPS(dm.getMaxIOPS());

            model.setDBCategory(dm.getCategory());
            model.setNetworkType(dm.getInstanceNetworkType());
            model.setZoneId(dm.getZoneId());
            model.setVersion(dm.getEngineVersion());
            model.setDBInstanceClass(dm.getDBInstanceClass());
        }
        return model;
    }

    //获取所有redis和memchace实例
    public static DbsViewModel getKvInstance(ConfigModel cfg, String instanceId, String type) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest request = new com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest();
        request.setInstanceIds(instanceId);

        if ("4".equals(type)) {
            request.setInstanceType("Memcache");
        }


        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse response = client.getAcsResponse(request);
        List<DescribeInstancesResponse.KVStoreInstance> res = response.getInstances();
        DbsViewModel model = new DbsViewModel();

        model.instanceId = instanceId;

        if (res.size() > 0) {
            model.setBandWith(res.get(0).getBandwidth() + "");
            model.setMaxCon(res.get(0).getConnections());
            model.setQps(res.get(0).getQPS() + "");
            model.setType(1);
            model.setZoneId(res.get(0).getZoneId());
            model.setCapacity(res.get(0).getCapacity());
        }
        return model;
    }


    //获取所有redis和memchace实例
    public static List<com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> getKvInstances(ConfigModel cfg, int type) throws ClientException {
        IClientProfile profile = AliyunUtil.getProfile(cfg);

        IAcsClient client = new DefaultAcsClient(profile);
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest request = new com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest();
        if (type > 0) {
            request.setInstanceType("Memcache");  //默认为redis不需要设置
        }
        com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse response = client.getAcsResponse(request);
        List<com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> res = response.getInstances();
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
        baseQuery(cfg, 0, list, 0);
        baseQuery(cfg, 1, list, 0);
        baseQuery(cfg, 2, list, 0);
        baseQuery(cfg, 3, list, 0);
        return list;
    }


    //获取Rds所有实例的最新一次的监控数据
    public static List<DbsTrackModel> pullRedisTrack(ConfigModel cfg) throws Exception {

        List<com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> kvList = getKvInstances(cfg, 0);
        List<DbsTrackModel> list = new ArrayList<>();

        for (DescribeInstancesResponse.KVStoreInstance item : kvList) {
            DbsTrackModel ecsViewModel = new DbsTrackModel();

            ecsViewModel.instanceId = (item.getInstanceId());
            ecsViewModel.name = (item.getInstanceName());

            list.add(ecsViewModel);
        }
        baseQuery(cfg, 0, list, 2);
        baseQuery(cfg, 1, list, 2);
        baseQuery(cfg, 2, list, 2);
        return list;
    }

    //获取Rds所有实例的最新一次的监控数据
    public static List<DbsTrackModel> pullMemcacheTrack(ConfigModel cfg) throws Exception {

        List<com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> kvList = getKvInstances(cfg, 1);
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

        setRdsMetricInit(type,dataType, request);

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
                if (tmpHash.containsKey(tmp.instanceId) == false) {
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
                        tmp.memory_usage = tmpHash.get(tmp.instanceId);
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
