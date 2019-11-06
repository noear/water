package webapp.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.tools.TextUtils;
import webapp.controller.BaseController;
import webapp.dao.db.DbWindApi;
import webapp.models.water.ConfigModel;
import webapp.models.water.ServerTrackBlsModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@XController
@XMapping("/mot/")
public class BlsController extends BaseController {

    @XMapping("bls")
    public ModelAndView bls(String tag_name,String name, String sort) throws Exception {
        List<ConfigModel> tags = DbWindApi.getServerBlsAccounts();

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag_name) && tags.size()>0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackBlsModel> list =  DbWindApi.getServerBlsTracks(tag_name,name,sort);
        for(ServerTrackBlsModel item: list){
            item.traffic_tx=(int)(item.traffic_tx/1000.0);
        }

        viewModel.put("tag_name",tag_name);
        viewModel.set("list",list);


        return view("mot/bls");
    }


}
