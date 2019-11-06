package webapp.controller.mot;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import org.apache.http.util.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.db.DbWindApi;
import webapp.models.aliyun.AliyunEchartModel;
import webapp.models.aliyun.AliyunElineModel;
import webapp.models.aliyun.EcsTrackModel;
import webapp.models.water.ConfigModel;
import webapp.models.water.ServerTrackEcsModel;
import webapp.dao.AliyunCmsUtil;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XController
@XMapping("/mot/")
public class EcsController extends BaseController {
    //进入ecs视图
    @XMapping("ecs")
    public ModelAndView ecs(String tag_name,String name, String sort) throws Exception {
        List<ConfigModel> tags = DbWindApi.getServerEcsAccounts();

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag_name) && tags.size()>0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackEcsModel> list =  DbWindApi.getServerEcsTracks(tag_name,name,sort);

        viewModel.put("tag_name",tag_name);
        viewModel.set("list",list);

        return view("mot/ecs");
    }


    @XMapping("ecs/inner")
    public ModelAndView ecs_inner(String id) throws Exception {
        ConfigModel cfg = DbWindApi.getServerIaasAccount(id);

        if (cfg != null) {
            DescribeInstancesResponse.Instance instance = AliyunCmsUtil.getInstanceInfo(cfg, id);
            int size = AliyunCmsUtil.getEcsDiskInfo(cfg, id).getSize();
            viewModel.set("instance", instance);
            viewModel.set("size", size);
        }

        return view("mot/ecs_inner");
    }


    @XMapping("ecs/charts/ajax/reqtate")
    public List<AliyunElineModel> ecs_chart_ajax_reqtate(Integer dateType, Integer dataType, String inId ) throws SQLException{
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

        AliyunElineModel res =   AliyunCmsUtil.baseQuery(cfg,inId,dateType,dataType);
        List<AliyunElineModel> rearr=new ArrayList<>();

        if(dataType == 2 || dataType == 4){
            //增加多线支持
            Map<String,AliyunElineModel> mline = new HashMap<>();

            for(AliyunEchartModel m : res){
                if(mline.containsKey(m.label)==false){
                    mline.put(m.label,new AliyunElineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        }
        else{
            rearr.add(res);
        }

        if(dataType==3){
            AliyunElineModel res2 =   AliyunCmsUtil.baseQuery(cfg,inId,dateType,5);
            rearr.add(res2);
        }

        return  rearr;
    }

    @XMapping("ecs/track/ajax/pull")
    public ViewModel ecs_track_ajax_pull( ) throws Exception{
        List<ConfigModel> cfgList = DbWindApi.getIAASAccionts();

        for(ConfigModel cfg : cfgList) {
            List<EcsTrackModel> list = AliyunCmsUtil.pullEcsTrack(cfg);

            DbWindApi.setServerEcsTracks(list);
        }

        return viewModel.code(1,"OK");
    }
}
