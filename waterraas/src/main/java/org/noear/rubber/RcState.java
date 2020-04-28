package org.noear.rubber;

public class RcState {
    public long timespan; //处理时间
    public boolean is_match; //是否匹配?
    public boolean is_error; //是否出错?

    private long _time_start;
    private long _time_end;

    public RcState start() {
        _time_start = System.currentTimeMillis();

        return this;
    }

    public RcState stop(boolean is_match, boolean is_error) {
        _time_end = System.currentTimeMillis();
        this.timespan = _time_end - _time_start;
        this.is_match = is_match;
        this.is_error = is_error;

        return this;
    }
}
