package webapp.dao;

public interface JtLogger {
    void write(String tag, String label, String txt);
    void debug(String tag, String label, String txt);
    void error(String tag, String label, Exception ex);
}
