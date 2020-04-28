package org.noear.rubber.workflow;

//节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束
public class NodeType {
    public static final int start = 0;
    public static final int line = 1;
    public static final int execute = 2;
    public static final int exclusive = 3;
    public static final int parallel = 4;
    public static final int converge = 5;
    public static final int stop = 9;
}
