package webapp.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.tools.TextUtils;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbWaterApi;
import webapp.models.TagCountsModel;
import webapp.models.water.LoggerModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/mot/")
public class Log2Controller extends BaseController {

    //日志统计
    @XMapping("log")
    public ModelAndView logger(String tag) throws Exception {
        List<TagCountsModel> resp = DbWaterApi.getLoggerTags();

        BcfTagChecker.filter(resp, m -> m.tag);

        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag",tag);
        } else {
            if (resp.isEmpty() == false && resp.size()>0) {
                viewModel.put("tag",resp.get(0).tag);
            } else {
                viewModel.put("tag",null);
            }
        }
        viewModel.put("resp",resp);
        return view("mot/log");
    }

    //日志统计列表
    @XMapping("log/inner")
    public ModelAndView loggerInner(String tag,Integer _state, String sort) throws Exception {
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
        List<LoggerModel> list = DbWaterApi.getLoggersByTag(tag,_state, sort);
        viewModel.put("loggers",list);
        viewModel.put("_state",_state);
        return view("mot/log_inner");
    }
}
