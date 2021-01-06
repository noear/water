package testapi2.controller;

import org.noear.water.annotation.WaterConfig;
import org.noear.water.dso.ConfigHandler;
import org.noear.water.model.ConfigSetM;

/**
 * @author noear 2021/1/6 created
 */
@WaterConfig("water")
public class TestConfigHandler implements ConfigHandler {
    @Override
    public void handler(ConfigSetM cfgSet) {

    }
}
