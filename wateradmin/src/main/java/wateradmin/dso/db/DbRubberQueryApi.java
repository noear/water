package wateradmin.dso.db;

import org.noear.weed.DbContext;
import wateradmin.Config;
import wateradmin.models.water.CodeQueryModel;
import wateradmin.models.water_paas.RebberBlockModel;
import wateradmin.models.water_paas.RebberModelFieldModel;
import wateradmin.models.water_paas.RebberModelModel;
import wateradmin.models.water_paas.RebberSchemeModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbRubberQueryApi {
    private static DbContext db() {
        return Config.water_paas;
    }

    //代码片段查询
    public static List<CodeQueryModel>  codeQuery(int code_type, String code) throws SQLException{
        List<CodeQueryModel> list = new ArrayList<>();
        if (code_type == 0) {
            //模型构造代码
            return queryModelInit(code);
        } else if (code_type == 1) {
            //字段动态代码
            return queryModelField(code);
        } else if (code_type == 2) {
            //计算事件代码
            return querySchemeEvent(code);
        }else if (code_type == 3) {
            //计算事件代码
            return queryBlockScan(code);
        }
        return list;
    }

    public static List<CodeQueryModel> queryModelInit(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<RebberModelModel> list = db().table("rubber_model")
                .where("init_expr like ?", "%" + code + "%")
                .limit(20)
                .select("*")
                .getList(new RebberModelModel());
        for (RebberModelModel m:list) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 0;
            query.name = m.name;
            query.tag = m.tag;
            query.code = m.init_expr;
            query.id = m.model_id;
            query.note = m.name_display;
            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryModelField(String code) throws SQLException {
        List<CodeQueryModel> result = new ArrayList<>();
        List<RebberModelFieldModel> list = db().table("rubber_model_field f")
                .innerJoin("rubber_model m").on("f.model_id = m.model_id")
                .where("f.expr like ?", "%" + code + "%")
                .limit(20)
                .select("f.*,m.tag")
                .getList(new RebberModelFieldModel());

        for (RebberModelFieldModel m : list) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 1;
            query.name = "m:" + m.model_id + " / " + m.name;
            query.tag = m.tag;
            query.code = m.expr;
            query.id = m.field_id;
            query.pid = m.model_id;
            query.note = m.name_display;

            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> querySchemeEvent(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<RebberSchemeModel> list = db().table("rubber_scheme")
                .where("event like ?", "%" + code + "%")
                .limit(20)
                .select("*")
                .getList(new RebberSchemeModel());
        for (RebberSchemeModel m:list) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 2;
            query.name = m.name;
            query.tag = m.tag;
            query.code = m.event;
            query.id = m.scheme_id;
            query.note = m.name_display;
            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryBlockScan(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<RebberBlockModel> list = db().table("rubber_block")
                .where("app_expr like ?", "%" + code + "%")
                .limit(20)
                .select("*")
                .getList(new RebberBlockModel());
        for (RebberBlockModel m:list) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 3;
            query.name = m.name;
            query.tag = m.tag;
            query.code = m.app_expr;
            query.id = m.block_id;
            query.note = m.name_display;
            result.add(query);
        }
        return result;
    }


    //代码查询，显示代码
    public static String queryCode(int code_type,int id) throws SQLException{
        String code = "";
        if (code_type == 0) {
            code = db().table("rubber_model")
                    .where("model_id = ?", id)
                    .select("init_expr")
                    .getValue("");
        } else if (code_type == 1) {
            code = db().table("rubber_model_field")
                    .where("field_id = ?", id)
                    .select("expr")
                    .getValue("");
        } else if (code_type == 2) {
            code = db().table("rubber_scheme")
                    .where("scheme_id = ?", id)
                    .select("event")
                    .getValue("");
        }else if (code_type == 3) {
            code = db().table("rubber_block")
                    .where("block_id = ?", id)
                    .select("app_expr")
                    .getValue("");
        }
        return code;
    }

}