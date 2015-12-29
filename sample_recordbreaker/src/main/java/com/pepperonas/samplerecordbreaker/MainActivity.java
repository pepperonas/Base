package com.pepperonas.samplerecordbreaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pepperonas.jbasx.log.Log;

public class MainActivity extends AppCompatActivity implements OnRecordResolvedListener {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.textView);
        final EditText et = (EditText) findViewById(R.id.editText);
        Button btnSend = (Button) findViewById(R.id.btn_send);
        Button btnReceive = (Button) findViewById(R.id.btn_receive);

        tv.setText("locked");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRecord.Builder(MainActivity.this, "swef", "Alf")
                        .setValue1("ok")
                        .setValue2("1")
                        .expiresInMinutes(1)
                        .send();
            }
        });


        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResolveRecord.Builder(MainActivity.this, "background").resolve();
            }
        });

    }


    @Override
    public void onRecordBreakerSuccess(RecordBreaker.Status success, String... params) {
        Log.i(TAG, "onUserRegistered: " + success.name());
        Log.d(TAG, "onUserRegistered", params);

        if (params[0].contains("background")) {
            RelativeLayout rl = (RelativeLayout) MainActivity.this.findViewById(R.id.main_rl);
            rl.setBackgroundColor(com.pepperonas.jbasx.color.ColorUtils.toInt(params[3].replace(" ", "")));
        }

    }


    @Override
    public void onRecordBreakerFailed(RecordBreaker.Status error) {
        Log.e(TAG, "onFailed: " + error.name());
    }
}
