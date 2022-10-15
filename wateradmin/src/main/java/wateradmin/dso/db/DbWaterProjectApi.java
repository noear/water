package wateradmin.dso.db;

import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_ops.ProjectModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterProjectApi {
    private static DbContext db() {
        return Config.water;
    }

    //获取项目标签
    public static List<TagCountsModel> getProjectTags() throws SQLException {
        return db().table("water_ops_project")
                .groupBy("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    public static List<ProjectModel> getProjectByTagName(String tag, Integer is_enabled) throws SQLException {
        return db().table("water_ops_project")
                .where("tag = ?", tag)
                .and("is_enabled = ?", is_enabled)
                .orderBy("`name` ASC")
                .selectList("*", ProjectModel.class);
    }


    //根据id获取project信息。
    public static ProjectModel getProjectByID(int project_id) throws SQLException {
        return db().table("water_ops_project")
                .where("project_id = ?", project_id)
                .selectItem("*", ProjectModel.class);
    }

    public static boolean addProject(String tag, String name, String git_url, String git_user, String git_password, String git_ssh,
                                     String host_plan, String service_name, String port_plan, int type) throws SQLException {
        return db().table("water_ops_project")
                .set("tag", tag)
                .set("name", name)
                .set("git_url", git_url)
                .set("git_user", git_user)
                .set("git_password", git_password)
                .set("git_ssh", git_ssh)
                .set("host_plan", host_plan)
                .set("service_name", service_name)
                .set("port_plan", port_plan)
                .set("type", type)
                .insert() > 0;
    }

    //修改project信息。
    public static long updateProject(Integer project_id, String tag, String name, String note, String git_url, int type, String developer) throws SQLException {
        DbTableQuery qr = db().table("water_ops_project")
                .set("tag", tag)
                .set("name", name)
                .set("note", note)
                .set("type", type)
                .set("git_url", git_url)
                .set("developer", developer);

        if (project_id > 0) {
            return qr.where("project_id = ?", project_id).update();
        } else {
            return qr.insert();
        }
    }

    public static boolean updateProjectStatus(Integer project_id, Integer is_enabled) throws SQLException {
        return db().table("water_ops_project")
                .where("project_id = ?", project_id)
                .set("is_enabled", is_enabled)
                .update() > 0;
    }
}
