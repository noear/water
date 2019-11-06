package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.BcfTagChecker;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.LoggerModel;
import webapp.models.water.WhitelistModel;
import webapp.viewModels.ViewModel;

import java.util.List;

@XController
@XMapping("/cfg/")
public class WhitelistController extends BaseController{

    //IP白名单列表
    @XMapping("whitelist")
    public ModelAndView whitelist(String ip) throws Exception {
        List<LoggerModel> tags = DbWaterApi.getLoggerTags();

        List<WhitelistModel> list = DbWaterApi.getWhiteList(ip);

        BcfTagChecker.filter(list, m -> m.tag);

        viewModel.put("tags",tags);
        viewModel.put("list",list);
        return view("cfg/whitelist");
    }

    //跳转ip白名单新增页面
    @XMapping("whitelist/add")
    public ModelAndView whitelistAdd() {
        return view("cfg/whitelist_add");
    }

    //保存ip白名单新增
    @XMapping("whitelist/add/ajax/save")
    public ViewModel saveWhitelistAdd(String tag,String ip,String note) throws Exception{
        int is_admin = Session.current().getIsAdmin();
        if(is_admin==1) {
            boolean result = DbWaterApi.addWhiteList(tag,ip,note);
            if (result) {
                DbWaterApi.reloadWhitelist();

                viewModel.code(1,"新增成功！");
            } else {
                viewModel.code(0,"新增失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }



        return viewModel;
    }

    //删除IP白名单记录
    @XMapping("whitelist/ajax/del")
    public ViewModel saveWhitelistDel(Integer row_id) throws Exception{
        int is_admin = Session.current().getIsAdmin();
        if(is_admin==1) {
            boolean result = DbWaterApi.deleteWhiteList(row_id);
            if (result) {
                DbWaterApi.reloadWhitelist();

                viewModel.code(1,"删除成功！");
            } else {
                viewModel.code(0,"删除失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }
}
