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
        if("chk".equals(task.getName())){
            return true;
        }

        boolean is_ok = (TextUtils.isEmpty(sss) || sss.indexOf(task.getName()) >= 0);

        if(is_ok == false) {
            if ("tool".equals(sss)) {
                is_ok = ("zan,sub,sev,syn,mot".indexOf(task.getName()) >= 0);
            }
        }

        return is_ok;
    }
}
