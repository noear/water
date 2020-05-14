package org.noear.water.demo_api;

import org.noear.solon.annotation.XBean;
import org.noear.solon.annotation.XConfiguration;
import org.noear.water.annotation.Water;
import org.noear.weed.DbContext;

@XConfiguration
public class Config {
    @Water("water/water_bcf")
    DbContext bcfDb;

    @XBean("water_bcf")
    public DbContext water_bcf(){
        return bcfDb;
    }
}
