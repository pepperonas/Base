package com.pepperonas.sample_notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pepperonas.andbasx.system.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        common();

    }


    private void common() {
        new NotificationUtils.Builder(this, MainActivity.class)
                .title("Title")
                .message("Msg")
                .subtext("SubTxt")
                .vibration(false)
                .icon(R.mipmap.ic_launcher)
                .ledColor(NotificationUtils.LedColor.Red)
                .ledDuration(500, 250)
                .ledBrightness(100)
                .dismissable(false)
                .autoCancel(false)
                .fire();
    }

}
