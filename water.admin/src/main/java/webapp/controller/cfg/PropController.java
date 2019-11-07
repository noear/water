package webapp.controller.cfg;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.dao.BcfTagChecker;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.*;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/cfg/")
public class PropController extends BaseController {
    @XMapping("prop")
    public ModelAndView index(String tag_name) throws SQLException {
        List<ConfigModel> resp = DbWaterApi.getTagGroup();

        BcfTagChecker.filter(resp, m -> m.tag);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (resp.isEmpty() == false) {
                viewModel.put("tag_name",resp.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        viewModel.put("resp",resp);
        return view("cfg/prop");
    }
    //跳转到新增页面。
    @XMapping("prop/add")
    public ModelAndView addConfig(){
        viewModel.put("row_id",0);
        viewModel.put("cfg",new ConfigModel());
        return view("cfg/prop_edit");
    }
    //跳转编辑页面。
    @XMapping("prop/edit")
    public ModelAndView editConfig(Integer row_id) throws SQLException {
        ConfigModel cfg = DbWaterApi.getConfigByRowId(row_id);
        viewModel.put("row_id",row_id);
        viewModel.put("cfg",cfg);
        return view("cfg/prop_edit");
    }

    //编辑、保存功能。
    @XMapping("prop/edit/ajax/save")
    public ViewModel saveEdit(Integer row_id, String tag, String key, Integer type, String url, String user, String password, String explain) throws SQLException{
        int is_admin = Session.current().getIsAdmin();
        if(is_admin==1) {
            url = url.trim();
            boolean result = DbWaterApi.editcfg(row_id, tag, key, type, url, user, password, explain);
            if (result) {
                viewModel.code(1,"保存成功！");
            } else {
                viewModel.code(0,"保存失败！");
            }
        }else {
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }

    @XMapping("prop/inner")
    public ModelAndView cfgTagList(String tag_name,String key) throws SQLException {
        List<ConfigModel> cfgs = DbWaterApi.getConfigByTag(tag_name,key);
        viewModel.put("cfgs",cfgs);
        viewModel.put("tag_name",tag_name);
        return view("cfg/prop_inner");
    }

}
