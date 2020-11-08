package waterraas.dao;

import org.noear.luffy.model.AFileModel;

import java.util.HashMap;
import java.util.Map;

/**
 * AFile获取工具，会处理缓存
 * */
public class AFileUtil {
    private static String _lock = "";
    private static Map<String, AFileModel> _files = new HashMap<>();

    /**
     * 生成匹配数据库的路径
     * */
    public static String path2(String path){
        if (path.endsWith("/") == false) {
            if (path.lastIndexOf('/') > path.lastIndexOf('.')) {
                return path.replaceAll("/[^/]*$", "/*");
            }
        }

        return path;
    }

    /**
     * 获取缓存的文件
     * */
    public static AFileModel get(String path2) throws Exception {
        if(_files.containsKey(path2)==false){
            synchronized (_lock){
                if(_files.containsKey(path2)==false){
                    AFileModel tml =  DbPaaSApi.fileGet(path2);
                    _files.put(path2,tml);
                }
            }
        }

        return _files.get(path2);
    }

    /**
     * 移徐缓存的文件（后面自动重新加载）
     * */
    public static void remove(String path2){
        _files.remove(path2);
    }

    public static void removeAll(){
        _files.clear();
    }

}
