package webapp.models.water;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.*;
import java.util.*;

@Getter
@Setter
public class ServiceConsumerModel  {

    /**
     *
     */
    public int id;
    /**
     *服务
     */
    public String service;
    /**
     *消费者
     */
    public String consumer;
    /**
     *消费者地址
     * */
    public String consumer_address;
    /**
     *消费者远程ＩＰ
     */
    public String consumer_ip;
    /**
     *最后检查时间
     */
    public Date chk_fulltime;
    /**
     *
     */
    public Date log_fulltime;

    public long traffic_num;
    public double traffic_per;


}