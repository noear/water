package wateradmin.controller;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfUtil;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.bcf.models.BcfUserModel;
import org.noear.water.utils.ImageUtils;
import org.noear.water.utils.RandomUtils;
import org.noear.water.utils.TextUtils;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XMethod;
import wateradmin.dso.Session;
import wateradmin.viewModels.ViewModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@XController
public class LoginController extends BaseController {

    @XMapping("login") //视图 返回
    public ModelAndView login() {
        return view("login");
    }

    @XMapping("/")
    public void index() {
        redirect("/login");
    }
    //-----------------

    //ajax.path like "{view}/ajax/{cmd}"

    //$共享SESSOIN$::自动跳转
    @XMapping("/login/auto")
    public void login_auto() throws Exception {
        int puid = Session.current().getPUID();
        if (puid > 0) {
            String def_url = BcfClient.getUserFirstResource(puid).uri_path;
            if (TextUtils.isEmpty(def_url) == false) {
                redirect(def_url);
                return;
            }
        }

        redirect("/login");
    }

    @XMapping("/login/ajax/check")  // Map<,> 返回[json]  (ViewModel 是 Map<String,Object> 的子类)
    public ViewModel login_ajax_check(String userName, String passWord, String captcha) throws Exception {

        //验证码检查
        if (!captcha.toLowerCase().equals(Session.current().getValidation())) {
            return viewModel.set("code", 0).set("msg", "提示：验证码错误！");
        }

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
            return viewModel.set("code", 0).set("msg", "提示：请输入账号和密码！");
        }

        BcfUserModel user = BcfClient.login(userName, passWord);

        if (user.puid == 0)
            return viewModel.set("code", 0).set("msg", "提示：账号或密码不对！"); //set 直接返回；有利于设置后直接返回，不用另起一行
        else {

            Session.current().loadModel(user);

            //新方案 //xyj,20181120,(uadmin)
            BcfResourceModel res = BcfClient.getUserFirstResource(user.puid);
            String def_url = null;

            if (TextUtils.isEmpty(res.uri_path)) {
                return viewModel.set("code", 0)
                        .set("msg", "提示：请联系管理员开通权限");
            } else {
                def_url = BcfUtil.buildBcfUnipath(res);

                return viewModel.set("code", 1)
                        .set("msg", "ok")
                        .set("url", def_url);
            }
        }
    }

    /*
     * 获取验证码图片
     */
    @XMapping(value = "/login/validation/img", method = XMethod.GET, produces = "image/jpeg")
    public void getValidationImg(XContext ctx) throws IOException {
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
