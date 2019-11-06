package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.AccountModel;
import webapp.utils.CodeUtil;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/cfg/")
public class AccountController extends BaseController{


    //账号列表
    @XMapping("account")
    public ModelAndView accountList(String name) throws Exception{
        List<AccountModel> list = DbWaterApi.getAccountListByName(name);
        viewModel.put("list",list);
        return view("/cfg/account");
    }

    //跳转账户新增页面
    @XMapping("account/add")
    public  ModelAndView accountAdd(){
        viewModel.put("account",new AccountModel());
        return view("/cfg/account_edit");
    }


    //跳转账户编辑页面
    @XMapping("account/edit")
    public  ModelAndView accountEdit(Integer account_id) throws Exception{
        AccountModel account = DbWaterApi.getAccountById(account_id);
        viewModel.put("account",account);
        return view("/cfg/account_edit");
    }

    //保存账户编辑
    @XMapping("account/edit/ajax/save")
    public ViewModel saveAccountEdit(Integer account_id,String name,String alarm_mobile,String note,String access_id,String access_key) throws SQLException{
        int is_admin = Session.current().getIsAdmin();
        if(is_admin==1) {
            boolean result = DbWaterApi.updateAccount(account_id,name,alarm_mobile,note,access_id,access_key);
            if (result) {
                viewModel.code(1,"保存成功！");
            } else {
                viewModel.code(0,"保存失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }

    //生成账户access_id/access_key
    @XMapping("account/genKey")
    public ViewModel genKey(Integer type) throws SQLException{
        String key = CodeUtil.genAppKey();
        if (type == 2) {
            key = CodeUtil.genUUID();
        }

        return viewModel.code(1,key);
    }
}
