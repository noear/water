package webapp.model.water;

import lombok.Data;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Data
public class ReportModel implements IBinder {

    /** 查询ID */
    public int row_id;
    /** 分类标签（外部根据标签查询） */
    public String tag;
    /** 查询名称 */
    public String name;
    /** 查询代码 */
    public String code;
    public String note;
    public String args;
    /**  */
    public Date create_fulltime;

    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        code = s.get("code").value(null);
        note = s.get("note").value(null);
        args = s.get("args").value(null);
        create_fulltime = s.get("create_fulltime").value(null);
    }

    public IBinder clone() {
        return new ReportModel();
    }

}