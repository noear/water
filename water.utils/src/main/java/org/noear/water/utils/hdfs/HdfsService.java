package org.noear.water.utils.hdfs;

import java.io.File;

/**
 * 分布式文件服务
 *
 * @author noear 2021/4/7 created
 */
public interface HdfsService {
    String getObj(String key) throws Exception;

    String putObj(String key, String content) throws Exception;


    /**
     *
     * */
    String putObj(String key, File file) throws Exception;
}
