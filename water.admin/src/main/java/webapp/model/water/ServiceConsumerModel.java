package webapp.model.water;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
@Setter
public class ServiceConsumerModel implements IBinder {

    /**
     *
     */
    public int row_id;
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

    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").value(0);
        service = s.get("service").value("");
        consumer = s.get("consumer").value("");
        consumer_address = s.get("consumer_address").value("");
        consumer_ip = s.get("consumer_ip").value("");
        chk_fulltime = s.get("chk_fulltime").value(null);
        log_fulltime = s.get("log_fulltime").value(null);
    }

    public IBinder clone() {
        return new ServiceConsumerModel();
    }
}