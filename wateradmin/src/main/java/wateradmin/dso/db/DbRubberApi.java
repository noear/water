package wateradmin.dso.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.water.utils.TextUtils;
import org.noear.solon.Utils;
import wateradmin.Config;
import wateradmin.models.water_rebber.*;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_rebber.ModelFieldModel;
import wateradmin.models.water_rebber.ModelModel;
import wateradmin.models.water_rebber.SchemeModel;
import wateradmin.models.water_rebber.SchemeRuleModel;
import wateradmin.models.water_rebber.SchemeNodeDesignModel;
import wateradmin.setup.Setup;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DbRubberApi {
    private static DbContext db() {
        return Setup.water;
    }

    //获取模型tag
    public static List<TagCountsModel> getModelTags() throws SQLException {
        return db().table("rubber_model")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //获取模型tag
    public static List<TagCountsModel> getActorTags() throws SQLException {
        return db().table("rubber_actor")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    public static boolean containActorTag(List<TagCountsModel> list, String tag) {
        boolean flag = false;
        for (TagCountsModel m : list) {
            if (m.tag.equals(tag)) {
                flag = true;
            }
        }
        return flag;
    }

    //获取模型tag
    public static List<ModelModel> getRequestTags() throws SQLException {
        return Config.water_log.table("rubber_log_request")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(new ModelModel());
    }

    //根据request_id获取model
    public static List<LogRequestModel> getReuestList(int page, int pageSize, String key, CountModel count) throws SQLException {
        int start = (page - 1) * pageSize;
        DbTableQuery query = Config.water_log.table("rubber_log_request")
                .where("1=1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(key)) {
                        if (key.length() == 32) {
                            tb.and("(request_id = ? OR args_json LIKE ?)", key, "%" + key + "%");
                        } else {
                            tb.and("(args_json LIKE ?)", "%" + key + "%");
                        }
                    }
                });
        count.setCount(query.count());
        return query.orderBy("log_id DESC")
                .limit(start, pageSize)
                .select("*")
                .getList(new LogRequestModel());
    }

    //根据tag和名称获取model
    public static List<ModelModel> getModelList(String tag, String name) throws SQLException {
        return db().table("rubber_model")
                .where("tag = ?", tag)
                .build(tb -> {
                    if (!TextUtils.isEmpty(name)) {
                        tb.and("(name like ? or name_display like ?)", "%" + name + "%", "%" + name + "%");
                    }
                })
                .select("*")
                .getList(new ModelModel());
    }


    //新增或修改数据模型
    public static long setModel(int model_id, String tag, String name, String name_display, String init_expr, String debug_args, String related_db) throws SQLException {
        DbTableQuery tb = db().table("rubber_model").usingExpr(true)
                .set("tag", tag)
                .set("name", name.trim())
                .set("name_display", name_display)
                .set("init_expr", init_expr)
                .set("debug_args", debug_args)
                .set("related_db", related_db)
                .set("last_updatetime", new Date());

        if (model_id > 0) {
            long isOk = tb.where("model_id = ?", model_id)
                    .update();

            if (isOk > 0) {
                //添加热更新机制 //通过water通知订阅方
                WaterClient.Notice.updateCache("model:" + tag + "/" + name);
            }

            return isOk;
        } else {
            return tb.insert();
        }
    }

    public static void modelImp(String tag, ModelSerializeModel vm) throws SQLException{
        if (TextUtils.isEmpty(tag) == false) {
            vm.model.tag = tag;
        }

        if (TextUtils.isEmpty(vm.model.tag) || TextUtils.isEmpty(vm.model.name)) {
            return;
        }

        int model_id = (int)db().table("rubber_model")
                .set("tag", vm.model.tag)
                .set("name", vm.model.name)
                .set("name_display", vm.model.name_display)
                .set("related_db", vm.model.related_db)
                .set("field_count", vm.model.field_count)
                .set("init_expr", vm.model.init_expr)
                .set("debug_args", vm.model.debug_args)
                .set("last_updatetime", vm.model.last_updatetime)
                .insertBy("tag,name");

        if(model_id > 0) {
            db().table("rubber_model_field").insertList(vm.fields,(m,d)->{
                m.model_id = model_id;
                d.setEntity(m);
                d.remove("field_id");
                d.remove("tag");
            });
        }
    }

    //删除模型及模型字段
    public static boolean delModel(int model_id) throws SQLException {
        boolean result = db().table("rubber_model")
                .where("model_id = ?", model_id)
                .delete() > 0;
        if (result) {
            db().table("rubber_model_field")
                    .where("model_id = ?", model_id)
                    .delete();
            return true;
        } else {
            return false;
        }

    }

    //根据模型id获取模型
    public static ModelModel getModelById(int model_id) throws SQLException {
        if (model_id == 0) {
            return new ModelModel();
        }

        return db().table("rubber_model")
                .where("model_id = ?", model_id)
                .select("*")
                .getItem(new ModelModel());
    }

    //获取数据模型的字段列表
    public static List<ModelFieldModel> getFieldList(int model_id, String name) throws SQLException {
        return db().table("rubber_model_field")
                .where("model_id = ?", model_id)
                .build(tb -> {
                    if (!TextUtils.isEmpty(name)) {
                        tb.and("(name like ? or name_display like ?)", "%" + name + "%", "%" + name + "%");
                    }
                })
                .select("*")
                .getList(new ModelFieldModel());
    }

    //获取模型集合
    public static List<ModelModel> getModelList() throws SQLException {
        return db().table("rubber_model")
                .orderBy("tag ASC,name ASC")
                .select("model_id,tag,name,name_display")
                .getList(new ModelModel());
    }

    public static List<ModelModel> getModelByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("rubber_model")
                .whereIn("model_id", list)
                .select("*")
                .getList(new ModelModel());
    }


    //根据tag和名称获取参与人员
    public static List<ActorModel> getActorList(String tag, String name) throws SQLException {
        return db().table("rubber_actor")
                .where("tag = ?", tag)
                .build(tb -> {
                    if (!TextUtils.isEmpty(name)) {
                        tb.and("(name like ? or name_display like ?)", "%" + name + "%", "%" + name + "%");
                    }
                })
                .select("*")
                .getList(new ActorModel());
    }

    //新增或修改参与人员
    public static boolean setActor(int model_id, String tag, String name, String name_display, String note) throws SQLException {
        DbTableQuery tb = db().table("rubber_actor").usingExpr(true)
                .set("tag", tag)
                .set("name", name)
                .set("name_display", name_display)
                .set("note", note)
                .set("last_updatetime", new Date());

        if (model_id > 0) {
            return tb.where("actor_id = ?", model_id)
                    .update() > 0;
        } else {
            return tb.insert() > 0;
        }
    }

    //删除参与人员
    public static boolean delActor(int actor_id) throws SQLException {
        return db().table("rubber_actor")
                .where("actor_id = ?", actor_id)
                .delete() > 0;
    }


    //获取计算方案标签列表
    public static List<TagCountsModel> getSchemeTags() throws SQLException {
        return db().table("rubber_scheme")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //获取计算方案列表，通过标签和名称
    public static List<SchemeModel> getSchemeList(String tag, String name) throws SQLException {
        return db().table("rubber_scheme")
                .where("tag = ?", tag)
                .build(tb -> {
                    if (!TextUtils.isEmpty(name)) {
                        tb.and("name like ?", "%" + name + "%").or("name_display like ?", "%" + name + "%");
                    }
                })
                .select("*")
                .getList(new SchemeModel());
    }

    public static List<SchemeModel> getSchemeByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("rubber_scheme")
                .whereIn("scheme_id", list)
                .select("*")
                .getList(new SchemeModel());
    }

    //修改或者添加计算方案
    public static long setScheme(Integer id, String tag, String name, String name_display, String related_model, String model_display, String related_block, String debug_args) throws SQLException {
        DbTableQuery tb = db().table("rubber_scheme").usingExpr(true)
                .set("tag", tag)
                .set("name", name.trim())
                .set("name_display", name_display)
                .set("related_model", related_model)
                .set("related_model_display", model_display)
                .set("related_block", related_block)
                .set("debug_args", debug_args)
                .set("is_enabled", 1)
                .set("last_updatetime", new Date());

        if (id != null && id != 0) {
            return tb.where("scheme_id = ?", id)
                    .update();
        } else {

            return tb.insert();
        }

    }

    public static void schemeImp(String tag, SchemeSerializeModel vm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            vm.model.tag = tag;
        }

        if (TextUtils.isEmpty(vm.model.tag) || TextUtils.isEmpty(vm.model.name)) {
            return;
        }

        int scheme_id = (int) db().table("rubber_scheme").usingExpr(true)
                .set("tag", tag)
                .set("name", vm.model.name)
                .set("name_display", vm.model.name_display)
                .set("related_model", vm.model.related_model)
                .set("related_model_display", vm.model.related_model_display)
                .set("related_block", vm.model.related_block)
                .set("debug_args", vm.model.debug_args)
                .set("event", vm.model.event)
                .set("node_count", vm.model.node_count)
                .set("rule_count", vm.model.rule_count)
                .set("rule_relation", vm.model.rule_relation)
                .set("is_enabled", vm.model.is_enabled)
                .set("last_updatetime", vm.model.last_updatetime)
                .insertBy("tag,name");

        if (scheme_id > 0) {
            db().table("rubber_scheme_node_design")
                    .set("scheme_id",scheme_id)
                    .set("details",vm.node_design.details)
                    .insert();

            db().table("rubber_scheme_node").insertList(vm.nodes,(d, m)->{
                d.scheme_id = scheme_id;
                m.setEntity(d);
                m.remove("node_id");
            });

            db().table("rubber_scheme_rule").insertList(vm.rules,(d, m)->{
                d.scheme_id = scheme_id;
                m.setEntity(d);
                m.remove("rule_id");
            });
        }
    }

    //更新计算方案事件
    public static boolean updateSchemeEvent(int scheme_id, String event) throws SQLException {
        boolean isOk = db().table("rubber_scheme")
                .where("scheme_id = ?", scheme_id)
                .set("event", event)
                .update() > 0;


        if (isOk) { //更新后通知一下变更
            SchemeModel m = getSchemeById(scheme_id);
            WaterClient.Notice.updateCache("scheme:" + m.tag + "/" + m.name);
        }

        return isOk;
    }

    //另存为计算方案
    public static boolean saveAsScheme(String tag,int scheme_id, String name, String name_display, String debug_args) throws SQLException {
        SchemeModel scheme = getSchemeById(scheme_id);
        SchemeNodeDesignModel design = getSchemeNodeDesign(scheme_id);
        List<DataItem> schemeNodes = getSchemeNodeDataBySchemeId(scheme_id).getRows();

        List<DataItem> schemeRules = getSchemeRulesDataBySchemeId(scheme_id).getRows();

        ModelModel model = getModelById(scheme.model_id);

        int newId = new Long(setScheme(0, tag, name, name_display, scheme.related_model, model.name_display, scheme.related_block, debug_args)).intValue();

        if (!TextUtils.isEmpty(scheme.event)) {
            updateSchemeEvent(newId, scheme.event);
        }

        setSchemeFlowDesign(newId, design.details);

        for (DataItem m : schemeNodes) {
            m.remove("node_id");
            m.set("scheme_id", newId);
        }

        db().table("rubber_scheme_node").insertList(schemeNodes);
        db().table("rubber_scheme")
                .where("scheme_id = ?", newId)
                .set("node_count", schemeNodes.size())
                .update();

        for (DataItem m : schemeRules) {
            m.remove("rule_id");
            m.set("scheme_id", newId);
        }

        db().table("rubber_scheme_rule").insertList(schemeRules);

        updSchemeRuleCount(newId);

        return true;
    }

    //删除计算方案/计算方案规则/计算方案流程设计
    public static boolean delScheme(int scheme_id) throws SQLException {

        boolean result = db().table("rubber_scheme")
                .where("scheme_id = ?", scheme_id)
                .delete() > 0;

        if (result) {

            db().table("rubber_scheme_node")
                    .where("scheme_id = ?", scheme_id)
                    .delete();

            db().table("rubber_scheme_rule")
                    .where("scheme_id = ?", scheme_id)
                    .delete();

            db().table("rubber_scheme_node_design")
                    .where("scheme_id = ?", scheme_id)
                    .delete();

            return true;
        } else {
            return false;
        }
    }

    //根据scheme_id 获取 计算方案节点结合(DataItem)
    public static DataList getSchemeNodeDataBySchemeId(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node")
                .where("scheme_id = ?", scheme_id)
                .select("*")
                .getDataList();
    }

    //根据scheme_id 获取 计算方案规则结合(DataItem)
    public static DataList getSchemeRulesDataBySchemeId(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_rule")
                .where("scheme_id = ?", scheme_id)
                .select("*")
                .getDataList();
    }

    //根据方案id获取单条方案对象
    public static SchemeModel getSchemeById(int scheme_id) throws SQLException {
        return db().table("rubber_scheme")
                .where("scheme_id = ?", scheme_id)
                .limit(1)
                .select("*")
                .getItem(new SchemeModel());
    }

    //根据field_id获取数据模型字段
    public static ModelFieldModel getFieldById(int field_id) throws SQLException {
        if (field_id == 0) {
            return new ModelFieldModel();
        }

        return db().table("rubber_model_field")
                .where("field_id = ?", field_id)
                .select("*")
                .getItem(new ModelFieldModel());
    }

    //新增或更新数据模型字段
    public static boolean setModelField(int model_id, int field_id, String name, String name_display,
                                        String expr, String note,int is_pk) throws SQLException {
        DbTableQuery tb = db().table("rubber_model_field").usingExpr(true)
                .set("model_id", model_id)
                .set("name", name.trim())
                .set("name_display", name_display)
                .set("expr", expr)
                .set("note", note)
                .set("is_pk", is_pk)
                .set("last_updatetime", new Date());

        boolean isOk = false;
        if (field_id > 0) {
            isOk = tb.where("field_id = ?", field_id)
                    .update() > 0;
        } else {
            tb.insert();
            long field_count = getModelFieldCount(model_id);
            isOk = db().table("rubber_model")
                    .where("model_id = ?", model_id)
                    .set("field_count", field_count)
                    .update() > 0;
        }


        //添加热更新机制 //通过water通知订阅方
        ModelModel m = getModelById(model_id);
        WaterClient.Notice.updateCache("model:" + m.tag + "/" + m.name);


        return isOk;
    }

    //模型另存为
    public static boolean saveAsModel(String tag,int model_id, String name, String name_display, String debug_args, String init_expr, String related_db) throws SQLException {

        List<DataItem> fields = getModelFieldDataItemList(model_id).getRows();
        int newId = new Long(setModel(0, tag, name, name_display, init_expr, debug_args, related_db)).intValue();

        for (DataItem m : fields) {
            m.remove("field_id");
            m.set("model_id", newId);
        }
        db().table("rubber_model_field").insertList(fields);
        db().table("rubber_model")
                .where("model_id = ?", newId)
                .set("field_count", fields.size())
                .update();

        return true;
    }

    //根据模型id获取模型字段的数量
    public static long getModelFieldCount(int model_id) throws SQLException {
        return db().table("rubber_model_field")
                .where("model_id = ?", model_id)
                .select("count(*)")
                .getCount();
    }

    //根据模型id获取模型字段结合
    public static List<ModelFieldModel> getModelFieldListByModelId(int model_id) throws SQLException {
        return db().table("rubber_model_field")
                .where("model_id = ?", model_id)
                .select("*")
                .getList(new ModelFieldModel());
    }


    public static DataList getModelFieldDataItemList(int model_id) throws SQLException {
        return db().table("rubber_model_field")
                .where("model_id = ?", model_id)
                .select("*")
                .getDataList();
    }

    //删除模型字段
    public static boolean delModelField(int field_id, int model_id) throws SQLException {
        db().table("rubber_model_field")
                .where("field_id = ?", field_id)
                .delete();

        long field_count = getModelFieldCount(model_id);

        return db().table("rubber_model")
                .where("model_id = ?", model_id)
                .set("field_count", field_count)
                .update() > 0;

    }

    //根据方案id和name获取规则列表明细
    public static List<SchemeRuleModel> getSchemeRuleListBySchemeId(Integer scheme_id, String name) throws SQLException {
        return db().table("rubber_scheme_rule").
                where("scheme_id=?", scheme_id)
                .build(tb -> {
                    if (!Utils.isEmpty(name)) {
                        tb.and("name_display like ?", "%" + name + "%");
                    }
                })
                .orderBy("`sort` ASC,rule_id ASC")
                .select("*")
                .getList(new SchemeRuleModel());
    }

    //根据计算方案id获取规则列表
    public static List<SchemeRuleModel> getSchemeRulesSchemeId(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_rule")
                .where("scheme_id = ?", scheme_id)
                .select("*")
                .getList(new SchemeRuleModel());
    }

    //获取单条规则明细
    public static SchemeRuleModel getSchemeRuleByRuleId(int rule_id) throws SQLException {
        if(rule_id<1){
            return new SchemeRuleModel();
        }

        return db().table("rubber_scheme_rule").where("rule_id=?", rule_id)
                .select("*")
                .getItem(new SchemeRuleModel());
    }

    //通过规则编号列表获取规则列表
    public static List<SchemeRuleModel> getSchemeRuleByRuleIds(List<Integer> rule_ids) throws SQLException {
        return db().table("rubber_scheme_rule").where("rule_id in (?...)", rule_ids)
                .select("*")
                .getList(new SchemeRuleModel());
    }

    //更新计算方案启动的规则数量
    public static boolean updSchemeRuleCount(int scheme_id) throws SQLException {

        //获取新的记录
        long count = db().table("rubber_scheme_rule")
                .where("scheme_id = ?", scheme_id)
                .and("is_enabled = ?", 1)
                .select("count(*) counts")
                .getValue(0L);


        //更新记录数
        boolean isOk = db().table("rubber_scheme")
                .set("rule_count", count)
                .where("scheme_id = ?", scheme_id)
                .update() > 0;


        if (isOk) { //更新后通知一下变更
            SchemeModel m = getSchemeById(scheme_id);
            WaterClient.Notice.updateCache("scheme:" + m.tag + "/" + m.name);
        }

        return isOk;
    }

    //更新计算方案的规则关系
    public static boolean updataSchemeRuleRelation(int scheme_id, int rule_relation) throws SQLException {
        return db().table("rubber_scheme")
                .where("scheme_id = ?", scheme_id)
                .set("rule_relation", rule_relation)
                .update() > 0;
    }

    //删除计算方案-规则
    public static boolean delSchemeRule(int rule_id, int scheme_id) throws SQLException {
        db().table("rubber_scheme_rule")
                .where("rule_id = ?", rule_id)
                .delete();

        return updSchemeRuleCount(scheme_id);
    }


    //添加或修改计算方案中的规则列表
    public static boolean setSchemeRule(int rule_id, int scheme_id, String name_display, int advice, int score, int sort, String expr, String expr_display, int is_enabled) throws SQLException {
        DbTableQuery tb = db().table("rubber_scheme_rule").usingExpr(true)
                .set("scheme_id", scheme_id)
                .set("name_display", name_display)
                .set("advice", advice)
                .set("score", score)
                .set("sort", sort)
                .set("expr", expr)
                .set("is_enabled", is_enabled)
                .set("expr_display", expr_display)
                .set("last_updatetime", new Date());

        boolean isOk = false;
        if (rule_id > 0) {
            isOk = tb.where("rule_id = ?", rule_id)
                    .update() > 0;
        } else {
            isOk = tb.insert() > 0;
        }

        return isOk;
    }

    //根据规则编号批量更新规则状态
