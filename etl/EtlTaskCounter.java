package watersev.dso.etl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EtlTaskCounter {
    private Map<Integer, EtlTaskState> counter = new ConcurrentHashMap<>();

    private EtlTaskState tryInit(Integer taskID) {
        EtlTaskState taskState = counter.get(taskID);

        if (taskState == null) {
            taskState = new EtlTaskState(taskID);
            counter.put(taskID, taskState);
        }

        return taskState;
    }

    public long num(Integer taskID) {
        return tryInit(taskID).value;
    }

//    public EtlTaskState get(Integer taskID) {
//        return tryInit(taskID);
//    }

//    public void add(Integer taskID) {
//        tryInit(taskID).add();
//    }

    public void stop(Integer taskID) {
        tryInit(taskID).sub();
    }

    public void start(Integer taskID, Runnable target){
        EtlTaskState state = tryInit(taskID);
        state.add();
        state.run(target);
    }
}
