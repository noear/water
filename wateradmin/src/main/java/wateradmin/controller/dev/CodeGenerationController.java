package wateradmin.controller.dev;

import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.view.freemarker.RenderUtil;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbPaaSApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.FieldVoModel;
import wateradmin.models.water_paas.PaasFileModel;
import wateradmin.models.water_paas.PaasFileType;
import wateradmin.utils.UnderlineCamelUtil;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/dev/code")
public class CodeGenerationController extends BaseController {

    private static final int CFG_TYPE_DB = 10;

    private static final String TEMPLATE_TAG = "_code";

    private static String buildSqlGetFields(String table) {
        return "SHOW FULL FIELDS FROM `" + table + "`;";
    }

    @Mapping("")
    public ModelAndView code(String tag_name) throws SQLException {

        List<TagCountsModel> tags = DbWaterCfgApi.getConfigTagsByType(CFG_TYPE_DB);

        viewModel.set("tag_name", TextUtils.isNotEmpty(tag_name) ? tag_name : (tags.size() > 0 ? tags.get(0).tag : null));

        viewModel.set("resp", tags);

        return view("dev/code");

    }

    @Mapping("inner/{tag}")
    public ModelAndView inner(String tag) throws SQLException {

        List<ConfigModel> cfgs = DbWaterCfgApi.getConfigsByType(tag, CFG_TYPE_DB);

        List<PaasFileModel> tmls = DbPaaSApi.getFileList(TEMPLATE_TAG, PaasFileType.tml);

        viewModel.set("cfgs", cfgs);
        viewModel.set("tmls", tmls);

        viewModel.set("tag_name", tag);

        return view("dev/code_inner");

    }

    @Mapping("ajax/tb")
    public ViewModel tb(String tag,
                        String key) throws SQLException {

        ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(tag , key);
        DbContext db = cfg.getDb();

        List<String> tbs = new ArrayList<>();
        db.getMetaData().getTableAll().forEach((tw)->{
            tbs.add(tw.getName());
        });
        tbs.sort(String::compareTo);

        return new ViewModel().code(1, "成功").set("tbs", tbs);

    }

    @Mapping("ajax/getcode")
    public ViewModel get(String tag,
                         String key,
                         String tb,
                         int tml_id) throws Exception {

        DbContext db = DbWaterCfgApi.getConfigByTagName(tag , key).getDb();

        Map<String, Object> model = new HashMap<>();

        List<FieldVoModel> fields = db.sql(buildSqlGetFields(tb)).getList(FieldVoModel.class);

        for (FieldVoModel f : fields) {

            if ("PRI".equals(f.key)) {
                model.put("pri_key", f.field);
            }

            if (TextUtils.isEmpty(f.type)) {
                continue;
            }

            if (f.type.equals("tinyint(1)")) {
                f.type = "boolean";
                f.def = "false";
            } else if (f.type.startsWith("int")) {
                f.type = "int";
                f.def = "0";
            } else if(f.type.startsWith("tinyint") || f.type.startsWith("smallint")){
                f.type = "int";
                f.def = "0";
            } else if (f.type.startsWith("bigint")) {
                f.type = "long";
                f.def = "0L";
            } else if (f.type.startsWith("float")) {
                f.type = "float";
                f.def = "0F";
            } else if (f.type.startsWith("double")) {
                f.type = "double";
                f.def = "0D";
            } else if (f.type.startsWith("decimal")) {
                f.type = "BigDecimal";
                f.def = "BigDecimal.ZERO";
            } else if (f.type.startsWith("datetime") || f.type.startsWith("date") || f.type.startsWith("time")) {
                f.type = "Date";
                f.def = "null";
            } else {
                f.type = "String";
                f.def = "null";
            }
        }

        PaasFileModel tml = DbPaaSApi.getFile(tml_id);

        model.put("fields", fields);
        model.put("table_camel", UnderlineCamelUtil.underline2Camel(tb, false));
        model.put("tag", tag);
        model.put("key", key);
        model.put("tb", tb);

        String rst = RenderUtil.render(tml.content, model);

        return new ViewModel().code(1, "成功").set("rst", rst);
    }
}
