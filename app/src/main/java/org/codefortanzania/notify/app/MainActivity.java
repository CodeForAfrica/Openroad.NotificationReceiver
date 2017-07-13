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

public class MainActivity extends AppCompatActivity{
    TableLayout tab;

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
        }
    };
}
