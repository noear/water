package org.noear.water;

public class WW {
    public static final String water_version = "v2.7.2";

    public static final String mime_glog = "water/glog";
    public static final String mime_gzip = "application/x-gzip";
    public static final String mime_json = "application/json";
    public static final String mime_ndjson = "application/x-ndjson";


    public static final String http_header_job = "Water-Job-Name";
    public static final String http_header_from = "Water-From";
    public static final String http_header_trace = "Water-Trace-Id";
    public static final String http_header_token = "Water-Access-Token";

    public static final String msg_ucache_topic = "water.cache.update";
    public static final String msg_uconfig_topic = "water.config.update";

    public static final String cfg_data_header = "#Data#: ";

    public static final String path_run_job = "/_run/job/";//for cloud job call
    public static final String path_run_status = "/_run/status/";
    public static final String path_run_check = "/_run/check/";
    public static final String path_run_stop = "/_run/stop/";
    public static final String path_run_msg = "/_run/msg";


    public static final String water_host = "water.host";
    public static final String water_toekn = "water.token";
    public static final String water_logger = "water.logger";

    public static final String water_log_store = "water_log_store";

    public static final String water = "water";
    public static final String waterapi = "waterapi";
    public static final String watersev = "watersev";
    public static final String waterfaas = "waterfaas";


    public static final String watersev_det = "watersev-det";
    public static final String watersev_mot = "watersev-mot";
    public static final String watersev_msgchk = "watersev-msgchk";
    public static final String watersev_msgdis = "watersev-msgdis";
    public static final String watersev_msgexg = "watersev-msgexg";
    public static final String watersev_pln = "watersev-pln";
    public static final String watersev_sevchk = "watersev-sevchk";
    public static final String watersev_syn = "watersev-syn";

    public static final String water_redis = "water_redis";
    public static final String water_redis_track = "water_redis_track";
    public static final String water_cache = "water_cache";
    public static final String water_heihei = "water_heihei";

    public static final String water_msg_store = "water_msg_store";

    public static final String water_paas = "water_paas";
    public static final String water_paas_request = "water_paas_request";

    public static final String water_settings = "water_settings";

    public static final String logger_water_log_api = "water_log_api";
    public static final String logger_water_log_sev = "water_log_sev";
    public static final String logger_water_log_msg = "water_log_msg";
    public static final String logger_water_log_etl = "water_log_etl";
    public static final String logger_water_log_sql_p = "water_log_sql_p"; //性能
    public static final String logger_water_log_sql_b = "water_log_sql_b"; // 行为
    public static final String logger_water_log_faas = "water_log_faas";
    public static final String logger_water_log_raas = "water_log_raas";
    public static final String logger_water_log_admin = "water_log_admin";
    public static final String logger_water_log_heihei = "water_log_heihei";
    public static final String logger_water_log_upstream = "water_log_upstream";

    public static final String rubber_log_request = "rubber_log_request";

    public static final String track_service = "_service";
    public static final String track_from = "_from";

    public static final String driverType = "driverType";

    /**
     * 主控组
     */
    public static final String whitelist_tag_master = "master";
    /**
     * 客户端组（一般用于检测管理后台客户端）
     */
    public static final String whitelist_tag_client = "client";
    /**
     * 服务端组（一般用于检测服务端IP）
     */
    public static final String whitelist_tag_server = "server";


    public static final String whitelist_type_ip = "ip";
    public static final String whitelist_type_domain = "domain";
    public static final String whitelist_type_mobile = "mobile";
    public static final String whitelist_type_token = "token";

}
