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
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_cfg.BrokerModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Mapping("/cfg/")
public class BrokerController extends BaseController {

    @Mapping("broker")
    public ModelAndView broker(String tag_name, int _state) throws Exception {
        List<TagCountsModel> tags = DbWaterCfgApi.getBrokerTags();

        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("_state", _state);
        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/broker");
    }

    @Mapping("broker/inner")
    public ModelAndView brokerInner(String tag_name, int _state) throws Exception {
        List<BrokerModel> list = DbWaterCfgApi.getBrokersByTag(tag_name, _state == 0, null);

        viewModel.put("_state", _state);
        viewModel.put("brokers", list);
        viewModel.put("tag_name", tag_name);

        return view("cfg/broker_inner");
    }

    //日志配置编辑页面跳转。
    @Mapping("broker/edit")
    public ModelAndView brokerEdit(String tag_name, int broker_id) throws Exception {
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
    @AuthPermissions(SessionPerms.admin)
    @NotEmpty({"tag", "broker"})
    @Mapping("broker/edit/ajax/save")
    public ViewModel saveBroker(Integer broker_id, String tag, String broker, @Param(defaultValue = "") String source, String note, int keep_days, int is_alarm, int is_enabled) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限");
        }

        boolean result = DbWaterCfgApi.setBroker(broker_id, tag.trim(), broker.trim(), source.trim(), note, keep_days, is_alarm, is_enabled);

        if (result) {
            try {
                ProtocolHub.getMsgSource(broker).create();
                viewModel.code(1, "保存成功！");

                //发送通知消息
                NoticeUtils.updateCache("broker:" + broker);
            } catch (SQLNonTransientConnectionException e) {
                viewModel.code(0, "创建结构失败（连接异常或没有权限）！");
            }
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    //日志配置ajax 保存功能。
    @AuthPermissions(SessionPerms.admin)
    @NotZero("broker_id")
    @Mapping("broker/edit/ajax/del")
    public ViewModel delBroker(int broker_id) throws SQLException {
        DbWaterCfgApi.delBroker(broker_id);

        return viewModel.code(1, "操作成功！");
    }

    //日志启用/禁用
    @AuthPermissions(SessionPerms.admin)
    @NotZero("broker_id")
    @Mapping("broker/isEnable")
    public ViewModel brokerEnable(int broker_id, int is_enabled) throws SQLException {
        DbWaterCfgApi.setBrokerEnabled(broker_id, is_enabled);

        return viewModel.code(1, "保存成功！");
    }
}
