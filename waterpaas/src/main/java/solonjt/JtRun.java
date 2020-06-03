package solonjt;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XContextEmpty;
import org.noear.solon.core.XContextUtil;
import org.noear.solonjt.dso.CallUtil;
import org.noear.solonjt.dso.JtBridge;
import org.noear.solonjt.dso.JtFun;
import org.noear.solonjt.executor.ExecutorFactory;
import org.noear.solonjt.model.AFileModel;
import org.noear.water.utils.EncryptUtils;
import waterapp.Config;
import waterapp.dso.DbApi;

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

    public static Object call(String path, XContext ctx) throws Exception {
        AFileModel file = JtBridge.fileGet(path);

        return ExecutorFactory.execOnly(file, ctx);
    }

    public static void exec(String path, XContext ctx) throws Exception {
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

        XContext ctx = XContextEmpty.create();

        XContextUtil.currentSet(ctx);
        ExecutorFactory.execOnly(file, ctx);
        XContextUtil.currentRemove();
    }

    public static void xfunInit(){
        JtFun.g.set("afile_get_paths", (map) -> {
            String tag = (String) map.get("tag");
            String label = (String) map.get("label");
            Boolean useCache = (Boolean) map.get("useCache");
            return DbApi.fileGetPaths(tag, label, useCache);
        });

        JtFun.g.set("afile_get", (map) -> {
            String path = (String) map.get("path");
            return DbApi.fileGet(path);
        });

        CallUtil.callLabel(null, Config.faas_hook_start, false, Collections.EMPTY_MAP);

        initFuture.complete(1);
    }
}
