package webapp.dao;

import org.noear.water.admin.tools.dso.CacheUtil;
import org.noear.water.tools.EncryptUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasFunModel;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//简化版，不支持rock，不支持htttp，不支持sql
public class JtSQL {
    private ScriptEngine jsEngine = null;
    private JTAPI    jtapi = null;
    private JtLogger jtlog = null;

    public JtSQL(JtLogger logger){

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        jsEngine = scriptEngineManager.getEngineByName("nashorn");

        jtlog = logger;
        jtapi = new JTAPI(this, null);

        jsEngine.put("JTAPI",jtapi);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("var jtapi_global={};");
            sb.append("function require(url){var lib=md5(url);if(!jtapi_global[lib]){var code=http({url:url});jtapi_global[lib]=eval(code)};return jtapi_global[lib]};");
            sb.append("function include(tagKey){ JTAPI.loadfuns(tagKey);};");

            sb.append("this.List=function(ary){var list2=new java.util.ArrayList();ary.forEach(function(t){list2.add(t)});return list2};");

            sb.append("function guid(){return JTAPI.guid()};");
            sb.append("function md5(txt){return JTAPI.md5(txt)};");
            sb.append("function sha1(txt){return JTAPI.sha1(txt)};");
            sb.append("function sha256(str){return JTAPI.sha256(str)};");
            sb.append("function hmacSha256(str,key){return JTAPI.hmacSha256(str,key)};");
            sb.append("function xorEncode(str,key){return JTAPI.xorEncode(str,key)};");
            sb.append("function xorDecode(str,key){return JTAPI.xorDecode(str,key)};");
            sb.append("function b64Encode(str){return JTAPI.b64Encode(str)};");
            sb.append("function b64Decode(str){return JTAPI.b64Decode(str)};");

            sb.append("var Timecount = Java.type('org.noear.water.utils.Timecount');");
            sb.append("var Timespan = Java.type('org.noear.water.utils.Timespan');");

            sb.append("function Datetime(d){return JTAPI.dateTime(d?d:null)};");
            sb.append("function parseDatetime(str,f){return JTAPI.dateTime_parse(str,JSON.stringify(f))};");

            sb.append("function set(key,obj){JTAPI.set(key,JSON.stringify(obj))};");
            sb.append("function get(key){var txt=JTAPI.get(key);if(txt){return JSON.parse(txt)}else{return null}};");

            sb.append("function log(obj){JTAPI.log(JSON.stringify(obj,stringify_jtime))};");
            //sb.append("function http(obj){return JTAPI.http(JSON.stringify(obj))};"); //obj:{url:'xxx', form:{}, header:{}}
            //sb.append("function sql(txt){var _d=JTAPI.sql(txt); return db2json(_d);};");

            //计划任务专享
            sb.append("function sleep(millis){ JTAPI.sleep(millis);};");


            sb.append("var cache = JTAPI.cache();");
            sb.append("var water={};");
            sb.append("var rock={};");

            sb.append("water.client = Java.type('org.noear.water.WaterClient');");
            sb.append("water.cfg = function(tagKey){return water.client.Config.getByTagKey(tagKey)};");
            sb.append("water.db = function(tagKey){return water.cfg(tagKey).getDb()};");
            sb.append("water.rpc = function(service,fun,obj){return JTAPI.call_rpc(service,fun,JSON.stringify(obj));};");
            sb.append("water.paas = function(tag,name,arg){return JTAPI.call_paas(tag,name,JSON.stringify(arg));};");
            sb.append("water.raas = function(obj,tag,name,arg){return JTAPI.call_raas(obj,tag,name,JSON.stringify(arg));};");
            sb.append("water.heihei = function(target,msg){return JTAPI.call_heihei(target,msg);};");
            sb.append("water.updateCache = function(tags){water.client.Runner.updateCache(tags)};");



            //动态对象// jsEngine.eval("function db2json(_d){if(_d.getClass().getSimpleName() == 'DbQuery'){_d = _d.getDataList();};  if(_d&&typeof(_d)=='object'&&_d.getRow){var item=_d.getRow(0);var keys=item.keys();if(_d.getRowCount()==1){var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=item.get(k1)};return obj}else{var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=m1.get(k1)};ary.push(obj)}};return ary}}else{return _d}};");

            //返回list或::val// jsEngine.eval("function db2json(_d){if(_d.getClass().getSimpleName()=='DbQuery'){_d=_d.getDataList()};if(_d&&typeof(_d)=='object'&&_d.getRow){if(_d.getRowCount()==0){return[]};var item=_d.getRow(0);var keys=item.keys();var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=m1.get(k1)};ary.push(obj)}};return ary}else{return _d}};");

            //返回list或::val或::obj
            sb.append("function db2json(_d){if(_d&&_d.getClass().getSimpleName()=='DbQuery'){_d=_d.getDataList()};if(_d&&typeof(_d)=='object'){if(_d.getRow){if(_d.getRowCount()==0){return[]};var item=_d.getRow(0);var keys=item.keys();var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=m1.get(k1)};ary.push(obj)}};return ary};if(_d.getVariate){var keys=_d.keys();var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=_d.get(k1)};return obj};return _d}else{return _d}};");

            //为JSON.stringify 添加java的时间处理
            sb.append("function stringify_jtime(k,v){if(v&&v.before){return new Datetime(v).toString('yyyy-MM-dd HH:mm:ss')}; if(v&&v.getFullTime){return v.toString('yyyy-MM-dd HH:mm:ss')}; return v};");




            jsEngine.eval(sb.toString());

            //jsEngine.eval("function sql(txt){var _d=JTAPI.sql(txt);if(_d&&typeof(_d)=='object'&&_d.getRow){var item=_d.getRow(0);var keys=item.keys();if(_d.getRowCount()==1){var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=item.get(k1)};return obj}else{var ary=[];if(item.count()==1){var list=_d.toArray(0);for(var i in list){ary.push(list[i])}}else{var rows=_d.getRows();for(var j in rows){var m1=rows.get(j);var obj={};for(var i in keys){var k1=keys.get(i);obj[k1]=m1.get(k1)};ary.push(obj)}};return ary}}else{return _d}};");
        }catch (Exception ex){
            ex.printStackTrace();
            jtlog.error("JtSQL","init",ex);
        }
    }

    public void exec(String jtsql) throws ScriptException{
        String jscode = "(function(){\r\n\r\n"+compile(jtsql)+"\r\n\r\n})();";

        jsEngine.eval(jscode);
    }

    public void load(String jtsql) throws ScriptException {
        String jscode = compile(jtsql);

        jsEngine.eval(jscode);
    }

    public Object eval(String jtsql) throws ScriptException{
        String jscode = compile(jtsql);

        return jsEngine.eval(jscode);
    }

    public void tryUpdateFuns(String tagName) throws Exception{
        jtapi.updateFuns(tagName);
    }

    public String last_sql(){
        return jtapi.last_sql;
    }

    private String compile(String jtsql){
        return JtSQLCompiler.compile(jtsql);
    }

    //提供上下文数据操作
    private Map<String,Object> data = new ConcurrentHashMap();
    public void setData(String key,Object val){
        data.put(key,val);
    }
    public Object getData(String key){
        return data.get(key);
    }
    public void delData(String key){
        data.remove(key);
    }
    public void delDataAll(){data.clear();}

    /////////////////////////

    public class JTAPI {
        private DbContext context;
        public String last_sql = null;
        private JtSQL jtSQL;

        public JTAPI(JtSQL jtSQL, DbContext context) {
            this.jtSQL = jtSQL;
            this.context = context;
        }

        public String guid() {
            return UUID.randomUUID().toString();
        }

        public String md5(String str) {
            return EncryptUtils.md5(str);
        }

        public String sha1(String str) {
            return EncryptUtils.sha1(str);
        }


        ////////////////////////


        public ICacheServiceEx cache() {
            return CacheUtil.data;
        }

        ////////////////////////

        public void log(Object txt) {
            System.out.print("JtSQL.log::");
            System.out.println(txt.toString());
            jtSQL.jtlog.write("JtSQL.log", "log", txt.toString());
        }


        ////////////////////////
        private Map<String, String> setting = new HashMap<>();

        public void set(String key, String val) {
            setting.put(key, val);
        }

        public String get(String key) {
            if (setting.containsKey(key)) {
                return setting.get(key);
            }
            else {
                return null;
            }
        }

        public void clear() {
            setting.clear();
        }

        public void sleep(long millis) throws Exception {
            Thread.sleep(millis);
        }


        //============

        public void loadfuns(String tagKey) throws Exception {

            String[] ss = tagKey.split("\\/");

            if ("*".equals(ss[1])) {
                List<PaasFunModel> funs = DbPaaSApi.getFunsByTag(ss[0]);
                for (PaasFunModel fun : funs) {
                    loadJtFun(fun, false);
                }
            } else {
                PaasFunModel fun = DbPaaSApi.getFun(ss[0], ss[1]);

                loadJtFun(fun, false);
            }
        }

        public void updateFuns(String tagKey) throws Exception{
            String[] ss = tagKey.split("\\/");

            if ("*".equals(ss[1])) {
                List<PaasFunModel> funs = DbPaaSApi.getFunsByTag(ss[0]);
                for (PaasFunModel fun : funs) {
                    loadJtFun(fun, true);
                }
            } else {
                PaasFunModel fun = DbPaaSApi.getFun(ss[0], ss[1]);

                loadJtFun(fun, true);
            }
        }

        private void loadJtFun(PaasFunModel fun , boolean forUpdate) throws Exception {
            if(forUpdate){
                if (fun_loaded.contains(fun.fun_name)) {
                    doLoadJtFun(fun);
                }
            }else {
                if (fun_loaded.contains(fun.fun_name)) {
                    return;
                } else {
                    fun_loaded.add(fun.fun_name);
                }

                doLoadJtFun(fun);
            }
        }

        private void doLoadJtFun(PaasFunModel fun) throws Exception {
            if (fun.fun_name.startsWith("::")) {
                jtSQL.load(fun.code); //已内置编译
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("this.").append(fun.tag).append("_").append(fun.fun_name)//此处与PAAS-API不一样
                        .append("=")
                        .append("function(").append(fun.args).append(")")
                        .append("{\r\n\r\n")
                        .append(compile(fun.code)) //需要编译一下
                        .append("\r\n\r\n}; ");

                try {
                    jtSQL.load(sb.toString());
                }catch (Exception ex){
                    ex.printStackTrace();
                    jtSQL.jtlog.error("JtSQL","doLoadJtFun",ex);
                    throw ex;
                }
            }
        }

        //==============
        public void setData(String key,Object val){
            jtSQL.setData(key,val);
        }
        public Object getData(String key){
            return jtSQL.getData(key);
        }
        public void delData(String key){
            jtSQL.delData(key);
        }
    }
    private  List<String> fun_loaded =  Collections.synchronizedList(new ArrayList<>());
}
