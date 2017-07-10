package org.codefortanzania.notify.app;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

//import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by timothy on 6/20/17.
 */

public class NotificationService extends NotificationListenerService {
    Context context;

    private static final String SAVE_MESSAGE_URL = "http://192.168.0.53/ww-api/Api.php";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_SENDER_TITLE = "sender_title";
    public static final String KEY_BODY = "body";
    public static final String TAG = "Yousup API";

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

            // Saving the message:
            new HttpAsyncTask(sender, message).execute();

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
    }
  
    private class HttpAsyncTask extends AsyncTask<HashMap, Void, String> {
        private String sender;
        private String message;

        HttpAsyncTask(String sender, String message){
            this.sender = sender;
            this.message = message;
        }
        @Override
        protected String doInBackground(HashMap... queryData) {
            Log.i(TAG, sender+ " "+ message);
            return postToAPI(sender, message);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

        }
    }

    private String postToAPI(String sender, String message) {
        final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        sender = sender.replaceAll("\\u202A","");
        sender = sender.replaceAll("\\u202C","");
        String sms_sender = sender.replaceAll("[ +]","");
        String sms_sender_title = sender;
        builder.add(KEY_CATEGORY,"afya");
        builder.add(KEY_TYPE,"text");
        builder.add(KEY_SENDER, sms_sender);
        builder.add(KEY_SENDER_TITLE,sms_sender_title);
        builder.add(KEY_BODY,message);

        formBody = builder.build();
        Request request = new Request.Builder()
                .url(SAVE_MESSAGE_URL)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                Log.i(TAG, " Error contacting application server" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String strResponse = response.body().string();
                Log.i(TAG, strResponse); 
            }
        });

        return "  ";
    }
}