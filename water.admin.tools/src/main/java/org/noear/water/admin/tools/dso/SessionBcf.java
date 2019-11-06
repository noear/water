package org.noear.water.admin.tools.dso;

import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.core.XContext;
import org.noear.water.admin.tools.Config;
import org.noear.water.client_solon.XWaterSessionBcf;

//Session对象
public class SessionBcf extends XWaterSessionBcf {
    @Override
    public String domain() {
        if (XContext.current().url().indexOf("localhost") >= 0) {
            return "localhost";
        } else {
            return "zmapi.cn";
        }
    }

    @Override
    public String service() {
        return Config.water_service_name;
    }

    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadModel(BcfUserModel user) throws Exception {
        setPUID(user.puid);
        setUserId(user.user_id);
        setUserName(user.cn_name);

        boolean is_admin = BcfClient.hasResourceByUser(user.puid, "water_p_admin");
        boolean is_operator = BcfClient.hasResourceByUser(user.puid, "water_p_operator");

        if (is_admin) {
            is_operator = true;
        }

        setIsAdmin(is_admin ? 1 : 0);
        setIsOperator(is_operator ? 1 : 0);
    }

    public final int getIsAdmin() {
        return getAsInt("Is_Admin", 0);
    }

    public final void setIsAdmin(int Is_Admin) {
        set("Is_Admin", Is_Admin);
    }

    public final int getIsOperator() {
        return getAsInt("Is_Operator", 0);
    }

    public final void setIsOperator(int is_Operator) {
        set("Is_Operator", is_Operator);
    }

    public final String getValidation() {
        return get("Validation_String", null);
    }

    public final void setValidation(String validation) {
        set("Validation_String", validation.toLowerCase());
    }
}
