package org.noear.rubber;

import org.noear.rubber.models.*;
import org.noear.snack.ONode;
import org.noear.water.utils.Datetime;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.weed.IQuery;

import java.sql.SQLException;
import java.util.List;

public final class DbApi {
    public static DbContext db() {
        return RcConfig.water();
    }


    public static ModelModel getModel(String tag, String name) throws SQLException {
        IQuery query = db().table("rubber_model")
                .where("tag=? AND name=?", tag, name)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("model:" + tag + "/" + name);
        }

        return query.getItem(new ModelModel());

    }

    protected static ModelModel getModelByTagName(String model_tagName) throws Exception{
        String[] ss = model_tagName.split("/");
        ModelModel model = DbApi.getModel(ss[0], ss[1]);

        if (model == null || model.model_id < 1) {
            throw new RubberException("doesn't exist M:" + model_tagName);
        }

        return model;
    }

    public static boolean hasModelField(String model_tag, String model_name,String field_name) throws SQLException {
        ModelModel m = getModel(model_tag, model_name);

        return db().table("rubber_model_field")
                .where("model_id=? AND name=?", m.model_id, field_name).build(tb->{
                    if (RcConfig.is_debug == false) {
                        tb.caching(RcConfig.inner_cache)
                                .usingCache(60 * 5);
                    }
                })
                .exists();
    }

    public static List<ModelFieldModel> getModelFields(int model_id, String model_tag, String model_name) throws SQLException {
        IQuery query = db().table("rubber_model_field")
                .where("model_id=?", model_id)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("model:" + model_tag + "/" + model_name);
        }

        return query.getList(new ModelFieldModel());
    }

    public static SchemeModel getScheme(String tag, String name) throws SQLException {
        IQuery query = db().table("rubber_scheme")
                .where("tag=? AND name=?", tag, name)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("scheme:" + tag + "/" + name);
        }
        return query.getItem(new SchemeModel());
    }

    //尝试获取计算方案信息
    protected static SchemeModel getSchemeByTagName(String scheme_tagName) throws Exception{
        String[] ss = scheme_tagName.split("/");
        SchemeModel scheme = DbApi.getScheme(ss[0], ss[1]);

        if (scheme == null || scheme.scheme_id < 1) {
            throw new RubberException("doesn't exist S:" + scheme_tagName);
        }

        return scheme;
    }

    public static List<SchemeModel> getSchemeByTag(String tag) throws SQLException {
        IQuery query = db().table("rubber_scheme")
                .where("tag=?", tag)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("scheme:" + tag);
        }

        return query.getList(new SchemeModel());
    }

    public static List<SchemeNodeModel> getSchemeNodes(int scheme_id, String scheme_tag, String scheme_name) throws SQLException {
        IQuery query = db().table("rubber_scheme_node")
                .where("scheme_id=?", scheme_id)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("scheme:" + scheme_tag + "/" + scheme_name);
        }

        return query.getList(new SchemeNodeModel());
    }

    public static List<SchemeRuleModel> getSchemeRules(int scheme_id, String scheme_tag, String scheme_name) throws SQLException {
        IQuery query = db().table("rubber_scheme_rule")
                .where("scheme_id=? AND is_enabled=1", scheme_id)
                .orderBy("`sort` ASC,rule_id ASC")
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("scheme:" + scheme_tag + "/" + scheme_name);
        }

        return query.getList(new SchemeRuleModel());
    }
    /* //先留着别删
    public static SchemeRuleModel getSchemeRule(int rule_id) throws SQLException {
        IQuery query = db().table("rubber_scheme_rule")
                .where("rule_id=?", rule_id)
                .select("*");

        if (RbConfig.is_debug == false) {
            query.caching(RbConfig.cache).usingCache(60 * 5);
        }

        return query.getItem(new SchemeRuleModel());
    }
    */

    public static long logRequestAdd(String request_id,String scheme_tagname, String args_json,int policy,String callback) throws SQLException {
        //long log_id = RcConfig.newLogID();

        Datetime now = Datetime.Now();

        return RcConfig.water_log().table("rubber_log_request")
                .set("request_id", request_id)
                .set("scheme_tagname", scheme_tagname)
                .set("args_json", args_json)
                .set("start_fulltime", "$NOW()")
                .set("start_date", now.getDate())
                .set("log_date",now.getDate())
                .set("policy", policy)
                .set("callback", callback)
                //.set("log_id", log_id)
                .insert();

        //return log_id;
    }

    public static void logRequestSetState(long log_id,int state) throws SQLException{
        RcConfig.water_log().table("rubber_log_request")
                .set("state",state)
                .where("log_id=?",log_id)
                .update();

    }

    public static void logRequestSet(long log_id,RubberResponse response, String scheme_tagname, String args_json, String model_json, String session_json) throws Exception {

        //匹配报告
        ONode matcher_json = response.matcher_json();

        //评估报告
        ONode evaluation_json = response.evaluation_json();

        ONode note_json = new ONode();
        note_json.get("M")
                .set("value",response.matcher.value)
                .set("total",response.matcher.total);

        note_json.get("E")
                .set("score",response.evaluation.score)
                .set("advice",response.evaluation.advice)
                .set("exception",response.evaluation.exception);

        DbTableQuery tb = RcConfig.water_log().table("rubber_log_request")
                .set("request_id", response.request.request_id)
                .set("scheme_tagname", scheme_tagname)
                .set("args_json", args_json)
                .set("model_json", model_json)
                .set("matcher_json", matcher_json.toJson())
                .set("evaluation_json", evaluation_json.toJson())
                .set("session_json", session_json)
                .set("note_json", note_json.toJson())
                .set("end_fulltime", response.end_time)
                .set("timespan", response.timespan())
                .set("policy", response.request.policy)
                .set("state",2);

        if(log_id>0){
            tb.where("log_id=?",log_id).update();
        }else{
            Datetime time = new Datetime(response.start_time);

            tb.set("log_date",new Datetime(response.start_time).getDate());
            tb.set("start_fulltime", response.start_time);
            tb.set("start_date", time.getDate());
            tb.set("log_date", time.getDate());
            tb.insert();
        }
    }

    public static LogRequestModel logRequestGet(String request_id) throws SQLException{
        return
        RcConfig.water_log().table("rubber_log_request")
                .where("request_id=?",request_id)
                .orderBy("log_id DESC")
                .limit(1)
                .select("*")
                .getItem(new LogRequestModel());
    }

    //======================
    //D-Block
    public static List<BlockModel> getBlockByTag(String tag) throws SQLException {
        IQuery query = db().table("$.rubber_block")
                .where("tag=? AND is_enabled=1", tag)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("block:" + tag + "/*");
        }

        return query.getList(new BlockModel());

    }

    public static BlockModel getBlock(String tag, String name) throws SQLException {
        IQuery query = db().table("$.rubber_block")
                .where("tag=? AND name=?", tag, name)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("block:" + tag + "/" + name);
        }

        return query.getItem(new BlockModel());
    }

    public static BlockModel getBlockByTagName(String block_tagName) throws Exception{
        String[] ss = block_tagName.split("/");
        BlockModel model = DbApi.getBlock(ss[0], ss[1]);

        if (model == null || model.block_id < 1) {
            throw new RubberException("doesn't exist D:" + block_tagName);
        }

        return model;
    }


    //======================
    //公共函数
    public static List<PaasFunModel> getFunsByTag(String tag) throws SQLException {
        IQuery query = db().table("$.paas_fun")
                .where("tag=? AND is_enabled=1", tag)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("fun:" + tag + "/*");
        }

        return query.getList(new PaasFunModel());

    }

    public static PaasFunModel getFun(String tag, String name) throws SQLException {
        IQuery query = db().table("$.paas_fun")
                .where("tag=? AND fun_name=?", tag, name)
                .select("*");

        if (RcConfig.is_debug == false) {
            query.caching(RcConfig.inner_cache)
                    .usingCache(60 * 5)
                    .cacheTag("fun:" + tag + "/" + name);
        }

        return query.getItem(new PaasFunModel());
    }

}
