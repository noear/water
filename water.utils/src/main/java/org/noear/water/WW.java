package org.noear.water;

public class WW {
    public static final String mime_glog="water/glog";
    public static final String mime_gzip="application/x-gzip";
    public static final String mime_json="application/json";
    public static final String mime_ndjson = "application/x-ndjson";

    public static final String http_header_from = "Water-From";
    public static final String http_header_trace = "Water-Trace-Id";

    public static final String msg_ucache_topic = "water.cache.update";
    public static final String msg_uconfig_topic = "water.config.update";

    public static final String cfg_data_header = "#Data#: ";

    public static final String path_run_job = "/run/job/";//for cloud job call
    public static final String path_run_status = "/run/status/";
    public static final String path_run_check = "/run/check/";
    public static final String path_run_stop = "/run/stop/";
    public static final String path_msg_receiver = "/msg/receive";

    public static final String clz_BcfClient = "org.noear.bcf.BcfClient";

    public static final String water_host = "water.host";
    public static final String water_logger = "water.logger";



    public static final String water_log = "water_log";
    public static final String water_log_store = "water_log_store";

    public static final String water_log_upstream = "water_log_upstream";
    public static final String water_log_api = "water_log_api";
    public static final String water_log_msg = "water_log_msg";
    public static final String water_log_paas = "water_log_paas";
    public static final String water_log_sev = "water_log_sev";

    public static final String water = "water";
    public static final String waterapi = "waterapi";
    public static final String watersev = "watersev";

    public static final String water_redis = "water_redis";
    public static final String water_redis_track = "water_redis_track";
    public static final String water_cache = "water_cache";

    public static final String water_msg = "water_msg";
    public static final String water_msg_store = "water_msg_store";
    public static final String water_msg_queue = "water_msg_queue";

    public static final String water_paas = "water_paas";

    public static final String water_bcf = "water_bcf";

    public static final String track_service = "_service";
    public static final String track_from = "_from";

    public static final String driverType = "driverType";

    /**
     * 主控组
     * */
    public static final String whitelist_tag_master = "master";
    /**
     * 客户端组（一般用于检测管理后台客户端）
     * */
    public static final String whitelist_tag_client = "client";
    /**
     * 服务端组（一般用于检测服务端IP）
     * */
    public static final String whitelist_tag_server = "server";

}
