package wateradmin.models.water_cfg;

import lombok.Getter;

@Getter
public class WhitelistModel {
    public transient int row_id;
    public String tag;
    public String type;
    public String value;
    public String note;
    public int is_enabled;
}