package webapp.utils;

import webapp.dao.Session;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by konar on 2017/7/10.
 */
public class ImageUtil {
    /*
     * 获取验证码图片
     */
    public static BufferedImage getValidationImage() {

        //////////////////////////////////////////////// 常量 ////////////////////////////////////////////////

        final int imgWidth = 90;
        final int imgHeight = 40;

        /////////////////////////////////////////////////////////////////////////////////////////////////////

        BufferedImage buffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 从session获取验证码字符串
        final String validation = Session.current().getValidation();


        Random random = new Random();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imgWidth, imgHeight);

        int fontHeight = 20;
       /* // 创建字体
        String fonts[] =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Font font = new Font(fonts[0], Font.PLAIN, fontHeight);
        // 设置字体
        g.setFont(font);*/
        // 创建字体
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体
        g.setFont(font);

        int red = 0, green = 0, blue = 0;

        for (int i = 0; i < validation.length(); i++) {
            // 取出待绘制字符
            String stringToDraw = "" + validation.charAt(i);
            // 随机颜色
            red = random.nextInt(155) + 50;
            green = random.nextInt(155) + 50;
            blue = random.nextInt(155) + 50;


            //坐标
            int x = i * 20 + 5;
            int y = 25 + random.nextInt(10) * (random.nextInt(2) % 2 == 0 ? 1 : -1);
            g.setColor(new Color(red,green,blue));


            g.drawString(stringToDraw, x, y);
        }

        //画些直线
        int LINE_MAX_COUNT = 3;
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        for (int i = 0; i < LINE_MAX_COUNT; i++) {
            // 坐标
            x1 = random.nextInt(imgWidth);
            y1 = random.nextInt(imgHeight);
            x2 = random.nextInt(imgWidth);
            y2 = random.nextInt(imgHeight);

            // 颜色
            red = random.nextInt(155) + 50;
            green = random.nextInt(155) + 50;
            blue = random.nextInt(155) + 50;

            g.setColor(new Color(red,green,blue));

            g.drawLine(x1,y1,x2,y2);
        }

        return buffImg;
    }
}
