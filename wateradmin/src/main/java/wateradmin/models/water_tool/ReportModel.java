package wateradmin.models.water_tool;

import lombok.Data;
import org.noear.weed.*;
import java.util.*;

@Data
public class ReportModel implements IBinder {

    /**
     * 查询ID
     */
    public int row_id;
    /**
     * 分类标签（外部根据标签查询）
     */
    public String tag;
    /**
     * 查询名称
     */
    public String name;
    /**
     * 查询代码
     */
    public String code;
    public String note;
    public String args;

    public Date gmt_modified;

    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        code = s.get("code").value(null);
        note = s.get("note").value(null);
        args = s.get("args").value(null);

        gmt_modified = s.get("gmt_modified").dateValue(null);
        if (gmt_modified == null) {
            gmt_modified = new Date();
        }
    }

    public IBinder clone() {
        return new ReportModel();
    }

}