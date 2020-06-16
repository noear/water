package waterapp.models;

import waterapp.models.water_msg.MessageModel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by noear on 2017/7/18.
 */
public final class StateTag {
    public MessageModel msg;

    public int total = 0;//总数
    public int count = 0;//计数
    public int value = 0;//有效计数
}
