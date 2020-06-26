package wateradmin.controller.mot;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.LoggerModelEx;

import java.util.List;


@XController
@XMapping("/mot/")
public class Log2Controller extends BaseController {

    //日志统计
    @XMapping("log")
    public ModelAndView logger(String tag_name) throws Exception {
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
    @XMapping("log/inner")
    public ModelAndView loggerInner(String tag_name,Integer _state, String sort) throws Exception {
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
        List<LoggerModelEx> list = DbWaterCfgApi.getLoggersByTag(tag_name,_state, sort);
        viewModel.put("loggers",list);
        viewModel.put("_state",_state);
        return view("mot/log_inner");
    }
}
