package org.noear.water.admin.plugin_aliyun.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.plugin_aliyun.dso.AliyunBlsUtil;
import org.noear.water.admin.plugin_aliyun.dso.db.DbWindApi;
import org.noear.water.admin.plugin_aliyun.model.aliyun.AliyunEchartModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.AliyunElineModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.BlsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.BlsViewModel;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.client.model.ConfigM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XController
@XMapping("/mot/")
public class BlsController  extends BaseController {
    @XMapping("bls/inner")
    public ModelAndView bls_sinner(String id, String name) throws Exception {
        ConfigModel cfg = DbWindApi.getServerIaasAccount(id);

        if (cfg != null) {
            BlsViewModel blsViewModel = AliyunBlsUtil.getDescribeLoadBalancerAttribute(cfg, id);
            viewModel.set("id", id);
            viewModel.set("instance", blsViewModel);
            viewModel.set("model", blsViewModel);
        }

        return view("mot/bls_inner");
    }


    @XMapping("bls/charts/ajax/reqtate")
    public List<AliyunElineModel> bls_chart_ajax_reqtate(Integer dateType, Integer dataType, String inId ) throws Exception {
        if(dataType==null){
            dataType=0;
        }
        if(dateType==null){
            dateType=0;
        }

        ConfigModel cfg = DbWindApi.getServerIaasAccount(inId);

        if(cfg == null){
            return null;
        }


        List<AliyunElineModel> rearr=new ArrayList<>();

        AliyunElineModel res1 =   AliyunBlsUtil.baseQuery(cfg,inId,dateType,dataType);
        rearr.add(res1);

        if(dataType==0){ //并发连接
            AliyunElineModel res2 =   AliyunBlsUtil.baseQuery(cfg,inId,dateType,5);
            AliyunElineModel res3 =   AliyunBlsUtil.baseQuery(cfg,inId,dateType,6);
            rearr.add(res2);
            rearr.add(res3);
        }

        if(dataType==2){ //QPS
            rearr.clear();

            //增加多线支持
            Map<String,AliyunElineModel> mline = new HashMap<>();

            for(AliyunEchartModel m : res1){
                if(mline.containsKey(m.label)==false){
                    mline.put(m.label,new AliyunElineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        }

        if(dataType==3){ //流量
            AliyunElineModel res2 =   AliyunBlsUtil.baseQuery(cfg,inId,dateType,4);
            rearr.add(res2);
        }

        return  rearr;
    }

    @XMapping("bls/track/ajax/pull")
    public Object bls_track_ajax_pull( ) throws Exception{

        List<ConfigModel> cfgList = DbWindApi.getIAASAccionts();

        for(ConfigModel cfg : cfgList) {
            List<BlsTrackModel> list = AliyunBlsUtil.pullBlsTrack(cfg);

            DbWindApi.setServerBlsTracks(list);
        }

        return viewModel.code(1,"OK");
    }
}
