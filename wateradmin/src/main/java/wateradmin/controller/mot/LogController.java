package wateradmin.controller.mot;

import org.noear.solon.core.handle.Context;
import org.noear.water.utils.TextUtils;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SettingUtils;
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
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.forward("/mot/log/inner");
            return null;
        }

        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags(false);

        TagChecker.filter(tags, m -> m.tag);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false && tags.size() > 0) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        viewModel.put("tags", tags);
        return view("mot/log");
    }

    //日志统计列表
    @Mapping("log/inner")
    public ModelAndView loggerInner(String tag_name, int _state, String sort) throws Exception {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        List<LoggerModel> list = DbWaterCfgApi.getLoggersByTag(tag_name, _state == 0, sort);

        viewModel.put("loggers", list);
        viewModel.put("_state", _state);
        return view("mot/log_inner");
    }
}
