package wateradmin.models.water_paas;

import lombok.Getter;
import org.noear.water.track.TrackNames;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timespan;

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

    public String use_whitelist;

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

    public String timespan() {
        long timespan = plan_last_timespan;
        if (timespan < 1000) {
            return timespan + "ms";
        }
        timespan = timespan / 1000;

        if (timespan < 60) {
            return timespan + "s";
        }

        timespan = timespan / 60;

        return timespan + "m";

    }

    public String timexpre() {
        if (plan_begin_time == null || TextUtils.isEmpty(plan_interval)) {
            return "-";
        } else {
            if (plan_interval.length() > 7 && plan_interval.contains(" ")) {
                return plan_interval;
            } else {
                return Datetime.format(plan_begin_time, "MM.dd HH:mm") + " ^" +plan_interval;
            }
        }
    }

    public boolean staticize() {
        return is_staticize;
    }

    public boolean editable() {
        return is_editable;
    }

    public boolean disabled() {
        return is_disabled;
    }

    public String pathMd5() {
        return "%7Bmd5%7D" + EncryptUtils.md5(path);
    }

    public String whitelistLabel() {
        if (TextUtils.isEmpty(use_whitelist)) {
            return "";
        } else {
            return "#" + use_whitelist;
        }
    }
}
