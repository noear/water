package waterapi.controller.cmds;

import noear.weed.DataItem;
import noear.weed.DataList;
import org.apache.http.util.TextUtils;
import waterapi.dao.db.DbApi;
import waterapi.dao.db.DbLogApi;
import waterapi.dao.db.DbMsgApi;

import javax.servlet.http.Cookie;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_msg_view extends CMDBase {


    private boolean _isOutput = true;
    @Override
    public boolean isOutput() {
        return _isOutput;
    }

    @Override
    protected void cmd_exec() throws Exception{

        String access_key =  get("ak", true);

        if(DbApi.isAccess(access_key)==false){
            return;
        }else{
            response.addCookie(new Cookie("ak", access_key));
        }


        _isOutput = false;

        int topic_id = 0;
        int dist_count = getInt("c");
        String t = get("t");

        if(TextUtils.isEmpty(t) == false){
            topic_id = DbMsgApi.getTopicID(t);
        }

        if(dist_count==0 && topic_id == 0){
            dist_count=4;
        }

        try {
            DataList list = DbMsgApi.getMessageList(dist_count, topic_id);

            if(list != null){
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<title>msg</title>");
                sb.append("</head>");
                sb.append("<body>");

                for(DataItem d : list.getRows()){
                    sb.append("<div>")
                            .append("<span>").append(d.getDateTime("log_fulltime")).append("</span>")
                            .append("<span>").append("::").append("</span>")
                            .append("<span>").append(d.getString("topic_name")).append("::")
                                             .append(d.getLong("msg_id")).append("::")
                                             .append(d.getInt("dist_count"))
                            .append("</span>")
                            .append("</div>");

                    sb.append("<div>")
                            .append("<span>").append(d.getDateTime("log_fulltime")).append("</span>")
                            .append("<span>").append("::").append("</span>")
                            .append("<span>").append("").append(d.getString("content")).append("</span>")
                            .append("</div>");


                    sb.append("<br /><br /><br />");
                }
                sb.append("</body>");
                sb.append("</html>");

                response.setCharacterEncoding("UTF-8");//设置响应的编码类型为UTF-8
                response.setContentType("text/html;charset=UTF-8");

                response.getWriter().write(sb.toString());
                response.getWriter().flush();
            }

        }catch (Exception ex){
            try {
                response.setCharacterEncoding("UTF-8");//设置响应的编码类型为UTF-8
                response.getWriter().write(ex.getLocalizedMessage());
                response.getWriter().flush();
            }catch (Exception ee){

            }
        }
    }
}
