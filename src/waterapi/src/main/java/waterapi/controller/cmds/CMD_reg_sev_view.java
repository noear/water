package waterapi.controller.cmds;

import noear.weed.DataItem;
import noear.weed.DataList;
import org.apache.http.util.TextUtils;
import waterapi.dao.db.DbApi;
import waterapi.dao.db.DbMsgApi;
import waterapi.dao.db.DbSevApi;

import javax.servlet.http.Cookie;
import java.util.Date;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_reg_sev_view extends CMDBase {


    private boolean _isOutput = true;

    @Override
    public boolean isOutput() {
        return _isOutput;
    }

    @Override
    protected void cmd_exec() throws Exception {

        String access_key =  get("ak", true);

        if(DbApi.isAccess(access_key)==false){
            return;
        }else{
            response.addCookie(new Cookie("ak", access_key));
        }

        _isOutput = false;


        try {
            DataList list = DbSevApi.getServiceList();

            if (list != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<title>service</title>");
                sb.append("</head>");
                sb.append("<body>");

                sb.append("<table border='0'>");

                sb.append("<tr>");
                sb.append("<td>服务ID</td>");
                sb.append("<td>名称</td>");
                sb.append("<td>地址</td>");
                sb.append("<td>备注</td>");
                sb.append("<td>检查类型</td>");
                sb.append("<td>检查地址</td>");

                sb.append("<td>最后检查时间</td>");
                sb.append("<td>最后检查状态</td>");
                sb.append("<td>最后检查备注</td>");
                sb.append("</tr>");


                for (DataItem d : list.getRows()) {
                    int state = d.getInt("check_last_state");
                    int type = d.getInt("check_type");

                    Date last_time = d.getDateTime("check_last_time");

                    if (state == 0) {
                        sb.append("<tr>");
                    } else {
                        sb.append("<tr style='color:red;'>");
                    }


                    sb.append("<td>").append(d.getInt("service_id")).append("</td>");
                    sb.append("<td>").append(d.getString("name")).append("</td>");
                    sb.append("<td>").append(d.getString("address")).append("</td>");
                    sb.append("<td>").append(d.getString("note")).append("</td>");

                    sb.append("<td>").append((type == 0 ? "监视" : "签到")).append("</td>");

                    sb.append("<td>").append(d.getString("check_url")).append("</td>");

                    long secs = (new Date().getTime() - last_time.getTime()) / 1000;

                    if (secs > 5) {
                        sb.append("<td style='color:red;'>").append(last_time).append("</td>");
                    } else {
                        sb.append("<td>").append(last_time).append("</td>");
                    }


                    if (state == 0) {
                        sb.append("<td>").append("ok").append("</td>");
                    } else {
                        sb.append("<td>").append("error").append("</td>");
                    }

                    sb.append("<td>").append(d.getString("check_last_note")).append("</td>");

                    sb.append("</tr>");
                }
                sb.append("</table>");

                sb.append("<script>setTimeout(function(){location.reload();}, 2000)</script>");


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
