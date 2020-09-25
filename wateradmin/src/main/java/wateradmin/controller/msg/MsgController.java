package wateradmin.controller.msg;

import org.noear.snack.ONode;
import org.noear.water.WaterClient;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.HttpUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.viewModels.ViewModel;
import wateradmin.models.water_msg.MessageModel;
import wateradmin.models.water_msg.SubscriberModel;

import java.sql.SQLException;
import java.util.HashMap;

@XController
public class MsgController extends BaseController {
    @XMapping("/msg")
    public ModelAndView index(){
        return view("msg/msg");
    }


    //消息调试
    @XMapping("/msg/debug")
    public ModelAndView debug(String key) throws SQLException {
        MessageModel msg = DbWaterMsgApi.getMessageByKey(key);
        SubscriberModel sub = DbWaterMsgApi.getSubscriber(msg.topic_id);
        viewModel.put("key",key);
        viewModel.put("msg",msg);
        viewModel.put("sub",sub);
        return view("msg/debug");
    }

    //提交消息调试
    @XMapping("/msg/debug/ajax/submitDebug")
    public ViewModel submitDebug(Long id,String msg_key,String topic_name,Integer dist_count,String content,String url) throws Exception{
        MessageModel msg = DbWaterMsgApi.getMessageById(id);
        SubscriberModel sub = DbWaterMsgApi.getSubscriber(msg.topic_id);
        StringBuilder sb = new StringBuilder(200);
        sb.append(id).append("#");
        sb.append(msg_key).append("#");
        sb.append(topic_name).append("#");
        sb.append(content).append("#");
        sb.append(sub.access_key);
        String sgin = EncryptUtils.md5(sb.toString());

        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("key",msg.msg_key);
        map.put("topic",msg.topic_name);
        map.put("times",msg.dist_count);
        map.put("message", Base64Utils.encode(msg.content));
        map.put("sgin",sgin);
        String result = HttpUtils.http(url).data(map).post();

        return viewModel.code(1,result);
    }

    @XMapping("/msg/send")
    public ModelAndView distribute(){

        return view("msg/send");
    }

    @XMapping("/msg/send/ajax/dosend")
    public ViewModel sendMessage(String topic, String message,String tags) throws Exception {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean isOk = WaterClient.Message.sendMessageAndTags(topic, message, tags);

            if (isOk) {
                viewModel.code(1,"消息派发成功！");
            } else {
                viewModel.code(0,"消息发送失败!");
            }
        } else {
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }


    //消息清理
    @XMapping("/msg/clean")
    public ModelAndView clean(){
        return view("msg/msg_clean");
    }

    //执行消息清理
    @XMapping("/msg/clean/ajax/submitClean")
    public ViewModel submitClean(Integer state) throws SQLException{
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0,"没有权限！");
        }
        else {
            int i = DbWaterMsgApi.deleteMsg(state);
            return viewModel.code(1,"成功清理" + i + "消息！");
        }
    }

    //后端加密
    @XMapping("/msg/debug/ajax/getSign")
    public HashMap<String,String> getSign(Long id,String msg_key,String topic_name,String content,String access_key){
        StringBuilder sb = new StringBuilder(200);
        sb.append(id).append("#");
        sb.append(msg_key).append("#");
        sb.append(topic_name).append("#");
        sb.append(content).append("#");
        sb.append(access_key);
        String sign = EncryptUtils.md5(sb.toString());
        String message = Base64Utils.encode(content);
        HashMap<String, String> resp = new HashMap<>();
        resp.put("sign",sign);
        resp.put("message",message);
        return resp;
    }

}
