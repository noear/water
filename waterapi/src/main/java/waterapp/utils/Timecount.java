package waterapp.utils;

import java.util.Date;

public class Timecount {
    private Date start_time;

    public Timecount start() {
        start_time = new Date();
        return this;
    }

    public Timespan stop() {
        return new Timespan(new Date(), start_time);
    }

    public String stop(long ref_second) {
        double temp = (stop().milliseconds() / 10) / 100.00d;
        if (temp > ref_second) {
            return temp + "s******慢!!!";
        } else {
            return temp + "s";
        }
    }
}
