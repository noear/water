package wateradmin;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.utils.Datetime;
import org.noear.weed.DataItem;
import wateradmin.dso.db.DbLuffyApi;
import wateradmin.models.water_paas.LuffyFileModel;
import wateradmin.models.water_paas.LuffyFileType;


/**
 * @author noear 2022/3/19 created
 */
@Slf4j
public class Upgrade {
    public static void tryUpdate() {
        try {
            update_init();
        } catch (Throwable e) {
            TagsMDC.tag0("upgrade");
            log.error("Upgrade attempt failed: {}", e);
        }

        try {
            update_upgrade();
        } catch (Throwable e) {
            TagsMDC.tag0("upgrade");
            log.error("Upgrade attempt failed: {}", e);
        }
    }

    private static void update_init() throws Throwable {
        //for "api::/sdk_water/_init.jsx"
        String waterInitNew = Utils.getResourceAsString("upgrade/api_water_init.jsd");
        String waterInitNewMd5 = Utils.md5(waterInitNew);

        LuffyFileModel file = DbLuffyApi.getFileByPath("/sdk_water/_init.jsx");
        if (file.file_id > 0) {
            String fileContentMd5 = Utils.md5(file.content);

            if (waterInitNewMd5.equals(fileContentMd5) == false) {
                DbLuffyApi.setFileContent(file.file_id, waterInitNew);
            }
        }
    }

    private static void update_upgrade() throws Throwable {
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
            dataItem.set("content", waterUpgradeNew);

            DbLuffyApi.setFile(0, dataItem, LuffyFileType.pln);
        }
    }
}
