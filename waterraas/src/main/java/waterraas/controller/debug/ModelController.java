package waterraas.controller.debug;

import org.noear.rubber.Rubber;
import org.noear.rubber.RubberException;
import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import org.noear.water.utils.Timecount;
import waterraas.controller.SystemCode;

public class ModelController implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String model = context.param("model");
        String field = context.param("field");
        String args_str = context.param("args");

        ONode data = new ONode();

        if (TextUtils.isEmpty(model) || TextUtils.isEmpty(args_str)) {
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        if ("1".equals(context.param("debug"))) {
            Rubber.updateCache("model:"+model);
        }

        ONode args = ONode.load(args_str);


        data.set("code",1).set("msg", SystemCode.code_1);
        ONode jReq = data.get("request");

        jReq.set("model",model);
        if(TextUtils.isEmpty(field)==false){
            jReq.set("field",field);
        }
        jReq.set("args",args);

        ONode jResp = data.get("response");


        try {
            Timecount timecount = new Timecount().start();
            ONode temp = Rubber.model(model, args, field, true);
            long ts = timecount.stop().milliseconds();

            jResp.set("model", temp);
            jResp.set("ts", ts);

        }
        catch (RubberException ex){
            data.set("code",11).set("msg", SystemCode.code_11(ex.getMessage()));
        }
        catch (Exception ex){

            data.set("code",0).set("msg",SystemCode.code_0);
            jResp.set("hint", ThrowableUtils.getString(ex));
        }

        context.output(data.toJson());
        return ;
    }
}
