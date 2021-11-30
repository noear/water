package wateradmin.dso;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.extend.grit.SessionBase;

public final class Session extends SessionBase {
    private static final Session _current = new Session();

    public static Session current() {
        return _current;
    }


    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadSubject(Subject subject) throws Exception {
        if (subject == null || subject.subject_id == null) {
            return;
        }

        setSubjectId(subject.subject_id);
        setLoginName(subject.login_name);
        setDisplayName(subject.display_name);

        boolean is_admin = GritClient.global().auth().hasPermission(subject.subject_id, SessionPerms.admin);
        boolean is_operator = GritClient.global().auth().hasPermission(subject.subject_id, SessionPerms.operator);

        if (is_admin) {
            is_operator = true;
        }

        localSet(SessionPerms.admin, is_admin ? 1 : 0);
        localSet(SessionPerms.operator, is_operator ? 1 : 0);

        setIsAdmin(is_admin ? 1 : 0);
        setIsOperator(is_operator ? 1 : 0);
    }

    public boolean isAdmin() {
        return getIsAdmin() == 1;
    }

    public int getIsAdmin() {
        return localGetAsInt("Is_Admin", 0);
    }

    public void setIsAdmin(int Is_Admin) {
        localSet("Is_Admin", Is_Admin);
    }

    public boolean isOperator() {
        return getIsOperator() == 1;
    }

    public int getIsOperator() {
        return localGetAsInt("Is_Operator", 0);
    }

    public void setIsOperator(int is_Operator) {
        localSet("Is_Operator", is_Operator);
    }

    public String getValidation() {
        return localGet("Validation_String", null);
    }

    public void setValidation(String validation) {
        localSet("Validation_String", validation.toLowerCase());
    }
}
