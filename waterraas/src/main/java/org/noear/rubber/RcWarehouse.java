package org.noear.rubber;

import org.noear.rubber.models.SchemeModel;
import org.noear.rubber.models.SchemeRuleModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class RcWarehouse {
    private static Map<Integer,SchemeRuleModel> _ruleList = new ConcurrentHashMap<>();
    public static void ruleAdd(SchemeRuleModel rule) {
        _ruleList.put(rule.rule_id, rule);
    }

    public static SchemeRuleModel ruleGet(int rule_id){
        return _ruleList.get(rule_id);
    }

    private static Map<Integer,SchemeModel> _schemeList = new ConcurrentHashMap<>();
    public static void schemeAdd(SchemeModel scheme) {
        _schemeList.put(scheme.scheme_id, scheme);
    }

    public static SchemeModel schemeGet(int scheme_id){
        return _schemeList.get(scheme_id);
    }
}
