package org.noear.water.protocol.model.message;

/**
 * state -2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数
 *
 * @author noear 2021/2/4 created
 */
public enum MessageState {
    /**
     * 无派发对象
     * */
    notarget(-2),
    /**
     * 忽略
     * */
    ignore(-1),
    /**
     * 未处理
     * */
    undefined(0),
    /**
     * 处理中
     * */
    processed(1),
    /**
     * 已完成
     * */
    completed(2),
    /**
     * 派发超次数
     * */
    excessive(3),
    ;
    public final int code;

    MessageState(int code) {
        this.code = code;
    }
}
