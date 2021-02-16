package wateradmin.widget;


import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfGroupModel;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;
import wateradmin.dso.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 14-9-10.
 */
@Component("ftl:toolmenu")
public class ToolmenuTag implements TemplateDirectiveModel {
    private String pack;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env, Map map) throws Exception {
        NvMap mapExt = new NvMap(map);

        pack = mapExt.get("pack");

        Context request = Context.current();
        //当前视图path
        String cPath = request.path();
        StringBuffer sb = new StringBuffer();

        BcfGroupModel gPack = BcfClient.getGroupByCode(pack);

        if (gPack.pgid > 0) {
            sb.append("<toolmenu>");
            sb.append("<tabbar>");

            forPack(request, gPack.pgid, sb, cPath);

            sb.append("</tabbar>");
            sb.append("</toolmenu>");

            env.getOut().write(sb.toString());
        }
    }

    private void forPack(Context request, int packID, StringBuffer sb, String cPath) throws SQLException {
        List<BcfResourceModel> list = BcfClient.getUserResourcesByPack(Session.current().getPUID(), packID);

        for (BcfResourceModel r : list) {
            buildItem(request, sb, r.cn_name, r.uri_path, cPath);
        }
    }

    private void buildItem(Context request, StringBuffer sb, String title, String url, String cPath) {
        String url2 = url + "?" + request.uri().getQuery();

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
