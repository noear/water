package wateradmin.viewModels;

import org.noear.solon.core.ModelAndView;

/**
 * Created by noear on 14-9-10.
 */
//用作视图返回的模型
public class ViewModel extends ModelAndView {

    //put 的另一版本；返回自己；方便快速操作；
    public ViewModel set(String name, Object val)
    {
        put(name,val);
        return this;
    }

    public ViewModel code(int code){
        put("code",code);
        return this;
    }

    public ViewModel code(int code, String msg){
        put("code",code);
        put("msg",msg);
        return this;
    }

    public ViewModel msg(String msg) {
        put("msg", msg);
        return this;
    }
}
