package wateradmin.controller.paas;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.*;
import org.noear.weed.DataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.PaasUtils;
import wateradmin.dso.Session;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbPaaSApi;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_paas.PaasFileModel;
import wateradmin.models.water_paas.PaasFileType;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/paas/file")
public class FileController extends BaseController {
    static Logger paasLog = LoggerFactory.getLogger("water_log_paas");

    @Mapping("api/home")
    public ModelAndView api_home(String tag_name, String key) throws SQLException {
        return home(tag_name, PaasFileType.api, key);
    }

    @Mapping("tml/home")
    public ModelAndView tml_home(String tag_name, String key) throws SQLException {
        return home(tag_name, PaasFileType.tml, key);
    }

    @Mapping("msg/home")
    public ModelAndView msg_home(String tag_name, String key) throws SQLException {
        return home(tag_name, PaasFileType.msg, key);
    }

    @Mapping("pln/home")
    public ModelAndView pln_home(String tag_name, String key) throws SQLException {
        return home(tag_name, PaasFileType.pln, key);
    }

    private ModelAndView home(String tag_name, PaasFileType type, String key) throws SQLException {
        List<TagCountsModel> tags = DbPaaSApi.getFileTags(type);

        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("paas/file");
    }

    @Mapping("api/list")
    public ModelAndView api_list(Context ctx) throws SQLException {
        return list(ctx, PaasFileType.api);
    }

    @Mapping("pln/list")
    public ModelAndView pln_list(Context ctx) throws SQLException {
        return list(ctx, PaasFileType.pln);
    }

    @Mapping("msg/list")
    public ModelAndView msg_list(Context ctx) throws SQLException {
        return list(ctx, PaasFileType.msg);
    }

    @Mapping("tml/list")
    public ModelAndView tml_list(Context ctx) throws SQLException {
        return list(ctx, PaasFileType.tml);
    }

    private ModelAndView list(Context ctx, PaasFileType type) throws SQLException {
        String tag_name = ctx.param("tag_name", "");
        String key = ctx.param("key", "");
        int act = ctx.paramAsInt("act",11);
        int state = ctx.paramAsInt("state",0);


        TagUtil.cookieSet(tag_name);

        key = Base64Utils.decode(key);

        List<PaasFileModel> list = DbPaaSApi.getFileList(tag_name, type, (state == 1), key, act);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tag", tag_name);
        viewModel.put("key", key);

        if (type == PaasFileType.pln) {
            viewModel.put("_m", "2");
        } else {
            viewModel.put("_m", "1");
        }


        viewModel.put("state",state);
        viewModel.put("mlist", list);

        return view("paas/file_list_"+type.name());
    }

    @Mapping("api/edit")
    public ModelAndView api_edit(String tag, Integer file_id, Context ctx) throws SQLException {
        return edit(tag, PaasFileType.api, file_id, ctx);
    }

    @Mapping("pln/edit")
    public ModelAndView pln_edit(String tag, Integer file_id, Context ctx) throws SQLException {
        return edit(tag, PaasFileType.pln, file_id, ctx);
    }

    @Mapping("msg/edit")
    public ModelAndView msg_edit(String tag, Integer file_id, Context ctx) throws SQLException {
        return edit(tag, PaasFileType.msg, file_id, ctx);
    }

    @Mapping("tml/edit")
    public ModelAndView tml_edit(String tag, Integer file_id, Context ctx) throws SQLException {
        return edit(tag, PaasFileType.tml, file_id, ctx);
    }

    private ModelAndView edit(String tag, PaasFileType type, Integer file_id, Context ctx) throws SQLException {
        PaasFileModel file = null;
        if (file_id == null) {
            file_id = 0;
            file = new PaasFileModel();
        } else {
            file = DbPaaSApi.getFile(file_id);

            if (file.tag != null) {
                tag = file.tag;
            }
        }

        if (file.tag == null) {
            file.tag = tag;
        }

        List<TagCountsModel> whitelist = DbWaterCfgApi.getWhitelistTags();

        viewModel.put("id", file_id);
        viewModel.put("tag", tag);
        viewModel.put("m1", file);
        viewModel.put("whitelist", whitelist);

        return view("paas/file_edit_" + type.name());
    }

