package org.slf4j.impl;

import org.noear.water.log.Level;

import java.util.function.Supplier;

public class WaterLogEvent  {
    private String name;
    private Level level;
    private Supplier<String> msgSupplier;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    public Supplier<String> getMsgSupplier() {
        return msgSupplier;
    }
    public void setMsgSupplier(Supplier<String> msgSupplier) {
        this.msgSupplier = msgSupplier;
    }

    @Override
    public String toString() {
        return msgSupplier.get();
    }
}
