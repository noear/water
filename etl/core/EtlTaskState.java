package watersev.dso.etl;

import watersev.dso.IDUtil;

public class EtlTaskState {
    public EtlTaskState(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer taskID;

    public long value;
    public long total;

    public void add() {
        value++;
        total++;
    }

    public void sub() {
        value--;
    }

    public String threadID() {
        return taskID + "-" + value + "-" + IDUtil.theadID(taskID);
    }

    public void run(Runnable target) {
        new Thread(target, threadID()).start();
    }
}
