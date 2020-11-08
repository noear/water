package waterpaas.controller;


import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.luffy.dso.LogLevel;
import org.noear.luffy.dso.LogUtil;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.luffy.utils.ExceptionUtils;
import org.noear.luffy.utils.TextUtils;
import waterpaas.Config;
import waterpaas.dso.AFileUtil;
import waterpaas.dso.DbPaaSApi;

import java.util.HashMap;
import java.util.List;

/**
 * 文件路径拦截器的代理（数据库安全）
 * */
public class FrmInterceptor implements XHandler {
    private static final String _lock = "";
    private static  FrmInterceptor _g = null;
    public static FrmInterceptor g(){
        if(_g == null){
            synchronized (_lock){
                if(_g == null){
                    _g = new FrmInterceptor();
                }
            }
        }
        return  _g;
    }



    private FrmInterceptor(){
        reset();
    }


    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();

        _cacheMap.forEach((path2,suf)->{
            if (path.startsWith(suf)) {
                exec(ctx, path2);
            }
        });
    }

    private void exec(XContext ctx, String path2){
        try{
            do_exec(ctx,path2);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void do_exec(XContext ctx, String path2) throws Exception {
        AFileModel file = AFileUtil.get(path2);

        if (file.file_id == 0) {
            return;
        }

        //不支持路径代理，跳过
        if (Config.faas_filter_path.equals(file.label) == false) {
            return;
        }

        try {
            ExecutorFactory.execOnly(file, ctx);
        }catch (Exception ex) {
            String err = ExceptionUtils.getString(ex);
            ctx.output(err);
            LogUtil.log("_paas", file.tag, file.path, LogLevel.ERROR, "", err);

            ctx.setHandled(true);
        }
    }

    private HashMap<String,String> _cacheMap = new HashMap<>();
    public void del(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        _cacheMap.remove(path);
    }
    public void add(String path, String note) {
        if (TextUtils.isEmpty(note)) {
            return;
        }

        String suf = note.split("#")[0];

        //要有字符，且必须是目录
        if (suf.length() > 3 &&
                suf.endsWith("/") &&
                suf.startsWith("/")) {
            _cacheMap.put(path, suf);
        }
    }

    public void reset() {
        try {
            _cacheMap.clear();

            List<AFileModel> list = DbPaaSApi.pathFilters();
            for (AFileModel c : list) {
                if(TextUtils.isEmpty(c.note)){
                    continue;
                }

                String suf = c.note.split("#")[0];

                if (suf.length()>3 &&
                        suf.endsWith("/") &&
                        suf.startsWith("/")) {
                    _cacheMap.put(c.path, suf);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}