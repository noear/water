package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.annotation.XBean;
import org.noear.solon.core.XContext;

import java.io.IOException;
import java.util.Map;


@XBean("ftl:footer")
public class FooterTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        String cpath = XContext.current().path();

        if (cpath.startsWith("/login")) {
            StringBuffer sb = new StringBuffer();

            sb.append("<footer>");
            sb.append("<p>");
            sb.append("众马科技 浙ICP备16027614号");
            sb.append("</p>");
            sb.append("</footer>");

            env.getOut().write(sb.toString());
        }
    }
}