    @Mapping("api/ajax/save")
    public Object api_ajax_save(Context ctx) throws Exception {
        DataItem data = new DataItem();

        data.set("link_to", ctx.param("link_to"));
        data.set("content_type", ctx.param("content_type"));
        data.set("is_staticize", ctx.paramAsInt("is_staticize"));
        data.set("use_whitelist", ctx.param("use_whitelist", ""));

        int file_id = ctx.paramAsInt("id", 0);

        //处理消息订阅
        String label = ctx.param("label", "");
        String path = ctx.param("path", "");
        int is_disabled = ctx.paramAsInt("is_disabled");

        PaasUtils.trySubscribe(file_id, label, path, is_disabled == 1);

        return ajax_save(ctx, data, PaasFileType.api);
    }

    @Mapping("msg/ajax/save")
    public Object msg_ajax_save(Context ctx) throws Exception {
        DataItem data = new DataItem();

        int file_id = ctx.paramAsInt("id", 0);

        //处理消息订阅
        String label = ctx.param("label", "");
        String path = ctx.param("path", "");
        int is_disabled = ctx.paramAsInt("is_disabled");

        PaasUtils.trySubscribe(file_id, label, path, is_disabled == 1);

        return ajax_save(ctx, data, PaasFileType.msg);
    }

    @Mapping("pln/ajax/save")
    public Object pln_ajax_save(Context ctx) throws Exception {
        DataItem data = new DataItem();

        String plan_begin_time = ctx.param("plan_begin_time");
        String plan_last_time = ctx.param("plan_last_time");

        if (plan_begin_time != null) {
            data.set("plan_begin_time", Datetime.parse(plan_begin_time, "yyyy-MM-dd HH:mm:ss").getTicks());
        }
        if (plan_last_time != null) {
            data.set("plan_last_time", Datetime.parse(plan_last_time, "yyyy-MM-dd HH:mm:ss").getTicks());
        }

        data.set("plan_next_time", 0L);

        data.set("plan_interval", ctx.param("plan_interval"));
        data.set("plan_max", ctx.paramAsInt("plan_max"));


        String tag1 = ctx.param("tag", "");
        String tag2 = ctx.param("path", "");

        MDC.put("tag0", "_plan");
        MDC.put("tag1", tag1);
        MDC.put("tag2", tag2);

        paasLog.warn("New setting: {}",ONode.stringify(ctx.paramMap()));

        return ajax_save(ctx, data, PaasFileType.pln);
    }

    @Mapping("tml/ajax/save")
    public Object tml_ajax_save(Context ctx) throws SQLException {
        DataItem data = new DataItem();

        data.set("content_type", ctx.param("content_type"));
        data.set("rank", ctx.paramAsInt("rank"));

        return ajax_save(ctx, data, PaasFileType.tml);
    }

    public Object ajax_save(Context ctx, DataItem data, PaasFileType type) throws SQLException {
        data.set("label", ctx.param("label",""));
        data.set("tag", ctx.param("tag",""));
        data.set("path", ctx.param("path",""));
        data.set("edit_mode", ctx.param("edit_mode"));
        data.set("note", ctx.param("note",""));
        data.set("is_disabled", ctx.paramAsInt("is_disabled"));
        data.set("is_staticize", ctx.paramAsInt("is_staticize"));

        int file_id = ctx.paramAsInt("id", 0);
        DbPaaSApi.setFile(file_id, data, type);

        return viewModel.code(1,"ok");
    }

    @Mapping("{type}/ajax/del")
    public Object ajax_del(Context ctx, String type, Integer id) throws SQLException {
        if (id != null) {
            DbPaaSApi.delFile(id);
        }

        return viewModel.code(1,"ok");
    }

