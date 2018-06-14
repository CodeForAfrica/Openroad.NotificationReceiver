package org.codefortanzania.notify.app;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.codefortanzania.notify.util.Constants;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by timothy on 6/20/17.
 */

public class NotificationService extends NotificationListenerService {
    Context context;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MessageURLKey = "messageURL";
    public static final String ApiTokenKey = "apiToken";

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
            String sender = "";
            String message = "";
            String[] temp = null;

            // Processing Notifications.
            Notification mNotification = sbn.getNotification();

            CharSequence[] lines = mNotification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
            if(lines.length > 0){
                temp = ((String)lines[lines.length-1]).split(":");
                sender = temp[0];
                message = temp[1];

                Log.i("Sender", sender);
                Log.i("Message", message);

                // Sending to BroadcastListener.
                Intent msgrcv = new Intent("Msg");
                msgrcv.putExtra("package", pack);
                msgrcv.putExtra("sender",sender);
                msgrcv.putExtra("message",message);

                // Saving the message:
                new HttpAsyncTask(sender, message).execute();

                LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            }
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
            return postToAPI(sender, message);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i(Constants.TAG, result);
        }
    }

    private String postToAPI(String sender, String message) {
        final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String messageURL = sharedpreferences.getString(MessageURLKey,"");
        String apiToken = sharedpreferences.getString(ApiTokenKey,"");

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        sender = sender.replaceAll("\\u202A","");
        sender = sender.replaceAll("\\u202C","");
        builder.add(Constants.KEY_CATEGORY,"afya");
        builder.add(Constants.KEY_TYPE,"text");
        builder.add(Constants.KEY_SENDER, sender);
        builder.add(Constants.KEY_BODY,message);
        builder.add(Constants.KEY_API_KEY, apiToken);

        formBody = builder.build();
        Request request = new Request.Builder()
                .url(messageURL)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                Log.i(Constants.TAG, " Error contacting application server" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String strResponse = response.body().string();
                Log.i(Constants.TAG, strResponse);
            }
        });

        return "  ";
    }
}