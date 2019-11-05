package webapp.dso.db;


import org.noear.weed.DbContext;
import webapp.Config;
import webapp.models.water.*;
import webapp.models.water_paas.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Yunlong.Feng
 * @Description:
 */
public class DbPaaSQueryApi {
    private static DbContext db() {
        return Config.water;
    }

    //代码片段查询
    public static List<CodeQueryModel>  codeQuery(int code_type,String code) throws SQLException{
        List<CodeQueryModel> list = new ArrayList<>();
        if (code_type == 0) {
            //接口代码
            return queryPaasApi(code);

        } else if (code_type == 1) {
            //计划任务代码
            return queryPaasPlan(code);

        } else if (code_type == 2) {
            //同步任务代码
            return queryPaasEtl(code);

        } else if (code_type == 3){
            //公共函数代码
            return queryPaasFun(code);

        } else if (code_type == 4) {
            // 公共模版代码
            return queryPaasTml(code);
        }
        return list;
    }

    public static List<CodeQueryModel> queryPaasApi(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<PaasApiModel> paas_api = db().table("paas_api")
                .where("code like ?", "%" + code + "%")
                .select("*")
                .getList(new PaasApiModel());
        for (PaasApiModel m:paas_api) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 0;
            query.name = m.api_name;
            query.tag = m.tag;
            query.code = m.code;
            query.id = m.api_id;
            query.note = m.note;
            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryPaasPlan(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<PaasPlanModel> paas_plan = db().table("paas_plan")
                .where("code like ?", "%" + code + "%")
                .select("*")
                .getList(new PaasPlanModel());

        for (PaasPlanModel m:paas_plan) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 1;
            query.name = m.plan_name;
            query.tag = m.tag;
            query.code = m.code;
            query.id = m.plan_id;

            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryPaasEtl(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<PaasEtlModel> paas_etl = db().table("paas_etl")
                .where("code like ?", "%" + code + "%")
                .select("*")
                .getList(new PaasEtlModel());
        for (PaasEtlModel m:paas_etl) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 2;
            query.name = m.etl_name;
            query.tag = m.tag;
            query.code = m.code;
            query.id = m.etl_id;
            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryPaasFun(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<PaasFunModel> paas_etl = db().table("paas_fun")
                .where("code like ?", "%" + code + "%")
                .select("*")
                .getList(new PaasFunModel());
        for (PaasFunModel m:paas_etl) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 3;
            query.name = m.fun_name;
            query.tag = m.tag;
            query.code = m.code;
            query.id = m.fun_id;
            query.note = m.note;
            result.add(query);
        }
        return result;
    }

    public static List<CodeQueryModel> queryPaasTml(String code) throws SQLException{
        List<CodeQueryModel> result = new ArrayList<>();
        List<PaasTmlModel> paas_etl = db().table("paas_tml")
                .where("code like ?", "%" + code + "%")
                .select("*")
                .getList(new PaasTmlModel());
        for (PaasTmlModel m:paas_etl) {
            CodeQueryModel query = new CodeQueryModel();
            query.code_type = 4;
            query.name = m.tml_name;
            query.tag = m.tag;
            query.code = m.code;
            query.id = m.tml_id;
            query.note = m.name_display;
            result.add(query);
        }
        return result;
    }

    //代码查询，显示代码
    public static String queryCode(int code_type,int id) throws SQLException{
        String code = "";
        if (code_type == 0) {
            code = db().table("paas_api")
                    .where("api_id = ?", id)
                    .select("code")
                    .getValue("");
        } else if (code_type == 1) {
            code = db().table("paas_plan")
                    .where("plan_id = ?", id)
                    .select("code")
                    .getValue("");
        } else if (code_type == 2) {
            code = db().table("paas_etl")
                    .where("etl_id = ?", id)
                    .select("code")
                    .getValue("");
        } else if (code_type == 3) {
            code = db().table("paas_fun")
                    .where("fun_id = ?", id)
                    .select("code")
                    .getValue("");
        } else if (code_type == 4) {
            code = db().table("paas_tml")
                    .where("tml_id = ?", id)
                    .select("code")
                    .getValue("");
        }
        return code;
    }

}