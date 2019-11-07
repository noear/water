package webapp.controller.msg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import webapp.dao.DisttimeUtil;
import webapp.dao.db.DbWaterMsgApi;
import webapp.models.water_msg.MessageModel;

import java.sql.SQLException;
import java.util.List;


@XController
public class WaitController extends BaseController {
    //消息异常记录
    @XMapping("/msg/wait")
    public ModelAndView warm() throws SQLException {

        List<MessageModel> list = DbWaterMsgApi.getMessageByWaitList();
        viewModel.put("list",list);
        viewModel.put("currTime", DisttimeUtil.currTime());
        return view("msg/wait");
    }
}
