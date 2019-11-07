package org.noear.water.admin.plugin_aliyun.dso.db;

import org.noear.water.admin.plugin_aliyun.Config;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;

import java.sql.SQLException;
import java.util.List;

public class DbWaterApi {
    public static DbContext db(){
        return Config.db;
    }

    public static List<ConfigModel> getConfigByType(String tag,int type) throws SQLException {
        return db().table("water_base_config")
                .where("type = ?", type)
                .expre((tb)->{
                    if(TextUtils.isEmpty(tag) == false){
                        tb.and("tag = ?", tag);
                    }
                })
                .select("*")
                .getList(new ConfigModel());
    }



    public static ConfigModel getConfigByTagName(String tagName) throws SQLException {
        if(TextUtils.isEmpty(tagName)){
            return new ConfigModel();
        }

        String[] ss = tagName.split("/");

        return getConfigByTagName(ss[0], ss[1]);
    }

    public static ConfigModel getConfigByTagName(String tag, String name) throws SQLException {
        return db().table("water_base_config")
                .where("tag = ?", tag)
                .and("`key` = ?", name)
                .limit(1)
                .select("*")
                .getItem(new ConfigModel());
    }

    public static List<ConfigModel> getConfigByTag(String tag, String key) throws SQLException {
        return db().table("water_base_config")
                .where("tag = ?", tag)
                .expre(tb -> {
                    if (!TextUtils.isEmpty(key)) {
                        tb.and("`key` like ?", "%" + key + "%");
                    }
                })
                .select("*")
                .getList(new ConfigModel());
    }

}
