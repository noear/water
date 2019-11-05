package webapp.dso.db;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.noear.water.WaterClient;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.weed.IQuery;
import webapp.Config;
import webapp.models.water_paas.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * @Author:Yunlong.Feng
 * @Description:
 */
public class DbPaaSApi {
    private static DbContext db() {
        return Config.water;
    }

    //======================
    //公共函数
    public static List<PaasFunModel> getFunsByTag(String tag) throws SQLException {
        IQuery query = db().table("$.paas_fun")
                .where("tag=? AND is_enabled=1", tag)
                .select("*");

        return query.getList(new PaasFunModel());

    }

    public static PaasFunModel getFun(String tag, String fun_name) throws SQLException {
        IQuery query = db().table("$.paas_fun")
                .where("tag=? AND fun_name=?", tag, fun_name)
                .select("*");

        return query.getItem(new PaasFunModel());
    }


    //获取paas的tag分组信息。
    public static List<PaasApiModel> getApiTags() throws SQLException {
        return db().table("paas_api")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new PaasApiModel());
    }

    //根据tag_name和api_name、state获取paas列表。
    public static List<PaasApiModel> getApiList(String tag_name, String api_name, Integer state) throws SQLException {
        return db().table("paas_api")
                .where("is_enabled = ?", state)
                .and("tag = ?", tag_name)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(api_name) == false) {
                        tb.and("api_name like ?", api_name + "%");
                    }
                })
                .orderBy("api_name asc")
                .select("*")
                .getList(new PaasApiModel());
    }

    //根据id获取对应paas
    public static PaasApiModel getApiById(int api_id) throws SQLException {
        return db().table("paas_api")
                .where("api_id = ?", api_id)
                .limit(1)
                .select("*")
                .getItem(new PaasApiModel());

    }

    //编辑保存paas接口。
    public static boolean editApi(Integer api_id, String code, String tag, String api_name, String note, Integer cache_time,
                                  Integer is_enabled, String args, Integer is_get, Integer is_post) throws SQLException {

        boolean isOk = false;

        DbTableQuery tb = db().table("paas_api").usingExpr(false)
                .set("code", code)
                .set("tag", tag)
                .set("api_name", api_name.trim())
                .set("args", args)
                .set("note", note)
                .set("cache_time", cache_time)
                .set("is_enabled", is_enabled)
                .set("is_get", is_get)
                .set("is_post", is_post);
        if (api_id > 0) {
            isOk = tb.where("api_id = ?", api_id).update() > 0;
            if (isOk) {
                //添加热更新机制 //通过water通知订阅方
                WaterClient.Runner.updateCache("api:" + tag + "/" + api_name);
            }
        } else {
            isOk = tb.insert() > 0;
        }

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_api", "api_id", api_id);

        return isOk;
    }

    //获取函数的tag分组信息。
    public static List<PaasFunModel> getFunTags() throws SQLException {
        return db().table("paas_fun")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new PaasFunModel());
    }

    //根据tag_name和fun_name、state获取函数列表。
    public static List<PaasFunModel> getFunList(String tag_name, String fun_name, Integer state) throws SQLException {
        return db().table("paas_fun")
                .where("is_enabled = ?", state)
                .and("tag = ?", tag_name)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(fun_name) == false) {
                        tb.and("fun_name like ?", fun_name + "%");
                    }
                })
                .orderBy("fun_name asc")
                .select("*")
                .getList(new PaasFunModel());
    }

    //根据id获取对应函数fun
    public static PaasFunModel getFunById(int fun_id) throws SQLException {
        return db().table("paas_fun")
                .where("fun_id = ?", fun_id)
                .limit(1)
                .select("*")
                .getItem(new PaasFunModel());

    }

    //编辑保存paas函数。
    public static boolean editFun(Integer fun_id, String code, String tag, String fun_name, String name_display, String note, Integer is_enabled, String args) throws SQLException {
        boolean isOk = false;

        DbTableQuery tb = db().table("paas_fun").usingExpr(false)
                .set("code", code)
                .set("tag", tag)
                .set("fun_name", fun_name.trim())
                .set("name_display", name_display)
                .set("note", note)
                .set("is_enabled", is_enabled)
                .set("args", args);
        if (fun_id > 0) {
            isOk = tb.where("fun_id = ?", fun_id).update() > 0;

            if (isOk) {
                //添加热更新机制 //通过water通知订阅方
                WaterClient.Runner.updateCache("fun:" + tag + "/" + fun_name);
            }
        } else {
            isOk = tb.insert() > 0;
        }

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_fun", "fun_id", fun_id);

        return isOk;
    }

    ////////////////////////////////////////////////////
    //获取plan表的tag分组信息。
    public static List<PaasPlanModel> getPlanTags() throws SQLException {
        return db().table("paas_plan")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new PaasPlanModel());
    }

    //根据tag_name plan_name 和 is_enabled查询计划任务数据。
    public static List<PaasPlanModel> getPlanList(String tag_name, String plan_name, int is_enabled) throws SQLException {
        return db().table("paas_plan")
                .where("is_enabled = ?", is_enabled)
                .and("tag = ?", tag_name)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(plan_name) == false) {
                        tb.and("plan_name like ?", plan_name + "%");
                    }
                })
                .orderBy("plan_name asc")
                .select("*")
                .getList(new PaasPlanModel());
    }

    //根据plan_id查询code
    public static PaasPlanModel getPlanById(int plan_id) throws SQLException {

        return db().table("paas_plan")
                .where("plan_id = ?", plan_id)
                .limit(1)
                .select("*")
                .getItem(new PaasPlanModel());
    }

    //编辑或新增计划任务。
    public static boolean editPlan(Integer plan_id, String code, String tag, String plan_name, String source, String begin_time, String repeat_interval, Integer repeat_max, String last_exec_time, Integer is_enabled) throws SQLException, ParseException {

        if (repeat_max == null) {
            repeat_max = 0;
        }

        boolean isOk = false;

        DbTableQuery tb = db().table("paas_plan").usingExpr(false)
                .set("code", code)
                .set("tag", tag)
                .set("plan_name", plan_name.trim())
                .set("source", source)
                .set("begin_time", begin_time)
                .set("repeat_max", repeat_max)
                .set("repeat_interval", repeat_interval)
                .set("last_exec_time", last_exec_time)
                .set("is_enabled", is_enabled);

        if (plan_id > 0) {
            isOk = tb.where("plan_id = ?", plan_id).update() > 0;
            if (isOk) {
                //添加热更新机制 //通过water通知订阅方
                WaterClient.Runner.updateCache("plan:" + tag + "/" + plan_name);
            }
        } else {
            isOk = tb.insert() > 0;
        }

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_plan", "plan_id", plan_id);

        return isOk;
    }

    public static boolean resetPlanState(int plan_id) throws SQLException {
        return db().table("$.paas_plan")
                .set("state", 9)
                .set("last_exec_note", "RE")
                .where("plan_id=? AND state<>9", plan_id)
                .update() > 0;
    }

    ///////////////////ETL////////////////////////////////////////
    //获取etl任务标签
    public static List<PaasEtlModel> getETLTags() throws SQLException {
        return db().table("paas_etl")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new PaasEtlModel());
    }

    //根据tag和是否启用获取etl任务列表
    public static List<PaasEtlModel> getEtlList(String tag_name, String etl_name, int is_enabled) throws SQLException {
        return db().table("paas_etl")
                .where("is_enabled = ?", is_enabled)
                .and("tag = ?", tag_name)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(etl_name) == false) {
                        tb.and("etl_name like ?", etl_name + "%");
                    }
                })
                .orderBy("etl_name asc")
                .select("*")
                .getList(new PaasEtlModel());
    }

    //编辑/新增 同步任务
    public static boolean editEtl(int etl_id, String code, String tag, String etl_name,
                                  Integer is_enabled, Integer e_enabled, Integer t_enabled, Integer l_enabled, Integer is_update,
                                  int cursor_type, long cursor,
                                  String alarm_mobile, Integer e_max_instance,
                                  Integer t_max_instance, Integer l_max_instance) throws SQLException {

        boolean isOk = false;

        DbTableQuery tb = db().table("paas_etl").usingExpr(true)
                .set("code", code)
                .set("tag", tag)
                .set("etl_name", etl_name.trim())
                .set("is_enabled", is_enabled)
                .set("e_enabled", e_enabled)
                .set("t_enabled", t_enabled)
                .set("cursor_type", cursor_type)
                .set("e_max_instance", e_max_instance)
                .set("t_max_instance", t_max_instance)
                .set("l_max_instance", l_max_instance)
                .set("l_enabled", l_enabled).expre(tb2 -> {
                    if (is_update != null) {
                        if (is_update == 1) {
                            tb2.set("cursor", cursor);
                        } else {
                            tb2.set("cursor", "0");
                        }
                    }
                    if (!TextUtils.isEmpty(alarm_mobile)) {
                        tb2.set("alarm_mobile", alarm_mobile);
                    }
                });

        if (etl_id > 0) {
            isOk = tb.where("etl_id = ?", etl_id).update() > 0;
            if (isOk) {
                //添加热更新机制 //通过water通知订阅方
                WaterClient.Runner.updateCache("etl:" + tag + "/" + etl_name);
            }
        } else {
            isOk = tb.insert() > 0;
        }

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_etl", "etl_id", etl_id);

        return isOk;
    }

    //根据id获取etl
    public static PaasEtlModel getEtlById(long etl_id) throws SQLException {
        return db().table("paas_etl")
                .where("etl_id = ?", etl_id)
                .limit(1)
                .select("*")
                .getItem(new PaasEtlModel());
    }

    //
    public static List<PaasFunModel> getFunsByTagOrName(String tags) throws SQLException {
        String[] details = tags.split(";");
        return db().table("paas_fun").expre(tb -> {
            tb.where("1 != 1");
            for (String item : details) {
                tb.or();
                String[] split = item.split("/");
                String tag = split[0];
                String funcName = split[1];
                tb.begin("tag=?", tag);
                if (!"*".equals(funcName)) {
                    tb.and("fun_name", funcName);
                }
                tb.end();
            }
        }).select("fun_id,tag,fun_name,name_display").getList(new PaasFunModel());
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public static List<String> tmlGetTags() throws SQLException {
        return db().table("paas_tml")
                .select("DISTINCT `tag` AS `tags`")
                .getArray("tags");
    }

    public static List<PaasTmlModel> tmlGetList(String tag, String tml_name, int is_enabled) throws SQLException {
        return db().table("paas_tml")
                .where("tag = ?", tag)
                .and("is_enabled = ?", is_enabled)
                .expre((tb) -> {
                    if (StringUtils.isNotEmpty(tml_name)) {
                        tb.and("tml_name LIKE ?", "%" + tml_name + "%");
                    }
                })
                .select("*")
                .getList(new PaasTmlModel());
    }

    public static List<PaasTmlModel> tmlGetTagNames() throws SQLException {
        return db().table("paas_tml")
                .where("is_enabled = ?", true)
                .select("tag,tml_name")
                .getList(new PaasTmlModel());
    }

    public static PaasTmlModel tmlGet(int tml_id) throws SQLException {
        return db().table("paas_tml")
                .where("tml_id = ?", tml_id)
                .limit(1)
                .select("*")
                .getItem(new PaasTmlModel());
    }

    public static void tmlAdd(String tag,
                              String tml_name,
                              String name_display,
                              String code,
                              int is_enabled) throws SQLException {
        long tml_id = db().table("paas_tml")
                .set("tag", tag)
                .set("tml_name", tml_name)
                .set("name_display", name_display)
                .set("code", code)
                .set("is_enabled", is_enabled)
                .insert();

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_tml", "tml_id", tml_id);
    }

    public static void tmlMol(int tml_id,
                              String tag,
                              String tml_name,
                              String name_display,
                              String code,
                              int is_enabled) throws SQLException {
        db().table("paas_tml")
                .set("tag", tag)
                .set("tml_name", tml_name)
                .set("name_display", name_display)
                .set("code", code)
                .set("is_enabled", is_enabled)
                .where("tml_id = ?", tml_id)
                .update();

        /** 记录历史版本 */
        DbWaterApi.logVersion("paas_tml", "tml_id", tml_id);

        WaterClient.Runner.updateCache("tml:" + tag + "/" + tml_name);

    }

    //////////////////
    public static List<String> pullGetTags() throws SQLException {
        return db().table("paas_pull")
                .select("DISTINCT `tag` AS `tags`")
                .getArray("tags");
    }

    public static List<PaasPullModel> pullGetList(String tag, String pull_name, int is_enabled) throws Exception {
        return db().table("paas_pull")
                .where("tag = ?", tag)
                .and("is_enabled = ?", is_enabled)
                .expre((tb) -> {
                    if (StringUtils.isNotEmpty(pull_name)) {
                        tb.and("pull_name LIKE ?", "%" + pull_name + "%");
                    }
                })
                .select("*")
                .getList(PaasPullModel.class);
    }

    public static List<PaasPullModel> pullGetTagNames() throws Exception {
        return db().table("paas_pull")
                .where("is_enabled = ?", true)
                .select("tag,pull_name")
                .getList(PaasPullModel.class);
    }

    public static PaasPullModel pullGet(int pull_id) throws Exception {
        return db().table("paas_pull")
                .where("pull_id = ?", pull_id)
                .limit(1)
                .select("*")
                .getItem(PaasPullModel.class);
    }

    public static void pullIssue(int pull_id) throws Exception {
        db().table("paas_pull")
                .set("state", 1)
                .where("pull_id = ?", pull_id)
                .update();
    }

    public static void pullEdit(int pull_id, String tag, String pull_name, String source, String target_dir,String target_url, int is_enabled) throws SQLException {

        DbTableQuery qur = db().table("paas_pull")
                .set("tag", tag)
                .set("pull_name", pull_name)
                .set("source", source)
                .set("target_dir", target_dir)
                .set("target_url", target_url)
                .set("is_enabled", is_enabled);

        if (pull_id > 0) {
            qur.where("pull_id=?", pull_id).update();
        } else {
            qur.insert();
        }

        WaterClient.Runner.updateCache("pull:" + tag + "/" + pull_name);
    }
}