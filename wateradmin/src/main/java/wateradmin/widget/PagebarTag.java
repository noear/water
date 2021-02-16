package wateradmin.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;

import java.io.IOException;
import java.util.Map;

@Component("ftl:pagebar")
public class PagebarTag implements TemplateDirectiveModel {

    private int pageIndex;
    private int pageCount;
    private int rowCount;
    private int pageSize;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env,Map map) throws Exception {
        NvMap mapExt = new NvMap(map);
        pageSize = mapExt.getInt("pageSize",1);
        rowCount = mapExt.getInt("rowCount",0);

        if (pageSize == 0)//pageSize要有初始值，不然不会出错
            pageSize = 1;

        pageCount = rowCount / pageSize;
        if (rowCount % pageSize > 0)
            pageCount++;

        if (pageCount == 0)
            pageCount = 1;

        Context context = Context.current();

        pageIndex = context.paramAsInt("page", 1);


        if (pageIndex < 1)
            pageIndex = 1;

        StringBuilder sb = new StringBuilder();
        sb.append("<pagebar class='mar10-t'>");

        sb.append("<left>");
        sb.append(" <table>");
        sb.append("  <tr>");
        sb.append("   <td><button type='button' class='form_button' onclick=\"UrlQueryBy('page',1)\" " + (1 == pageIndex ? "disabled='disabled'" : "") + " >首页</button></td>");
        sb.append("   <td><button type='button' class='form_button' onclick=\"UrlQueryBy('page'," + (pageIndex - 1) + ")\" " + (1 == pageIndex ? "disabled='disabled'" : "") + " >上一页</button></td>");
        sb.append("   <td><input  id='js_pager_no' type='text' value='" + (pageIndex) + "' onblur=\"UrlQueryBy('page',this.value);\" class='form_text' /></td>");
        sb.append("   <td><button type='button' class='form_button' onclick=\"UrlQueryBy('page'," + (pageIndex + 1) + ")\" " + (pageCount == pageIndex ? "disabled='disabled'" : "") + " >下一页</button></td>");
        sb.append("   <td><button type='button' class='form_button' onclick=\"UrlQueryBy('page'," + (pageCount) + ")\" " + (pageCount == pageIndex ? "disabled='disabled'" : "") + " >尾页</button></td>");
        sb.append("  </tr>");
        sb.append(" </table>");
        sb.append("</left>");

        sb.append("<right>");
        sb.append("  <em>共<span>" + (pageCount) + "</span>页</em>");
        sb.append("  <em>（共<span>" + (rowCount) + "</span>条记录）</em>");
        sb.append("</right>");

        sb.append("</pagebar>");

        sb.append("<script type=\"text/javascript\">\n" +
                "    $(function () {\n" +
                "        \n" +
                "        $('#js_pager_no').keyup(function(event){\n" +
                "            if (event.keyCode == 13)\n" +
                "                UrlQueryBy('page',this.value);\n" +
                "        });\n" +
                "\n" +
                "        $(document).keyup(function (event) {\n" +
                "\n" +
                "            if (event.ctrlKey) {\n" +
                "                if (event.keyCode == 37)\n" +
                "                    UrlQueryBy('page'," + (pageIndex - 1) + ");\n" +
                "                else if (event.keyCode == 39)\n" +
                "                    UrlQueryBy('page'," + (pageIndex + 1) + ");\n" +
                "            }\n" +
                "        });\n" +
                "    });\n" +
                "</script>");


        env.getOut().write(sb.toString());
    }
}
