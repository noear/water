package webapp.dao.ops;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 执行Shell命令的环境变量
 *
 * @author dhb
 * @date 2018/12/13
 */
public class EnvModel extends HashMap<String, String> {

    @NonNull
    public List<String> targets = new ArrayList<>();

    public EnvModel(String target) {
        targets.add(target);
    }

    public EnvModel(@NonNull List<String> targets) {
        this.targets.addAll(targets);
    }
}
