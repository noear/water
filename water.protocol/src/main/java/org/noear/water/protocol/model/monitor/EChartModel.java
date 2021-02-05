package org.noear.water.protocol.model.monitor;

import java.util.List;

public class EChartModel {

    public String label;

    public String name;

    public List<String> value;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
