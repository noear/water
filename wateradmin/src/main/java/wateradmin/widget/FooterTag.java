package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;

import java.io.IOException;
import java.util.Map;


@Component("ftl:footer")
public class FooterTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        String cpath = Context.current().path();

        if (cpath.startsWith("/login")) {
            StringBuffer sb = new StringBuffer();

            sb.append("<footer>");
            sb.append("<p>");
            sb.append("</p>");
            sb.append("</footer>");

            env.getOut().write(sb.toString());
        }
    }
}
