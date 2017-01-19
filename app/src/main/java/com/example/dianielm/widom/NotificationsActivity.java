package com.example.dianielm.widom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

public class NotificationsActivity extends AppCompatActivity {
    //TAG'a używamy np gdy chcemy przekazać wiadomość
    public static final String TAG = "NOTIFICATION";

    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notifications);

        infoTextView = (TextView) findViewById(R.id.infoTextView);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                infoTextView.append("\n" + key + ": " + value);
            }
        }

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Token: " + token);
    }
}
