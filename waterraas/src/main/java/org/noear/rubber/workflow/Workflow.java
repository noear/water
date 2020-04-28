package org.noear.rubber.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//工作流对象
public class Workflow {
    private Node _start;
    private Map<String,Node> _nodes = new HashMap<>();

    //起始节点
    public Node start(){
        return _start;
    }

    //所有节点
    public Map<String,Node> nodes(){
        return _nodes;
    }

    //添加节点
    public void addNode(String id, String name, int type, String prveId, String nextId, String conditions, String tasks){
        Node node = new Node(this);

        node._conditions_str = conditions;
        node._tasks_str = tasks;

        node._id = id;
        node._name = name;
        node._type = type;
        node._prveId = prveId; //仅line才有
        node._nextId = nextId; //仅line才有

        _nodes.put(node.id(), node);

        if(type == 0){
            _start = node;
        }

    }

    //查找一个节点
    protected Node selectById(String id) {
        return nodes().get(id);
    }

    //查找前面的节点
    protected List<Node> selectByNextId(String id) {
        List<Node> nodes = new ArrayList<>();

        for(Node n: nodes().values()){
            if(id.equals(n.nextId())){
                nodes.add(n);
            }
        }

        return nodes;
    }

    //查找后面的节点
    protected List<Node> selectByPrveId(String id) {
        List<Node> nodes = new ArrayList<>();

        for (Node n : nodes().values()) {
            if (id.equals(n.prveId())) {
                nodes.add(n);
            }
        }

        return nodes;
    }
}
