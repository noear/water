package watersev.controller;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.water.WaterClient;
import org.noear.water.utils.LocalUtils;
import org.noear.solon.extend.schedule.IJob;

import watersev.Config;

//check in
//
@Component
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
        if (_args == null) {
            _args = ONode.loadObj(Solon.cfg().argx());
        }

        boolean is_unstable = Solon.cfg().isDriftMode();

        try {
            WaterClient.Registry.register(Solon.cfg().appGroup(), Config.water_service_name, LocalUtils.getLocalIp(), _args.toJson(), "", 1, "", is_unstable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
