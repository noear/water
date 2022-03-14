package watersev.dso.etl;

import org.noear.water.WaterClient;
import org.apache.commons.lang.exception.ExceptionUtils;

public class EtlLog {
    public static void write(EtlContext context, Class cls, String txt) {
        write(context, cls, "", txt);
    }

    public static void write(EtlContext context, Class cls, String label, String txt) {
        StringBuilder sb = new StringBuilder();
        sb.append(txt);

        String tname = Thread.currentThread().getName();
        if (tname != null) {
            label = tname + "::" + label;
        }

        WaterClient.Logger.append("water_log_etl", 0, context.name, context.table, cls.getSimpleName(), label, sb.toString());

        System.out.print("write::" + context.name + "::" + context.table + "::" + cls.getSimpleName());
        System.out.print("\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }

    public static void error(EtlContext context, Class cls, String label, Exception ex) {
        if(ex == null){
            return;
        }

        String tname = Thread.currentThread().getName();
        if (tname != null) {
            label = tname + "::" + label;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\r\n").append(ExceptionUtils.getFullStackTrace(ex));

        WaterClient.Logger.append("water_log_etl_error", 0, context.name, context.table, cls.getSimpleName(), label, sb.toString());

        System.out.print("error::" + context.name + "::" + context.table + "::" + cls.getSimpleName());
        System.out.print("\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }

    public static void error(EtlContext context, Class cls, String label, String ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\r\n").append(ex);

        String tname = Thread.currentThread().getName();
        if (tname != null) {
            label = tname + "::" + label;
        }

        WaterClient.Logger.append("water_log_etl_error", 0, context.name, context.table, cls.getSimpleName(), label, sb.toString());

        System.out.print("error::" + context.name + "::" + context.table + "::" + cls.getSimpleName());
        System.out.print("\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }

    public static void alarm(EtlContext context, Class cls, String label, String txt) {
        StringBuilder sb = new StringBuilder();
        sb.append(txt);

        String tname = Thread.currentThread().getName();
        if (tname != null) {
            label = tname + "::" + label;
        }

        WaterClient.Logger.append("water_log_etl_alarm", 0, context.name, context.table, cls.getSimpleName(), label, sb.toString());

        System.out.print("alarm::" + context.name + "::" + context.table + "::" + cls.getSimpleName());
        System.out.print("\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }
}
