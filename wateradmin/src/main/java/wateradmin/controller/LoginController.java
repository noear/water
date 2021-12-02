package wateradmin.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.Subject;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Result;
import org.noear.water.utils.ImageUtils;
import org.noear.water.utils.RandomUtils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.MethodType;
import wateradmin.dso.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class LoginController extends BaseController {
    @Mapping("/login") //视图 返回
    public ModelAndView login(Context ctx) {
        ctx.sessionClear();

        return view("login");
    }

    @Mapping("/")
    public void index() {
        redirect("/login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"

    //$共享SESSOIN$::自动跳转
    @Mapping("/login/auto")
    public void login_auto() throws Exception {
        long subjectId = Session.current().getSubjectId();

        if (subjectId > 0) {
            String link_uri = GritClient.global().auth().getUriFrist(subjectId).link_uri;

            if (Utils.isEmpty(link_uri) == false) {
                redirect(link_uri);
                return;
            }
        }

        redirect("/login");
    }

    @Mapping("/login/ajax/check")  // Map<,> 返回[json]  (ViewModel 是 Map<String,Object> 的子类)
    public Result login_ajax_check(String userName, String passWord, String captcha) throws Exception {

        //验证码检查
        if (!captcha.toLowerCase().equals(Session.current().getValidation())) {
            return Result.failure("提示：验证码错误！");
        }

        if (Utils.isEmpty(userName) || Utils.isEmpty(passWord)) {
            return Result.failure("提示：请输入账号和密码！");
        }

        Subject subject = GritClient.global().auth().login(userName, passWord);

        if (Subject.isEmpty(subject)) {
            return Result.failure("提示：账号或密码不对！");
        } else {

            Session.current().loadSubject(subject);
            Resource res = GritClient.global().auth().getUriFrist(subject.subject_id);

            if (Utils.isEmpty(res.link_uri)) {
                return Result.failure("提示：请联系管理员开通权限！");
            } else {
                String resUrl = GritUtil.buildDockUri(res);
                return Result.succeed(resUrl);
            }
        }
    }

    /*
     * 获取验证码图片
     */
    @Mapping(value = "/login/validation/img", method = MethodType.GET, produces = "image/jpeg")
    public void getValidationImg(Context ctx) throws IOException {
        // 生成验证码存入session
        String validation = RandomUtils.code(4);
        Session.current().setValidation(validation);

        // 获取图片
        BufferedImage bufferedImage = ImageUtils.getValidationImage(validation);

        // 禁止图像缓存
        ctx.headerSet("Pragma", "no-cache");
        ctx.headerSet("Cache-Control", "no-cache");
        ctx.headerSet("Expires", "0");

        // 图像输出
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "jpeg", ctx.outputStream());
    }
}
