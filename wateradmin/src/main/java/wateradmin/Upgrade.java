package wateradmin;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.Datetime;
import org.noear.wood.DataItem;
import org.noear.wood.DbContext;
import wateradmin.dso.db.DbLuffyApi;
import wateradmin.models.water_paas.LuffyFileModel;
import wateradmin.models.water_paas.LuffyFileType;


/**
 * @author noear 2022/3/19 created
 */
@Slf4j
public class Upgrade {
    public static void tryUpdate() {
        //for v1:2.4.8
        try {
            luffy_file_update("/sdk_water/_init.jsx", "upgrade/api_water_init.jsd");
        } catch (Throwable e) {
            TagsMDC.tag0("upgrade");
            log.error("Upgrade attempt failed: {}", e);
        }

        try {
            pln_water_upgrade();
        } catch (Throwable e) {
            TagsMDC.tag0("upgrade");
            log.error("Upgrade attempt failed: {}", e);
        }

        //for v2:2.5.0
        try {
            String sql = Utils.getResourceAsString("upgrade/water.sql");
            tryInitSchemaBySplitSql(Config.water, sql);
        } catch (Throwable e) {
            TagsMDC.tag0("upgrade");
            log.error("Upgrade water schema failed: {}", e);
        }

        //for v2.10.0
        try{
            luffy_file_update("/water/speed_sync", "upgrade/pln_water_speed_sync.jsd");
            luffy_file_update("/water/speed_sync_date", "upgrade/pln_water_speed_sync_date.jsd");
            luffy_file_update("/water/speed_sync_hour", "upgrade/pln_water_speed_sync_hour.jsd");

            luffy_file_update("/water/log_stat_sync", "upgrade/pln_water_log_stat_sync.jsd");
        }catch (Throwable e){
            TagsMDC.tag0("upgrade");
            log.error("Upgrade water schema failed: {}", e);
        }

        try {
            ConfigM lang_type = WaterClient.Config.get("_system", "lang_type");
            if (lang_type == null || Utils.isEmpty(lang_type.value)) {
                String txt = Utils.getResourceAsString("upgrade/lang_type.txt");
                if (Utils.isNotEmpty(txt)) {
                    WaterClient.Config.set("_system", "lang_type", txt);
                }
            }
        } catch (Throwable e) {

        }
    }

    private static void luffy_file_update(String oldFile, String newFile) throws Throwable {
        String waterInitNew = Utils.getResourceAsString(newFile);
        String waterInitNewMd5 = Utils.md5(waterInitNew);

        LuffyFileModel file = DbLuffyApi.getFileByPath(oldFile);
        if (file.file_id > 0) {
            String fileContentMd5 = Utils.md5(file.content);

            if (waterInitNewMd5.equals(fileContentMd5) == false) {
                DbLuffyApi.setFileContent(file.file_id, waterInitNew);
            }
        }
    }

    private static void pln_water_upgrade() throws Throwable {
        //for "pln::/water/_upgrade"
        String waterUpgradeNew = Utils.getResourceAsString("upgrade/pln_water_upgrade.jsd");
        String waterUpgradeNewMd5 = Utils.md5(waterUpgradeNew);

        LuffyFileModel file = DbLuffyApi.getFileByPath("/water/_upgrade");
        if (file.file_id > 0) {
            String fileContentMd5 = Utils.md5(file.content);

            if (waterUpgradeNewMd5.equals(fileContentMd5) == false) {
                DbLuffyApi.setFileContent(file.file_id, waterUpgradeNew);
            }

            //立即执行
            DbLuffyApi.resetFilePlan(String.valueOf(file.file_id));
        } else {
            DataItem dataItem = DbLuffyApi.getFileDataByPath("/water/speed_sync");

            dataItem.remove("file_id");
            dataItem.remove("plan_last_time");

            dataItem.set("path", "/water/_upgrade");
            dataItem.set("plan_interval", "1h");
            dataItem.set("plan_begin_time", Datetime.Now().addDay(-1).getTicks());
            dataItem.set("plan_last_timespan", 0);
            dataItem.set("plan_state", 1);
            dataItem.set("plan_max", 0);
            dataItem.set("plan_count", 0);
            dataItem.set("note", "框架升级");
            dataItem.set("content", waterUpgradeNew);

            DbLuffyApi.setFile(0, dataItem, LuffyFileType.pln);
        }
    }

    private static void tryInitSchemaBySplitSql(DbContext db, String sql) throws Exception {
        if (Utils.isNotEmpty(sql)) {
            for (String sqlItem : sql.split(";")) {
                sqlItem = sqlItem.trim();
                if (Utils.isNotEmpty(sqlItem)) {
                    db.sql(sqlItem).log(false).execute();
                }
            }
        }
    }
}
