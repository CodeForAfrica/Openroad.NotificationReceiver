package org.codefortanzania.notify.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MessageURLKey = "messageURL";
    public static final String ApiTokenKey = "apiToken";

    SharedPreferences sharedpreferences;
    EditText editTextMessageURL;
    EditText editTextApiToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextMessageURL = (EditText) findViewById(R.id.message_url);
        editTextApiToken = (EditText) findViewById(R.id.api_token);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String messageURL = sharedpreferences.getString(MessageURLKey,"");
        String apiToken = sharedpreferences.getString(ApiTokenKey,"");

        editTextMessageURL.setText(messageURL);
        editTextApiToken.setText(apiToken);

        Button buttonSave = (Button) findViewById(R.id.save_btn);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String url = editTextMessageURL.getText().toString();

                final String token = editTextApiToken.getText().toString();

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(MessageURLKey, url);
                editor.putString(ApiTokenKey, token);
                editor.commit();

                Toast.makeText(getApplicationContext(), "Message URL & API Token Saved!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
