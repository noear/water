package wateradmin.controller.mot;

import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterMsgApi;

import java.util.List;


@Controller
@Mapping("/mot/")
public class Msg2Controller extends BaseController {

    //消息异常记录
    @Mapping("/msg")
    public ModelAndView warm() throws Exception {
        int dist_count = 0;

        if (dist_count == 0) {
            dist_count = 1;
        }

        List<MessageModel> list = ProtocolHub.getMsgSource(null).getMessageWarmList(dist_count, "");
        viewModel.put("list", list);
        return view("mot/msg");
    }
}
