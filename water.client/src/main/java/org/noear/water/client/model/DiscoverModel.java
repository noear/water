package org.noear.water.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现模型
 * */
public class DiscoverModel {
    //策略
    public String policy;
    //有域网址
    public String url;
    //服务列表
    public final List<TargetModel> list = new ArrayList<>();

    public void add(String protocol, String address, int w) {
        list.add(new TargetModel(protocol, address, 1));
    }


    /**
     * 目标模型
     * */
    public static class TargetModel {
        /** 地址 */
        public final String address;
        /** 权重 */
        public final int weight;
        /** 协议 */
        public final String protocol;

        public TargetModel(String protocol, String address, int weight) {
            this.address = address;
            this.weight = weight;
            this.protocol = protocol;
        }
    }
}
