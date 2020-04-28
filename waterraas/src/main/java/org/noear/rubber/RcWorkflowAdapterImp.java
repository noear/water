package org.noear.rubber;

import org.noear.rubber.workflow.Condition;
import org.noear.rubber.workflow.NodeTask;
import org.noear.rubber.workflow.TaskType;
import org.noear.rubber.workflow.WorkflowAdapter;

import java.util.List;

final class RcWorkflowAdapterImp implements WorkflowAdapter {
    private RubberContext _context;

    public RcWorkflowAdapterImp(RubberContext context) {
        _context = context;
    }

    @Override
    public boolean is_cancel() {
        return _context.is_cancel;
    }

    @Override
    public boolean condition_handle(Condition condition) throws Exception {
        return RcRunner.runCondition(_context, condition);
    }

    @Override
    public void task_handle(List<NodeTask> tasks) throws Exception {
        for (NodeTask t : tasks) {
            if (t.type() == TaskType.function) {
                RcRunner.runExpr(_context, t.content());
            }

            if (t.type() == TaskType.rule) {
                RcRunner.runScheme(_context, t.content());
            }
        }
    }
}
