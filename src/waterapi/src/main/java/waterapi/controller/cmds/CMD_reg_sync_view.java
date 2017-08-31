package waterapi.controller.cmds;

import noear.weed.DataItem;
import noear.weed.DataList;
import waterapi.dao.db.DbApi;
import waterapi.dao.db.DbSevApi;

import javax.servlet.http.Cookie;
import java.util.Date;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_reg_sync_view extends CMDBase {


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
            DataList list = DbApi.getSyncList();

            if (list != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<title>sync</title>");
                sb.append("</head>");
                sb.append("<body>");

                sb.append("<table border='0'>");

                sb.append("<tr>");
                sb.append("<td>同步ID</td>");
                sb.append("<td>间隔时间</td>");
                sb.append("<td>目标</td>");
                sb.append("<td>源</td>");
                sb.append("<td>同步主键</td>");
                sb.append("<td>同步字段</td>");

                sb.append("</tr>");


                for (DataItem d : list.getRows()) {
                    sb.append("<tr>");

                    sb.append("<td>").append(d.getInt("sync_id")).append("</td>");
                    sb.append("<td>").append(d.getInt("interval")).append("</td>");
                    sb.append("<td>").append(d.getString("target")).append("</td>");
                    sb.append("<td>").append(d.getString("source")).append("</td>");

                    sb.append("<td>").append(d.getString("sync_key")).append("</td>");

                    sb.append("<td>").append(d.getString("sync_fields")).append("</td>");

                    sb.append("</tr>");
                }
                sb.append("</table>");

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
