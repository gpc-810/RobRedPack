package com.gpc.robredpack;

import android.app.Notification;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by GPC on 2016/12/29.
 * NotificationCollectorService
 */
public class NotificationCollectorService extends NotificationListenerService {



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.e("zpf", "open" + "-----" + sbn.toString());
        String msg = sbn.getNotification().tickerText.toString();

        if (msg.contains(Config.WX_HONGBAO_STRING)||msg.contains(Config.QQ_HONGBAO_STRING)) {
            Notification notification=sbn.getNotification();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
                MyAccessibilityService.canGet=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }





    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e("zpf", "shut" + "-----" + sbn.toString());

    }
}
