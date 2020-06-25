package wateradmin.controller.mot;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.water_msg.MessageModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/mot/")
public class Msg2Controller extends BaseController {

    //消息异常记录
    @XMapping("/msg")
    public ModelAndView warm() throws SQLException {
        int topic_id = 0;
        String t = "";
        int dist_count = 0;

        if(TextUtils.isEmpty(t) == false){
            topic_id = DbWaterMsgApi.getTopicID(t).topic_id;
        }

        if(dist_count==0 && topic_id == 0){
            dist_count=1;
        }
        List<MessageModel> list = DbWaterMsgApi.getMessageList(dist_count, topic_id);
        viewModel.put("list",list);
        return view("mot/msg");
    }

}
