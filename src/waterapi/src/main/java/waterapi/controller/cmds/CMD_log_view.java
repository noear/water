package waterapi.controller.cmds;

import noear.weed.DataItem;
import noear.weed.DataList;
import org.apache.http.util.TextUtils;
import waterapi.dao.db.DbApi;
import waterapi.dao.db.DbLogApi;

import javax.servlet.http.Cookie;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_log_view extends CMDBase {
    public String logger;

    private boolean _isOutput = true;

    @Override
    public boolean isOutput() {
        return _isOutput;
    }

    @Override
    protected void cmd_exec() throws Exception {

        String access_key = get("ak", true);

        if (DbApi.isAccess(access_key) == false) {
            return;
        } else {
            response.addCookie(new Cookie("ak", access_key));
        }


        _isOutput = false;

        long start_id = getlong("start");
        int date = getInt("date");
        String tag = get("tag");
        long tag1 = getlong("tag1");
        long tag2 = getlong("tag2");


        try {
            DataList list = DbLogApi.getLogList(logger, tag, tag1,tag2, date, start_id);


            if (list != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<title>log</title>");
                sb.append("</head>");
                sb.append("<body>");

                for (DataItem d : list.getRows()) {

                    sb.append("<div>")
                            .append("<span>").append(d.getDateTime("log_fulltime")).append("</span>")
                            .append("<span>").append("::").append("</span>")
                            .append("<span>").append(d.getLong("log_id"))
                                             .append("@").append(d.getString("tag"))
                                             .append("@").append(d.getLong("tag1"))
                                             .append("@").append(d.getLong("tag2")).append("</span>")
                            .append("</div>");

                    sb.append("<div>")
                            .append("<span>").append(d.getDateTime("log_fulltime")).append("</span>")
                            .append("<span>").append("::").append("</span>")
                            .append("<span>").append("").append(d.getString("label")).append("</span>")
                            .append("</div>");

                    sb.append("<div>")
                            .append("<span>").append(d.getDateTime("log_fulltime")).append("</span>")
                            .append("<span>").append("::").append("</span>")
                            .append("<span>").append("").append(d.getString("content")).append("</span>")
                            .append("</div>");


                    sb.append("<br /><br />");
                }

                sb.append("</body>");
                sb.append("</html>");

                response.setCharacterEncoding("UTF-8");//设置响应的编码类型为UTF-8
                response.setContentType("text/html;charset=UTF-8");

                response.getWriter().write(sb.toString());
                response.getWriter().flush();
            }

        } catch (Exception ex) {
            try {
                response.setCharacterEncoding("UTF-8");//设置响应的编码类型为UTF-8
                response.getWriter().write(ex.getLocalizedMessage());
                response.getWriter().flush();
            } catch (Exception ee) {

            }
        }
    }
}
