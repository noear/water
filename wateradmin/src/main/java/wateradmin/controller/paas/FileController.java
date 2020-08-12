package wateradmin.controller.paas;

import org.noear.snack.ONode;
import org.noear.snack.core.TypeRef;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XFile;
import org.noear.water.WaterClient;
import org.noear.water.utils.*;
import org.noear.weed.DataItem;
import wateradmin.Config;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.Session;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbPaaSApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_paas.PaasFileModel;
import wateradmin.models.water_paas.PaasFileType;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/paas/file")
public class FileController extends BaseController {

    @XMapping("api/home")
    public ModelAndView nav_api(String tag_name, String key) throws SQLException {
        return nav(tag_name, PaasFileType.api, key);
    }

    @XMapping("tml/home")
    public ModelAndView nav_tml(String tag_name, String key) throws SQLException {
        return nav(tag_name, PaasFileType.tml, key);
    }

    @XMapping("pln/home")
    public ModelAndView nav_pln(String tag_name, String key) throws SQLException {
        return nav(tag_name, PaasFileType.pln, key);
    }

    private ModelAndView nav(String tag_name, PaasFileType type, String key) throws SQLException {
        List<TagCountsModel> tags = DbPaaSApi.getFileTags(type);

        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("paas/file");
    }

    @XMapping("api/list")
    public ModelAndView api_list(XContext ctx) throws SQLException {
        return list(ctx, PaasFileType.api);
    }

    @XMapping("pln/list")
    public ModelAndView pln_list(XContext ctx) throws SQLException {
        return list(ctx, PaasFileType.pln);
    }

    @XMapping("tml/list")
    public ModelAndView tml_list(XContext ctx) throws SQLException {
        return list(ctx, PaasFileType.tml);
    }

    private ModelAndView list(XContext ctx, PaasFileType type) throws SQLException {
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

    @XMapping("api/edit")
    public ModelAndView api_edit(String tag, Integer file_id, XContext ctx) throws SQLException {
        return edit(tag, PaasFileType.api, file_id, ctx);
    }

    @XMapping("pln/edit")
    public ModelAndView pln_edit(String tag, Integer file_id, XContext ctx) throws SQLException {
        return edit(tag, PaasFileType.pln, file_id, ctx);
    }

    @XMapping("tml/edit")
    public ModelAndView tml_edit(String tag, Integer file_id, XContext ctx) throws SQLException {
        return edit(tag, PaasFileType.tml, file_id, ctx);
    }

    private ModelAndView edit(String tag, PaasFileType type, Integer file_id, XContext ctx) throws SQLException {
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

        viewModel.put("id", file_id);
        viewModel.put("tag", tag);
        viewModel.put("m1", file);
        viewModel.put("ref_url", ctx.header("referer"));

        return view("paas/file_edit_" + type.name());
    }

    @XMapping("api/ajax/save")
    public Object api_ajax_save(XContext ctx) throws Exception {
        DataItem data = new DataItem();

        data.set("link_to", ctx.param("link_to"));
        data.set("content_type", ctx.param("content_type"));
        data.set("is_staticize", ctx.paramAsInt("is_staticize"));

        Object tmp = ajax_save(ctx, data, PaasFileType.api);

        String label = ctx.param("label","");
        String path = ctx.param("path");
        if (label.startsWith("@") && TextUtils.isEmpty(path) == false) {
            String receiver_url = Config.paas_uri() + path;
            String topic = label.substring(1);
            int is_disabled = ctx.paramAsInt("is_disabled");

            if (is_disabled == 1) {
                WaterClient.Message.unSubscribeTopic("waterpaas", topic);
            } else {
                WaterClient.Message.subscribeTopic("waterpaas", receiver_url,
                        Config.waterpaas_secretKey, "", 0, false, topic);
            }
        }

        return tmp;
    }

    @XMapping("pln/ajax/save")
    public Object pln_ajax_save(XContext ctx) throws Exception {
        DataItem data = new DataItem();

        data.set("plan_begin_time", ctx.param("plan_begin_time"));
        data.set("plan_interval", ctx.param("plan_interval"));
        data.set("plan_max", ctx.paramAsInt("plan_max"));

        return ajax_save(ctx, data, PaasFileType.pln);
    }

    @XMapping("tml/ajax/save")
    public Object tml_ajax_save(XContext ctx) throws SQLException {
        DataItem data = new DataItem();

        data.set("content_type", ctx.param("content_type"));
        data.set("rank", ctx.paramAsInt("rank"));

        return ajax_save(ctx, data, PaasFileType.tml);
    }

    public Object ajax_save(XContext ctx, DataItem data, PaasFileType type) throws SQLException {
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

    @XMapping("{type}/ajax/del")
    public Object ajax_del(XContext ctx, String type, Integer id) throws SQLException {
        if (id != null) {
            DbPaaSApi.delFile(id);
        }

        return viewModel.code(1,"ok");
    }

    @XMapping("pln/ajax/reset")
    public Object reset(XContext ctx, String ids) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        DbPaaSApi.resetFilePlan(ids);

        return viewModel.code(1, "ok");
    }

    @XMapping("{type}/code")
    public ModelAndView code(XContext ctx, Integer file_id) throws SQLException {
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

        if (file.content == null) {
            file.content = "";
        }

        viewModel.put("edit_mode", edit_mode);
        viewModel.put("code64", Base64Utils.encode(file.content));

        return view("paas/file_code");
    }

    @XMapping("{type}/code/ajax/save")
    public Object code_save(XContext ctx, Integer id, String fc64, String path) throws SQLException {
        if(id == null || id == 0){
            return viewModel.code(0,"");
        }

        String fc = Base64Utils.decode(fc64);

        DbPaaSApi.setFileContent(id, fc);

        return viewModel.code(1,"ok");
    }


    //批量导出
    @XMapping("{type}/ajax/export")
    public void exportDo(XContext ctx, String type, String tag, String ids) throws Exception {
        List<PaasFileModel> list = DbPaaSApi.getFilesByIds(PaasFileType.valueOf(type), ids);
        String json = ONode.stringify(list);
        String jsonX = JsonxUtils.encode(json);

        String filename2 = "water_paasfile_" + type +"_" + tag + "_" + Datetime.Now().getDate() + ".jsonx";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonX);
    }


    //批量导入
    @XMapping("{type}/ajax/import")
    public ViewModel importDo(XContext ctx, String type, String tag, XFile file) throws Exception {
        if (Session.current().isAdmin() == false) {
            return viewModel.code(0, "没有权限！");
        }

        String jsonX = IOUtils.toString(file.content);
        String json = JsonxUtils.decode(jsonX);

        List<PaasFileModel> list = ONode.deserialize(json, new TypeRef<List<PaasFileModel>>() {
        }.getClass());

        PaasFileType fileType = PaasFileType.valueOf(type);

        for (PaasFileModel m : list) {
            DbPaaSApi.impFile(fileType, tag, m);
        }

        return viewModel.code(1,"ok");
    }

    //批量删除
    @XMapping("{type}/ajax/batch")
    public ViewModel batchDo(XContext ctx, String type, String tag, Integer act, String ids) throws Exception {
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
