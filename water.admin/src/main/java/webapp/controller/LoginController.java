package webapp.controller;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfUtil;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XMethod;
import org.noear.water.tools.TextUtils;
import webapp.dao.Session;
import webapp.utils.ImageUtil;
import webapp.viewModels.ViewModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by yuety on 14-9-10.
 */
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
    public ViewModel login_ajax_check(String userName, String passWord, String validationCode) throws Exception {

        //验证码检查
        if (!validationCode.toLowerCase().equals(Session.current().getValidation())) {
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
    public void getValidationImg() throws IOException {
        // 生成验证码存入session
        int MAX_LENGTH = 4;
        int TEMPLATE_SIZE = 62;
        char codeTemplate[] = {
                'a','b','c','d','e','f',
                'g','h','i','j','k','l',
                'm','n','o','p','q','r',
                's','t','u','v','w','x',
                'y','z',
                'A','B','C','D','E','F',
                'G','H','I','J','K','L',
                'M','N','O','P','Q','R',
                'S','T','U','V','W','X',
                'Y','Z',
                '0','1','2','3','4','5',
                '6','7','8','9'
        };
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < MAX_LENGTH; i++) {
            sb.append(codeTemplate[random.nextInt(TEMPLATE_SIZE) % TEMPLATE_SIZE]);
        }
        Session.current().setValidation(sb.toString());

        // 获取图片
        BufferedImage bufferedImage = ImageUtil.getValidationImage();

        XContext resp = Session.getContext();
        // 禁止图像缓存
        resp.headerSet("Pragma", "no-cache");
        resp.headerSet("Cache-Control", "no-cache");
        resp.headerSet("Expires", "0");

        // 图像输出

        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "jpeg", resp.outputStream());
    }
}
