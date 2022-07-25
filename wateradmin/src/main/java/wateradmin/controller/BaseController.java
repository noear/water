package wateradmin.controller;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Render;
import org.noear.solon.validation.annotation.Valid;
import org.noear.water.WW;
import org.noear.water.utils.Datetime;
import wateradmin.Config;
import wateradmin.dso.Session;
import wateradmin.viewModels.ViewModel;

/**
 * Created by noear on 14-9-11.
 */
@Valid
@Singleton(false)
public class BaseController implements Render {
    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title", Solon.cfg().appTitle());


        //当前用户信息, new
        viewModel.put("user_id", Session.current().getSubjectId());
        viewModel.put("user_display_name", Session.current().getDisplayName());

        //操作权限
        int is_admin = Session.current().getIsAdmin();
        int is_operator = Session.current().getIsOperator();
        if (is_admin == 1) {
            is_operator = 1;
        }

        viewModel.put("is_admin", is_admin);
        viewModel.put("is_operator", is_operator);

        viewModel.put("faas_uri", Config.faas_uri());
        viewModel.put("raas_uri", Config.raas_uri());


        viewModel.put("timenow", Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)") + " - " + WW.water_version);
        viewModel.put("_version", WW.water_version);

        return viewModel.view(viewName + ".ftl");
    }

    /*
     * @return 输出一个跳转视图
     * @prarm  url 可以是任何URL地址
     * */
    public void redirect(String url) {
        Context.current().redirect(url);
    }

    @Override
    public void render(Object data, Context ctx) throws Throwable {
        if (data instanceof Throwable) {
            if (ctx.path().contains("/ajax/")) {
                Throwable ex = (Throwable) data;

                ViewModel vm = new ViewModel();
                vm.code(0, "操作失败：" + ex.getLocalizedMessage());

                ctx.status(200);
                ctx.render(vm.model());
                return;
            }
        }

        ctx.render(data);
    }
}
