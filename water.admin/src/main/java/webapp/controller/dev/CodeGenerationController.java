package webapp.controller.dev;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.client.WaterClient;
import org.noear.water.tools.StringUtils;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.view.freemarker.XRenderUtil;
import webapp.dao.db.DbPaaSApi;
import webapp.dao.db.DbWaterApi;
import webapp.models.vo.FieldModel;
import webapp.models.water.ConfigModel;
import webapp.models.water_paas.PaasTmlModel;
import webapp.utils.UnderlineCamelUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XController
@XMapping("/dev/code")
public class CodeGenerationController extends BaseController {

    private static final int CFG_TYPE_DB = 10;

    private static final String TEMPLATE_TAG = "_code";

    private static final String SEPARATOR_BETWEEN_TAG_AND_KEY = ".";

    private static final String SQL_GET_TABLES() {
        return "SHOW TABLES;";
    }

    private static String buildSqlGetFields(String table) {
        return "SHOW FULL FIELDS FROM `" + table + "`;";
    }

    @XMapping("")
    public ModelAndView code(String tag_name) throws SQLException {

        List<ConfigModel> resp = DbWaterApi.getTagGroupWithType(CFG_TYPE_DB);

        viewModel.set("tag_name", TextUtils.isNotEmpty(tag_name) ? tag_name : (resp.size() > 0 ? resp.get(0).tag : null));

        viewModel.set("resp", resp);

        return view("dev/code");

    }

    @XMapping("inner/{tag_name}")
    public ModelAndView inner(String tag_name) throws SQLException {

        List<ConfigModel> cfgs = DbWaterApi.getConfigByType(tag_name, CFG_TYPE_DB);

        List<PaasTmlModel> tmls = DbPaaSApi.tmlGetList(TEMPLATE_TAG, null, 1);

        viewModel.set("cfgs", cfgs);
        viewModel.set("tmls", tmls);

        viewModel.set("tag_name", tag_name);

        return view("dev/code_inner");

    }

    @XMapping("ajax/tb")
    public ViewModel tb(String tag,
                        String key) throws SQLException {

        DbContext db =  WaterClient.Config.getByTagKey(tag + SEPARATOR_BETWEEN_TAG_AND_KEY + key).getDb();

        List<String> tbs = db.sql(SQL_GET_TABLES()).getDataList().toArray(0);

        return new ViewModel().code(1, "成功").set("tbs", tbs);

    }

    @XMapping("ajax/getcode")
    public ViewModel get(String tag,
                         String key,
                         String tb,
                         int tml_id) throws Exception {

        DbContext db = WaterClient.Config.getByTagKey(tag + SEPARATOR_BETWEEN_TAG_AND_KEY + key).getDb();

        Map<String, Object> model = new HashMap<>();

        List<FieldModel> fields = db.sql(buildSqlGetFields(tb)).getList(new FieldModel());

        for (FieldModel f : fields) {

            if ("PRI".equals(f.key)) {
                model.put("pri_key", f.field);
            }

            if (StringUtils.isEmpty(f.type)) {
                continue;
            }

            if (f.type.startsWith("int")||f.type.startsWith("tinyint")) {
                f.type = "int";
                f.def = "0";
            } else if (f.type.startsWith("bigint")) {
                f.type = "long";
                f.def = "0l";
            } else if (f.type.startsWith("double") || f.type.startsWith("decimal")) {
                f.type = "double";
                f.def = "0D";
            } else if (f.type.startsWith("varchar") || f.type.startsWith("char") || f.type.startsWith("text")) {
                f.type = "String";
                f.def = "null";
            } else if (f.type.startsWith("datetime") || f.type.startsWith("date") || f.type.startsWith("time")) {
                f.type = "Date";
                f.def = "null";
            }
        }

        PaasTmlModel tml = DbPaaSApi.tmlGet(tml_id);

        model.put("fields", fields);
        model.put("table_camel", UnderlineCamelUtil.underline2Camel(tb, false));
        model.put("tag", tag);
        model.put("key", key);
        model.put("tb", tb);

        String rst = XRenderUtil.reander(tml.code, model);

        return new ViewModel().code(1, "成功").set("rst", rst);

    }

}
