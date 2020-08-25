package watersev.controller;

import org.noear.snack.ONode;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XBean;
import org.noear.water.WaterClient;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.LocalUtils;
import org.noear.solon.extend.schedule.IJob;

import watersev.Config;

//check in
//
@XBean
public class ChkController implements IJob {
    private ONode _args;

    @Override
    public String getName() {
        return "chk";
    }

    @Override
    public int getInterval() {
        return 1000 * 5;
    }

    @Override
    public void exec() {
        Datetime time = Datetime.Now();
        int hours = time.getHours();

        if (hours > 1 && hours < 6) { //半夜不做事
            return;
        }

        if (_args == null) {
            _args = ONode.loadObj(XApp.cfg().argx());
        }

        boolean is_unstable = XApp.cfg().isDriftMode();

        try {
            WaterClient.Registry.register(Config.water_service_name, LocalUtils.getLocalIp(), _args.toJson(), "", 1, "", is_unstable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
