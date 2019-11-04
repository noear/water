package org.noear.water.client.model;

/**
 * 接收方式
 * */
public enum ReceiveWay {
    //0:http异步等待
    HTTP_WAIT(0),
    HTTP_DONT_WAIT(2);
    //2:http异步不等待

    public int code;
    ReceiveWay(int code){
        this.code = code;
    }
}
