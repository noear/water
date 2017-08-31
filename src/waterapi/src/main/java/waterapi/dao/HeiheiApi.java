package waterapi.dao;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleClient;

import java.util.Collection;
import java.util.List;

public class HeiheiApi {

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private static final String masterSecret = "4a8cd168ca71dabcca306cac";
    private static final String appKey ="af9a9da3c73d23aa30ea4af1";

    public static final JPushClient jPushClient = new JPushClient(masterSecret, appKey);


    public static void push(String alias, String text) {

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
                        .setBadge(0)
                        .setSound("happy")
                        .build())
                        .setAlert(text).build())
                .build();


        try {
            jPushClient.sendPush(payload);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void push(Collection<String> alias, String text)  {

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
                        .setBadge(0)
                        .setSound("happy")
                        .build())
                        .setAlert(text).build())
                .build();


        try {
            jPushClient.sendPush(payload);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
