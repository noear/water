package org.noear.water.model;

import java.util.ArrayList;
import java.util.List;

public class DiscoverM {
    //策略
    public String policy;
    //有域网址
    public String url;
    //服务列表
    public final List<DiscoverTargetM> list = new ArrayList<>();

    public void add(String protocol, String address, String meta, int w) {
        list.add(new DiscoverTargetM(protocol, address, meta,1));
    }
}
