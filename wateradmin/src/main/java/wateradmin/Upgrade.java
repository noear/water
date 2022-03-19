package wateradmin;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Utils;
import org.noear.solon.core.event.EventBus;
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
            EventBus.push(e);
        }

        try {
            update_upgrade();
        } catch (Throwable e) {
            EventBus.push(e);
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
            file = DbLuffyApi.getFileByPath("/water/speed_sync");

            file.path = "/water/_upgrade";
            file.plan_interval = "1h";
            file.plan_begin_time = Datetime.Now().addDay(-1).getFulltime();
            file.plan_last_time = null;
            file.plan_last_timespan = 0;
            file.plan_state = 1;
            file.content = waterUpgradeNew;
            DataItem dataItem = new DataItem();
            dataItem.setEntity(file);
            dataItem.remove("file_id");

            DbLuffyApi.setFile(0, dataItem, LuffyFileType.pln);
        }
    }
}
