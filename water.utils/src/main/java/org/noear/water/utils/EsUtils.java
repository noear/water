package org.noear.water.utils;

import org.noear.snack.ONode;

import java.io.IOException;
import java.util.*;

/**
 * 只支持 7.x +
 *
 * @author noear
 */
public class EsUtils {

    static final String mime_json = "application/json";
    static final String mime_ndjson = "application/x-ndjson";

    private final String[] urls;
    private int urlIndex;
    private final String username;
    private final String paasword;

    public EsUtils(Properties prop) {
        this(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("paasword"));
    }

    public EsUtils(String url) {
        this(url, null, null);
    }

    public EsUtils(String url, String username, String paasword) {
        this.username = username;
        this.paasword = paasword;

        List<String> urlAry = new ArrayList<>();
        for (String ser : url.split(",")) {
            if (ser.contains("://")) {
                urlAry.add(ser);
            } else {
                urlAry.add("http://" + ser);
            }
        }
        this.urls = urlAry.toArray(new String[urlAry.size()]);
    }

    private String getUrl() {
        if (urls.length == 0) {
            return urls[0];
        } else {
            if (urlIndex > 10000000) {
                urlIndex = 0;
            }

            return urls[urlIndex % urls.length];
        }
    }

    private HttpUtils getHttp(String path) {
        HttpUtils http = HttpUtils.http(getUrl() + path);

        if (TextUtils.isNotEmpty(username)) {
            String token = Base64Utils.encode(username + ":" + paasword);
            String auth = "Basic " + token;

            http.header("Authorization", auth);
        }

        return http;
    }

    /**
     * 索引创建
     *
     * @param indiceName 索引名字
     */
    public String indiceCreate(String indiceName, String dsl) throws IOException {
        HttpUtils http = getHttp(String.format("/%s", indiceName));

        String tmp = http.bodyTxt(dsl, mime_json).put();
        //return: {"acknowledged":true,"shards_acknowledged":true,"index":"water$water_log_api_202110"}

        return tmp;
    }

    /**
     * 索引是否存在
     *
     * @param indiceName 索引名字
     */
    public boolean indiceExist(String indiceName) throws IOException {
        int tmp = getHttp(String.format("/%s", indiceName)).head();

        return tmp == 200; //404不存在
    }

    /**
     * 索引删除
     *
     * @param indiceName 索引名字
     */
    public String indiceDrop(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).delete();

        return tmp;
    }

    /**
     * 索引结构获取
     *
     * @param indiceName 索引名字
     */
    public String indiceGet(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).get();

        return tmp;
    }

    /**
     * 文档添加或修改
     *
     * @param indiceName 索引名字
     */
    public <T> String documentPut(String indiceName, String docId, T doc) throws IOException {
        String docJson = ONode.stringify(doc);

        HttpUtils http = getHttp(String.format("/%s/_doc/%s", indiceName, docId));

        String tmp = http.bodyTxt(docJson, mime_json).put(); //需要 put
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }


    /**
     * 文档批量添加或修改
     *
     * @param indiceName 索引名字
     */
    public <T> String documentPutAll(String indiceName, Map<String, T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((docId, doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").set("_id", docId)).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        HttpUtils http = getHttp(String.format("/%s/_doc/_bulk", indiceName));

        String tmp = http.bodyTxt(docJson.toString(), mime_ndjson).post(); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    /**
     * 文档添加
     *
     * @param indiceName 索引名字
     */
    public <T> String documentAdd(String indiceName, T doc) throws IOException {
        String docJson = ONode.stringify(doc);

        HttpUtils http = getHttp(String.format("/%s/_doc/", indiceName));

        String tmp = http.bodyTxt(docJson, mime_json).post(); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    /**
     * 文档批量添加
     *
     * @param indiceName 索引名字
     */
    public <T> String documentAddAll(String indiceName, List<T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").asObject()).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        HttpUtils http = getHttp(String.format("/%s/_doc/_bulk", indiceName));

        String tmp = http.bodyTxt(docJson.toString(), mime_ndjson).post();
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    /**
     * 文档删除
     *
     * @param indiceName 索引名字
     */
    public String documentDelete(String indiceName, String... docIds) throws IOException {
        String docIdsStr = String.join(",", docIds);

        HttpUtils http = getHttp(String.format("/%s/_doc/%s", indiceName, docIdsStr));

        String tmp = http.delete();

        return tmp;
    }

    /**
     * 文档搜索
     *
     * @param indiceName 索引名字
     */
    public <T> EsResult<T> documentSearch(String indiceName, Map<String, Object> map, long start, long size, Class<T> clz) throws IOException {
        return documentSearchDo(indiceName, map, start, size, null, false, clz);
    }

    /**
     * 文档搜索
     *
     * @param indiceName 索引名字
     */
    public <T> EsResult<T> documentSearchAsc(String indiceName, Map<String, Object> map, long start, long size, String orderBy, Class<T> clz) throws IOException {
        return documentSearchDo(indiceName, map, start, size, orderBy, true, clz);
    }

    public <T> EsResult<T> documentSearchDesc(String indiceName, Map<String, Object> map, long start, long size, String orderBy, Class<T> clz) throws IOException {
        return documentSearchDo(indiceName, map, start, size, orderBy, false, clz);
    }

    private <T> EsResult<T> documentSearchDo(String indiceName, Map<String, Object> map, long start, long size, String sort, boolean asc, Class<T> clz) throws IOException {
        ONode oNode = new ONode();
        oNode.set("from", start);
        oNode.set("size", size);

        ONode oQuery = oNode.getNew("query").asObject();
        if (map.size() == 1) {
            oQuery.getNew("match").fill(map).toJson();
        } else {
            oQuery.getNew("multi_match").fill(map).toJson();
        }

        if (TextUtils.isNotEmpty(sort)) {
            ONode oOrder = oNode.getOrNew("sort").getOrNew(sort).getOrNew("order");
            if (asc) {
                oOrder.val("asc");
            } else {
                oOrder.val("desc");
            }
        }

        String dsl = oNode.toJson();

        String json = documentSearch(indiceName, dsl);

        ONode oHits = ONode.loadStr(json).get("hits");

        long total = oHits.get("total").get("value").getLong();
        List<T> list = oHits.get("hits").toObjectList(clz);

        return new EsResult<>(total, list);
    }

    /**
     * 文档搜索
     *
     * @param indiceName 索引名字
     */
    public String documentSearch(String indiceName, String dsl) throws IOException {
        HttpUtils http = getHttp(String.format("/%s/_search", indiceName));

        String tmp = http.bodyTxt(dsl, mime_json).post();

        return tmp;
    }
}
