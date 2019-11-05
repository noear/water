package webapp.model.water_log;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class LogModel implements IBinder {
    public long log_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String label;
    public String content;
    public int log_date;
    public Date log_fulltime;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        log_id = s.get("log_id").value(0L);
        level = s.get("level").value(0);
        tag = s.get("tag").value(null);
        tag1 = s.get("tag1").stringValue("");
        tag2 = s.get("tag2").stringValue("");
        label = s.get("label").value(null);
        content = s.get("content").value(null);
        log_date = s.get("log_date").value(0);
        log_fulltime = s.get("log_fulltime").value(null);
    }

    public IBinder clone()
    {
        return new LogModel();
    }


    public String labelHtml() {
        if(label==null) {
            return "";
        }
        else{
            return HtmlEncode.encode(label); //label.replaceAll("\\sbgcolor="," _bgcolor=");
        }
    }

    public String contentHtml(){
        if(content==null) {
            return "";
        }
        else{
            return HtmlEncode.encode(content); //content.replaceAll("\\sbgcolor="," _bgcolor=");
        }
    }
}
