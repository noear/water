package webapp.controller;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.water.tools.TextUtils;
import webapp.dso.db.DbApi;
import webapp.model.ConfigModel;

import java.util.Date;
import java.util.List;

@XMapping("/cfg/")
@XController
public class CfgController implements XHandler {
    @Override
    public void handle(XContext ctx) throws Throwable {
        String tag = ctx.param("tag");

        ONode nList = new ONode().asObject();

        if(TextUtils.isEmpty(tag) == false){
            List<ConfigModel> list =  DbApi.getConfigByTag(tag);

            Date def_time = new Date();

            for(ConfigModel m1: list){
                if(m1.update_fulltime == null){
                    m1.update_fulltime = def_time;
                }

                ONode n = nList.getNew(m1.key);
                n.set("key",m1.key);
                n.set("value",m1.value);
                n.set("lastModified",m1.update_fulltime);
            }
        }


        ctx.outputAsJson(nList.toJson());
    }
}