//    public static boolean setRuleEnable(List<Integer> rule_ids, int type) throws SQLException {
//        return db().table("rubber_scheme_rule")
//                .set("is_enabled", type).
//                        where("rule_id in (?...)", rule_ids)
//                .update() > 0;
//    }

    //根据scheme_id获取设计流程展示详情
    public static SchemeNodeDesignModel getSchemeNodeDesign(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node_design")
                .where("scheme_id = ?", scheme_id)
                .select("*")
                .getItem(new SchemeNodeDesignModel());
    }

    //根据node_id获取节点
    public static SchemeNodeModel getSchemeNodeByNodeKey(String node_key, int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node")
                .where("node_key = ?", node_key)
                .and("scheme_id = ?", scheme_id)
                .select("*")
                .getItem(new SchemeNodeModel());
    }

    //获取除指定参与人员外的所有参与人员集合
    public static List<ActorModel> getActtorListNotInActor(String actor) throws SQLException {
        return db().table("rubber_actor")
                .where("1 = 1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(actor)) {
                        tb.and("actor <> ?", actor);
                    }
                })
                .select("*")
                .getList(new ActorModel());
    }

    /*public static JSONObject schemeNodeDeal(String node_key,int node_type) throws SQLException{
        JSONObject resp = new JSONObject();

        SchemeNodeModel node = getSchemeNodeByNodeKey(node_key);
        resp.put("node",JSONObject.toJSON(node));
        if (node_type == 2) {
            //执行节点
            String actor = node.actor;
            List<ActorModel> actors = getActtorListNotInActor(actor);
            List<ActorModel> listActor = new ArrayList<>();
            ActorModel m = new ActorModel();
            m.name = actor;
            listActor.add(m);
            listActor.addAll(actors);
            resp.put("actors",JSONObject.toJSON(listActor));
        }
        resp.put("code",1);
        return resp;
    }*/

    public static ActorModel getActorModel(Integer actor_id) throws SQLException {
        return db().table("rubber_actor").where("actor_id=?", actor_id).select("*")
                .getItem(new ActorModel());
    }

    //保存计算方案流程详情
    public static boolean setSchemeFlowDesign(int scheme_id, String details) throws SQLException {
        SchemeNodeDesignModel design = getSchemeNodeDesign(scheme_id);
        if (design.scheme_id > 0) {
            return db().table("rubber_scheme_node_design")
                    .where("scheme_id = ?", scheme_id)
                    .set("details", details)
                    .update() > 0;
        } else {
            return db().table("rubber_scheme_node_design")
                    .set("scheme_id", scheme_id)
                    .set("details", details)
                    .insert() > 0;
        }
    }

    //删除流程设计画布内容
    public static boolean delSchemeFlowDesignById(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node_design")
                .where("scheme_id = ?", scheme_id)
                .delete() > 0;
    }

    //通过计算方案关联模型获取模型下的字段列表
    public static ModelModel getModelFieldListByModelTagAndName(String related_model) throws SQLException {
        String[] split = related_model.split("/");
        String tag = split[0];
        String name = split[1];

        return db().table("rubber_model")
                .where("tag = ? and name = ?", tag, name)
                .select("*")
                .getItem(new ModelModel());
    }


    //获取所有计算方案
    public static List<SchemeModel> getSchemes() throws SQLException {
        return db().table("rubber_scheme")
                .where("is_enabled = 1")
                .orderBy("tag ASC,name ASC")
                .select("*")
                .getList(new SchemeModel());
    }


    //获取计算方案的流程设计的执行节点详情
    public static List<SchemeNodeRespModel> getSchemeNodeTask(SchemeNodeModel node) throws SQLException {
        List<SchemeNodeRespModel> resp = new ArrayList<>();
        List<SchemeModel> schemes = getSchemes();
        SchemeModel scheme = getSchemeById(node.scheme_id);

        //List<PaasFunModel> apis = !TextUtils.isEmpty(scheme.related_block) ? DbPaaSApi.getFunsByTagOrName(scheme.related_block) : new ArrayList<>();

        List<SchemeNodeNameModel> schemeList = new ArrayList<>();
        for (SchemeModel m : schemes) {
            SchemeNodeNameModel schemeMap = new SchemeNodeNameModel();
            schemeMap.name = m.tag + "/" + m.name;
            schemeMap.display_name = m.name_display;
            schemeMap.tag = m.tag;
            schemeList.add(schemeMap);
        }

//        List<SchemeNodeNameModel> funList = new ArrayList<>();
//        for (PaasFunModel m : apis) {
//            SchemeNodeNameModel funMap = new SchemeNodeNameModel();
//            funMap.name = m.tag + "/" + m.fun_name;
//            funMap.display_name = m.name_display;
//            funMap.tag = m.tag;
//            funList.add(funMap);
//        }

        if (node.node_id > 0 && !TextUtils.isEmpty(node.tasks)) {
            String[] taskArray = node.tasks.split(";");
            for (String m : taskArray) {

                String[] value = m.split(",");
                String type = value[0];
                String name = value[1];

//                if ("F".equals(type)) {
//                    //动态函数
//                    SchemeNodeRespModel F = new SchemeNodeRespModel();
//                    F.type = "F";
//                    List<SchemeNodeNameModel> funList1 = new ArrayList<>();
//                    for (SchemeNodeNameModel m1 : funList) {
//                        SchemeNodeNameModel m2 = new SchemeNodeNameModel();
//                        m2.select = name;
//                        m2.name = m1.name;
//                        m2.display_name = m1.display_name;
//                        m2.tag = m1.tag;
//                        funList1.add(m2);
//                    }
//                    F.name_resp = funList1;
//                    resp.add(F);
//                }

                if ("R".equals(type)) {
                    //计算方案
                    SchemeNodeRespModel R = new SchemeNodeRespModel();
                    R.type = "R";
                    List<SchemeNodeNameModel> schemeList1 = new ArrayList<>();
                    for (SchemeNodeNameModel m1 : schemeList) {
                        SchemeNodeNameModel m2 = new SchemeNodeNameModel();
                        m2.select = name;
                        m2.name = m1.name;
                        m2.display_name = m1.display_name;
                        m2.tag = m1.tag;
                        schemeList1.add(m2);
                    }
                    R.name_resp = schemeList1;
                    resp.add(R);
                }
            }
        } else {
            SchemeNodeRespModel R = new SchemeNodeRespModel();
            R.type = "R";
            List<SchemeNodeNameModel> schemeList1 = new ArrayList<>();
            SchemeNodeNameModel schemeMap1 = new SchemeNodeNameModel();
            schemeMap1.name = "";
            schemeMap1.display_name = "";
            schemeMap1.select = "";
            schemeMap1.tag = "";
            schemeList1.add(schemeMap1);
            for (SchemeNodeNameModel m1 : schemeList) {
                m1.select = "";
                schemeList1.add(m1);
            }
            R.name_resp = schemeList1;
            resp.add(R);
        }

        return resp;
    }

    //获取所有角色
    public static List<ActorModel> getActorList(SchemeNodeModel node) throws SQLException {
        List<ActorModel> list = new ArrayList<>();
        List<ActorModel> actors = db().table("rubber_actor")
                .select("*")
                .getList(new ActorModel());
        if (!TextUtils.isEmpty(node.actor_display)) {
            list = actors;
        } else {
            list.add(new ActorModel());
            list.addAll(actors);
        }
        return list;
    }

    //根据执行任务类型获取默认执行任务列表
    public static JSONArray getTaskDefultByType(String taskType, int scheme_id) throws SQLException {
        JSONArray resp = new JSONArray();
        if ("R".equals(taskType)) {
            //动态函数
            List<SchemeModel> schemes = getSchemes();
            JSONArray objects = JSONArray.parseArray(JSON.toJSONString(schemes));
            resp.addAll(objects);
        }

//        if ("F".equals(taskType)) {
//            SchemeModel scheme = getSchemeById(scheme_id);
//            //List<PaasFunModel> paasFuns = DbPaaSApi.getPaasFuns();
//            List<PaasFunModel> paasFuns = !TextUtils.isEmpty(scheme.related_block) ? DbPaaSApi.getFunsByTagOrName(scheme.related_block) : new ArrayList<>();
//            JSONArray objects = JSONArray.parseArray(JSON.toJSONString(paasFuns));
//            resp.addAll(objects);
//        }

        return resp;
    }

    //处理校验scheme_node表tasks字段
    public static String checkTasks(String tasks) {
        if (TextUtils.isEmpty(tasks)) {
            return tasks;
        } else {
            StringBuilder resp = new StringBuilder();
            String[] arr = tasks.split(";");
            for (String m : arr) {
                String[] content = m.split(",");
                if (content.length == 2) {
                    resp.append(m + ";");
                }
            }

            String s = resp.toString();
            if (!TextUtils.isEmpty(s)) {
                s = s.substring(0, s.length() - 1);
            }

            return s;
        }
    }

    //设置计算方案-流程设计(执行节点)
    public static boolean setSchemeNodeExcute(int scheme_id, String node_key, String name, String actor, String tasks) throws SQLException {

        tasks = checkTasks(tasks);
        DbTableQuery query = db().table("rubber_scheme_node")
                .set("scheme_id", scheme_id)
                .set("name", name)
                .set("type", 2)
                .set("tasks", tasks)
                .set("last_updatetime", new Date());

        String actorname = "";
        String actor_display = "";
        if (!TextUtils.isEmpty(actor)) {
            String[] list = actor.split(",");
            if (list.length > 1) {
                actorname = list[0];
                actor_display = list[1];
                query.set("actor", actorname)
                        .set("actor_display", actor_display);
            }
        }
        SchemeNodeModel node = getSchemeNodeByNodeKey(node_key, scheme_id);
        if (node.node_id > 0 && 2 == node.type) {
            //update
            query.where("node_key = ?", node_key)
                    .and("scheme_id = ?", scheme_id)
                    .update();
        } else {
            //insert
            query.set("node_key", node_key).insert();
        }
        return true;
    }

    //初始化设置流程设计节点
    public static boolean setSchemeNode(int scheme_id, String node_key, String name, int type, String prve_key, String next_key) throws SQLException {
        SchemeNodeModel node = getSchemeNodeByNodeKey(node_key, scheme_id);

        if (node.node_id == 0) {
            return db().table("rubber_scheme_node")
                    .set("scheme_id", scheme_id)
                    .set("node_key", node_key)
                    .set("type", type)
                    .build(tb -> {
                        if (!TextUtils.isEmpty(name)) {
                            tb.set("name", name);
                        }
                        if (!TextUtils.isEmpty(prve_key)) {
                            tb.set("prve_key", prve_key);
                        }
                        if (!TextUtils.isEmpty(next_key)) {
                            tb.set("next_key", next_key);
                        }
                    })
                    .set("last_updatetime", new Date())
                    .insert() > 0;
        } else {
            return db().table("rubber_scheme_node")
                    .where("node_id = ?", node.node_id)
                    .set("scheme_id", scheme_id)
                    .set("node_key", node_key)
                    .set("type", type)
                    .build(tb -> {
                        if (!TextUtils.isEmpty(name)) {
                            tb.set("name", name);
                        }
                        if (!TextUtils.isEmpty(prve_key)) {
                            tb.set("prve_key", prve_key);
                        }
                        if (!TextUtils.isEmpty(next_key)) {
                            tb.set("next_key", next_key);
                        }
                    })
                    .set("last_updatetime", new Date())
                    .update() > 0;
        }
    }

    //根据scheme_id获取scheme_node
    public static List<SchemeNodeModel> getSchemeNodeBySchemeId(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node")
                .where("scheme_id = ?", scheme_id)
                .select("*")
                .getList(new SchemeNodeModel());
    }

    //检查开始结束节点数量是否正确
    public static boolean checkStartAndEndNode(JSONArray nodeDataArray) {
        int startCount = 0;
        int endCount = 0;
        for (Iterator iterator = nodeDataArray.iterator(); iterator.hasNext(); ) {
            JSONObject m = (JSONObject) iterator.next();
            String text = m.get("text").toString();
            if ("开始".equals(text)) {
                startCount = startCount + 1;
            }
            if ("结束".equals(text)) {
                endCount = endCount + 1;
            }
        }
        if (startCount != 1 || endCount != 1) {
            return false;
        }
        return true;
    }

    //删除节点
    public static boolean delSchemeNod(String node_key, int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node")
                .where("node_key = ?", node_key)
                .and("scheme_id = ?", scheme_id)
                .delete() > 0;
    }

    //根据计算方案id删除该计算方案所有节点
    public static boolean delAllSchemeNodeBySchemeId(int scheme_id) throws SQLException {
        return db().table("rubber_scheme_node")
                .where("scheme_id = ?", scheme_id)
                .delete() > 0;
    }

    //根据绘图json设置scheme_node
    public static JSONObject setSchemeNodeAll(int scheme_id, String details) throws SQLException {
        int node_count = 0;
        JSONObject resp = new JSONObject();
        List<SchemeNodeModel> nodeCurrent = getSchemeNodeBySchemeId(scheme_id);

        if (!TextUtils.isEmpty(details)) {
            JSONObject json = JSONObject.parseObject(details);
            //节点数组
            JSONArray nodeDataArray = json.getJSONArray("nodeDataArray");
            if (nodeDataArray.size() == 0) {
                delAllSchemeNodeBySchemeId(scheme_id);
                delSchemeFlowDesignById(scheme_id);
                updateSchemeNodeCount(scheme_id, 0);
                resp.put("code", 1);
                resp.put("msg", "保存成功");
                return resp;
            }
            //检查是否有开始结束节点
            boolean result = checkStartAndEndNode(nodeDataArray);
            if (!result) {
                resp.put("code", 0);
                resp.put("msg", "开始/结束 节点数量不对");
                return resp;
            }

            HashMap<String, String> map = new HashMap<>();
            HashMap<String, String> mapFigure = new HashMap<>();

            for (Iterator iterator = nodeDataArray.iterator(); iterator.hasNext(); ) {
                JSONObject m = (JSONObject) iterator.next();
                String node_key = m.get("key").toString();
                String name = m.get("text").toString();
                String figure = m.get("figure").toString();
                mapFigure.put(node_key, figure);

                int type = 0;
                //0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束'
                if ("Circle".equals(figure) && "开始".equals(name)) {//开始节点
                    type = 0;
                    node_count++;
                } else if ("Circle".equals(figure) && "结束".equals(name)) {//结束节点
                    type = 9;
                    node_count++;
                } else if ("execute".equals(figure)) {//执行节点
                    type = 2;
                    node_count++;
                } else if ("Diamond".equals(figure)) {//排他节点
                    type = 3;
                    node_count++;
                } else if ("Parallelogram2".equals(figure)) {//并行节点
                    type = 4;
                    node_count++;
                } else if ("StopSign".equals(figure)) {//汇集节点
                    type = 5;
                    node_count++;
                }
                map.put(node_key, node_key);
                setSchemeNode(scheme_id, node_key, name, type, "", "");
            }

            //线数组
            JSONArray linkDataArray = json.getJSONArray("linkDataArray");
            for (Iterator iterator = linkDataArray.iterator(); iterator.hasNext(); ) {

                JSONObject m = (JSONObject) iterator.next();
                String prve_key = m.get("from").toString();
                String next_key = m.get("to").toString();
                String node_key = prve_key + next_key;
                int type = 1;
                node_count++;
                setSchemeNode(scheme_id, node_key, "", type, prve_key, next_key);
                map.put(node_key, node_key);

            }

            //移除之前产生的在当前节点集合中不存在的节点
            for (SchemeNodeModel m : nodeCurrent) {
                String node_key = m.node_key;
                if (map.get(node_key) == null) {
                    delSchemeNod(node_key, scheme_id);
                }
            }
        }
        setSchemeFlowDesign(scheme_id, details);
        updateSchemeNodeCount(scheme_id, node_count);
        resp.put("code", 1);
        resp.put("msg", "保存成功");

        {
            SchemeModel m = getSchemeById(scheme_id);
            WaterClient.Notice.updateCache("scheme:" + m.tag + "/" + m.name);
        }

        return resp;

    }

    //更新计算方案节点数量
    public static boolean updateSchemeNodeCount(int scheme_id, int node_count) throws SQLException {
        return db().table("rubber_scheme")
                .where("scheme_id = ?", scheme_id)
                .set("node_count", node_count)
                .update() > 0;
    }

    //处理分支表达式
    public static String checkCondition(String condition) {
        List<JSONObject> list = new ArrayList<>();
        if (!Utils.isEmpty(condition)) {
            list = (List<JSONObject>) JSON.parse(condition);
        }
        String exprDisplay = "";
        int index = 1;
        JSONObject exprValue = new JSONObject();
        for (JSONObject json : list) {
            String leftValue = json.getString("left");
            String leftDisplay = json.getString("leftValue");
            String center = json.getString("center");
            String right = json.getString("right");                     //key
            String rightVal = json.getString("rightValue");            //显示名
            String ct = json.getString("ct");

            JSONObject exprValueItem = new JSONObject();
            exprValueItem.put("l", leftValue);
            exprValueItem.put("op", center);
            if (!Utils.isEmpty(right)) {
                exprValueItem.put("r", right);
            } else {
                exprValueItem.put("r", rightVal);
            }
            exprValueItem.put("ct", ct);
            exprValue.put("_" + index, exprValueItem);

            String rightValue = "";
            if (Utils.isEmpty(right)) {
                rightValue = "{_" + index + ":" + rightVal + "}";
            } else {
                rightValue = rightVal;
            }
            String ctDisplay = "";
            if ("&&".equals(ct)) {
                ctDisplay = "并且";
            } else if ("||".equals(ct)) {
                ctDisplay = "或者";
            }
            exprDisplay += MessageFormat.format("{0} {1} {2} {3} ", leftDisplay, center, rightValue, ctDisplay);
            index++;
        }
        return exprValue.toJSONString();
    }

    //设置计算方案-流程设计(分支节点)
    public static boolean setSchemeNodeBranch(int scheme_id, String node_key, String name, String condition) throws SQLException {
        condition = checkCondition(condition);

        DbTableQuery query = db().table("rubber_scheme_node")
                .set("scheme_id", scheme_id)
                .set("name", name)
                .set("type", 1)
                .set("condition", condition)
                .set("last_updatetime", new Date());
        SchemeNodeModel node = getSchemeNodeByNodeKey(node_key, scheme_id);
        if (node.node_id > 0) {
            //update
            query.where("node_key = ?", node_key)
                    .and("scheme_id = ?", scheme_id)
                    .update();
        } else {
            //insert
            query.set("node_key", node_key).insert();
        }
        return true;
    }

    //根据log_id获取请求记录
    public static LogRequestModel getLogReqById(long log_id) throws SQLException {
        return Config.water_log.table("rubber_log_request")
                .where("log_id = ?", log_id)
                .select("*")
                .getItem(new LogRequestModel());
    }

    //根据id获取数据块
    public static BlockModel getBlockById(int block_id) throws SQLException {
        return db().table("rubber_block")
                .where("block_id = ?", block_id)
                .select("*")
                .getItem(new BlockModel());
    }

    public static List<BlockModel> getBlockByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("rubber_block")
                .whereIn("block_id", list)
                .select("*")
                .getList(new BlockModel());
    }

    //获取数据block的tag集合
    public static List<TagCountsModel> getBlockTags() throws SQLException {
        return db().table("rubber_block")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //根据tag获取数据块集合
    public static List<BlockModel> getBlocks(String tag) throws SQLException {
        return db().table("rubber_block")
                .where("tag = ?", tag)
                .orderBy("name ASC")
                .select("*")
                .getList(new BlockModel());
    }

    //新增或修改数据块
    public static long blockSave(Integer block_id, String tag, String name, String name_display, String related_db, String related_tb,
                                 String struct, String app_expr, int is_editable, String note) throws SQLException {
        DbTableQuery tb = db().table("rubber_block").usingExpr(true)
                .set("tag", tag)
                .set("name", name.trim())
                .set("name_display", name_display)
                .set("note", note)
                .set("is_editable", is_editable)
                .set("related_db", related_db)
                .set("related_tb", related_tb)
                .set("struct", struct)
                .set("app_expr", app_expr);

        if (block_id > 0) {
            long isOk = tb.where("block_id = ?", block_id).update();

            WaterClient.Notice.updateCache("block:" + tag + "/" + name);

            return isOk;
        } else {
            return tb.set("last_updatetime", new Date()).insert();
        }
    }

    public static int blockDel(Integer block_id) throws SQLException {
        return db().table("rubber_block")
                .whereEq("block_id", block_id)
                .delete();
    }

    public static void blockImp(String tag, BlockModel vm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            vm.tag = tag;
        }

        if (TextUtils.isEmpty(vm.tag) || TextUtils.isEmpty(vm.name)) {
            return;
        }

        db().table("rubber_block")
                .set("tag", vm.tag)
                .set("name", vm.name)
                .set("name_display", vm.name_display)
                .set("note", vm.note)
                .set("is_editable", vm.is_editable)
                .set("related_db", vm.related_db)
                .set("related_tb", vm.related_tb)
                .set("struct", vm.struct)
                .set("app_expr", vm.app_expr)
                .set("last_updatetime", vm.last_updatetime)
                .insertBy("tag,name");

        WaterClient.Notice.updateCache("block:" + vm.tag + "/" + vm.name);
    }

    //获取数据块item集合
    public static DataList getBlockItems(BlockModel block, String fname, String fval) throws SQLException {

        if (block.cols().size() == 0) {
            return new DataList();
        }

        String cols_str = block.cols_str();

        if (TextUtils.isEmpty(block.related_db) || TextUtils.isEmpty(block.related_tb)) {
            return new DataList();
        } else {
            ConfigM cfg = WaterClient.Config.getByTagKey(block.related_db);

            if (cfg.value.indexOf("mysql:") > 0) {
                //查询引用的tsql-db
                //
                return cfg.getDb().table(block.related_tb)
                        .build(tb -> {
                            if (TextUtils.isEmpty(fval) == false) {
                                tb.where(fname + " LIKE ?", "%" + fval + "%");
                            }
                        })
                        .limit(100)
                        .select(cols_str)
                        .getDataList();
            } else {

                //查询引用的nosql-db
                //
                int tb = Integer.parseInt(block.related_tb);
                String key = "block_" + block.block_id;
                String search = (fval == null ? "*" : "*" + fval + "*");

                List<Map.Entry<String, String>> list = cfg.getRd(tb).open1((rs) -> rs.key(key).hashScan(search, 100));
                DataList dataList = new DataList();
                if (list != null) {

                    list.forEach(kv -> {
                        DataItem item = new DataItem();
                        JSONObject obj = JSONObject.parseObject(kv.getValue());

                        item.set("key",kv.getKey());

                        obj.forEach((k, v) -> {
                            item.set(k, v);
                        });

                        dataList.addRow(item); //key,f,x,d,r
                    });
                }

                return dataList;
            }
        }
    }


    //根据id获取block_item
    public static DataItem getBlockItem(BlockModel block, String item_key) throws SQLException {
        if (block.cols().size() == 0 || TextUtils.isEmpty(item_key)) {
            return new DataItem();
        }

        String cols_key = block.cols_key();
        String cols_str = block.cols_str();

        if (TextUtils.isEmpty(block.related_db)) {
            return new DataItem();
        } else {
            ConfigM cfg = WaterClient.Config.getByTagKey(block.related_db);

            if (cfg.value.indexOf("mysql:") > 0) {
                return cfg.getDb()
                        .table(block.related_tb)
                        .where(cols_key + "= ?", item_key)
                        .select(cols_str)
                        .getDataItem();
            } else {
                int tb = Integer.parseInt(block.related_tb);
                String key = "block_" + block.block_id;

                String temp = cfg.getRd(tb).open1((rs) -> rs.key(key).hashGet(item_key));
                JSONObject obj = JSONObject.parseObject(temp);
                DataItem item = new DataItem();

                item.set("key",key);
                obj.forEach((k, v) -> {
                    item.set(k, v);
                });

                return item;
            }
        }


    }

    //新增或修改数据块
    public static void setBlockItem(BlockModel block, String item_key, JSONObject data) throws SQLException {
        if (block.cols().size() == 0) {
            return;
        }

        String cols_key = block.cols_key();

        if (TextUtils.isEmpty(block.related_tb) == false) {
            ConfigM cfg = WaterClient.Config.getByTagKey(block.related_db);

            if (cfg.value.indexOf("mysql:") > 0) {
                DbTableQuery tb = cfg.getDb().table(block.related_tb).usingExpr(false);
                data.forEach((k, v) -> {
                    tb.set(k, v);
                });

                if (TextUtils.isEmpty(item_key)) {
                    tb.insert();
                } else {
                    tb.where(cols_key + "= ?", item_key).update();
                }
            } else {
                int tb = Integer.parseInt(block.related_tb);
                String key = "block_" + block.block_id;

                cfg.getRd(tb).open0((rs) -> {
                    rs.key(key).hashDel(item_key);
                    rs.key(key).hashSet(data.getString(cols_key), data.toJSONString());
                });
            }
        }
    }

    public static List<BlockModel> getBlocksByTagOrNameArray(String tags) throws SQLException {
        String[] details = tags.split(";");
        return db().table("rubber_block").build(tb ->{
            tb.where("1 != 1");
            for (String item : details){
                tb.or();
                String[] split = item.split("/");
                String tag = split[0];
                String name = split[1];
                tb.begin("tag=?", tag);
                if (!"*".equals(name)){
                    tb.and("name" ,name);
                }
                tb.end();
            }
        }).select("block_id,tag,name,name_display").getList(new BlockModel());
    }
}
