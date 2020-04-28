package waterapp.controller.cmds;

import waterapp.dso.db.DbWaterMsgApi;
import waterapp.models.TopicModel;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_msg_set_topic extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String name = get("name"); //消息key //派发时会回传

        if (checkParamsIsOk(name) == false) {
            return;
        }

        //如果不需要修改，检查是否已存在
        //
        TopicModel topic = DbWaterMsgApi.getTopicID(name);

        data.set("max_msg_num:old", topic.max_msg_num);

        topic.max_msg_num = getInt("max_msg_num",topic.max_msg_num);

        data.set("max_msg_num:new", topic.max_msg_num);

        data.set("code", 0);
        data.set("msg", "unknown");
    }
}
