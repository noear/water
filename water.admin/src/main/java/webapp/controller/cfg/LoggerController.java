package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.tools.TextUtils;
import webapp.controller.BaseController;
import webapp.dao.BcfTagChecker;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.ConfigModel;
import webapp.models.water.LoggerModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/cfg/")
public class LoggerController extends BaseController{

    @XMapping("logger")
    public ModelAndView logger(String tag_name) throws Exception {
        List<LoggerModel> tags = DbWaterApi.getLoggerTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name",tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name",tags.get(0).tag);
            } else {
                viewModel.put("tag_name",null);
            }
        }
        viewModel.put("tags",tags);
        return view("cfg/logger");
    }

    @XMapping("logger/inner")
    public ModelAndView loggerInner(String tag_name,Integer _state) throws Exception {
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
        List<LoggerModel> list = DbWaterApi.getLoggersByTag(tag_name,_state, null);
        viewModel.put("loggers",list);
        viewModel.put("_state",_state);
        return view("cfg/logger_inner");
    }
    //日志配置编辑页面跳转。
    @XMapping("logger/edit")
    public ModelAndView loggerEdit(Integer logger_id) throws Exception {
        LoggerModel logger = DbWaterApi.getLoggerById(logger_id);
        List<ConfigModel> configs= DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
                option_sources.add(config.tag+"."+config.key);
        }
        viewModel.put("option_sources",option_sources);
        viewModel.put("log",logger);
        return view("cfg/logger_edit");
    }
    //日志配置新增页面跳转。
    @XMapping("logger/add")
    public ModelAndView loggerAdd() throws SQLException {
        List<ConfigModel> configs= DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
            option_sources.add(config.tag+"."+config.key);
        }
        viewModel.put("option_sources",option_sources);
        LoggerModel log = new LoggerModel();
        log.keep_days = 15;
        viewModel.put("log",log);
        return view("cfg/logger_edit");
    }
    //日志配置ajax 保存功能。
    @XMapping("logger/edit/ajax/save")
    public  ViewModel saveLoggerEdit(Integer logger_id,String tag,String logger,String source,String note,int keep_days) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if(is_admin==1) {
            boolean result = DbWaterApi.updateLogger(logger_id,tag,logger,source,note,keep_days);
            if (result) {
                viewModel.code(1,"保存成功！");
            } else {
                viewModel.code(0,"保存失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }

    //日志启用/禁用
    @XMapping("logger/isEnable")
    public boolean loggerDelete(Integer logger_id,Integer is_enabled) throws SQLException {
        return DbWaterApi.isEnableLogger(logger_id,is_enabled);
    }
}
