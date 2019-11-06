package webapp.utils;

import java.text.DecimalFormat;
import java.util.List;

public class StringUtil {

    public static String stringJoin(List<Double> input,String link) {
        StringBuilder sb = new StringBuilder();
        for (Double item : input) {
            sb.append(item);
            sb.append(link);
        }
        String res=sb.toString();
        return res.substring(0,res.length()-1);
    }

    public static String doubleFormat(Double input){
        DecimalFormat df = new DecimalFormat("#.00");
        String res=df.format(input);
        return res;
    }
}
