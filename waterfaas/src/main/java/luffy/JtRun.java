package luffy;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.luffy.dso.CallUtil;
import org.noear.luffy.dso.JtBridge;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.utils.EncryptUtils;
import waterfaas.Config;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class JtRun {
    private static CompletableFuture<Integer> initFuture = new CompletableFuture<>();

    private static JtExecutorAdapter jtAdapter;
    public static void init() {
        if (jtAdapter == null) {
            jtAdapter = new JtExecutorAdapter();
            JtBridge.executorAdapterSet(jtAdapter);
            JtBridge.configAdapterSet(jtAdapter);
        }
    }

    public static Object call(String path, Context ctx) throws Exception {
        AFileModel file = JtBridge.fileGet(path);

        return ExecutorFactory.execOnly(file, ctx);
    }

    public static void exec(String path, Context ctx) throws Exception {
        AFileModel file = JtBridge.fileGet(path);

        ExecutorFactory.execOnly( file, ctx);
    }

    public static void exec(String code) throws Exception {
        AFileModel file = new AFileModel();
        file.path = EncryptUtils.md5(code);
        file.content = code;
        file.edit_mode = "javascript";

        exec(file);
    }

    public static void exec(AFileModel file) throws Exception {
        initFuture.get();

        Context ctx = ContextUtil.current();

        if (ctx == null) {
            ctx = ContextEmpty.create();

            ContextUtil.currentSet(ctx);
            ExecutorFactory.execOnly(file, ctx);
            ContextUtil.currentRemove();
        } else {
            ExecutorFactory.execOnly(file, ctx);
        }
    }

    public static void xfunInit(){
        CallUtil.callLabel(null, Config.faas_hook_start, false, Collections.EMPTY_MAP);

        //再等0.5秒 //执行引擎需要加载东西
        try {
            Thread.sleep(500);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        initFuture.complete(1);
    }
}
