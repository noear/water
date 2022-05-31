package watersev.dso;

import org.noear.solon.extend.schedule.JobEntity;
import org.noear.solon.extend.schedule.JobRunner;
import org.noear.water.utils.TextUtils;

public class JobRunnerEx extends JobRunner {
    private String sss;

    public JobRunnerEx(String sss) {
        this.sss = sss;
    }

    @Override
    public boolean allow(JobEntity task) {
        if ("reg".equals(task.getName())) {
            return true;
        }

        boolean is_ok = (TextUtils.isEmpty(sss) || sss.indexOf(task.getName()) >= 0);

        if (is_ok == false) {
            switch (sss) {
                case "tool":
                    is_ok = ("msgexg,msgchk,sevchk,syn,mot,det".indexOf(task.getName()) >= 0);
                    break;
                case "tol":
                    is_ok = ("msgchk,sevchk,syn,mot,det".indexOf(task.getName()) >= 0);
                    break;
                case "msg":
                    is_ok = ("msgdis".indexOf(task.getName()) >= 0);
                    break;
            }
        }

        return is_ok;
    }
}
