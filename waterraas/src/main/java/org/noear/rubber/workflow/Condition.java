package org.noear.rubber.workflow;

import java.util.List;

/**
 * 条件（一般用于分支条件）
 * */
public class Condition {
    private String _name;
    private List<ConditionItem> _items = null;

    public String name(){
        return _name;
    }
    public List<ConditionItem> items(){return _items;}

    public Condition(String name, String conditions_str){
        this._name = name;
        this._items = ConditionItem.parse(conditions_str);
    }

    public boolean isEmpty(){
        return _items == null || _items.size()==0;
    }
}