    /**
     * 立即执行
     * */
    @Mapping("pln/ajax/reset")
    public Object reset(Context ctx, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        DbPaaSApi.resetFilePlan(ids);

        return viewModel.code(1, "ok");
    }

    @Mapping("{type}/code")
    public ModelAndView code(Context ctx, Integer file_id, Integer readonly) throws SQLException {
        PaasFileModel file = DbPaaSApi.getFile(file_id);

        viewModel.put("id", file_id);
        viewModel.put("m1", file);

        String edit_mode = file.edit_mode;
        if ("freemarker".equals(edit_mode)) {
            edit_mode = "ftl";
        }

        if ("thymeleaf".equals(edit_mode)) {
            edit_mode = "html";
        }

        if ("beetl".equals(edit_mode)) {
            edit_mode = "jsp";
        }

        if ("enjoy".equals(edit_mode)) {
            edit_mode = "velocity";
        }

        if ("graaljs".equals(edit_mode)) {
            edit_mode = "javascript";
        }

        if ("shell".equals(edit_mode)) {
            edit_mode = "sh";
        }

        if (file.content == null) {
            file.content = "";
        }

        viewModel.put("edit_mode", edit_mode);
        viewModel.put("code64", Base64Utils.encode(file.content));

        if(readonly != null){
            return view("paas/file_code_readonly");
        }else{
            if("ftl".equals(edit_mode) || "velocity".equals(edit_mode)) {
                return view("paas/file_code");
            }else{
                return view("paas/file_code2");
            }
        }
    }

    @Mapping("{type}/code/ajax/save")
    public Object code_save(Context ctx, Integer id, String fc64, String path) throws SQLException {
        if(id == null || id == 0){
            return viewModel.code(0,"");
        }

        String fc = Base64Utils.decode(fc64);

        DbPaaSApi.setFileContent(id, fc);

        return viewModel.code(1,"ok");
    }


    //批量导出
    @Mapping("{type}/ajax/export")
    public void exportDo(Context ctx, String type, String tag, String ids) throws Exception {
        List<PaasFileModel> list = DbPaaSApi.getFilesByIds(PaasFileType.valueOf(type), ids);

        String jsonD = JsondUtils.encode("paas_file", list);

        String filename2 = "water_paasfile_" + type +"_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");

        ctx.output(jsonD);
    }


    //批量导入
    @Mapping("{type}/ajax/import")
    public ViewModel importDo(Context ctx, String type, String tag, UploadedFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonD = IOUtils.toString(file.content);
        JsondEntity entity = JsondUtils.decode(jsonD);

        if(entity == null || "paas_file".equals(entity.table) == false){
            return viewModel.code(0, "数据不对！");
        }

        List<PaasFileModel> list = entity.data.toObjectList(PaasFileModel.class);

        PaasFileType fileType = PaasFileType.valueOf(type);

        for (PaasFileModel m : list) {
            //支持跨tag导（路径自动改为新tag开头）
            if (m.path.indexOf(tag) < 0) {
                int start = m.path.indexOf("/", 2);
                m.path = "/" + tag + m.path.substring(start);
            }

            //订阅在先
            if (fileType == PaasFileType.api || fileType == PaasFileType.msg) {
                PaasUtils.trySubscribe(m.file_id, m.label, m.path, m.is_disabled);
            }

            //存入在后
            DbPaaSApi.impFile(fileType, tag, m);
        }

        return viewModel.code(1, "ok");
    }

    //批量删除
    @Mapping("{type}/ajax/batch")
    public ViewModel batchDo(Context ctx, String type, String tag, Integer act, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        if(act == null){
            act = 0;
        }

        DbPaaSApi.delFileByIds(PaasFileType.valueOf(type),act, ids);

        return viewModel.code(1, "ok");
    }
}
