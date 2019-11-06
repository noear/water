package webapp.dao;

import lombok.Getter;
import lombok.Setter;

/**
 * Linux脚本命令执行结果
 *
 * @author dhb
 * @date 2018/12/07
 */
@Getter
@Setter
public class ShellResult {

    public boolean isOk;
    public String output;

    public ShellResult(boolean isOk) {
        this(isOk, null);
    }

    public ShellResult(boolean isOk, String output) {
        this.isOk = isOk;
        this.output = output;
    }
}
