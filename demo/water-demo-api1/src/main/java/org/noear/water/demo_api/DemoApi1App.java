package org.noear.water.demo_api;

import org.noear.fairy.Fairy;
import org.noear.fairy.decoder.SnackDecoder;
import org.noear.solon.Solon;

public class DemoApi1App {
    public static void main(String[] args) {
        Fairy.defaultDecoder = SnackDecoder.instance;

        Solon.start(DemoApi1App.class, args);
    }
}
