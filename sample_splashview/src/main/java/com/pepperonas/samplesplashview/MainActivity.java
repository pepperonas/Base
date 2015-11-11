package com.pepperonas.samplesplashview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pepperonas.andbasx.animation.CircleAnimation;
import com.pepperonas.andbasx.concurrency.ThreadUtils;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SplashView mSplashView = (SplashView) findViewById(R.id.splash_view);
        final Button btn = (Button) findViewById(R.id.button);
        btn.setVisibility(View.INVISIBLE);

        ThreadUtils.runDelayed(2000, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                mSplashView.finish();
                return null;
            }
        });

        ThreadUtils.runDelayed(4000, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                btn.setVisibility(View.VISIBLE);
                CircleAnimation.animate(btn, 30, 15, 400);
                return null;
            }
        });
    }
}
