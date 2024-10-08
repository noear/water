package xwater.controller.cfg;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import xwater.Config;
import xwater.controller.BaseController;
import xwater.dso.TagChecker;
import xwater.dso.TagUtil;
import xwater.dso.db.DbWaterCfgApi;
import xwater.models.TagCountsModel;
import xwater.models.view.water_cfg.ConfigModel;
import xwater.models.view.water_cfg.LoggerModel;
import xwater.viewModels.ViewModel;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Mapping("/cfg/")
public class LoggerController extends BaseController {
    private void tryInit() {
        if (ProtocolHub.logSourceFactory == null) {
            ProtocolHub.config = Config::getCfg;
            ConfigM logCfg = Config.getCfg(WW.water, WW.water_log_store);

            if (logCfg != null) {
                try {
                    ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(logCfg, DbWaterCfgApi::getLogger);
                } catch (Exception e) {
                    ProtocolHub.logSourceFactory = null;
                    throw e;
                }
            }
        }
    }

    @Mapping("logger")
    public ModelAndView logger(String tag_name) throws Exception {
        tryInit();

        List<TagCountsModel> tags = DbWaterCfgApi.getLoggerTags(true);

        TagChecker.filterWaterTag(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/logger");
    }

    @Mapping("logger/inner")
    public ModelAndView loggerInner(String tag_name) throws Exception {
        tryInit();

        List<LoggerModel> list = DbWaterCfgApi.getLoggersByTag(tag_name,null);
        viewModel.put("loggers", list);
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
    @Mapping("logger/edit/ajax/save")
    public ViewModel saveLogger(Integer logger_id, String tag, String logger, @Param(defaultValue = "") String source, String note, int keep_days, int is_alarm, int is_enabled) throws Exception {


        boolean result = DbWaterCfgApi.setLogger(logger_id, tag.trim(), logger.trim(), source.trim(), note, keep_days, is_alarm, is_enabled);

        if (result) {
            try {
                ProtocolHub.logQuerier.create(logger, keep_days);
                viewModel.code(1, "保存成功！");
            } catch (SQLNonTransientConnectionException e) {
                viewModel.code(0, "创建结构失败（连接异常或没有权限）！");
            }
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    //日志配置ajax 保存功能。
    @Mapping("logger/edit/ajax/del")
    public ViewModel delLogger(Integer logger_id) throws SQLException {

        DbWaterCfgApi.delLogger(logger_id);
        return viewModel.code(1, "操作成功！");
    }

    //日志启用/禁用
    @Mapping("logger/isEnable")
    public ViewModel loggerDelete(Integer logger_id, int is_enabled) throws SQLException {
        DbWaterCfgApi.setLoggerEnabled(logger_id, is_enabled);

        return viewModel.code(1, "保存成功！");
    }
}
