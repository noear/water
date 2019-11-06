package webapp.dao;


public class JtFunRunner {
    private JtSQL jtSQL = null;
    public JtFunRunner(String tag) {
        jtSQL = new JtSQL(new JtLogger() {
            @Override
            public void write(String tag, String label, String txt) {

            }

            @Override
            public void debug(String tag, String label, String txt) {

            }

            @Override
            public void error(String tag, String label, Exception ex) {

            }
        });

        try {
            String code = "include('" + tag + "/*');";
            jtSQL.exec(code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object eval(String code)  {
        //2.2.执行
        try {
            return jtSQL.eval(code);
        } catch (Exception ex) {
            return null;
        }
    }

    public Object eval(String code, Object def)  {
        Object tmp = eval(code);
        if(tmp == null){
            return def;
        }else{
            return tmp;
        }
    }
}
