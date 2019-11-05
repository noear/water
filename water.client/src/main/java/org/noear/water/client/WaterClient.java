package org.noear.water.client;

import org.noear.water.client.dso.*;

public class WaterClient {
    public final static ConfigurationApi configuration = new ConfigurationApi();
    public final static LogApi log = new LogApi();
    public final static MessageApi message = new MessageApi();
    public final static RegistryApi registry = new RegistryApi();

    public final static TrackApi track = new TrackApi();
}
