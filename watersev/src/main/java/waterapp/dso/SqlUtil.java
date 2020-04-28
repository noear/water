package waterapp.dso;


import org.noear.water.utils.Datetime;

public final class SqlUtil {
    public static String preProcess(String sql){
        Datetime now = Datetime.Now();
        String _idate = now.toString("yyyyMMdd");

        now.addDay(-7);
        String _i7date = now.toString("yyyyMMdd");

        now.addDay(-23);
        String _i30date = now.toString("yyyyMMdd");

        now.addDay(30 -1);
        String _i1date = now.toString("yyyyMMdd");

        now.addDay(-2);
        String _i3date = now.toString("yyyyMMdd");

        sql = sql.replaceAll("@i30date",_i30date);
        sql = sql.replaceAll("@i7date",_i7date);

        sql = sql.replaceAll("@i3date",_i3date);
        sql = sql.replaceAll("@i1date",_i1date);
        sql = sql.replaceAll("@idate",_idate);

        return sql;
    }

    public static boolean isSafe(String sql){
        String sql2 = sql.toLowerCase();

       return  sql2.indexOf("insert ")<0 &&
               sql2.indexOf("delete ")<0 &&
               sql2.indexOf("drop ") <0 &&
               sql2.indexOf("update ") <0 &&
               sql2.indexOf(";") <0 ;
    }
}
