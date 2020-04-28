package org.noear.rubber.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

public class LogRequestModel implements IBinder
{
    public long log_id;
    public String request_id;
    public String scheme_tagname;
    public int policy;
    public String args_json;
    public String model_json;
    public String matcher_json; //:details
    public String evaluation_json; //:details
    public String session_json;
    public Date start_fulltime;
    public Date end_fulltime;
    public long timespan;
    public int state; //0:刚记录；1:计算中；2:已计算
    public String callback;

	public void bind(GetHandlerEx s)
	{
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
        start_fulltime = s.get("start_fulltime").value(null);
        end_fulltime = s.get("end_fulltime").value(null);
        timespan = s.get("timespan").value(0L);
        state = s.get("state").value(0);
        callback = s.get("callback").value(null);
	}
	
	public IBinder clone()
	{
		return new LogRequestModel();
	}

	public static String getEvaluationEnum(int evalua) {
	    //0-,1交易放行,2审慎审核,3阻断交易
        String resp = "";
        switch (evalua){
            case -1:resp = "异常";
                break;
            case 0:resp = "-";
                break;
            case 1:resp = "交易放行";
                break;
            case 2:resp = "审慎审核";
                break;
            case 3:resp = "阻断交易";
                break;
            default:break;
        }
        return resp;
    }

    public String getPolicyName() {
        //  1000, 匹配模式，有不匹配的马上返回false；
        //  1001, 匹配模式，且记录触发明细
        //  2000, 分值模式，累记分值不记录明细；
        //  2001, 分值+明细模式，累记分值且记录触发明细；
        //
        switch (policy){
            case 1000:return "匹配模式";
            case 1001:return "匹配详情模式";
            case 1002:return "生产";
            case 2000:return "分值模式";
            case 2001:return "分值+明细模式";
            default:return policy+"";
        }
    }
}