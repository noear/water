package waterapp.models.water_rebber;

import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.weed.*;
import org.noear.water.utils.TextUtils;

import java.util.*;

/// <summary>
/// 生成:2018/05/21 02:03:21
/// 
/// </summary>
@Getter
public class LogRequestModel implements IBinder {
    public long log_id;
    public String request_id;
    public String scheme_tagname;
    public int policy;
    public String args_json;
    public String model_json;
    public String matcher_json; //:details
    public String evaluation_json; //:details
    public String session_json;
    public String note_json;
    public Date start_fulltime;
    public Date end_fulltime;
    public long timespan;
    public int state;
    public String callback;

    public void bind(GetHandlerEx s) {
        //1.source:数据源
        //
        log_id = s.get("log_id").value(0L);
        request_id = s.get("request_id").value(null);
        scheme_tagname = s.get("scheme_tagname").value(null);
        policy = s.get("policy").value(null);
        args_json = s.get("args_json").value(null);
        model_json = s.get("model_json").value(null);
        matcher_json = s.get("matcher_json").value(null);
        evaluation_json = s.get("evaluation_json").value(null);
        session_json = s.get("session_json").value(null);
        note_json = s.get("note_json").value(null);
        start_fulltime = s.get("start_fulltime").value(null);
        end_fulltime = s.get("end_fulltime").value(null);
        timespan = s.get("timespan").value(0L);
        state = s.get("state").value(0);
        callback = s.get("callback").value(null);
    }

    public IBinder clone() {
        return new LogRequestModel();
    }

    public static String getEvaluationEnum(int evalua) {
        //0-,1交易放行,2审慎审核,3阻断交易
        String resp = "";
        switch (evalua) {
            case -1:
                resp = "异常";
                break;
            case 0:
                resp = "-";
                break;
            case 1:
                resp = "交易放行";
                break;
            case 2:
                resp = "审慎审核";
                break;
            case 3:
                resp = "阻断交易";
                break;
            default:
                break;
        }
        return resp;
    }

    public String getPolicyName() {
        //  1000, 匹配模式，有不匹配的马上返回false；
        //  1001, 匹配模式，且记录触发明细
        //  2000, 分值模式，累记分值不记录明细；
        //  2001, 分值+明细模式，累记分值且记录触发明细；
        //
        switch (policy) {
            case 1000:
                return "匹配模式";
            case 1001:
                return "匹配详情模式";
            case 1002:
                return "生产";
            case 2000:
                return "分值模式";
            case 2001:
                return "分值+明细模式";
            default:
                return policy + "";
        }
    }

    public String state_str() {
        if (TextUtils.isEmpty(callback)) {
            return String.valueOf(state);
        } else {
            if (callback.indexOf(":/") > 0) {
                return state + "**";
            } else {
                return state + "*";
            }
        }
    }

    public String args_json_str(){
        if(TextUtils.isEmpty(args_json))
            return args_json;
        else{
            return args_json.replaceAll("\"","'");
        }
    }

    public String node_str(){
        if(TextUtils.isEmpty(note_json)){
            return "";
        }else{
            ONode temp = ONode.load(note_json);
            if(temp.contains("E")){
                StringBuilder sb  =new StringBuilder();
                sb.append("匹配度=")
                        .append(temp.get("M").get("value").getInt())
                        .append("/")
                        .append(temp.get("M").get("total").getInt());
                sb.append("；");
                sb.append("估计值=")
                        .append(temp.get("E").get("score").getInt()).append("分")
                        .append("，")
                        .append(getEvaluationEnum(temp.get("E").get("advice").getInt()))
                        .append("，")
                        .append(temp.get("E").get("exception").getInt()).append("异常");


                return sb.toString();
            }else{
                return "";
            }
        }
    }
}