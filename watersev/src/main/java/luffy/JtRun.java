package luffy;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.luffy.dso.CallUtil;
import org.noear.luffy.dso.JtBridge;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.utils.EncryptUtils;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public static void initAwait() throws InterruptedException, ExecutionException {
        initFuture.get();
    }

    public static Object call(String path, Context ctx) throws Exception {
        AFileModel file = JtBridge.fileGet(path);

        return ExecutorFactory.execOnly(file, ctx);
    }

    public static void exec(String path, Context ctx) throws Exception {
        AFileModel file = JtBridge.fileGet(path);

        ExecutorFactory.execOnly(file, ctx);
    }

    public static Object exec(String code) throws Exception {
        AFileModel file = new AFileModel();
        file.path = EncryptUtils.md5(code);
        file.content = code;
        file.edit_mode = "javascript";

        return exec(file);
    }

    public static Object exec(AFileModel file) throws Exception {
        initFuture.get();

        Context ctx = ContextUtil.current();
        Object rst = null;

        if (ctx == null) {
            ctx = ContextEmpty.create();

            ContextUtil.currentSet(ctx);
            try {
                rst = ExecutorFactory.execOnly(file, ctx);
            } finally {
                ContextUtil.currentRemove();
            }
        } else {
            rst = ExecutorFactory.execOnly(file, ctx);
        }

        return rst;
    }

    public static void xfunInit(){
        CallUtil.callLabel(null, "hook.start", false, Collections.EMPTY_MAP);

        //再等0.5秒 //执行引擎需要加载东西
        try {
            Thread.sleep(500);
        }catch (InterruptedException ex){

        }

        initFuture.complete(1);
    }
}
