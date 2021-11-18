package watersev.dso;

import org.noear.solon.Utils;
import org.noear.water.utils.TextUtils;
import watersev.utils.JsUtil;

import java.util.ArrayList;
import java.util.List;

public class RuleUtil {
    static List<String> funcList = new ArrayList<>();

    public synchronized static boolean loadFunc(String fun,String code) {

        try {
            StringBuilder func = new StringBuilder(500);

            if (funcList.contains(fun)) {
                func.append(fun).append(" = function(m)");
            } else {
                funcList.add(fun);
                func.append("function ").append(fun).append("(m)");
            }

            func.append("{");
            func.append(code);
            func.append("};\r\n\n");

            JsUtil.loadJs(func.toString());

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("RuleUtil", "", "loadFunc::" + code + "\n" + Utils.throwableToString(ex));
            return false;
        }
    }

    //如果rule被match了，说明的问题
    public static boolean match(String model,String funKey, String ruleCode){
        if (TextUtils.isEmpty(ruleCode)) {
            return false;
        }

        boolean isOk = loadFunc(funKey,ruleCode);

        if(isOk == false){
            return false;
        }

        StringBuilder jscode = new StringBuilder(200);
        jscode.append(funKey).append("(");
        jscode.append(model);
        jscode.append(");");

        Object temp = JsUtil.evalJs(jscode.toString());

        if(temp == null) {
            return false;
        }
        else {
            return (Boolean) temp;
        }
    }


    public static String format(String model, String funKey, String fomratString) {
        if (TextUtils.isEmpty(fomratString)) {
            return null;
        }

        if(fomratString.indexOf("{{")<0){
            return fomratString;
        }

        String fmtCode  = null;
        if(fomratString.indexOf("return")<0) {
            fmtCode = "return '" + fomratString + "';";
        }else{
            fmtCode = fomratString;
        }
        fmtCode = fmtCode.replaceAll("\\{\\{","'+");
        fmtCode = fmtCode.replaceAll("\\}\\}","+'");

        boolean isOk = loadFunc(funKey,fmtCode);

        if(isOk == false){
            return null;
        }

        StringBuilder jscode = new StringBuilder(200);
        jscode.append(funKey).append("(");
        jscode.append(model);
        jscode.append(");");

        Object temp = JsUtil.evalJs(jscode.toString());

        if(temp == null) {
            return "";
        }
        else {
            return (String) temp;
        }
    }
}
