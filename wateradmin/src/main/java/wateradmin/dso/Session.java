package wateradmin.dso;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfSessionBase;
import org.noear.bcf.models.BcfUserModel;
import wateradmin.Config;

public final class Session extends BcfSessionBase {
    private static final Session _current = new Session();
    public static Session current() {
        return _current;
    }


    @Override
    public String service() {
        return Config.water_service_name;
    }

    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadModel(BcfUserModel user) throws Exception {
        if (user == null) {
            return;
        }

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

    public final  boolean isAdmin() {
        return getIsAdmin() == 1;
    }

    public final int getIsAdmin() {
        return get("Is_Admin", 0);
    }

    public final void setIsAdmin(int Is_Admin) {
        set("Is_Admin", Is_Admin);
    }

    public final boolean isOperator() {
        return getIsOperator() == 1;
    }

    public final int getIsOperator() {
        return get("Is_Operator", 0);
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
