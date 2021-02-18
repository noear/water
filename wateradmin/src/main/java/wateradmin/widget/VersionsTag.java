package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.core.NvMap;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.solon.annotation.Component;
import wateradmin.dso.IDUtil;
import wateradmin.dso.db.DbWaterVerApi;
import wateradmin.models.water.VersionModel;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component("ftl:versions")
public class VersionsTag implements TemplateDirectiveModel {

    private String table;
    private String keyName;
    private String keyValue;

    private String script;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map, body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env,Map map,TemplateDirectiveBody body) throws Exception {
        StringWriter sw = new StringWriter();
        body.render(sw);
        script = sw.toString();

        NvMap mapExt = new NvMap(map);
        table = mapExt.getOrDefault("table","");
        keyName = mapExt.getOrDefault("keyName","");
        keyValue = mapExt.getOrDefault("keyValue","");

        if (TextUtils.isEmpty(table) == false && TextUtils.isEmpty(keyName) == false && TextUtils.isEmpty(keyValue) == false) {
            String tagHtml = buildHtml();
            env.getOut().write(tagHtml);
        }
    }

    public String buildHtml() throws SQLException {
        StringBuilder sb = new StringBuilder();

        List<VersionModel> list = DbWaterVerApi.getVersions(table, keyName, keyValue);

        if(list.size()==0){
            return "";
        }

        String fun = "_"+ IDUtil.buildGuid();

        sb.append("<script>");
        sb.append("function ").append(fun).append("(m){ ");
        sb.append(script);
        sb.append(" }");
        sb.append("</script>");

        sb.append("<select onchange=\"versions_onselect(").append("this,").append(fun).append(")\">");
        sb.append("<option value='0'>历史版本...").append("</option>");
        for(VersionModel v : list){
            sb.append("<option value='").append(v.commit_id).append("'>")
                    .append("ver:")
                    .append(new Datetime(v.log_fulltime).toString("yyyy-MM-dd HH:mm:ss"))
                    .append(",").append(v.log_user)
                    .append("</option>");
        }
        sb.append("</select>");


        return sb.toString();
    }
}
