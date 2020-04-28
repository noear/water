package org.noear.rubber.workflow;

import java.util.List;

//工作流运行器
public class WorkflowRunner {
    public WorkflowRunner(Workflow workflow, WorkflowAdapter adapter) {
        this.workflow = workflow;
        this.adapter = adapter;
    }

    protected WorkflowAdapter adapter;
    protected Workflow workflow;

    public void run() throws Exception {
        Node start = workflow.start();

        node_run(start);
    }

    //检查条件
    private boolean condition_check(Condition condition) throws Exception {
        return adapter.condition_handle(condition);
    }

    //执行任务
    private void task_exec(List<NodeTask> tasks) throws Exception {
        adapter.task_handle(tasks);
    }

    //.运行节点
    private void node_run(Node node) throws Exception {
        if (adapter.is_cancel()) { //如果取消，就不再执行了
            return;
        }

        switch (node.type()) {
            case NodeType.start: {
                node_run(node.nextNode());
            }
            break;
            case NodeType.stop: {
                //无动作
            }
            break;
            case NodeType.execute: {
                task_exec(node.tasks());

                node_run(node.nextNode());
            }
            break;
            case NodeType.exclusive: {
                exclusive_run(node);
            }
            break;
            case NodeType.parallel: {
                parallel_run(node);
            }
            break;
            case NodeType.converge: {
                converge_run(node);
            }
            break;
        }
    }

    //.运行排他网关
    private void exclusive_run(Node node) throws Exception {
        List<Node> lines = node.nextLines();
        Node def_line = null;
        for (Node l : lines) {
            if (l.condition().isEmpty()) {
                def_line = l;
            } else {
                if (condition_check(l.condition())) {
                    node_run(l.nextNode());
                    return;
                }
            }
        }

        node_run(def_line.nextNode());
    }

    //.运行并行网关
    private void parallel_run(Node node) throws Exception {
        for (Node n : node.nextNodes()) {
            node_run(n);
        }
    }

    //.运行汇聚网关//起到等待和卡位的作用；
    private void converge_run(Node node) throws Exception {
        node.counter++; //运行次数累计
        if (node.prveLines().size() > node.counter) { //等待所有支线计数完成
            return;
        }

        node_run(node.nextNode()); //然后到下一个节点
    }
}
