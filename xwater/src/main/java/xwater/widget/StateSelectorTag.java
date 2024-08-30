package xwater.widget;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;

import java.io.IOException;
import java.util.Map;


@Component("view:stateselector")
public class StateSelectorTag implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            build(env, map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(Environment env, Map map) throws Exception {
        NvMap mapExt = NvMap.from(map);

        clientID = mapExt.getOrDefault("clientID","");
        forPage = mapExt.getBool("forPage", true);
        onSelect = mapExt.getOrDefault("onSelect","");
        state = mapExt.getInt("state", 0);
        items = mapExt.getOrDefault("items","");
        stateKey = mapExt.getOrDefault("stateKey", "_state");

        StringBuilder sb = new StringBuilder();

        String clinetStateKey = clientID + stateKey;

        sb.append("<script>").append("function " + clientID + "_onStateSelect(val,e) { ");

        if (forPage && TextUtils.isEmpty(onSelect))
            sb.append("    UrlQueryBy('" + stateKey + "',val,'page');");
        else {
            sb.append("    $('#" + clinetStateKey + "').val(val);")
                    .append("    var m = $('#" + clientID + "');")
                    .append("    m.find('.selected').removeClass('selected');")
                    .append("    $(e).addClass('selected');");

            if (TextUtils.isEmpty(onSelect) == false) {
                sb.append(onSelect).append("(val,e);");
            }
        }

        sb.append("} </script>");

        sb.append("<input id='" + clinetStateKey + "' name='" + clinetStateKey + "' value='" + getState() + "' type='hidden' />");
        sb.append("<span class='selector' id='" + clientID + "'>");
        sb.append(buildHtml());
        sb.append("</span>");

        env.getOut().write(sb.toString());

    }


    //--------
    private String clientID = "";
    private boolean forPage = true;
    private String onSelect = "";

    private String stateKey = "_state";

    private int state = -1;//按自然顺序，0,1,2,3,4

    public int getState() {

        String key = clientID + stateKey;
        int s1 = getInt(key);

        if (s1 > -1)
            return s1;

        if (state > -1)
            return state;

        return 0;
    }

    private String items;

    private int getInt(String key) {
        return Context.current().paramAsInt(key, -1);
    }

    protected String buildHtml() {
        if (items == null || items.length() == 0)
            return "";


        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for (String item : items.split(",")) {
            if (idx == getState())
                sb.append("<span class='stateItem selected' onclick='" + clientID + "_onStateSelect(" + idx + ",this)'>" + item + "</span>");
            else
                sb.append("<span class='stateItem' onclick='" + clientID + "_onStateSelect(" + idx + ",this)'>" + item + "</span>");

            idx++;
        }
        return sb.toString();
    }
}
