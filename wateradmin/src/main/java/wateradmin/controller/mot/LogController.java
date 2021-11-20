package wateradmin.controller.mot;

import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.OptionUtils;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.LoggerModel;

import java.util.List;


@Controller
@Mapping("/mot/")
public class LogController extends BaseController {

    //日志统计
    @Mapping("log")
    public ModelAndView logger(Context ctx, String tag_name) throws Exception {
        if(OptionUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.redirect("/mot/log/inner");
            return null;
        }

        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false && tags.size()>0) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        viewModel.put("tags",tags);
        return view("mot/log");
    }

    //日志统计列表
    @Mapping("log/inner")
    public ModelAndView loggerInner(String tag_name,Integer _state, String sort) throws Exception {
        if(OptionUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null)
            _state = 1;
        List<LoggerModel> list = DbWaterCfgApi.getLoggersByTag(tag_name,_state, sort);
        viewModel.put("loggers",list);
        viewModel.put("_state",_state);
        return view("mot/log_inner");
    }
}
