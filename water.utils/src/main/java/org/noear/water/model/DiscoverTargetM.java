package org.noear.water.model;

public class DiscoverTargetM {
    public final String protocol;
    public final String address;
    public final String meta;
    public final int weight;

    public DiscoverTargetM(String protocol, String address, String meta, int weight) {
        this.protocol = protocol;
        this.address = address;
        this.meta = meta;
        this.weight = weight;
    }
}
