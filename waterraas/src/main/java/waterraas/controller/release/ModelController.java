package waterraas.controller.release;

import org.noear.rubber.Rubber;
import org.noear.rubber.RubberException;
import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timecount;
import waterraas.Config;
import waterraas.controller.SystemCode;
import waterraas.dao.LogUtil;

public class ModelController implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String model = context.param("model");
        if(model == null) {
            model = context.path().substring(3); //通过路径传入
        }

        String args_str = context.param("args");

        if (TextUtils.isEmpty(model) || TextUtils.isEmpty(args_str)) {
            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        try{
            Timecount timecount = new Timecount().start();

            run(context,model,args_str);

            long timespan = timecount.stop().milliseconds();
            WaterClient.Track.track(Config.water_service_name, "model", model, timespan, WaterClient.localServiceHost());
        }catch (Exception ex){
            ONode data = new ONode();
            data.set("code", 0).set("msg", ex.getMessage());
            LogUtil.logModelError(model, args_str, ex);
        }

    }

    private void run(Context context, String model,String args_str) throws Exception{
        ONode data = new ONode();

        ONode args = ONode.load(args_str);


        data.set("code",1).set("msg",SystemCode.code_1);
        ONode jReq = data.get("request");

        jReq.set("model",model);
        jReq.set("args",args);

        ONode jResp = data.get("response").asObject();


        try {
            ONode temp = Rubber.model(model,args, null, false);

            if(temp.count()>0){
                for(String key : temp.obj().keySet()){
                    data.set("response", temp.get(key));
                    break;
                }
            }

        }
        catch (RubberException ex){
            data.set("code",11).set("msg", SystemCode.code_11(ex.getMessage()));
        }
        catch (Exception ex) {

            data.set("code", 0).set("msg", ex.getMessage());

            LogUtil.logModelError(model, args_str, ex);
        }

        context.output(data.toJson());
    }
}
