package org.noear.water.protocol.model.log;

import org.noear.water.model.LogLevel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.HtmlEncode;
import org.noear.water.utils.TextUtils;

/**
 * @author noear 2021/9/29 created
 */

public class LogFormater {
    public static final LogFormater instance = new LogFormater();

    public String html(LogModel log) {
        StringBuilder buf = new StringBuilder(500);

        //头
        buf.append("<span class='level").append(log.level).append("'>");
        buf.append("[").append(LogLevel.of(log.level).name()).append("] ");

        Datetime log_fulltime = new Datetime(log.log_fulltime);
        buf.append("<a time='").append(log_fulltime.toString("yyyy-MM-dd HH:mm:ss.SSS")).append("'>");
        buf.append(log_fulltime.toString("yyyy-MM-dd HH:mm:ss.SSS Z"));
        buf.append("</a>");

        buf.append(" ");

        if (TextUtils.isNotEmpty(log.thread_name)) {
            buf.append("[-").append(log.thread_name).append("]");
        }

        if (TextUtils.isNotEmpty(log.trace_id)) {
            buf.append("<a tagx='*").append(log.trace_id).append("'>");
            buf.append("[*").append(log.trace_id).append("]");
            buf.append("</a>");
        }

        if (TextUtils.isNotEmpty(log.tag)) {
            buf.append("<a tagx='").append(log.tag).append("'>");
            buf.append("[@tag0:").append(log.tag).append("]");
            buf.append("</a>");
        }
        if (TextUtils.isNotEmpty(log.tag1)) {
            buf.append("<a tagx='@").append(log.tag1).append("'>");
            buf.append("[@tag1:").append(log.tag1).append("]");
            buf.append("</a>");
        }
        if (TextUtils.isNotEmpty(log.tag2)) {
            buf.append("<a tagx='@@").append(log.tag2).append("'>");
            buf.append("[@tag2:").append(log.tag2).append("]");
            buf.append("</a>");
        }
        if (TextUtils.isNotEmpty(log.tag3)) {
            buf.append("<a tagx='@@@").append(log.tag3).append("'>");
            buf.append("[@tag3:").append(log.tag3).append("]");
            buf.append("</a>");
        }

        if (TextUtils.isNotEmpty(log.tag4)) {
            buf.append("<a tagx='@@@@").append(log.tag4).append("'>");
            buf.append("[@tag3:").append(log.tag4).append("]");
            buf.append("</a>");
        }


        if (TextUtils.isNotEmpty(log.class_name)) {
            buf.append(" ").append(log.class_name);
        }


        if (TextUtils.isNotEmpty(log.from)) {
            buf.append("#").append(log.from);
        }

        buf.append("</span>");

        //内容
        buf.append(":<br/>");

        buf.append(contentHtml(log));

        return buf.toString();
    }


    public String levelHtml(LogModel log) {
        return "<span class='level" + log.level + "'>[" + LogLevel.of(log.level).name() + "]</span>";
    }

    public String contentHtml(LogModel log) {
        if (log.content == null) {
            return "";
        } else {
            return HtmlEncode.encode(log.content).replaceAll("\n", "<br/>");
        }
    }
}