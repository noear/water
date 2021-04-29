package org.noear.water.protocol.model.log;


import lombok.Getter;
import org.noear.solon.core.util.PrintUtil;
import org.noear.water.log.Level;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.HtmlEncode;
import org.noear.water.utils.TextUtils;

import java.util.Date;

@Getter
public class LogModel {
    public long log_id;
    public String trace_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String tag3;
    public String summary;
    public String class_name;
    public String thread_name;
    public String content;
    public String from;
    public int log_date;
    public Date log_fulltime;

    public String levelHtml() {
        return "<span class='level" + level + "'>[" + Level.of(level).name() + "]</span>";
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
            return HtmlEncode.encode(content).replaceAll("\n", "<br/>");
        }
    }

    public String html() {
        StringBuilder buf = new StringBuilder(500);

        //头
        buf.append("<span class='level" ).append(level).append("'>");
        buf.append("[").append(Level.of(level).name()).append("] ");
        buf.append(new Datetime(log_fulltime).toString("yyyy-MM-dd HH:mm:ss.SSS")).append(" ");

        if (TextUtils.isNotEmpty(thread_name)) {
            buf.append("[-").append(thread_name).append("]");
        }

        if (TextUtils.isNotEmpty(trace_id)) {
            buf.append("<a href='#' onclick=\"queryTraceId('*")
                    .append(trace_id).append("');\">[*")
                    .append(trace_id).append("]</a>");
        }

        if (TextUtils.isNotEmpty(tag)) {
            buf.append("[@tag0:").append(tag).append("]");
        }
        if (TextUtils.isNotEmpty(tag1)) {
            buf.append("[@tag1:").append(tag1).append("]");
        }
        if (TextUtils.isNotEmpty(tag2)) {
            buf.append("[@tag2:").append(tag2).append("]");
        }
        if (TextUtils.isNotEmpty(tag3)) {
            buf.append("[@tag3:").append(tag3).append("]");
        }


        if (TextUtils.isNotEmpty(class_name)) {
            buf.append(" ").append(class_name);
        }


        if (TextUtils.isNotEmpty(from)) {
            buf.append("#").append(from);
        }

        buf.append("</span>");

        //内容
        buf.append(":<br/>");

        if (TextUtils.isNotEmpty(summary)) {
            buf.append(summaryHtml()).append("<br/>");
        }

        buf.append(contentHtml());

        return buf.toString();
    }
}
