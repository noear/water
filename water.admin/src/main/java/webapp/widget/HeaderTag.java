package webapp.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.http.util.TextUtils;
import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfUtil;
import org.noear.bcf.models.BcfGroupModel;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.solon.annotation.XBean;
import org.noear.solon.core.XContext;
import webapp.Config;
import webapp.dao.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@XBean("ftl:header")
public class HeaderTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try{
            build(env);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void build(Environment env) throws Exception{
        //当前视图path //此处改过，xyj，20180831
        XContext context = XContext.current();
        String cPath = context.path();


        if (Session.current().getPUID() == 0) {   //检查用户是已登录
            context.redirect("/login");
            return;
        }

        List<BcfGroupModel> list = BcfClient.getUserPacks(Session.current().getPUID());

        if(list.size() == 0){
            context.redirect("/login");
            return;
        }


        StringBuffer sb = new StringBuffer();
        sb.append("<header>");
        sb.append("<div>");
        sb.append("<label>"); //new
        //cls1
        sb.append("<a>").append(Config.web_title).append("</a>");

        sb.append("</label>");//new
        sb.append("<nav>");//new

        for(BcfGroupModel g :list) {
            BcfResourceModel res = BcfClient.getUserFirstResourceByPack(Session.current().getPUID(), g.pgid);

            if (TextUtils.isEmpty(res.uri_path) == false) {
                buildItem(sb, g.cn_name, res, cPath, g.uri_path); //::en_name 改为 uri_path
            }
        }

        sb.append("</nav>");

        sb.append("<aside>");//new
        String temp = Session.current().getUserName();
        if(temp!=null) {
            sb.append("欢迎 ");
            sb.append(temp);
        }
        sb.append(" （<a href='#' onclick='modifyMm();return false;' >修改密码</a>）");
        sb.append("</aside>");//new

        sb.append("</div>");
        sb.append("</header>");

        env.getOut().write(sb.toString());
    }

    private void buildItem(StringBuffer sb,String title,BcfResourceModel res,String cPath,String pack) {

        //此处改过，xyj，201811(uadmin)
        String newUrl = BcfUtil.buildBcfUnipath(res);

        if(cPath.indexOf(pack)==0)
        {
            sb.append("<a class='sel' href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        }
        else
        {
            sb.append("<a href='" + newUrl + "'>");
            sb.append(title);
            sb.append("</a>");
        }

    }
}
