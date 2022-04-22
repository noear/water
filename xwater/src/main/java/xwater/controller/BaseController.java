package xwater.controller;


import org.noear.solon.Solon;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.utils.Datetime;
import xwater.viewModels.ViewModel;


/**
 * Created by noear on 14-9-11.
 */
@Singleton(false)
public class BaseController {
    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*
     * @return 输出一个视图（自动放置viewModel）
     * @param viewName 视图名字(内部uri)
     * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/css");
        viewModel.put("js", "/js");
        viewModel.put("img", "/img");
        viewModel.put("title", Solon.cfg().appTitle());


        //支持设置模式
        viewModel.put("is_admin", 1);
        viewModel.put("is_operator", 1);


        viewModel.put("timenow", Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)") + " - " + WW.water_version);
        viewModel.put("_version", WW.water_version);
        return viewModel.view(viewName + ".ftl");
    }
}
