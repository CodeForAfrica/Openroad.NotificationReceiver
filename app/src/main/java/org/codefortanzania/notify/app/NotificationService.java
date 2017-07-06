package org.codefortanzania.notify.app;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by timothy on 6/20/17.
 */

public class NotificationService extends NotificationListenerService {
    Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        // Fetching WhatsApp Notification only.
        if(pack.toString().equalsIgnoreCase("com.whatsapp")){

        }
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        String sender = "", message = "";
        String[] temp = null;

        // Trying get notifications.
        Notification mNotification = sbn.getNotification();

        CharSequence[] lines = mNotification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
        Log.i("LastText", (String)lines[lines.length-1]);

        temp = ((String)lines[lines.length-1]).split(":");
        sender = temp[0];
        message = temp[1];

        Log.i("Sender", sender);
        Log.i("Message", message);


        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);
        msgrcv.putExtra("sender",sender);
        msgrcv.putExtra("message",message);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
    }
}