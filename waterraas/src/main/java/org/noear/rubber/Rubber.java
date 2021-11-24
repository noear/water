package org.noear.rubber;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.rubber.models.BlockModel;
import org.noear.rubber.models.LogRequestModel;
import org.noear.rubber.models.ModelModel;
import org.noear.rubber.models.SchemeModel;

import java.sql.SQLException;
import java.util.UUID;

//执行入口
public class Rubber {
    public static void tryInit(ICacheServiceEx cache) throws Exception{
        RcConfig._data_cache = cache;
        RcConfig.inner_cache = cache;
        RcRunner.tryInit();

        Solon.global().sharedAdd("RubberUtil", new RubberUtil());
    }

    //
    //@request_id:: 请求ID，用guid生成
    //@scheme_tagName:: 方案代号，用tag/name 形式
    //@args:: 业务参数
    //@policy:: 返回策略（
    //  1000, 匹配模式，有不匹配的马上返回false；
    //  1001, 匹配模式，且记录触发明细
    //  2000, 分值模式，累记分值不记录明细；
    //  2001, 分值+明细模式，累记分值且记录触发明细；
    //
    public static RubberResponse scheme(String request_id, String scheme_tagName, int policy, ONode args, int type, String rule, boolean is_debug) throws Exception {

        if (scheme_tagName.indexOf("/") < 0) {
            throw new RubberException("doesn't exist S:" + scheme_tagName);
        }

        //0.初始化运行环境
        RcRunner.tryInit();

        //1.初始化上下文
        RubberContext context = new RubberContext(new RubberRequest(request_id, scheme_tagName, policy, args));
        context.is_debug = is_debug; //主要控制 SR_test 的运行

        //2.1.开始响应计时
        context.response().start();


        //0.2.获取方案配置 //放这里，计时更科学
        SchemeModel scheme = DbApi.getSchemeByTagName(scheme_tagName);

        if (scheme.scheme_id < 1) {
            throw new RubberException("doesn't exist S:" + scheme_tagName);
        }


        //2.3.注册上下文
        RubberUtil.registerContext(context);

        try {
            //2.4.3.执行计算方案
            if (type == 0) {
                RcRunner.runScheme(context, scheme);
            } else {
                if (type == 1) {
                    RcRunner.runSchemeEvent(context, scheme);
                }

                if (type == 2) {
                    RcRunner.runSchemeRule(context, scheme, rule);
                }

                if (type == 3) {
                    RcRunner.runSchemeFlow(context, scheme);
                }
            }

        } finally {
            //2.5.注销上下文
            RubberUtil.unregisterContext(context);
        }

        //2.6.结束响应计时
        context.response().end(context);

        //3.输出上下文
        return context.response();
    }


    public static ONode model(String model_tagName, ONode args, String field, boolean is_debug) throws Exception {
        String request_id = Rubber.guid();
        int policy = 1000;

        String ss[] = model_tagName.split("/");

        if (TextUtils.isEmpty(field) == false) {
            if (DbApi.hasModelField(ss[0], ss[1], field) == false) {
                throw new RubberException(model_tagName + " doesn't exist " + field);
            }
        }

        //0.初始化运行环境
        RcRunner.tryInit();

        //1.初始化上下文
        RubberContext context = new RubberContext(new RubberRequest(request_id, policy, args));

        context.is_debug = is_debug;

        //2.3.注册上下文
        RubberUtil.registerContext(context);

        try {
            //2.4.1.不需要计时
            //2.4.2.加载模型
            context.loadModel(model_tagName);

            if (TextUtils.isEmpty(field)) {
                RcRunner.runModel(context);
            } else {
                RcRunner.runModelField(context, field);
            }
            //2.4.4.不需要计时
        } finally {
            //2.5.注销上下文
            RubberUtil.unregisterContext(context);
        }

        return context.model_json();
    }

    public static ONode block(String block_tagName, ONode args) throws Exception {
        //0.初始化运行环境
        RcRunner.tryInit();

        //1.初始化数据块
        RcRunner.tryInitBlock(block_tagName, false);

        //2.执行
        Object val = RcRunner.runBlock(block_tagName, args);

        //3.输出值
        return ONode.load(val);
    }

    public static RubberQuery query(String scheme_tagName, int limit) throws Exception {
        SchemeModel scheme = DbApi.getSchemeByTagName(scheme_tagName);
        ModelModel model = DbApi.getModelByTagName(scheme.related_model);

        if (TextUtils.isEmpty(model.related_db)) {
            throw new RubberException(scheme.related_model + " doesn't exist @related_db setting");
        }

        String sql = RcCompiler.loadSchemeRuleAsTsql(scheme, model, limit);

        DbContext db = WaterClient.Config.getByTagKey(model.related_db).getDb();

        return new RubberQuery(db,sql);
    }

    public static String schemePreviewAsTsql(String scheme_tagName) throws Exception {
        SchemeModel scheme = DbApi.getSchemeByTagName(scheme_tagName);
        ModelModel model = DbApi.getModelByTagName(scheme.related_model);
        return RcCompiler.loadSchemeRuleAsTsql(scheme, model, 10);
    }

    public static String schemePreview(String scheme_tagName) throws Exception {
        SchemeModel scheme = DbApi.getSchemeByTagName(scheme_tagName);
        return RcCompiler.loadSchemeRuleAsFunc(scheme,true);
    }

    public static String modelPreview(String model_tagName) throws Exception {
        ModelModel model = DbApi.getModelByTagName(model_tagName);
        return RcCompiler.loadModelAsFunc(model,true);
    }

    public static String blockPreview(String block_tagName) throws Exception {
        BlockModel model = DbApi.getBlockByTagName(block_tagName);
        return RcCompiler.loadBlockAsFunc(model, true);
    }

    //=============
    public static String guid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


    public static long add(String request_id,String scheme_tagname, String args_json,int policy,String callback)throws Exception{
        return DbApi.logRequestAdd(request_id,scheme_tagname,args_json,policy,callback);
    }

    public static void log(long log_id, RubberResponse response) throws Exception {
        //3.3.记录结果
        DbApi.logRequestSet(log_id, response,
                response.request.scheme_tagName,
                response.request.args.toJson(),
                response.model_json.toJson(),
                response.session_json.toJson());
    }

    public static LogRequestModel get(String request_id) throws SQLException{
       return DbApi.logRequestGet(request_id);
    }

    public static void setBegin(long log_id) throws SQLException{
        DbApi.logRequestSetState(log_id,1);
    }

    public static void setRestart(long log_id) throws SQLException{
        DbApi.logRequestSetState(log_id,0);
    }

    public static void setEnd(long log_id) throws SQLException{
        DbApi.logRequestSetState(log_id,3);
    }

    public static void updateCache(String cache_tag) throws Exception {
        RcConfig.inner_cache.clear(cache_tag);

        if (cache_tag.indexOf(":") > 0 && cache_tag.indexOf("/") > 0) {
            String[] ss = cache_tag.split(":");

            switch (ss[0]) {
                case "model":
                    RcRunner.tryInitModel(ss[1], true);
                    break;
                case "scheme":
                    SchemeModel scheme = DbApi.getSchemeByTagName(ss[1]);
                    RcRunner.tryInitScheme(scheme, true);
                    break;
                case "block":
                    RcRunner.tryInitBlock(ss[1], true);
                    break;
            }
        }
    }
}
