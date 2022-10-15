package wateradmin.models.water_ops;

import lombok.Getter;
import org.noear.wood.GetHandlerEx;
import org.noear.wood.IBinder;

/// <summary>
/// 生成:2017/12/28 11:55:12
/// 
/// </summary>
@Getter
public class ProjectModel implements IBinder {
    public int project_id;
    public String tag;
    public String name;
    public int type;
    public String note;
    public String developer;
    public String git_url;
    public String git_user;
    public String git_password;
    public String git_ssh;
    public String host_plan;
    public String service_name;
    public String port_plan;
    public int is_enabled;
    public long counts;

    public void bind(GetHandlerEx s) {
        //1.source:数据源
        //
        project_id = s.get("project_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        type = s.get("type").value(0);
        note = s.get("note").value(null);
        developer = s.get("developer").value(null);
        git_url = s.get("git_url").value(null);
        git_user = s.get("git_user").value(null);
        git_password = s.get("git_password").value(null);
        git_ssh = s.get("git_ssh").value(null);
        host_plan = s.get("host_plan").value(null);
        service_name = s.get("service_name").value(null);
        port_plan = s.get("port_plan").value(null);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0L);
    }

    public IBinder clone() {
        return new ProjectModel();
    }
}