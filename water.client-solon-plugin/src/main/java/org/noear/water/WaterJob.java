package org.noear.water;

import org.noear.water.integration.solon.WaterUpstream;

/**
 * @author noear 2021/5/26 created
 */
public class WaterJob {
    public static String call(String service, String name) throws Exception {
        return WaterUpstream.get(service).http(WW.path_run_job).data("name", name).post();
    }
}