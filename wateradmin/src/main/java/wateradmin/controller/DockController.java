package wateradmin.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Resource;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;

import java.net.URLDecoder;

 //非单例
@Controller
public class DockController extends BaseController {
    //支持外部url
    @Mapping("/**/$*") //视图 返回
    public ModelAndView dock1(Context ctx) {
        String path = ctx.path().toLowerCase();

        try {
            Resource res = GritClient.global().resource().getResourceByUri(path);
            viewModel.set("fun_name", res.display_name);
            viewModel.set("fun_url", res.link_uri);

            if (res.is_fullview) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception e) {
            EventBus.push(e);
        }

        return view("dock");
    }

    //此处改过，noear，201811(uadmin) //增加内部url支持
    @Mapping("/**/@*") //视图 返回
    public ModelAndView dock2(Context ctx) {
        String path = ctx.path();
        String query = ctx.queryString();

        String fun_name = path.split("/@")[1];
        String fun_url = path.split("/@")[0].toLowerCase();

        if(TextUtils.isEmpty(query)==false) {
            fun_url = fun_url + "?" + query;
        }

        try {
            viewModel.set("fun_name", URLDecoder.decode(fun_name, "utf-8"));
            viewModel.set("fun_url", fun_url);

            if (query != null && query.indexOf("@=") >= 0) {
                viewModel.set("fun_type", 1);
            } else {
                viewModel.set("fun_type", 0);
            }
        } catch (Exception e) {
            EventBus.push(e);
        }

        return view("dock");
    }
}
