package org.noear.water.protocol;

public interface ILogSourceFactory {
    ILogSource getSource(String logger);
}
