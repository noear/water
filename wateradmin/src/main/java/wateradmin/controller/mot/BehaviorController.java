package wateradmin.controller.mot;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfServiceChecker;
import wateradmin.dso.SettingUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterLogApi;
import wateradmin.models.ScaleType;

import java.util.List;


@Controller
@Mapping("/mot/")
public class BehaviorController extends BaseController {

    private final static String logger = WW.logger_water_log_sql_b;

    //消息异常记录
    @Mapping("behavior")
    public ModelAndView behavior(Context ctx, String tag_name) throws Exception {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            ctx.redirect("/mot/behavior/inner");
            return null;
        }

        List<TagCountsM> tags = DbWaterLogApi.getSqlGroupsByLogger(logger);

        BcfServiceChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tags", tags);
        viewModel.put("tag_name", tag_name);

        return view("mot/behavior");
    }

    /**
     * state: ALL,SELECT,UPDATE,INSERT,DELETE,OTHER
     */
    @Mapping("behavior/inner")
    public ModelAndView behavior_inner(String tag_name, String serviceName, String tagx, String log_date, String path, Integer _state, long startId) throws Exception {
        if (SettingUtils.serviceScale().ordinal() < ScaleType.medium.ordinal()) {
            tag_name = null;
        }

        List<TagCountsM> services = DbWaterLogApi.getSqlServicesByLogger(logger, tag_name);

        //BcfServiceChecker.filter(services, m -> m.tag);
        if (Utils.isEmpty(serviceName)) {
            if (services.size() > 0) {
                serviceName = services.get(0).tag;
            }
        }

        if (Utils.isEmpty(serviceName)) {
            return null;
        }


        viewModel.put("tabs", services);
        viewModel.put("tag_name", tag_name);

        //////

        List<TagCountsM> peratorList = DbWaterLogApi.getSqlOperatorTags(logger, tag_name, serviceName);

        String method = null;

        if (_state != null) {
            switch (_state) {
                case 1:
                    method = "SELECT";
                    break;
                case 2:
                    method = "UPDATE";
                    break;
                case 3:
                    method = "INSERT";
                    break;
                case 4:
                    method = "DELETE";
                    break;
                case 5:
                    method = "OTHER";
                    break;
            }
        }

        long timestamp = 0;
        if (TextUtils.isNotEmpty(log_date)) {
            if (log_date.contains(".")) {
                timestamp = Datetime.parse(log_date, "yyyyMMdd.HH").getTicks();
            } else {
                timestamp = Datetime.parse(log_date, "yyyyMMdd").getTicks();
            }
        }

        int pageSize = 50;
        List<LogModel> list = DbWaterLogApi.getSqlLogsByPage(logger, tag_name, serviceName, method, 0, tagx, path, startId, timestamp);

        viewModel.put("pageSize", pageSize);
        viewModel.put("listSize", list.size());
        viewModel.put("list", list);
        viewModel.put("peratorList", peratorList);
        viewModel.put("serviceName", serviceName);

        if (list.size() > 0) {
            viewModel.put("lastId", list.get(list.size() - 1).log_id);
        } else {
            viewModel.put("lastId", 0L);
        }

        return view("mot/behavior_inner");
    }
}
