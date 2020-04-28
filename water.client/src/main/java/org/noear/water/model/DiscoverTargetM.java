package org.noear.water.model;

public class DiscoverTargetM {
    public final String address;
    public final int weight;
    public final String protocol;

    public DiscoverTargetM(String protocol, String address, int weight) {
        this.address = address;
        this.weight = weight;
        this.protocol = protocol;
    }
}
