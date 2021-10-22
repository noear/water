package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.Utils;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.water.log.Level;
import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.utils.EsResult;
import org.noear.water.utils.EsUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class EsTest {
    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";

    EsUtils client = new EsUtils("eshost:30480,eshost:30480");

    @Test
    public void test0() throws Exception {
        assert client.indiceExist(indiceNoExit) == false;
    }

    @Test
    public void test1() throws Exception {
        if (client.indiceExist(indiceNew)) {
            client.indiceDrop(indiceNew);
        }

        assert client.indiceExist(indiceNew) == false;

        String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
        client.indiceCreate(indiceNew, dsl);


        assert client.indiceExist(indiceNew) == true;
    }

    @Test
    public void test2() throws Exception {
        if (client.indiceExist(indice) == false) {
            String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
            client.indiceCreate(indice, dsl);
        }

        assert client.indiceExist(indice) == true;
    }

    @Test
    public void test3() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test1";
        logDo.level = Level.INFO.code;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();


        client.documentPut(indice, Utils.guid(), logDo);
    }

    @Test
    public void test4() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test2";
        logDo.level = Level.INFO.code;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();


        client.documentAdd(indice, logDo);
    }

    @Test
    public void test5() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        Map<String, LogDo> docs = new LinkedHashMap<>();

        for (int i = 0; i < 20; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "map1";
            logDo.level = Level.INFO.code;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.put(Utils.guid(), logDo);
        }


        client.documentPutAll(indice, docs);
    }

    @Test
    public void test6() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        List<LogDo> docs = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "list1";
            logDo.level = Level.INFO.code;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.add(logDo);
        }


        client.documentAddAll(indice, docs);
    }

    @Test
    public void test7() throws Exception {
        Map<String, Object> filter = new LinkedHashMap<>();
        filter.put("tag", "list1");

        EsResult<LogDo> result = client.documentSearch(indice, filter, 0, 10, LogDo.class);

        assert result.getSize() == 10;
    }
}
