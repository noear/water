package wateradmin.controller.msg;

import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.water.WaterClient;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.viewModels.ViewModel;

import java.util.HashMap;
import java.util.List;

@Controller
public class MsgController extends BaseController {
    @Mapping("/msg")
    public ModelAndView index() {
        return view("msg/msg");
    }


    //消息调试
    @Mapping("/msg/debug")
    public ModelAndView debug(Context ctx,String broker, String key) throws Exception {
        MessageModel msg = ProtocolHub.getMsgSource(broker).getMessageByKey(key);
        SubscriberModel sub = DbWaterMsgApi.getSubscriber(msg.topic_name);

        if (TextUtils.isEmpty(broker)) {
            broker = ctx.cookie("wateradmin_msg__broker");
        } else {
            ctx.cookieSet("wateradmin_msg__broker", broker);
        }

        List<TagCountsModel> brokerList = DbWaterCfgApi.getBrokerNameTags();

        viewModel.put("broker", broker);
        viewModel.put("brokerList", brokerList);
        viewModel.put("key", key);
        viewModel.put("msg", msg);
        viewModel.put("sub", sub);
        return view("msg/debug");
    }

    //提交消息调试
    @NotEmpty({"msg_key", "topic_name", "receive_key"})
    @Mapping("/msg/debug/ajax/submitDebug")
    public ViewModel submitDebug(Long id, String msg_key, String topic_name, Integer dist_count, String content, String receive_key, String url) throws Exception {
        if (dist_count == null) {
            dist_count = 0;
        }

        StringBuilder sb = new StringBuilder(200);

        sb.append(msg_key).append("#");
        sb.append(topic_name).append("#");
        sb.append(content).append("#");
        sb.append(receive_key);

        String sgin = EncryptUtils.md5(sb.toString());

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("key", msg_key);
        map.put("topic", topic_name);
        map.put("times", dist_count);
        map.put("message", Base64Utils.encode(content));
        map.put("sgin", sgin);

        try {
            String result = HttpUtils.http(url).data(map).post();

            return viewModel.code(1, result);
        } catch (Exception ex) {
            return viewModel.code(1, ex.getMessage());
        }
    }

    @Mapping("/msg/send")
    public ModelAndView distribute(Context ctx) throws Exception {
        String broker = ctx.cookie("wateradmin_msg__broker");

        List<TagCountsModel> brokerList = DbWaterCfgApi.getBrokerNameTags();

        viewModel.put("broker", broker);
        viewModel.put("brokerList", brokerList);

        return view("msg/send");
    }

    @NotEmpty({"message", "topic"})
    @AuthPermissions(SessionPerms.admin)
    @Mapping("/msg/send/ajax/dosend")
    public ViewModel sendMessage(String broker, String topic, String message, String tags) throws Exception {

        topic = topic.trim();

        if (topic.startsWith("@")) {
            topic = topic.substring(1);
        }

        if (broker == null) {
            broker = "";
        }

        boolean isOk = WaterClient.Message.sendMessageAndTags(broker.trim(), topic.trim(), message.trim(), tags);

        if (isOk) {
            viewModel.code(1, "消息派发成功！");
        } else {
            viewModel.code(0, "消息发送失败!");
        }

        return viewModel;
    }

    //后端加密
    @Mapping("/msg/debug/ajax/getSign")
    public HashMap<String, String> getSign(String msg_key, String topic_name, String content, String receive_key) {
        StringBuilder sb = new StringBuilder(200);

        sb.append(msg_key).append("#");
        sb.append(topic_name).append("#");
        sb.append(content).append("#");
        sb.append(receive_key);

        String sign = EncryptUtils.md5(sb.toString());

        String message = Base64Utils.encode(content);
        HashMap<String, String> resp = new HashMap<>();

        resp.put("sign", sign);
        resp.put("message", message);
        return resp;
    }
}
