package webapp.dao;


public class JtSQLCompiler {

    public static String compile(String code) {
        StringBuilder sb = new StringBuilder();

        String new_code = null;
        int idx_start = 0;
        int idx_end = 0;

        while (true) {
            //查找下一段jtsql代码
            idx_start = code.indexOf("$<",idx_end);

            if (idx_start >= 0) {

                if(idx_end>0){
                    idx_end+=2;
                }

                String temp = code.substring(idx_end,idx_start);
                sb.append(temp);

                idx_start+=2;

                idx_end = code.indexOf(">;", idx_start);

                if(idx_end<0){
                    throw new RuntimeException("编译出错（没有找到>;）");
                }

                new_code = compile_part(code.substring(idx_start, idx_end));

                sb.append("sql(\"").append(new_code).append("\");");
            }else{
                if(idx_end>0){
                    idx_end+=2;
                }

                String temp = code.substring(idx_end);
                sb.append(temp);

                break;
            }
        }

        return sb.toString();
    }

    private static String compile_part(String jtsql){
        String jscode = jtsql.replaceAll("\r"," ");
        jscode = jscode.replaceAll("\n"," ");
        jscode = jscode.replaceAll("\t"," ");


        jscode = jscode.replaceAll("\\{\\{","\"+");
        jscode = jscode.replaceAll("\\}\\}","+\"");

        //jtSQL.jtlog.write("JtSQL.code", tag, jscode);

        return jscode;
    }
}
