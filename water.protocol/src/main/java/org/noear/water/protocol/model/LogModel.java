package org.noear.water.protocol.model;


import lombok.Getter;
import org.noear.water.log.Level;
import org.noear.water.utils.HtmlEncode;

import java.util.Date;

@Getter
public class LogModel {
    public long log_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String tag3;
    public String summary;
    public String content;
    public String from;
    public int log_date;
    public Date log_fulltime;

    public String levelHtml(){
        return Level.of(level).name();
    }

    public String summaryHtml() {
        if (summary == null) {
            return "";
        } else {
            return HtmlEncode.encode(summary);
        }
    }

    public String contentHtml() {
        if (content == null) {
            return "";
        } else {
            if(content.indexOf("Exception:") > 0 || content.indexOf("Error:") > 0){
                return HtmlEncode.encode(content).replaceAll("\n","<br/>");
            }else {
                return HtmlEncode.encode(content);
            }
        }
    }
}
