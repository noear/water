package xwater.controller.cfg;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import xwater.Config;
import xwater.controller.BaseController;
import xwater.dso.TagChecker;
import xwater.dso.TagUtil;
import xwater.dso.db.DbWaterCfgApi;
import xwater.models.TagCountsModel;
import xwater.models.view.water_cfg.BrokerModel;
import xwater.models.view.water_cfg.ConfigModel;
import xwater.viewModels.ViewModel;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Mapping("/cfg/")
public class BrokerController extends BaseController {
    private void tryInit() {
        if (ProtocolHub.msgBrokerFactory == null) {
            ProtocolHub.config = Config::getCfg;
            ConfigM msgCfg = Config.getCfg(WW.water, WW.water_msg_store);

            if (msgCfg != null) {
                try {
                    ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(msgCfg, Config.cache, DbWaterCfgApi::getBroker);
                } catch (Exception e) {
                    ProtocolHub.msgBrokerFactory = null;
                    throw e;
                }
            }
        }
    }

    @Mapping("broker")
    public ModelAndView broker(String tag_name) throws Exception {
        tryInit();

        List<TagCountsModel> tags = DbWaterCfgApi.getBrokerTags();

        TagChecker.filterWaterTag(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/broker");
    }

    @Mapping("broker/inner")
    public ModelAndView brokerInner(String tag_name) throws Exception {
        tryInit();

        List<BrokerModel> list = DbWaterCfgApi.getBrokersByTag(tag_name, null);
        viewModel.put("brokers", list);
        viewModel.put("tag_name", tag_name);
        return view("cfg/broker_inner");
    }

    //日志配置编辑页面跳转。
    @Mapping("broker/edit")
    public ModelAndView brokerEdit(String tag_name, Integer broker_id) throws Exception {
        if (broker_id == null) {
            broker_id = 0;
        }

        BrokerModel broker = DbWaterCfgApi.getBroker(broker_id);
        List<ConfigModel> configs = DbWaterCfgApi.getMsgStoreConfigs();
        List<String> option_sources = new ArrayList<>();
        for (ConfigModel config : configs) {
            option_sources.add(config.tag + "/" + config.key);
        }

        if (broker.broker_id == 0) {
            broker.keep_days = 15;
            broker.is_enabled = 1;
        } else {
            tag_name = broker.tag;
        }

        viewModel.put("option_sources", option_sources);
        viewModel.put("model", broker);
        viewModel.put("tag_name", tag_name);

        return view("cfg/broker_edit");
    }

    //日志配置ajax 保存功能。
    @Mapping("broker/edit/ajax/save")
    public ViewModel saveBroker(Integer broker_id, String tag, String broker, @Param(defaultValue = "") String source, String note, int keep_days, int is_alarm,int is_enabled) throws Exception {
        boolean result = DbWaterCfgApi.setBroker(broker_id, tag.trim(), broker.trim(), source.trim(), note, keep_days, is_alarm, is_enabled);

        if (result) {
            try {
                ProtocolHub.getMsgSource(broker).create();
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
    @Mapping("broker/edit/ajax/del")
    public ViewModel delBroker(Integer broker_id) throws SQLException {

        DbWaterCfgApi.delBroker(broker_id);
        return viewModel.code(1, "操作成功！");
    }

    //日志启用/禁用
    @Mapping("broker/isEnable")
    public ViewModel brokerDelete(Integer broker_id, int is_enabled) throws SQLException {
        DbWaterCfgApi.setBrokerEnabled(broker_id, is_enabled);

        return viewModel.code(1, "保存成功！");
    }
}
