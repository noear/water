package demoapi;

import org.noear.solon.Solon;
import org.noear.solon.web.cors.CrossInterceptor;

/**
 * @author noear 2021/11/7 created
 */
public class App {
    public static void main(String[] args){
        Solon.start(App.class, args, app->{
            //添加跨域支持
            app.routerInterceptor(new CrossInterceptor());
        });
    }
}
