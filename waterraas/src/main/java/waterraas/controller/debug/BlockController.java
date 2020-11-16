package waterraas.controller.debug;

import org.noear.rubber.Rubber;
import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timecount;
import waterraas.controller.SystemCode;
import waterraas.dao.LogUtil;

public class BlockController implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String block = context.param("block");
        if(block == null) {
            block = context.path().substring(3); //通过路径传入
        }

        String args_str = context.param("args");

        if (TextUtils.isEmpty(block) || TextUtils.isEmpty(args_str)) {
            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        if ("1".equals(context.param("debug"))) {
            Rubber.updateCache("block:"+block);
        }

        ONode args =  ONode.load(args_str);

        try{
            run(context,block, args);
        }catch (Exception ex){
            ONode data = new ONode();
            data.set("code", 0).set("msg", ex.getMessage());
            LogUtil.logBlockError(block, args.toJson(), ex);
        }

    }

    private void run(Context context, String block, ONode args) throws Exception{
        ONode data = new ONode();

        data.set("code",1).set("msg",SystemCode.code_1);
        ONode jReq = data.get("request");

        jReq.set("block",block);
        jReq.set("args",args);

        ONode jResp = data.get("response").asObject();

        try {
            Timecount timecount = new Timecount().start();
            ONode temp = Rubber.block(block, args);
            long ts = timecount.stop().milliseconds();

            jResp.set("return", temp);
            jResp.set("ts",ts);
        }
        catch (Exception ex) {
            data.set("code", 0).set("msg", SystemCode.code_0);
            jResp.set("hint",ex.getMessage());
            LogUtil.logBlockError(block, args.toJson(), ex);
        }

        context.output(data.toJson());
    }
}
