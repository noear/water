package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.water.WW;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import wateradmin.dso.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("view:header")
public class HeaderTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try{
            build(env);
        }catch (Exception ex){
            log.error("{}",ex);
        }
    }

    private void build(Environment env) throws Exception{
        Context ctx = Context.current();
        long subjectId = Session.current().getSubjectId();
        String path = ctx.pathNew();

        if (subjectId == 0) {
            //如果用户未登录
            ctx.redirect("/login");
            return;
        }

        List<ResourceGroup> groupList = GritClient.global().auth().getUriGroupList(subjectId);

        if (groupList.size() == 0) {
            ctx.redirect("/login");
            return;
        }


        StringBuilder buf = new StringBuilder();
        buf.append("<header>");

        buf.append("<label title='").append(WW.water_version).append("'>"); //new
        buf.append(Solon.cfg().appTitle());
        buf.append("</label>\n");//new


        buf.append("<nav>");

        for (ResourceGroup group : groupList) {
            ResourceEntity res = GritClient.global().auth().getUriFristByGroup(subjectId, group.resource_id);

            if (Utils.isEmpty(res.link_uri) == false) {
                buildGroupItem(buf, group, res, path);
            }
        }

        buf.append("</nav>\n");

        buf.append("<aside>");//new

        String userDisplayName = Session.current().getDisplayName();
        if (Utils.isNotEmpty(userDisplayName)) {
            buf.append("<a>");
            buf.append("<i class='fa fa-user'></i> ");
            buf.append(userDisplayName);
            buf.append("</a>");
        }

        if(Session.current().isAdmin()){
            buf.append("<a class='split' href='/admin/@设置?@='><i class='fa fa-cogs'></i></a>");
        }

        buf.append("<a class='split' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>");
        buf.append("</aside>");//new

        buf.append("</header>\n");

        env.getOut().write(buf.toString());
    }

    private void buildGroupItem(StringBuilder buf, ResourceGroup resourceGroup, Resource res, String path) {
        String newUrl = GritUtil.buildDockUri(res);

        if (path.indexOf(resourceGroup.link_uri) == 0) {
            buf.append("<a class='sel' href='" + newUrl + "'>");
            buf.append(resourceGroup.display_name);
            buf.append("</a>");
        } else {
            buf.append("<a href='" + newUrl + "'>");
            buf.append(resourceGroup.display_name);
            buf.append("</a>");
        }
    }
}
