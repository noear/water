package watersev.dso.etl;
//
//import watersev.dso.JtLogger;
//import watersev.dso.JtSQL;
//
//public class EtlLoggerJt implements JtLogger {
//    @Override
//    public void write(String tag, String label, String txt) {
//        if(EtlContext.current() == null){
//            return;
//        }
//
//        EtlLog.write(EtlContext.current(), JtSQL.class, label, txt);
//    }
//
//    @Override
//    public void debug(String tag, String label, String txt) {
//        if(EtlContext.current() == null){
//            return;
//        }
//
//        EtlLog.write(EtlContext.current(), JtSQL.class, label, txt);
//    }
//
//    @Override
//    public void error(String tag, String label, Exception ex) {
//        if(EtlContext.current() == null){
//            return;
//        }
//
//        EtlLog.error(EtlContext.current(), JtSQL.class, label, ex);
//    }
//}
