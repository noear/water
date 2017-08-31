package waterapi.controller.cmds;

import noear.snacks.ONode;
import org.apache.http.util.TextUtils;
import waterapi.dao.LogUtil;
import waterapi.dao.db.DbApi;
import waterapi.utils.HttpUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by yuety on 2017/7/19.
 */
public abstract class CMDBase {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ONode data;

    protected String get(String key) {
        return get(key, false);
    }

    protected String get(String key, boolean tryCookie) {
        String temp = request.getParameter(key);
        if (tryCookie) {
            Cookie[] cookies = request.getCookies();
            if(cookies!= null){
                for(Cookie c : cookies){
                    if(key.equals(c.getName())){
                        return c.getValue();
                    }
                }
            }
        }
        return temp;
    }

    protected int getInt(String key){
        String val = get(key);
        if(TextUtils.isEmpty(val))
            return 0;
        else
            return Integer.parseInt(val);
    }

    protected long getlong(String key){
        String val = get(key);
        if(TextUtils.isEmpty(val))
            return 0;
        else
            return Long.parseLong(val);
    }

    public boolean isOutput(){
        return true;
    }

    public void exec(HttpServletRequest request, HttpServletResponse response){

        this.request = request;
        this.response = response;
        this.data = new ONode();

        try {
            if (do1_check_ip()) {
                do2_exec();
            }
        }catch (Exception ex){
            ex.printStackTrace();

            LogUtil.error(request,ex);

            data.set("code","0");
            data.set("msg", ex.getMessage());
        }

        if(isOutput()) {
            do3_ouput();
        }
    }

    protected abstract void cmd_exec() throws Exception;

    private final boolean do1_check_ip() throws SQLException{
        String ip = HttpUtil.getIP(request);

        data.set("_ip",ip);

        if (DbApi.isWhitelist(ip) == false) {
            data.set("code", 2);
            data.set("msg", "not whitelist(" + ip + ")");
            return false;
        }else{
            return true;
        }
    }

    private final  void do2_exec() throws Exception{
        cmd_exec();
    }

    private final void do3_ouput() {
        try {
            response.setCharacterEncoding("UTF-8");//设置响应的编码类型为UTF-8
            response.setContentType("text/json;charset=UTF-8");

            response.getWriter().write(data.toJson());
            response.getWriter().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error(request, ex);
        }
    }

    //==========
    protected final boolean checkParamsIsOk(String... strs) {
        for (String str : strs) {
            if (TextUtils.isEmpty(str)) {

                data.set("code", 3);
                data.set("msg", "not parameter");

                return false;
            }
        }

        return true;
    }
}
