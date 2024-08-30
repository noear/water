package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;
import wateradmin.dso.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 14-9-10.
 */
@Slf4j
@Component("view:toolmenu")
public class ToolmenuTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map);
        } catch (Exception ex) {
            log.error("{}",ex);
        }
    }

    private void build(Environment env, Map map) throws Exception {
        NvMap mapExt = NvMap.from(map);

        String groupCode = mapExt.get("pack");

        Context ctx = Context.current();
        String path = ctx.pathNew();
        StringBuffer buf = new StringBuffer();

        Resource resourceGroup = GritClient.global().resource().getResourceByCode(groupCode);

        if (resourceGroup.resource_id > 0) {
            buf.append("<toolmenu>");
            buf.append("<tabbar>");

            List<ResourceEntity> list = GritClient.global().auth()
                    .getUriListByGroup(Session.current().getSubjectId(), resourceGroup.resource_id);

            for (Resource r : list) {
                buildItem(ctx, buf, r.display_name, r.link_uri, path);
            }

            buf.append("</tabbar>");
            buf.append("</toolmenu>");

            env.getOut().write(buf.toString());
        }
    }

    private void buildItem(Context ctx, StringBuffer sb, String title, String url, String cPath) {
        String url2 = url + "?" + ctx.uri().getQuery();

        if (cPath.indexOf(url) > 0) {
            sb.append("<button onclick=\"location='" + url2 + "'\" class='sel'>");
            sb.append(title);
            sb.append("</button>");
        } else {
            sb.append("<button onclick=\"location='" + url2 + "'\">");
            sb.append(title);
            sb.append("</button>");
        }
    }
}
