package org.noear.water;

public class WW {
    public static final String mime_gzip="application/x-gzip";
    public static final String mime_json="application/json";

    public static final String WATER_ID = "WATER_ID";
    public static final String WATER_ID_HOUR = "WATER_ID_HOUR";
    public static final String WATER_ID_DATE = "WATER_ID_DATE";


    public static final String http_header_from = "Water-From";

    public static final String msg_ucache_topic = "water.cache.update";
    public static final String msg_uconfig_topic = "water.config.update";

    public static final String cfg_water_log_gzip = "water.log.pipeline.gzip";
    public static final String cfg_water_log_level = "water.log.pipeline.level";
    public static final String cfg_water_log_interval = "water.log.pipeline.interval";
    public static final String cfg_water_log_packetSize = "water.log.pipeline.packetSize";

    public static final String path_service_check = "/run/check/";
    public static final String path_service_stop = "/run/stop/";
    public static final String path_msg_receiver = "/msg/receive";

    public static final String clz_BcfClient = "org.noear.bcf.BcfClient";

    public static final String water_host = "water.host";
    public static final String water_logger = "water.logger";



    public static final String water_log = "water_log";
    public static final String water_log_upstream = "water_log_upstream";
    public static final String water_log_api = "water_log_api";

    public static final String water = "water";
    public static final String waterapi = "waterapi";
    public static final String watersev = "watersev";

    public static final String water_redis = "water_redis";
    public static final String water_redis_track = "water_redis_track";
    public static final String water_cache = "water_cache";

    public static final String water_msg = "water_msg";
    public static final String water_paas = "water_paas";

    public static final String water_session = "water_session";

    public static final String type_logger = "logger.type";
    public static final String type_queue = "queue.type";

}
