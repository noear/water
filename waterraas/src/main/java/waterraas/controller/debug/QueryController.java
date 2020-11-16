package waterraas.controller.debug;

import org.noear.rubber.Rubber;
import org.noear.rubber.RubberException;
import org.noear.rubber.RubberQuery;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import org.noear.weed.DataList;
import waterraas.controller.SystemCode;
import waterraas.dao.TxtUtil;

public class QueryController implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String scheme = context.param("scheme"); //通过参数传入
        int limit = context.paramAsInt("limit");
        if (scheme == null) {
            scheme = context.path().substring(3); //通过路径传入
        }

        int type = context.paramAsInt("type"); //=9时用于查询

        if (TextUtils.isEmpty(scheme) ) {
            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        if ("1".equals(context.param("debug"))) {
            Rubber.updateCache("scheme:" + scheme);
        }

        if (limit < 1) {
            limit = 10;
        }

        if (type == 10) {
            forData(context, scheme, type,  limit);
        } else {
            forView(context, scheme, type,  limit);
        }

    }

    void forData(Context context, String scheme, int type, int limit) throws Exception {
        context.contentType("text/json;charset=UTF-8");

        ONode data = new ONode();
        data.set("code", 1).set("msg", SystemCode.code_1);
        ONode jReq = data.get("request");

        jReq.set("scheme", scheme);

        RubberQuery rubberQuery = null;

        ONode jResp = data.get("response");

        try {
            rubberQuery = Rubber.query(scheme, limit);
            DataList dlist = rubberQuery.query();

            ONode jlist = TxtUtil.buildJson(dlist);

            jResp.set("list", jlist);

        } catch (RubberException ex) {
            data.set("code", 11).set("msg", SystemCode.code_11(ex.getMessage()));
        } catch (Exception ex) {
            data.set("code", 0).set("msg", ex.getMessage());

            jResp.set("hint", ThrowableUtils.getString(ex));
        }

        context.output(data.toJson());
    }

    void forView(Context context, String scheme, int type,  int limit) throws Exception {
        RubberQuery rubberQuery = null;

        try {
            rubberQuery = Rubber.query(scheme, limit);

            DataList dlist = rubberQuery.query();

            if (type == 11) {
                String text = TxtUtil.buildHtm(dlist);

                context.charset("UTF-8");
                context.contentType("text/html;charset=UTF-8");
                context.output(text);
            } else if (type == 12) {
                String text = TxtUtil.buildCsv(dlist);

                String fileName = scheme.replace("/", "_") + ".csv";
                context.charset("UTF-8");
                context.contentType("text/csv;charset=UTF-8");
                context.header("Content-Disposition", "attachment;filename=" + fileName);
                context.output(text);
            } else if (type == 19) {
                context.output(rubberQuery.sql);
            }
        } catch (RubberException ex) {
            context.output("出错提示：" + ex.getMessage());
        } catch (Exception ex) {
            if (rubberQuery != null) {
                context.output(rubberQuery.sql + "\r\n\n");
            }

            context.output(Utils.getFullStackTrace(ex));
        }
    }
}
