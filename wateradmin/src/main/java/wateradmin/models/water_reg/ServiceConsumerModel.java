package wateradmin.models.water_reg;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.*;
import java.util.*;

@Getter
@Setter
public class ServiceConsumerModel implements IBinder {

    /**
     *
     */
    public long row_id;
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
     *最后检查状态（0：OK；1：error）
     * */
    public int chk_last_state;
    /**
     *最后检查时间
     */
    public Date chk_fulltime;
    /**
     *
     */
    public Date log_fulltime;

    /**
     * 是否启用
     * */
    public int is_enabled;

    public long traffic_num;
    public double traffic_per;

    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").longValue(0L);
        service = s.get("service").value("");
        consumer = s.get("consumer").value("");
        consumer_address = s.get("consumer_address").value("");
        consumer_ip = s.get("consumer_ip").value("");
        chk_last_state = s.get("chk_last_state").value(0);
        chk_fulltime = s.get("chk_fulltime").value(null);
        log_fulltime = s.get("log_fulltime").value(null);
        is_enabled = s.get("is_enabled").value(0);
    }

    public IBinder clone() {
        return new ServiceConsumerModel();
    }
}