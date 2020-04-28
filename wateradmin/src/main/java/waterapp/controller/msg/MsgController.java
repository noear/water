package waterapp.controller.msg;

import org.noear.snack.ONode;
import org.noear.water.WaterClient;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.HttpUtils;
import waterapp.controller.BaseController;
import waterapp.viewModels.ViewModel;
import waterapp.dso.IDUtil;
import waterapp.dso.Session;
import waterapp.dso.db.DbWaterMsgApi;
import waterapp.models.water_msg.MessageModel;
import waterapp.models.water_msg.SubscriberModel;
import waterapp.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
    public ViewModel sendMessage(String topic, String message) throws Exception {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            String msg_key = IDUtil.buildGuid();
            ONode data = WaterClient.Message.sendMessage(msg_key, topic, message);

            if (data.get("code").getInt() == 1) {
                viewModel.code(1,"消息派发成功！");
            } else {
                viewModel.code(0,"消息发送失败:" + data.toJson());
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

    //主题列表
    @XMapping("/msg/topic")
    public ModelAndView topic(String topic_name) throws SQLException{
        List<TopicModel> list = DbWaterMsgApi.getTopicList(topic_name);
        viewModel.put("list",list);
        return view("msg/topic");
    }

    //跳转主题添加页面
    @XMapping("/msg/topic/add")
    public ModelAndView topicAdd() {
        viewModel.put("topic", new TopicModel());
        return view("msg/topic_edit");
    }

    //跳转主题编辑页面
    @XMapping("/msg/topic/edit")
    public ModelAndView topicEdit(Integer topic_id) throws SQLException{
        TopicModel topic = DbWaterMsgApi.getTopicByID(topic_id);
        viewModel.put("topic",topic);
        return view("msg/topic_edit");
    }

    //保存主题编辑
    @XMapping("/msg/topic/edit/ajax/save")
    public ViewModel topicEditSave(String topic_name,Integer topic_id,Integer max_msg_num,Integer max_distribution_num,Integer max_concurrency_num,Integer alarm_model) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.updateTopic(topic_name, topic_id, max_msg_num, max_distribution_num, max_concurrency_num, alarm_model);
            if (result) {
                viewModel.code(1, "保存成功！");
            } else {
                viewModel.code(0, "保存失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }
}
