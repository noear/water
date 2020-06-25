package waterraas.dao;

import org.noear.snack.ONode;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;

public class TxtUtil {
    public static ONode buildJson(DataList dlist){
        ONode jlist = new ONode().asArray();
        for (DataItem item : dlist.getRows()) {
            jlist.add(ONode.load(item.getMap()));
        }
        return jlist;
    }

    public static String buildHtm(DataList dlist){
        StringBuilder sb = new StringBuilder();

        sb.append("<!doctype html>");
        sb.append("<html lang='zh_CN'>");

        sb.append("<head>");
        sb.append("<style>");
        sb.append(" table { border-collapse: collapse;}\n" +
                " thead { background: #f5f6fa; }\n" +
                " thead td { height: 20px; color:#999; text-align: center; }\n" +
                " td { text-align: center; border: 1px solid #f5f6fa; padding: 4px!important; line-height: 18px; white-space:nowrap;}\n" +
                " tbody tr{background: #fff;}\n" +
                " tbody tr:hover { background: #f9f9fa; }\n" +
                " tbody tr:hover td { background: #f9f9fa; }\n" +
                " tbody tr:hover a { }\n" );
        sb.append("</style>");
        sb.append("</head>");

        sb.append("<body>");
        sb.append("<table>");

        sb.append("<thead>");
        if(dlist.getRowCount()>0){

            sb.append("<tr>");
            DataItem item = dlist.getRow(0);
            item.forEach((k,v)->{
                sb.append("<td>");
                sb.append(k);
                sb.append("</td>");
            });
            sb.append("</tr>");
        }
        sb.append("</thead>");

        sb.append("<tbody>");
        for (DataItem item : dlist.getRows()) {
            sb.append("<tr>");
            item.forEach((k,v)->{
                sb.append("<td>");
                if(v == null){
                    sb.append("null");
                }else{
                    sb.append(v.toString());
                }
                sb.append("</td>");//\t防止出现科学计数法
            });
            sb.append("</tr>");
        }
        sb.append("</tbody>");

        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    public static String buildCsv(DataList dlist){
        StringBuilder sb = new StringBuilder();
        sb.append(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));//解决excel打开乱码

        if(dlist.getRowCount()>0){
            DataItem item = dlist.getRow(0);
            item.forEach((k,v)->{
                sb.append("\"");
                sb.append(k);
                sb.append("\",");
            });
            sb.append("\r\n");
        }

        for (DataItem item : dlist.getRows()) {
            item.forEach((k,v)->{
                sb.append("\"");
                if(v == null){
                    sb.append("null");
                }else{
                    sb.append(v.toString().replace("\"", "\"\""));
                }
                sb.append("\t\",");//\t防止出现科学计数法
            });
            sb.append("\r\n");
        }

        return sb.toString();
    }
}
