package org.noear.rubber.workflow;

import java.util.List;

/**
 * 工作流适配器
 * */
public interface WorkflowAdapter {
    //是否取消流程
    boolean is_cancel();

    //条件处理
    boolean condition_handle(Condition condition) throws Exception;

    //任务处理
    void task_handle(List<NodeTask> tasks) throws Exception;
}
