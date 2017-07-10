package org.codefortanzania.notify.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    TableLayout tab;

    private static final String SAVE_MESSAGE_URL = "http://192.168.0.53/ww-api/Api.php";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_SENDER_TITLE = "sender_title";
    public static final String KEY_BODY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab = (TableLayout)findViewById(R.id.tab);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String sender = intent.getStringExtra("sender");
            String message = intent.getStringExtra("message");


            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml("<b>" + sender + " : </b>" + message + "<br/>"));
            tr.addView(textview);
            tab.addView(tr);

            // Sending to Database.
            //saveMessage(sender,message);
        }
    };

    private void saveMessage(final String sender, final String message){
        /*
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SAVE_MESSAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        VolleyLog.d("YOY", "Error: " + error.getMessage());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String sms_sender = sender.replaceAll("[ +]","");
                String sms_sender_title = sender;
                params.put(KEY_CATEGORY,"afya");
                params.put(KEY_TYPE,"text");
                params.put(KEY_SENDER, sms_sender);
                params.put(KEY_SENDER_TITLE,sms_sender_title);
                params.put(KEY_BODY,message);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        */

        /*
        HashMap params = new HashMap();

        String sms_sender = sender.replaceAll("[ +]","");
        String sms_sender_title = sender;

        params.put(KEY_CATEGORY,"afya");
        params.put(KEY_TYPE,"text");
        params.put(KEY_SENDER, sms_sender);
        params.put(KEY_SENDER_TITLE,sms_sender_title);
        params.put(KEY_BODY,message);

        //You pass postData as the 2nd argument of the constructor
        PostResponseAsyncTask saveMessageTask = new PostResponseAsyncTask(this, params);
        saveMessageTask.execute(SAVE_MESSAGE_URL);

        */
    }
}
