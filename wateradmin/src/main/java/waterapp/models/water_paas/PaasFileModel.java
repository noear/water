package waterapp.models.water_paas;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
public class PaasFileModel {
    public transient int file_id;
    public int file_type;
    public String tag;
    public String label;
    public String note;
    public String path;
    public int rank;
    public boolean is_staticize;
    public boolean is_editable;
    public boolean is_disabled;
    public String link_to;
    public String edit_mode;
    public String content_type;
    public String content;
    public int plan_state;
    public Date plan_begin_time;
    public Date plan_last_time;
    public long plan_last_timespan;
    public String plan_interval;
    public int plan_max;
    public int plan_count;
    public Date create_fulltime;
    public Date update_fulltime;
    public transient boolean _is_day_task;

    public String alarm_sign;
    public String alarm_mobile;

    public String typeStr() {
        if (file_type == 1) {
            return "pln";
        } else if (file_type == 2) {
            return "tml";
        } else {
            return "api";
        }
    }
}
