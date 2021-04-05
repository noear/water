package watersev.dso.etl;

import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtil;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import watersev.dso.JtSQL;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class EtlContext {
    public static final int policy0_update_pinert = 0;
    public static final int policy1_update_inert1 = 1;
    public static final int policy2_only_update   = 2;
    public static final int policy3_only_pinsert  = 3;
    public static final int policy4_only_insert1  = 4;
    public static final int policy10_stream       = 10;
    public static final int policy11_stream_tran  = 11;
    public static final int policy12_stream_batch = 12;

    //根据配置加载的变量
    public Integer id;
    public String md5;
    public String name;

    protected String[] include = new String[]{};
    protected long interval;//轮旬间隔时间(秒)

    protected DbContext db;
    protected RedisX dataRd;
    protected RedisX lockRd;
    protected String table;
    protected String key;
    protected String cursor;
    protected boolean cursor_is_id = false;

    protected String model;
    protected int limit;
    protected String[] constraint  = new String[]{};
    protected String[] require     = new String[]{};
    protected String[] lock        = new String[]{};
    protected String[] fields      = new String[]{};
    protected boolean isFullFields = false;
    protected Map<String, String> trans = new HashMap<>();
    protected String stream;
    protected String event;
    protected int policy; //policy//默认为0(0:更新且批量插入;1更新且逐条插入;2只更新;3只批量插入;4只逐条插入; 11只执行事件)
    //根据运行需要添加的变量
    public boolean isStop;  //是否停止，交由代理动态控制//停止后线程取消;
    public boolean isPause; //是否暂停，交由代码动态控制//暂停后线程仍在;
    public int pause_state;

    protected String queue1; //队列1，存放抽取的数据
    protected String queue2; //队列2，存放转换的数据
    protected int alarmCount; //报警次数
    protected boolean isKeyBuild = false;

    private static ThreadLocal<EtlContext> _contextState = new ThreadLocal<>();

    public static EtlContext current(){
        return _contextState.get();
    }

    private String _section;
    protected EtlContext(ONode cfg, String section) throws Exception{
        _contextState.set(this);

        _section = section; //留下section，为之后的loadConfig时用
        id = cfg.get("id").getInt();

        name = cfg.get("name").getString();
        queue1 = name + "_1";
        queue2 = name + "_2";

        ConfigM rcfg = WaterClient.Config.get("water", "water_etl_redis");
        dataRd = rcfg.getRd( 10, 300);
        lockRd = rcfg.getRd( 11, 300);

        try {
            do_loadConfig(cfg);
        } catch (Exception ex) {
            ex.printStackTrace();
            EtlLog.error(this, EtlContext.class, ex.getMessage(), ex);
            throw ex;
        }
    }

    //重新加载配置
    private void do_loadConfig(ONode cfg) throws Exception{
        trans.clear();

        md5 = cfg.get("md5").getString();
        interval = cfg.get("interval").getInt();
        if(interval<1){
            interval = 1000;
        }else {
            interval = interval * 1000;
        }

        if (cfg.contains("include")) {
            include = cfg.get("include").getString().split(",|;");
        }


        ONode n = cfg.get(_section);


        model = n.get("model").getString();
        limit = n.get("limit").getInt();
        policy = n.get("policy").getInt();
        key = n.get("key").getString();
        cursor = n.get("cursor").getString();
        cursor_is_id = cfg.get("cursor_type").getInt()>0;
        table = n.get("table").getString();


        if(policy<10) {
            String db_str = n.get("db").getString();
            if (TextUtils.isEmpty("db_str")) {
                throw new Exception("缺少db配置代码");
            }

            ConfigM dbcfg = WaterClient.Config.getByTagKey(db_str);

            if (dbcfg == null) {
                WaterClient.Config.reload(db_str.split("\\.")[0]);//再尝试找下配置//如果有下次就可加载了
                throw new Exception("缺少db的water配置");
            }

            db = dbcfg.getDb();
        }

        if (n.contains("constraint")) {
            constraint = n.get("constraint").getString().split(",");
        }

        if (n.contains("lock")) {
            lock = n.get("lock").getString().split(",");
        }

        if(n.contains("require")){
            require = n.get("require").getString().split(",");
        }

        if (n.contains("fields")) {
            String val = n.get("fields").getString();
            isFullFields = "*".equals(val);
            fields = val.split(",");
        }

        if (n.contains("trans")) {
            ONode jTrans = n.get("trans");
            jTrans.forEach((k,v)->{
                trans.put(k, v.getString());
            });
        }

        stream = n.get("stream").getString();
        event = n.get("event").getString();

        isKeyBuild = inArray(key, constraint) || inArray(key, require);
    }

    public String guid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public boolean tryLock(DataItem dataItem){
        String lockKey = buildLockKey(dataItem);

        //尝试锁，并获取锁的val
        String lockVal = lockRd.open1((ru) -> {
            ru.key(lockKey).expire(60).lock(name);
            return ru.key(lockKey).get();
        });

        return name.equals(lockVal);
    }

    private String buildLockKey(DataItem dataItem) {
        StringBuilder sb = new StringBuilder();
        sb.append(table);

        for (String f : lock) {
            sb.append(".").append(f).append("_").append(dataItem.get(f).toString());
        }

        return sb.toString();
    }

    public void reloadConfig(ONode cfg, Class cls) throws Exception{
        do_loadConfig(cfg);
        EtlLog.write(this, cls, "已重新加载配置******");
    }

    //重新加载函数
    public void reloadFuns(Class cls) throws Exception {
        //与eval通过tryInitJtSQL，共享同步锁
        if (tryInitJtSQL() == false) {
            do_loadFuns();
        }

        EtlLog.write(this, cls, "已重新加载函数******");
    }


    ///////////////////////////////////////////////
    // 动态脚本管控
    //
    private JtSQL _jtSQL = null;

    private  ReentrantLock run_lock = new ReentrantLock();
    private boolean tryInitJtSQL() throws Exception {
        boolean isFirstInit = false;
        run_lock.lock();
        if (_jtSQL == null) {
            //初始化公共函数引擎
            _jtSQL = new JtSQL(null, new EtlLoggerJt());

            do_loadFuns();
            isFirstInit = true;
        }
        run_lock.unlock();

        return isFirstInit;
    }

    private void do_loadFuns() throws Exception{
        //加载公共函数（支持以多组函数库：, 隔开）
        for (String s : include) {
            String inc = "include('" + s.trim() + "');";
            _jtSQL.load(inc);
        }
    }

    protected JtSQL jtSQL() throws Exception{
        tryInitJtSQL();//与loadFuns通过tryInitJtSQL，共享同步锁

        return _jtSQL;
    }

    protected void delContextDataAll(){
        if(_jtSQL != null){
            _jtSQL.delDataAll();
        }
    }

    //执行脚本并返回结果
    protected Object eval(String jCode, EtlModel jObj) throws Exception {
        if(jCode.startsWith("$")){
            tryInitJtSQL();//与loadFuns通过tryInitJtSQL，共享同步锁

            String tmp = jCode.substring(1);//第一位是：$符
            if (jCode.indexOf("(m") > 0) {
                tmp = tmp.replace("(m", "(" + jObj.buildJtSQLArgs(this));
            }
            return _jtSQL.eval(tmp); //可以(m,'yyyy');
        }else{
            return jCode; //没有$开头，表示直接是值
        }
    }

    protected Object eval(String jCode, List<EtlModel> jObj) throws Exception {
        if (jCode.startsWith("$")) {
            tryInitJtSQL();//与loadFuns通过tryInitJtSQL，共享同步锁

            List<DataItem> list = jObj.stream().map(o -> o.data).collect(Collectors.toList());

            String guid = this.guid();
            jtSQL().setData(guid, list);

            try {
                String tmp = jCode.substring(1);//第一位是：$符
                if (jCode.indexOf("(m") > 0) {
                    tmp = tmp.replace("(m", "JTAPI.getData('" + guid + "')");
                }
                return _jtSQL.eval(tmp); //可以(m,'yyyy');
            } finally {
                jtSQL().delData(guid);
            }

        } else {
            return jCode; //没有$开头，表示直接是值
        }
    }



    //>构建目标数据值（sourceItem 源数据，k 字段名）
    public Object builVal(EtlModel dataModel,String k, boolean incKey) throws Exception {
        if (trans.containsKey(k)) {
            if (incKey == false && k.equals(key)) {
                return null;
            } else {
                return eval(trans.get(k), dataModel);
            }
        } else {
            return dataModel.data.get(k);
        }
    }
    private boolean inArray(String field, String[] ary){
        for(String f: ary){
            if(f.equals(field)){
                return true;
            }
        }

        return false;
    }

    ///////////////////////////////////////////////
    // 报警
    //
    protected void alarm(Class cls, String label, DataItem dataItem) {
        if (dataItem == null) {
            return;
        }

        //确保内部的错误不影响外部//如果出错，不需要再记录
        try {
            EtlLog.alarm(this, cls, label, json(dataItem, constraint));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected String json(DataItem dataItem, String[] fields) {
        try {
            DataItem temp = new DataItem();

            //拉取关键数据形成json，并记录
            if (dataItem.exists(key)) {
                temp.set(key, dataItem.get(key));
            }

            for (String k : fields) {
                temp.set(k, dataItem.get(k));
            }

            return ONode.stringify(temp.getMap());
        }catch (Exception ex){
            ex.printStackTrace();
            return "{}";
        }
    }
}
