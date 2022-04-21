package wateradmin.controller.cfg;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import org.noear.water.dso.NoticeUtils;
import org.noear.water.protocol.ProtocolHub;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.dso.TagUtil;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_cfg.LoggerModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Mapping("/cfg/")
public class LoggerController extends BaseController {

    @Mapping("logger")
    public ModelAndView logger(String tag_name) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags();

        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/logger");
    }

    @Mapping("logger/inner")
    public ModelAndView loggerInner(String tag_name, Integer _state) throws Exception {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }

        if (_state == null) {
            _state = 1;
        }

        List<LoggerModel> list = DbWaterCfgApi.getLoggersByTag(tag_name, _state, null);
        viewModel.put("loggers", list);
        viewModel.put("_state", _state);
        viewModel.put("tag_name", tag_name);
        return view("cfg/logger_inner");
    }

    //日志配置编辑页面跳转。
    @Mapping("logger/edit")
    public ModelAndView loggerEdit(String tag_name, Integer logger_id) throws Exception {
        if (logger_id == null) {
            logger_id = 0;
        }

        LoggerModel logger = DbWaterCfgApi.getLogger(logger_id);
        List<ConfigModel> configs = DbWaterCfgApi.getLogStoreConfigs();
        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : configs) {
            option_sources.add(config.tag + "/" + config.key);
        }

        if (logger.logger_id == 0) {
            logger.keep_days = 15;
            logger.is_enabled = 1;
        } else {
            tag_name = logger.tag;
        }

        viewModel.put("option_sources", option_sources);
        viewModel.put("model", logger);
        viewModel.put("tag_name", tag_name);

        return view("cfg/logger_edit");
    }

    //日志配置ajax 保存功能。
    @AuthPermissions(SessionPerms.admin)
    @NotEmpty({"tag", "logger"})
    @Mapping("logger/edit/ajax/save")
    public ViewModel saveLogger(Integer logger_id, String tag, String logger, @Param(defaultValue = "") String source, String note, int keep_days, int is_alarm, int is_enabled) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        DbWaterCfgApi.setLogger(logger_id, tag.trim(), logger.trim(), source.trim(), note, keep_days, is_alarm, is_enabled);

        try {
            //尝试建记录器
            ProtocolHub.logQuerier.create(logger, keep_days);
            //发送通知消息
            NoticeUtils.updateCache("logger:" + logger);

            viewModel.code(1, "保存成功！");
        } catch (Exception e) {
            viewModel.code(0, "创建结构失败（" + e.getLocalizedMessage() + "）！");
        }

        return viewModel;
    }

    //日志配置ajax 保存功能。
    @AuthPermissions(SessionPerms.admin)
    @NotZero("logger_id")
    @Mapping("logger/edit/ajax/del")
    public ViewModel delLogger(Integer logger_id) throws SQLException {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        DbWaterCfgApi.delLogger(logger_id);
        return viewModel.code(1, "操作成功！");
    }

    //日志启用/禁用
    @NotZero("logger_id")
    @Mapping("logger/isEnable")
    public ViewModel loggerDelete(Integer logger_id, int is_enabled) throws SQLException {
        DbWaterCfgApi.setLoggerEnabled(logger_id, is_enabled);

        return viewModel.code(1, "保存成功！");
    }
}
