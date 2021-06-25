package org.noear.water.model;

public class JobM {
    public String name;
    public String cron7x;
    public String description;

    //用于序列化
    public JobM() {

    }

    public JobM(String name, String cron7x, String description) {
        this.name = name;
        this.cron7x = cron7x;
        this.description = description;
    }
}
