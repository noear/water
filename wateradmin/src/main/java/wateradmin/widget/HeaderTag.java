package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfUtil;
import org.noear.bcf.models.BcfGroupModel;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.solon.Solon;
import org.noear.water.utils.TextUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import wateradmin.Config;
import wateradmin.dso.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component("ftl:header")
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
        //当前视图path //此处改过，noear，20180831
        Context context = Context.current();
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

        sb.append("<label>"); //new
        sb.append(Solon.cfg().appTitle());
        sb.append("</label>\n");//new


        sb.append("<nav>");

        for(BcfGroupModel g :list) {
            BcfResourceModel res = BcfClient.getUserFirstResourceByPack(Session.current().getPUID(), g.pgid);

            if (TextUtils.isEmpty(res.uri_path) == false) {
                buildItem(sb, g.cn_name, res, cPath, g.uri_path); //::en_name 改为 uri_path
            }
        }

        sb.append("</nav>\n");

        sb.append("<aside>");//new
        String temp = Session.current().getUserName();
        if(temp!=null) {
            sb.append("<i class='fa fa-user'></i> ");
            sb.append(temp);
        }

        sb.append("<a class='logout' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        sb.append("</aside>");//new

        sb.append("</header>\n");

        env.getOut().write(sb.toString());
    }

    private void buildItem(StringBuffer sb,String title,BcfResourceModel res,String cPath,String pack) {

        //此处改过，noear，201811(uadmin)
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
