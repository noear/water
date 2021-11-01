package wateradmin.models.water_cfg;

import lombok.Getter;
import org.noear.water.protocol.model.message.BrokerMeta;
import org.noear.water.utils.EncryptUtils;

@Getter
public class BrokerModel implements BrokerMeta {
    public int broker_id;
    public String tag;
    public String broker;
    public String source;
    public int keep_days;
    public long row_num;
    public long row_num_today;
    public long row_num_today_error;
    public long row_num_yesterday;
    public long row_num_yesterday_error;
    public long row_num_beforeday;
    public long row_num_beforeday_error;
    public String note;
    public long counts;
    public int is_enabled;
    public int is_alarm;

    public boolean isHighlight() {
        return (row_num_today_error > 0);
    }

    @Override
    public int getKeepDays() {
        return keep_days;
    }

    public String broker_md5() {
        return "%7Bmd5%7D" + EncryptUtils.md5(broker);
    }
}